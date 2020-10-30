package ec.redcode.tas.on.facturacion.reporte;

import ec.gob.sri.comprobantes.administracion.modelo.Compensaciones;
import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito;
import ec.gob.sri.comprobantes.modelo.reportes.*;
import ec.gob.sri.comprobantes.sql.CompensacionSQL;
import ec.gob.sri.comprobantes.sql.ImpuestoValorSQL;
import ec.gob.sri.comprobantes.util.TipoImpuestoEnum;
import ec.gob.sri.comprobantes.util.TipoImpuestoIvaEnum;
import ec.gob.sri.comprobantes.util.reportes.ReporteUtil;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacturaReporte {

    enum TipoFormaPago {

        SIN("01", "SIN UTILIZACION DEL SISTEMA FINANCIERO"),
        COMPEN("15", "COMPENSACIÓN DE DEUDAS"),
        DEBITO("16", "TARJETA DE DÉBITO"),
        ELECTRONICO("17", "DINERO ELECTRÓNICO"),
        PREPAGO("18", "TARJETA PREPAGO"),
        CREDITO("19", "TARJETA DE CRÉDITO"),
        OTROS("20", "OTROS CON UTILIZACION DEL SISTEMA FINANCIERO"),
        ENDOSO("21", "ENDOSO DE TÍTULOS");

        String key;
        String value;

        TipoFormaPago(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    @Getter @Setter private Factura factura;
    @Getter @Setter private NotaCredito notaCredito;
    @Getter @Setter private String detalle1;
    @Getter @Setter private String detalle2;
    @Getter @Setter private String detalle3;
    private List<DetallesAdicionalesReporte> detallesAdiciones;
    private List<InformacionAdicional> infoAdicional;
    private List<FormasPago> formasPago;
    private List<TotalesComprobante> totalesComprobante;
    private SolicitudEnvioService solicitudEnvioService;
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Number[] valoresInfoAdicional = {0, 0.0, BigDecimal.ZERO}; //cajas, peso, costoTotal

    public FacturaReporte(Factura factura, SolicitudEnvioService solicitudEnvioService) {
        this.factura = factura;
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public FacturaReporte(NotaCredito notaCredito, SolicitudEnvioService solicitudEnvioService) {
        this.notaCredito = notaCredito;
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public List<DetallesAdicionalesReporte> getDetallesAdiciones() throws SQLException, ClassNotFoundException {
        this.detallesAdiciones = new ArrayList<>();

        for(int i = 0; i < this.getFactura().getDetalles().getDetalle().size(); i++){
            Factura.Detalles.Detalle det = this.getFactura().getDetalles().getDetalle().get(i);
            boolean esUltimoElemento = false;
            if(i == this.getFactura().getDetalles().getDetalle().size()-1){
                esUltimoElemento = true;
            }
            DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
            detAd.setCodigoPrincipal(det.getCodigoPrincipal());
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(det.getCodigoPrincipal());
            detAd.setNumeroDocCliente(solicitudEnvio.getSolicitudEnvioNumeroDocCliente());
            detAd.setCodigoAuxiliar(det.getCodigoAuxiliar());
            detAd.setDescripcion(det.getDescripcion());
            detAd.setCantidad(det.getCantidad().toPlainString());
            detAd.setPrecioTotalSinImpuesto(det.getPrecioTotalSinImpuesto().toString());
            detAd.setPrecioUnitario(det.getPrecioUnitario());
            detAd.setPrecioSinSubsidio(det.getPrecioSinSubsidio());
            if (det.getDescuento() != null) {
                detAd.setDescuento(det.getDescuento().toString());
            }

            int j = 0;
            if (det.getDetallesAdicionales() != null && det.getDetallesAdicionales().getDetAdicional() != null && !det.getDetallesAdicionales().getDetAdicional().isEmpty()) {
                for(Iterator<Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional> j$ = det.getDetallesAdicionales().getDetAdicional().iterator(); j$.hasNext(); ++j) {
                    Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional detAdicional = j$.next();
                    if (j == 0) {
                        detAd.setDetalle1(detAdicional.getNombre());
                    }

                    if (j == 1) {
                        detAd.setDetalle2(detAdicional.getNombre());
                    }

                    if (j == 2) {
                        detAd.setDetalle3(detAdicional.getNombre());
                    }
                }
            }

            if (this.getFormasPago() != null) {
                detAd.setFormasPago(this.getFormasPago());
            }

            detAd.setTotalesComprobante(this.getTotalesComprobante());
            this.detallesAdiciones.add(detAd);
            detAd.setInfoAdicional(this.getInfoAdicional(esUltimoElemento));
        }
        return this.detallesAdiciones;
    }

    public void setDetallesAdiciones(List<DetallesAdicionalesReporte> detallesAdiciones) {
        this.detallesAdiciones = detallesAdiciones;
    }

    private List<InformacionAdicional> getInfoAdicional(boolean esUltimoElemento) {

        if (this.getFactura().getInfoAdicional() != null) {
            this.infoAdicional = new ArrayList<>();
            if (this.getFactura().getInfoAdicional().getCampoAdicional() != null && !this.factura.getInfoAdicional().getCampoAdicional().isEmpty()) {

                for (Factura.InfoAdicional.CampoAdicional ca : this.getFactura().getInfoAdicional().getCampoAdicional()) {
                    this.infoAdicional.add(new InformacionAdicional(ca.getValue(), ca.getNombre()));
                }
            }
        }

        if(esUltimoElemento){
            getInfoAdicionalLocal();
            setValoresReporte();
        }

        return this.infoAdicional;
    }

    private void getInfoAdicionalLocal(){
        if(infoAdicional == null) infoAdicional = new ArrayList<>();

        for(DetallesAdicionalesReporte detalle : detallesAdiciones){
            try{
                SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(detalle.getCodigoPrincipal());
                int idPeso = solicitudEnvio.getSolicitudEnvioUnidadPeso();
                switch (idPeso){
                    case 37: //tonelada
                        valoresInfoAdicional[1] = (Double)valoresInfoAdicional[1] + solicitudEnvio.getSolicitudEnvioPeso()*1000;
                        break;
                    case 38: //gramo
                        valoresInfoAdicional[1] = (Double)valoresInfoAdicional[1] + solicitudEnvio.getSolicitudEnvioPeso()/1000;
                        break;
                    case 39: //kilogramo
                        valoresInfoAdicional[1] = (Double)valoresInfoAdicional[1] + solicitudEnvio.getSolicitudEnvioPeso();
                        break;
                    case 40: //libra
                        valoresInfoAdicional[1] = (Double)valoresInfoAdicional[1] + solicitudEnvio.getSolicitudEnvioPeso()/2.2046226;
                        break;
                }
                valoresInfoAdicional[0] = (Integer) valoresInfoAdicional[0] +Integer.parseInt(detalle.getDetalle1());
                valoresInfoAdicional[2] = ((BigDecimal)valoresInfoAdicional[2]).add(new BigDecimal(detalle.getPrecioTotalSinImpuesto()));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setValoresReporte(){
        String unidadPeso = "Kg";

        Integer cajasTotales = (Integer)valoresInfoAdicional[0];
        if(cajasTotales == 0) return;
        Double pesoEnvio = (Double)valoresInfoAdicional[1];
        BigDecimal costoPromedioPorCaja = ((BigDecimal)valoresInfoAdicional[2]).divide(new BigDecimal(cajasTotales), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal costoPromedioPorUnidadPeso = ((BigDecimal)valoresInfoAdicional[2]).divide(new BigDecimal(pesoEnvio), 2, BigDecimal.ROUND_HALF_UP);

        infoAdicional.add(new InformacionAdicional(cajasTotales + " Cajas", "Total cajas trasladadas:"));
        infoAdicional.add(new InformacionAdicional(decimalFormat.format(pesoEnvio)+" "+unidadPeso,"Peso trasladado:"));
        infoAdicional.add(new InformacionAdicional(costoPromedioPorCaja + " USD/Caja","Costo promedio:"));
        infoAdicional.add(new InformacionAdicional(costoPromedioPorUnidadPeso + " USD/"+unidadPeso,"Costo promedio:"));
    }

    public void setInfoAdicional(List<InformacionAdicional> infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public List<FormasPago> getFormasPago() {
        System.out.println("--->" + this.getFactura());
        if (this.getFactura().getInfoFactura().getPagos() != null) {
            this.formasPago = new ArrayList();
            if (this.getFactura().getInfoFactura().getPagos().getPagos() != null && !this.factura.getInfoFactura().getPagos().getPagos().isEmpty()) {

                for (Factura.InfoFactura.Pago.DetallePago pa : this.getFactura().getInfoFactura().getPagos().getPagos()) {

                    String desc = TipoFormaPago.SIN.value;
                    switch (pa.getFormaPago()){
                        case "01":
                            desc = TipoFormaPago.SIN.value;
                            break;
                        case "15":
                            desc = TipoFormaPago.COMPEN.value;
                            break;
                        case "19":
                            desc = TipoFormaPago.CREDITO.value;
                            break;

                    }

                    this.formasPago.add(new FormasPago(desc, pa.getTotal().setScale(2).toString()));
                }
            }
        }

        return this.formasPago;
    }

    public void setFormasPago(List<FormasPago> formasPago) {
        this.formasPago = formasPago;
    }

    public List<TotalesComprobante> getTotalesComprobante() throws SQLException, ClassNotFoundException {
        this.totalesComprobante = new ArrayList();
        BigDecimal importeTotal = BigDecimal.ZERO.setScale(2);
        BigDecimal compensaciones = BigDecimal.ZERO.setScale(2);
        TotalComprobante tc = this.getTotales(this.factura.getInfoFactura());
        Iterator i$ = tc.getIvaDistintoCero().iterator();

        IvaDiferenteCeroReporte iva;
        while(i$.hasNext()) {
            iva = (IvaDiferenteCeroReporte)i$.next();
            this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL " + iva.getTarifa(), iva.getSubtotal(), false));
        }

        //this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL IVA 0%", tc.getSubtotal0(), false));
        this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL NO OBJETO IVA", tc.getSubtotalNoSujetoIva(), false));
        //this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL EXENTO IVA", tc.getSubtotalExentoIVA(), false));
        this.totalesComprobante.add(new TotalesComprobante("SUBTOTAL SIN IMPUESTOS", this.factura.getInfoFactura().getTotalSinImpuestos(), false));
        this.totalesComprobante.add(new TotalesComprobante("DESCUENTO", this.factura.getInfoFactura().getTotalDescuento(), false));
        //this.totalesComprobante.add(new TotalesComprobante("ICE", tc.getTotalIce(), false));
        i$ = tc.getIvaDistintoCero().iterator();

        while(i$.hasNext()) {
            iva = (IvaDiferenteCeroReporte)i$.next();
            this.totalesComprobante.add(new TotalesComprobante("IVA " + iva.getTarifa(), iva.getValor(), false));
        }

        //this.totalesComprobante.add(new TotalesComprobante("IRBPNR", tc.getTotalIRBPNR(), false));
        //this.totalesComprobante.add(new TotalesComprobante("PROPINA", this.factura.getInfoFactura().getPropina(), false));
        Factura.InfoFactura.compensacion.detalleCompensaciones compensacion;
        if (this.factura.getInfoFactura().getCompensaciones() != null) {
            for(i$ = this.factura.getInfoFactura().getCompensaciones().getCompensaciones().iterator(); i$.hasNext(); compensaciones = compensaciones.add(compensacion.getValor())) {
                compensacion = (Factura.InfoFactura.compensacion.detalleCompensaciones)i$.next();
            }

            importeTotal = this.factura.getInfoFactura().getImporteTotal().add(compensaciones);
        }

        if (!compensaciones.equals(BigDecimal.ZERO.setScale(2))) {
            this.totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", importeTotal, false));
            i$ = this.factura.getInfoFactura().getCompensaciones().getCompensaciones().iterator();

            while(i$.hasNext()) {
                compensacion = (Factura.InfoFactura.compensacion.detalleCompensaciones)i$.next();
                if (!compensacion.getValor().equals(BigDecimal.ZERO.setScale(2))) {
                    CompensacionSQL compensacionSQL = new CompensacionSQL();
                    String detalleCompensacion = ((Compensaciones)compensacionSQL.obtenerCompensacionesPorCodigo(compensacion.getCodigo()).get(0)).getTipoCompensacion();
                    this.totalesComprobante.add(new TotalesComprobante("(-) " + detalleCompensacion, compensacion.getValor(), true));
                }
            }

            this.totalesComprobante.add(new TotalesComprobante("VALOR A PAGAR", this.factura.getInfoFactura().getImporteTotal(), false));
        } else {
            this.totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", this.factura.getInfoFactura().getImporteTotal(), false));
        }

        return this.totalesComprobante;
    }

    public void setTotalesComprobante(List<TotalesComprobante> totalesComprobante) {
        this.totalesComprobante = totalesComprobante;
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

        for (Factura.InfoFactura.TotalConImpuestos.TotalImpuesto ti : infoFactura.getTotalConImpuestos().getTotalImpuesto()) {
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
            ivaDiferenteCero.add(this.LlenaIvaDiferenteCero());
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

    private IvaDiferenteCeroReporte LlenaIvaDiferenteCero() {
        BigDecimal valor = BigDecimal.ZERO.setScale(2);
        String porcentajeIva = this.ObtieneIvaRideFactura(this.factura.getInfoFactura().getTotalConImpuestos());
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
        } while(TipoImpuestoEnum.IVA.getCode() != cod || impuesto.getValor().doubleValue() <= 0.0D);

        return this.obtenerPorcentajeIvaVigente(impuesto.getCodigoPorcentaje());
    }

    private String obtenerPorcentajeIvaVigente() {
        BigDecimal porcentaje = new BigDecimal(0);
        return porcentaje.setScale(0).toString() + "%";
    }

    private String obtenerPorcentajeIvaVigente(String cod) {
        try {
            ImpuestoValorSQL impvalorSQL = new ImpuestoValorSQL();
            BigDecimal porcentaje = BigDecimal.valueOf((impvalorSQL.obtenerDatosIvaCodigoPorcentaje(cod).get(0)).getPorcentaje());
            return porcentaje.setScale(0).toString();
        } catch (SQLException var4) {
            Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, null, var4);
            return "";
        } catch (ClassNotFoundException var5) {
            Logger.getLogger(ReporteUtil.class.getName()).log(Level.SEVERE, null, var5);
            return "";
        }
    }

}
