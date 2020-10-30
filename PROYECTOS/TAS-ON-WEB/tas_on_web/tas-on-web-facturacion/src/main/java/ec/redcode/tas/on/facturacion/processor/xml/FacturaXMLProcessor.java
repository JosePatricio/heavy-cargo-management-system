package ec.redcode.tas.on.facturacion.processor.xml;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle;
import ec.gob.sri.comprobantes.modelo.factura.Impuesto;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.FacturaManual;
import ec.redcode.tas.on.facturacion.processor.BaseFacturaProcessor;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaXMLProcessor extends BaseFacturaProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        Date now = new Date();
        Factura factura = exchange.getIn().getBody(Factura.class);
        String secuencia = getSecuencial(SRIEnum.COMPROBANTE_FACTURA);
        InfoTributaria infoTributaria = getInfoTributaria(SRIEnum.COMPROBANTE_FACTURA.getValue());
        infoTributaria.setSecuencial(secuencia);
        String claveAcceso = generateClaveAcceso(now, infoTributaria.getRuc(), secuencia, SRIEnum.COMPROBANTE_FACTURA.getValue());
        infoTributaria.setClaveAcceso(claveAcceso);
        factura.setInfoTributaria(infoTributaria);
        factura.getInfoFactura().setFechaEmision(this.dateFormatFechaEmision.format(now));

        Impuesto impuesto;
        Detalle.Impuestos impuestos;
        List<Impuesto> impuestoList;

        List<Detalle> detalleList = factura.getDetalles().getDetalle();

        BigDecimal total = new BigDecimal(0.0);

        for (Detalle detalle: detalleList){
            detalle.setDescuento(new BigDecimal(0));
            detalle.setPrecioTotalSinImpuesto(detalle.getPrecioUnitario());

            Detalle.DetallesAdicionales detallesAdicionales = new Detalle.DetallesAdicionales();
            Detalle.DetallesAdicionales.DetAdicional detAdicional = new Detalle.DetallesAdicionales.DetAdicional();
            detAdicional.setNombre("NA");
            detAdicional.setValor("NA");
            List<Detalle.DetallesAdicionales.DetAdicional> detAdicionals = new ArrayList<>();
            detAdicionals.add(detAdicional);
            detallesAdicionales.getDetAdicional().addAll(detAdicionals);
            detalle.setDetallesAdicionales(detallesAdicionales);

            impuesto = new Impuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje("6");
            impuesto.setTarifa(new BigDecimal(0));
            impuesto.setValor(new BigDecimal(0));
            impuesto.setBaseImponible(detalle.getPrecioUnitario());
            impuestos = new Detalle.Impuestos();
            impuestoList = new ArrayList<>();
            impuestoList.add(impuesto);
            impuestos.getImpuesto().addAll(impuestoList);
            detalle.setImpuestos(impuestos);

            total = total.add(detalle.getPrecioUnitario());

        }

        factura.getInfoFactura().setImporteTotal(total.setScale(2, RoundingMode.UP));
        factura.getInfoFactura().setTotalSinImpuestos(total.setScale(2, RoundingMode.UP));

        Factura.InfoFactura.TotalConImpuestos totalConImpuestos = new Factura.InfoFactura.TotalConImpuestos();
        List<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto> totalConImpuestosList = new ArrayList<>();
        Factura.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
        totalImpuesto.setCodigo("2");
        totalImpuesto.setCodigoPorcentaje("6");
        totalImpuesto.setBaseImponible(total.setScale(2, RoundingMode.UP));
        totalImpuesto.setValor(new BigDecimal(0.0));
        totalConImpuestosList.add(totalImpuesto);
        totalConImpuestos.getTotalImpuesto().addAll(totalConImpuestosList);
        //totalConImpuestos.setTotalImpuesto(totalConImpuestosList);
        factura.getInfoFactura().setTotalConImpuestos(totalConImpuestos);

        Factura.InfoFactura.Pago pagos = new Factura.InfoFactura.Pago();
        List<Factura.InfoFactura.Pago.DetallePago> pagoList = new ArrayList<>();
        Factura.InfoFactura.Pago.DetallePago pago = new Factura.InfoFactura.Pago.DetallePago();
        pago.setFormaPago("01");
        pago.setTotal(total.setScale(2, RoundingMode.UP));
        pago.setFormaPago("01");
        pago.setPlazo("30");
        pago.setUnidadTiempo("dias");
        pagoList.add(pago);
        pagos.getPagos().addAll(pagoList);

        factura.getInfoFactura().setPagos(pagos);

        exchange.getIn().setHeader("claveAcceso", claveAcceso);
        exchange.getIn().setBody(factura);

        if(exchange.getIn().getHeader("facturaManual") != null){
            guardarFacturaManual(factura, (String) exchange.getIn().getHeader("usuarioFacturaManual"));
        }
    }

    private void guardarFacturaManual(Factura factura, String usuarioCreaFactura){
        FacturaManual facturaManual = new FacturaManual();
        facturaManual.setFacturaManualUsuarioId(usuarioCreaFactura);
        facturaManual.setFacturaManualClaveAcceso(factura.getInfoTributaria().getClaveAcceso());
        facturaManual.setFacturaManualSecuencial(factura.getInfoTributaria().getSecuencial());
        facturaManual.setFacturaManualFechaEmision(new Timestamp(new Date().getTime()));
        facturaManual.setFacturaManualEstado("Esperando aprobación SRI");
        facturaManual.setFacturaManualTotal(TasOnUtil.roundDouble(factura.getInfoFactura().getImporteTotal().doubleValue(), 2));
        facturaManual.setFacturaManualAdquiriente(factura.getInfoFactura().getIdentificacionComprador());
        facturaManualService.create(facturaManual);
    }

}
