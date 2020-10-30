package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.reportes.FormasPago;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
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
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class RideWriterProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(RideWriterProcessor.class);
    @Setter private SolicitudEnvioService solicitudEnvioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        ReportUtilTasOn report = new ReportUtilTasOn();
        String numeroAutorizacion = (String) exchange.getIn().getHeader("numeroAutorizacion");
        String path = TasOnUtil.getComprobantesFacturasPath() + File.separator + numeroAutorizacion + ".pdf";
        logger.info("Starting writing RIDE in {}", path);
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(ec.gob.sri.comprobantes.modelo.factura.Factura.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = (ec.gob.sri.comprobantes.modelo.factura.Factura) unmarshaller.unmarshal(sr);
        FacturaReporte facturaReporte = new FacturaReporte(factura, solicitudEnvioService);
        DetallesAdicionalesReporte detalles;
        List<DetallesAdicionalesReporte> detallesAdicionalesReportes = new ArrayList<>();
        detalles = new DetallesAdicionalesReporte();
        List<FormasPago> formasPagos = new ArrayList<>();
        FormasPago formasPago;
        for (Factura.Detalles.Detalle detalle: factura.getDetalles().getDetalle()){
            detalles.setCantidad(String.valueOf(detalle.getCantidad()));
            detalles.setCodigoPrincipal(detalle.getCodigoPrincipal());
            detalles.setCodigoAuxiliar(detalle.getCodigoAuxiliar());
            detalles.setDescripcion(detalle.getDescripcion());
            detalles.setPrecioUnitario(detalle.getPrecioUnitario());
            detalles.setPrecioTotalSinImpuesto(String.valueOf(detalle.getPrecioTotalSinImpuesto()));
            for (Factura.InfoFactura.Pago.DetallePago pago: factura.getInfoFactura().getPagos().getPagos()) {
                formasPago = new FormasPago(String.valueOf(pago.getFormaPago()), String.valueOf(pago.getTotal()));
                formasPagos.add(formasPago);
            }
            detalles.setDetalle1(detalle.getDetallesAdicionales().getDetAdicional().get(0).getValor());
            detalles.setFormasPago(formasPagos);
            detallesAdicionalesReportes.add(detalles);
        }
        facturaReporte.setFormasPago(formasPagos);
        facturaReporte.setDetallesAdiciones(detallesAdicionalesReportes);
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        report.generateRide(path, facturaReporte, numeroAutorizacion, fechaAutorizacion.toString() );
        logger.info("Finish writing RIDE");
        exchange.getIn().setHeader("ruta", "facturas");
        exchange.getIn().setHeader("template", Constant.EMAIL_FACTURA);
    }

}
