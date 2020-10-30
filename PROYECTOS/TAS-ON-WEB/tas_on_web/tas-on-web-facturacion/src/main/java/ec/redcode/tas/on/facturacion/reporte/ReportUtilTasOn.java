package ec.redcode.tas.on.facturacion.reporte;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.factura.Impuesto;
import ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito;
import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import ec.gob.sri.comprobantes.modelo.reportes.ComprobanteRetencionReporte;
import ec.gob.sri.comprobantes.modelo.reportes.IvaDiferenteCeroReporte;
import ec.gob.sri.comprobantes.modelo.reportes.TotalComprobante;
import ec.gob.sri.comprobantes.util.TipoAmbienteEnum;
import ec.gob.sri.comprobantes.util.TipoEmisionEnum;
import ec.gob.sri.comprobantes.util.TipoImpuestoEnum;
import ec.gob.sri.comprobantes.util.TipoImpuestoIvaEnum;
import ec.gob.sri.comprobantes.util.reportes.ReporteUtil;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportUtilTasOn {

    public void generateRide(String urlReporte, FacturaReporte fact, String numAut, String fechaAut) throws SQLException, ClassNotFoundException, JRException {
        JRDataSource dataSource = new JRBeanCollectionDataSource(fact.getDetallesAdiciones());
        String pathReporte = TasOnUtil.getJbossReportesPath() + "factura.jasper";
        JasperPrint reporte_view = JasperFillManager.fillReport(pathReporte, this.obtenerMapaParametrosReportes(this.obtenerParametrosInfoTriobutaria(fact.getFactura().getInfoTributaria(),
                numAut, fechaAut), this.obtenerInfoFactura(fact.getFactura().getInfoFactura(), fact)), dataSource);
        JasperExportManager.exportReportToPdfFile(reporte_view, urlReporte);
    }

    public String generateEbillingRide(FacturaReporte fact, List<DetallesAdicionalesReporte> detallesAdicionalesReporte,
                                     Map<String, Object> parametrosInformacionTributaria) throws JRException {
        JRDataSource dataSource = new JRBeanCollectionDataSource(detallesAdicionalesReporte);
        String pathReporte = TasOnUtil.getJbossReportesPath() + "ebilling.jasper";
        JasperPrint reporte_view = JasperFillManager.fillReport(pathReporte,
                this.obtenerMapaParametrosReportes(parametrosInformacionTributaria,
                        this.obtenerInfoFactura(fact.getFactura().getInfoFactura(), fact)), dataSource);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(reporte_view, output);
        byte[] pdfBytes = output.toByteArray();
        return Base64.encodeBase64String(pdfBytes);
    }


    public void generateNCRide(String urlReporte, FacturaReporte fact, List<DetallesAdicionalesReporte> detallesAdicionalesReporte,
                               Map<String, Object> parametrosInformacionTributaria) throws JRException {
        JRDataSource dataSource = new JRBeanCollectionDataSource(detallesAdicionalesReporte);
        String pathReporte = TasOnUtil.getJbossReportesPath() + "notaCredito.jasper";
        JasperPrint reporte_view = JasperFillManager.fillReport(pathReporte,
                this.obtenerMapaParametrosReportes(parametrosInformacionTributaria,
                        this.obtenerInfoNC(fact.getNotaCredito().getInfoNotaCredito())), dataSource);
        JasperExportManager.exportReportToPdfFile(reporte_view, urlReporte);
    }

    public void generateComprobante(String urlReporte, ComprobanteRetencionReporte comprobante,  String numAut, String fechaAut) throws JRException {
        JRDataSource dataSource = new JRBeanCollectionDataSource(comprobante.getDetallesAdiciones());
        String pathReporte = TasOnUtil.getJbossReportesPath() + "comprobanteRetencion.jasper";
        JasperPrint reporte_view = JasperFillManager.fillReport(pathReporte, this.obtenerMapaParametrosReportes(this.obtenerParametrosInfoTriobutaria(comprobante.getComprobanteRetencion().getInfoTributaria(),
                numAut, fechaAut), this.obtenerInfoCompRetencion(comprobante.getComprobanteRetencion().getInfoCompRetencion())), dataSource);
        JasperExportManager.exportReportToPdfFile(reporte_view, urlReporte);
    }

    private Map<String, Object> obtenerInfoCompRetencion(ComprobanteRetencion.InfoCompRetencion infoComp) {
        Map<String, Object> param = new HashMap();
        //param.put("DIR_SUCURSAL", infoComp.getDirEstablecimiento());
        param.put("RS_COMPRADOR", infoComp.getRazonSocialSujetoRetenido());
        param.put("RUC_COMPRADOR", infoComp.getIdentificacionSujetoRetenido());
        param.put("FECHA_EMISION", infoComp.getFechaEmision());
        param.put("CONT_ESPECIAL", infoComp.getContribuyenteEspecial());
        param.put("LLEVA_CONTABILIDAD", infoComp.getObligadoContabilidad());
        param.put("EJERCICIO_FISCAL", infoComp.getPeriodoFiscal());
        return param;
    }

    private Map<String, Object> obtenerMapaParametrosReportes(Map<String, Object> mapa1, Map<String, Object> mapa2) {
        mapa1.putAll(mapa2);
        return mapa1;
    }

    private Map<String, Object> obtenerParametrosInfoTriobutaria(InfoTributaria infoTributaria, String numAut, String fechaAut) {
        Map<String, Object> param = new HashMap();
        param.put("RUC", infoTributaria.getRuc());
        param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
        param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
        param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
        param.put("ACT_COMERCIAL", SRIEnum.ACTIVIDAD_COMERCIAL.getValue());
        param.put("LOGO",ReportUtilTasOn.getTasOnLogo());
        param.put("SUBREPORT_DIR", TasOnUtil.getJbossReportesPath());
        param.put("SUBREPORT_PAGOS", TasOnUtil.getJbossReportesPath());
        param.put("SUBREPORT_TOTALES", TasOnUtil.getJbossReportesPath());
        param.put("TIPO_EMISION", this.obtenerTipoEmision(infoTributaria));
        param.put("NUM_AUT", numAut);
        param.put("FECHA_AUT", fechaAut);
        param.put("MARCA_AGUA", this.obtenerMarcaAgua(infoTributaria.getAmbiente()));
        param.put("NUM_FACT", infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi() + "-" + infoTributaria.getSecuencial());
        param.put("AMBIENTE", this.obtenerAmbiente(infoTributaria));
        return param;
    }

    public static FileInputStream getTasOnLogo(){
        try {
           return new FileInputStream(TasOnUtil.getJbossImagesPath().concat("logo.jpeg"));
        } catch (FileNotFoundException var8) {
            try {
                return new FileInputStream(TasOnUtil.getJbossImagesPath().concat("logo.jpeg"));
            } catch (FileNotFoundException var7) {
                Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, (String)null, var7);
            }
        }
        return null;
    }

    public static String obtenerTipoEmision(InfoTributaria infoTributaria) {
        if (infoTributaria.getTipoEmision().equals("2")) {
            return TipoEmisionEnum.PREAUTORIZADA.getCode();
        } else {
            return infoTributaria.getTipoEmision().equals("1") ? TipoEmisionEnum.NORMAL.getCode() : null;
        }
    }

    public static InputStream obtenerMarcaAgua(String ambiente) {
        try {
            BufferedInputStream is;
            if (ambiente.equals(TipoAmbienteEnum.PRODUCCION.getCode())) {
                is = new BufferedInputStream(new FileInputStream(TasOnUtil.getJbossImagesPath().concat("produccion.jpeg")));
                return is;
            } else {
                is = new BufferedInputStream(new FileInputStream(TasOnUtil.getJbossImagesPath().concat("pruebas.jpeg")));
                return is;
            }
        } catch (FileNotFoundException var3) {
            Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, (String)null, var3);
            return null;
        }
    }

    public static String obtenerAmbiente(InfoTributaria infoTributaria) {
        return infoTributaria.getAmbiente().equals("2") ? TipoAmbienteEnum.PRODUCCION.toString() : TipoAmbienteEnum.PRUEBAS.toString();
    }

    private Map<String, Object> obtenerInfoFactura(Factura.InfoFactura infoFactura, FacturaReporte fact) {
        BigDecimal TotalSinSubsidio = BigDecimal.ZERO;
        BigDecimal TotalSinDescuento = BigDecimal.ZERO;
        BigDecimal TotalSubsidio = BigDecimal.ZERO;
        Map<String, Object> param = new HashMap();
        param.put("DIR_SUCURSAL", infoFactura.getDirEstablecimiento());
        param.put("CONT_ESPECIAL", infoFactura.getContribuyenteEspecial());
        param.put("LLEVA_CONTABILIDAD", infoFactura.getObligadoContabilidad());
        param.put("RS_COMPRADOR", infoFactura.getRazonSocialComprador());
        param.put("RUC_COMPRADOR", infoFactura.getIdentificacionComprador());
        param.put("DIRECCION_CLIENTE", infoFactura.getIDireccionComprador());
        param.put("FECHA_EMISION", infoFactura.getFechaEmision());
        param.put("GUIA", infoFactura.getGuiaRemision());
        TotalComprobante tc = this.getTotales(infoFactura);
        if (infoFactura.getTotalSubsidio() != null) {
            TotalSinSubsidio = this.obtenerTotalSinSubsidio(fact);
            TotalSinDescuento = this.obtenerTotalSinDescuento(fact);
            TotalSubsidio = TotalSinSubsidio.subtract(TotalSinDescuento).setScale(2, RoundingMode.UP);
            if (Double.valueOf(tc.getTotalIRBPNR().toString()) < 0.0D) {
                TotalSinSubsidio = TotalSinSubsidio.add(tc.getTotalIRBPNR());
            }

            if (infoFactura.getPropina() != null) {
                TotalSinSubsidio = TotalSinSubsidio.add(infoFactura.getPropina());
            }
        }

        param.put("TOTAL_SIN_SUBSIDIO", TotalSinSubsidio.setScale(2, RoundingMode.UP));
        param.put("AHORRO_POR_SUBSIDIO", TotalSubsidio.setScale(2, RoundingMode.UP));
        return param;
    }

    private Map<String, Object> obtenerInfoNC(NotaCredito.InfoNotaCredito infoNotaCredito) {
        Map<String, Object> param = new HashMap<>();
        param.put("DIR_SUCURSAL", infoNotaCredito.getDirEstablecimiento());
        param.put("CONT_ESPECIAL", infoNotaCredito.getContribuyenteEspecial());
        param.put("LLEVA_CONTABILIDAD", infoNotaCredito.getObligadoContabilidad());
        param.put("RS_COMPRADOR", infoNotaCredito.getRazonSocialComprador());
        param.put("RUC_COMPRADOR", infoNotaCredito.getIdentificacionComprador());
        param.put("FECHA_EMISION", infoNotaCredito.getFechaEmision());
        param.put("COMPROBANTE_MODIFICA_NUMERO", infoNotaCredito.getNumDocModificado());
        param.put("COMPROBANTE_MODIFICA_RAZON", infoNotaCredito.getMotivo());
        param.put("COMPROBANTE_MODIFICA_FECHA_EMISION", infoNotaCredito.getFechaEmisionDocSustento());
        return param;
    }

    private TotalComprobante getTotales(Factura.InfoFactura infoFactura) {
        List<IvaDiferenteCeroReporte> ivaDiferenteCero = new ArrayList();
        BigDecimal totalIva = new BigDecimal(0.0D);
        BigDecimal totalIva0 = new BigDecimal(0.0D);
        BigDecimal totalExentoIVA = new BigDecimal(0.0D);
        BigDecimal totalICE = new BigDecimal(0.0D);
        BigDecimal totalIRBPNR = new BigDecimal(0.0D);
        BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
        TotalComprobante tc = new TotalComprobante();
        Iterator i$ = infoFactura.getTotalConImpuestos().getTotalImpuesto().iterator();

        while(i$.hasNext()) {
            Factura.InfoFactura.TotalConImpuestos.TotalImpuesto ti = (Factura.InfoFactura.TotalConImpuestos.TotalImpuesto)i$.next();
            Integer cod = new Integer(ti.getCodigo());
            if (TipoImpuestoEnum.IVA.getCode() == cod && ti.getValor().doubleValue() > 0.0D) {
                //TODO: codigo porcentual ver bien
                String codigoPorcentaje = "12";
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
            ivaDiferenteCero.add(this.LlenaIvaDiferenteCero(infoFactura));
        }

        tc.setIvaDistintoCero(ivaDiferenteCero);
        tc.setSubtotal0(totalIva0);
        tc.setTotalIce(totalICE);
        tc.setSubtotal(totalIva0.add(totalIva));
        tc.setSubtotalExentoIVA(totalExentoIVA);
        tc.setTotalIRBPNR(totalIRBPNR);
        tc.setSubtotalNoSujetoIva(totalSinImpuesto);
        return tc;
    }

    private BigDecimal obtenerTotalSinSubsidio(FacturaReporte fact) {
        BigDecimal totalSinSubsidio = BigDecimal.ZERO;
        BigDecimal ivaDistintoCero = BigDecimal.ZERO;
        BigDecimal iva0 = BigDecimal.ZERO;
        double iva = 0.0D;
        List<Factura.Detalles.Detalle> detalles = fact.getFactura().getDetalles().getDetalle();

        for(int i = 0; i < detalles.size(); ++i) {
            BigDecimal sinSubsidio = BigDecimal.ZERO.setScale(2, RoundingMode.UP);
            if (((Factura.Detalles.Detalle)detalles.get(i)).getPrecioSinSubsidio() != null) {
                sinSubsidio = BigDecimal.valueOf(Double.valueOf(((Factura.Detalles.Detalle)detalles.get(i)).getPrecioSinSubsidio().toString()));
            }

            BigDecimal cantidad = BigDecimal.valueOf(Double.valueOf(((Factura.Detalles.Detalle)detalles.get(i)).getCantidad().toString()));
            if (Double.valueOf(sinSubsidio.toString()) <= 0.0D) {
                sinSubsidio = BigDecimal.valueOf(Double.valueOf(((Factura.Detalles.Detalle)detalles.get(i)).getPrecioUnitario().toString()));
            }

            List<Impuesto> impuesto = ((Factura.Detalles.Detalle)detalles.get(i)).getImpuestos().getImpuesto();
            double iva1 = 0.0D;

            for(int c = 0; c < impuesto.size(); ++c) {
                if (((Impuesto)impuesto.get(c)).getCodigo().equals(String.valueOf(TipoImpuestoEnum.IVA.getCode())) && !((Impuesto)impuesto.get(c)).getTarifa().equals(BigDecimal.ZERO)) {
                    iva = Double.valueOf(((Impuesto)impuesto.get(c)).getTarifa().toString());
                    iva1 = Double.valueOf(((Impuesto)impuesto.get(c)).getTarifa().toString());
                }
            }

            if (iva1 > 0.0D) {
                ivaDistintoCero = ivaDistintoCero.add(sinSubsidio.multiply(cantidad));
            } else {
                iva0 = iva0.add(sinSubsidio.multiply(cantidad));
            }
        }

        if (iva > 0.0D) {
            iva = iva / 100.0D + 1.0D;
            ivaDistintoCero = ivaDistintoCero.multiply(BigDecimal.valueOf(iva));
        }

        totalSinSubsidio = totalSinSubsidio.add(ivaDistintoCero).add(iva0);
        return totalSinSubsidio.setScale(2, RoundingMode.HALF_UP);
    }

    private IvaDiferenteCeroReporte LlenaIvaDiferenteCero(Factura.InfoFactura infoFactura) {
        BigDecimal valor = BigDecimal.ZERO.setScale(2);
        String porcentajeIva = this.obtenerPorcentajeIvaVigente();
        return new IvaDiferenteCeroReporte(valor, porcentajeIva, valor);
    }

    private String obtenerPorcentajeIvaVigente() {
        BigDecimal porcentaje = new BigDecimal(0);
        return porcentaje.setScale(0).toString() + "%";
    }

    private BigDecimal obtenerTotalSinDescuento(FacturaReporte fact) {
        BigDecimal totalConSubsidio = BigDecimal.ZERO;
        BigDecimal ivaDistintoCero = BigDecimal.ZERO;
        BigDecimal iva0 = BigDecimal.ZERO;
        double iva = 0.0D;
        List<Factura.Detalles.Detalle> detalles = fact.getFactura().getDetalles().getDetalle();

        for(int i = 0; i < detalles.size(); ++i) {
            BigDecimal sinSubsidio = BigDecimal.valueOf(Double.valueOf(((Factura.Detalles.Detalle)detalles.get(i)).getPrecioUnitario().toString()));
            BigDecimal cantidad = BigDecimal.valueOf(Double.valueOf(((Factura.Detalles.Detalle)detalles.get(i)).getCantidad().toString()));
            List<Impuesto> impuesto = ((Factura.Detalles.Detalle)detalles.get(i)).getImpuestos().getImpuesto();
            double iva1 = 0.0D;

            for(int c = 0; c < impuesto.size(); ++c) {
                if (((Impuesto)impuesto.get(c)).getCodigo().equals(String.valueOf(TipoImpuestoEnum.IVA.getCode())) && !((Impuesto)impuesto.get(c)).getTarifa().equals(BigDecimal.ZERO)) {
                    iva = Double.valueOf(((Impuesto)impuesto.get(c)).getTarifa().toString());
                    iva1 = Double.valueOf(((Impuesto)impuesto.get(c)).getTarifa().toString());
                }
            }

            if (iva1 > 0.0D) {
                ivaDistintoCero = ivaDistintoCero.add(sinSubsidio.multiply(cantidad));
            } else {
                iva0 = iva0.add(sinSubsidio.multiply(cantidad));
            }
        }

        if (iva > 0.0D) {
            iva = iva / 100.0D + 1.0D;
            ivaDistintoCero = ivaDistintoCero.multiply(BigDecimal.valueOf(iva));
        }

        totalConSubsidio = totalConSubsidio.add(ivaDistintoCero).add(iva0);
        return totalConSubsidio.setScale(2, RoundingMode.HALF_UP);
    }

    public Date DeStringADate(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String strFecha = fecha;
        Date fechaDate = null;

        try {
            fechaDate = formato.parse(strFecha);
            return fechaDate;
        } catch (ParseException var6) {
            return fechaDate;
        }
    }

    private String getJbossHome(){
        String jbossHome = System.getenv("jboss.home");
        if (jbossHome == null)
            jbossHome = System.getenv("JBOSS_HOME");
        return jbossHome;
    }


}
