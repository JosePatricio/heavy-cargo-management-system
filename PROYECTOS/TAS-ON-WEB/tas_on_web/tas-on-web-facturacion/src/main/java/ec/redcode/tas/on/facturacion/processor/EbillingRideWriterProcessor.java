package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.administracion.modelo.Compensaciones;
import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.reportes.FormasPago;
import ec.gob.sri.comprobantes.modelo.reportes.IvaDiferenteCeroReporte;
import ec.gob.sri.comprobantes.modelo.reportes.TotalComprobante;
import ec.gob.sri.comprobantes.modelo.reportes.TotalesComprobante;
import ec.gob.sri.comprobantes.sql.CompensacionSQL;
import ec.gob.sri.comprobantes.sql.ImpuestoValorSQL;
import ec.gob.sri.comprobantes.util.TipoAmbienteEnum;
import ec.gob.sri.comprobantes.util.TipoEmisionEnum;
import ec.gob.sri.comprobantes.util.TipoImpuestoEnum;
import ec.gob.sri.comprobantes.util.TipoImpuestoIvaEnum;
import ec.gob.sri.comprobantes.util.reportes.ReporteUtil;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.redcode.tas.on.facturacion.reporte.DetallesAdicionalesReporte;
import ec.redcode.tas.on.facturacion.reporte.FacturaReporte;
import ec.redcode.tas.on.facturacion.reporte.ReportUtilTasOn;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class EbillingRideWriterProcessor implements Processor {

    private String reportesPath = TasOnUtil.getJbossReportesPath();
    private String imagesPath = TasOnUtil.getJbossImagesPath();
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
    private static final Logger logger = LoggerFactory.getLogger(EbillingRideWriterProcessor.class);
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private EbillingService ebillingService;
    @Setter private UsuarioEbillingService usuarioEbillingService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private UsuarioService usuarioService;
    @Setter private AdquirienteService adquirienteService;

    @Override
    public void process(Exchange exchange) throws Exception {
        ReportUtilTasOn report = new ReportUtilTasOn();
        Integer ebillingId = (Integer) exchange.getIn().getHeader("ebilling");
        Ebilling ebilling = ebillingService.read(ebillingId);
        Adquiriente adquiriente = adquirienteService.read(ebilling.getEbillingAdquiriente());
        UsuarioEbilling usuarioEbilling = usuarioEbillingService.read(ebilling.getEbillingUsuarioEbilling());
        Usuario usuario = usuarioService.readUsuario(usuarioEbilling.getUsuarioEbillingIdUsuario());
        StringReader sr = new StringReader(ebilling.getEbillingXml());
        JAXBContext jaxbContext = JAXBContext.newInstance(Factura.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Factura factura = (Factura) unmarshaller.unmarshal(sr);
        FacturaReporte facturaReporte = new FacturaReporte(factura, solicitudEnvioService);
        List<DetallesAdicionalesReporte> detallesAdicionalesReportes = new ArrayList<>();
        for (Factura.Detalles.Detalle detalle : factura.getDetalles().getDetalle()) {
            DetallesAdicionalesReporte detalles = new DetallesAdicionalesReporte();
            detalles.setCantidad(String.valueOf(detalle.getCantidad()));
            detalles.setCodigoPrincipal(detalle.getCodigoPrincipal());
            detalles.setCodigoAuxiliar(detalle.getCodigoAuxiliar());
            detalles.setDescripcion(detalle.getDescripcion());
            detalles.setPrecioUnitario(detalle.getPrecioUnitario());
            detalles.setDescuento(String.valueOf(detalle.getDescuento()));
            detalles.setPrecioTotalSinImpuesto(String.valueOf(detalle.getPrecioTotalSinImpuesto()));
            detalles.setTotalesComprobante(getTotalesComprobante(factura));
            detalles.setDetalle1(detalle.getDetallesAdicionales().getDetAdicional().get(0).getValor());
            detalles.setFormasPago(getFormasPago(factura));
            detallesAdicionalesReportes.add(detalles);
        }
        facturaReporte.setFormasPago(getFormasPago(factura));
        facturaReporte.setDetallesAdiciones(detallesAdicionalesReportes);

        logger.info("Starting writing ebilling RIDE");
        String reportePDF = report.generateEbillingRide(facturaReporte, detallesAdicionalesReportes,
                obtenerParametrosInfoTriobutaria(factura.getInfoTributaria(), ebilling.getEbillingClaveAcceso(),
                        formatoFecha.format(ebilling.getEbillingFechaAutorizacion()), usuarioEbilling));
        ebilling.setEbillingRide(reportePDF);
        ebillingService.update(ebilling);
        logger.info("Finish writing RIDE");
        Map<String, Object> data = new HashMap<>();
        data.put("template", Constant.EMAIL_EBILLING);
        data.put("adquiriente", adquiriente.getAdquirienteRazonSocial());
        data.put("nombreUsuarioEbilling", usuario.getUsuarioNombres().trim().concat(" ").concat(usuario.getUsuarioApellidos()));
        data.put("telefonoUsuarioEbilling", usuario.getUsuarioCelular());
        data.put("mailUsuarioEbilling", usuario.getUsuarioMail());
        data.put("correos", usuario.getUsuarioMail() + "," + adquiriente.getAdquirienteMail());
        data.put("nroDocumento", ebilling.getEbillingNumero());
        exchange.getIn().setBody(data);
    }

    public List<FormasPago> getFormasPago(Factura factura) {
        List<FormasPago> formasPago = new ArrayList();
        List<CatalogoItem> catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_FORMAS_PAGO);
        if (factura.getInfoFactura().getPagos() != null) {
            if (factura.getInfoFactura().getPagos().getPagos() != null && !factura.getInfoFactura().getPagos().getPagos().isEmpty()) {
                for (Factura.InfoFactura.Pago.DetallePago pa : factura.getInfoFactura().getPagos().getPagos()) {
                    String desc = catalogoItem.stream().filter(c -> pa.getFormaPago().equals(c.getCatalogoItemValor())).findAny().orElse(null).getCatalogoItemDescripcion();
                    formasPago.add(new FormasPago(desc, pa.getTotal().setScale(2).toString()));
                }
            }
        }

        return formasPago;
    }


    public List<TotalesComprobante> getTotalesComprobante(Factura factura) throws SQLException, ClassNotFoundException {
        List<TotalesComprobante> totalesComprobante = new ArrayList<>();
        BigDecimal importeTotal = BigDecimal.ZERO.setScale(2);
        BigDecimal compensaciones = BigDecimal.ZERO.setScale(2);
        TotalComprobante tc = getTotales(factura);
        Iterator i$ = tc.getIvaDistintoCero().iterator();

        IvaDiferenteCeroReporte iva;
        while (i$.hasNext()) {
            iva = (IvaDiferenteCeroReporte) i$.next();
            totalesComprobante.add(new TotalesComprobante("SUBTOTAL " + iva.getTarifa(), iva.getSubtotal(), false));
        }

        //this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL IVA 0%", tc.getSubtotal0(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL NO OBJETO IVA", tc.getSubtotalNoSujetoIva(), false));
        //this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL EXENTO IVA", tc.getSubtotalExentoIVA(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL SIN IMPUESTOS", factura.getInfoFactura().getTotalSinImpuestos(), false));
        totalesComprobante.add(new TotalesComprobante("DESCUENTO", factura.getInfoFactura().getTotalDescuento(), false));
        //this.totalesComprobante.add(new TotalesComprobante("ICE", tc.getTotalIce(), false));
        i$ = tc.getIvaDistintoCero().iterator();

        while (i$.hasNext()) {
            iva = (IvaDiferenteCeroReporte) i$.next();
            totalesComprobante.add(new TotalesComprobante("IVA " + iva.getTarifa(), iva.getValor(), false));
        }

        //this.totalesComprobante.add(new TotalesComprobante("IRBPNR", tc.getTotalIRBPNR(), false));
        //this.totalesComprobante.add(new TotalesComprobante("PROPINA", this.factura.getInfoFactura().getPropina(), false));
        Factura.InfoFactura.compensacion.detalleCompensaciones compensacion;
        if (factura.getInfoFactura().getCompensaciones() != null) {
            for (i$ = factura.getInfoFactura().getCompensaciones().getCompensaciones().iterator(); i$.hasNext(); compensaciones = compensaciones.add(compensacion.getValor())) {
                compensacion = (Factura.InfoFactura.compensacion.detalleCompensaciones) i$.next();
            }

            importeTotal = factura.getInfoFactura().getImporteTotal().add(compensaciones);
        }

        if (!compensaciones.equals(BigDecimal.ZERO.setScale(2))) {
            totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", importeTotal, false));
            i$ = factura.getInfoFactura().getCompensaciones().getCompensaciones().iterator();

            while (i$.hasNext()) {
                compensacion = (Factura.InfoFactura.compensacion.detalleCompensaciones) i$.next();
                if (!compensacion.getValor().equals(BigDecimal.ZERO.setScale(2))) {
                    CompensacionSQL compensacionSQL = new CompensacionSQL();
                    String detalleCompensacion = ((Compensaciones) compensacionSQL.obtenerCompensacionesPorCodigo(compensacion.getCodigo()).get(0)).getTipoCompensacion();
                    totalesComprobante.add(new TotalesComprobante("(-) " + detalleCompensacion, compensacion.getValor(), true));
                }
            }

            totalesComprobante.add(new TotalesComprobante("VALOR A PAGAR", factura.getInfoFactura().getImporteTotal(), false));
        } else {
            totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", factura.getInfoFactura().getImporteTotal(), false));
        }

        return totalesComprobante;
    }

    private TotalComprobante getTotales(Factura factura) {
        List<IvaDiferenteCeroReporte> ivaDiferenteCero = new ArrayList();
        BigDecimal totalIva = new BigDecimal(0.0D);
        BigDecimal totalIva0 = new BigDecimal(0.0D);
        BigDecimal totalExentoIVA = new BigDecimal(0.0D);
        BigDecimal totalICE = new BigDecimal(0.0D);
        BigDecimal totalIRBPNR = new BigDecimal(0.0D);
        BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
        TotalComprobante tc = new TotalComprobante();

        for (Factura.InfoFactura.TotalConImpuestos.TotalImpuesto ti : factura.getInfoFactura().getTotalConImpuestos().getTotalImpuesto()) {
            Integer cod = new Integer(ti.getCodigo());
            if (TipoImpuestoEnum.IVA.getCode() == cod && ti.getValor().doubleValue() > 0.0D) {
                String codigoPorcentaje = this.obtenerPorcentajeIvaVigente(ti.getCodigoPorcentaje());
                IvaDiferenteCeroReporte iva = new IvaDiferenteCeroReporte(ti.getBaseImponible(), codigoPorcentaje, ti.getValor());
                ivaDiferenteCero.add(iva);
            }

            if (TipoImpuestoEnum.IVA.getCode() == cod && TipoImpuestoIvaEnum.IVA_VENTA_0.getCode().equals(ti.getCodigoPorcentaje())) {
                totalIva0 = totalIva0.add(ti.getBaseImponible());
            }

            if (TipoImpuestoEnum.IVA.getCode() == cod && TipoImpuestoIvaEnum.IVA_NO_OBJETO.getCode().equals(ti.getCodigoPorcentaje())) {
                totalSinImpuesto = totalSinImpuesto.add(ti.getBaseImponible());
            }

            if (TipoImpuestoEnum.IVA.getCode() == cod && TipoImpuestoIvaEnum.IVA_EXCENTO.getCode().equals(ti.getCodigoPorcentaje())) {
                totalExentoIVA = totalExentoIVA.add(ti.getBaseImponible());
            }

            if (TipoImpuestoEnum.ICE.getCode() == cod) {
                totalICE = totalICE.add(ti.getValor());
            }

            if (TipoImpuestoEnum.IRBPNR.getCode() == cod) {
                totalIRBPNR = totalIRBPNR.add(ti.getValor());
            }
        }

        if (ivaDiferenteCero.isEmpty()) {
            ivaDiferenteCero.add(this.LlenaIvaDiferenteCero(factura));
        }

        tc.setIvaDistintoCero(ivaDiferenteCero);
        tc.setSubtotal0(totalIva0);
        tc.setTotalIce(totalICE);
        tc.setSubtotal(totalIva0.add(totalIva).add(totalSinImpuesto).add(totalExentoIVA));
        tc.setSubtotalExentoIVA(totalExentoIVA);
        tc.setTotalIRBPNR(totalIRBPNR);
        tc.setSubtotalNoSujetoIva(totalSinImpuesto);
        return tc;
    }

    private IvaDiferenteCeroReporte LlenaIvaDiferenteCero(Factura factura) {
        BigDecimal valor = BigDecimal.ZERO.setScale(2);
        String porcentajeIva = this.ObtieneIvaRideFactura(factura.getInfoFactura().getTotalConImpuestos());
        return new IvaDiferenteCeroReporte(valor, porcentajeIva, valor);
    }

    private String ObtieneIvaRideFactura(Factura.InfoFactura.TotalConImpuestos impuestos) {
        Iterator<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto> i$ = impuestos.getTotalImpuesto().iterator();

        Factura.InfoFactura.TotalConImpuestos.TotalImpuesto impuesto;
        Integer cod;
        do {
            if (!i$.hasNext()) {
                return this.obtenerPorcentajeIvaVigente();
            }

            impuesto = i$.next();
            cod = new Integer(impuesto.getCodigo());
        } while (TipoImpuestoEnum.IVA.getCode() != cod || impuesto.getValor().doubleValue() <= 0.0D);

        return obtenerPorcentajeIvaVigente(impuesto.getCodigoPorcentaje());
    }

    private String obtenerPorcentajeIvaVigente() {
        return BigDecimal.ZERO.setScale(0).toString() + "%";
    }

    private String obtenerPorcentajeIvaVigente(String cod) {
        try {
            ImpuestoValorSQL impvalorSQL = new ImpuestoValorSQL();
            BigDecimal porcentaje = BigDecimal.valueOf((impvalorSQL.obtenerDatosIvaCodigoPorcentaje(cod).get(0)).getPorcentaje());
            return porcentaje.setScale(0).toString();
        } catch (SQLException var4) {
            java.util.logging.Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, null, var4);
            return "";
        } catch (ClassNotFoundException var5) {
            java.util.logging.Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, null, var5);
            return "";
        }
    }

    private Map<String, Object> obtenerParametrosInfoTriobutaria(InfoTributaria infoTributaria, String numAut, String fechaAut, UsuarioEbilling usuarioEbilling) {
        Map<String, Object> param = new HashMap();
        param.put("RUC", infoTributaria.getRuc());
        param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
        param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
        param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
        param.put("ACT_COMERCIAL", usuarioEbilling.getUsuarioEBillingActividadComercial());
        try{
            if(usuarioEbilling.getUsuarioEbillingLogo()==null || usuarioEbilling.getUsuarioEbillingLogo().isEmpty())
                throw new Exception("Sin logo");
            param.put("LOGO", new ByteArrayInputStream(Base64.getDecoder().decode(usuarioEbilling.getUsuarioEbillingLogo())));
        }catch (Exception e){
            logger.warn(e.getMessage());
            try{
                param.put("LOGO", new FileInputStream(imagesPath.concat("sinLogo.jpg")));
            } catch (Exception ex){
                logger.error(ex.getMessage());
            }
        }
        param.put("SUBREPORT_DIR", reportesPath);
        param.put("SUBREPORT_PAGOS", reportesPath);
        param.put("SUBREPORT_TOTALES", reportesPath);
        param.put("TIPO_EMISION", obtenerTipoEmision(infoTributaria));
        param.put("NUM_AUT", numAut);
        param.put("FECHA_AUT", fechaAut);
        param.put("MARCA_AGUA", obtenerMarcaAgua(infoTributaria.getAmbiente()));
        param.put("NUM_FACT", infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi() + "-" + infoTributaria.getSecuencial());
        param.put("AMBIENTE", obtenerAmbiente(infoTributaria));
        //param.put("NOM_COMERCIAL", infoTributaria.getNombreComercial());
        return param;
    }

    private String obtenerTipoEmision(InfoTributaria infoTributaria) {
        if (infoTributaria.getTipoEmision().equals("2")) {
            return TipoEmisionEnum.PREAUTORIZADA.getCode();
        } else {
            return infoTributaria.getTipoEmision().equals("1") ? TipoEmisionEnum.NORMAL.getCode() : null;
        }
    }

    private InputStream obtenerMarcaAgua(String ambiente) {
        try {
            BufferedInputStream is;
            if (ambiente.equals(TipoAmbienteEnum.PRODUCCION.getCode())) {
                is = new BufferedInputStream(new FileInputStream(imagesPath.concat("produccion.jpeg")));
                return is;
            } else {
                is = new BufferedInputStream(new FileInputStream(imagesPath.concat("pruebas.jpeg")));
                return is;
            }
        } catch (FileNotFoundException var3) {
            java.util.logging.Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, null, var3);
            return null;
        }
    }

    private String obtenerAmbiente(InfoTributaria infoTributaria) {
        return infoTributaria.getAmbiente().equals("2") ? TipoAmbienteEnum.PRODUCCION.toString() : TipoAmbienteEnum.PRUEBAS.toString();
    }

}
