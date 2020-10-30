package ec.redcode.tas.on.facturacion.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UpdateFacturaProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFacturaProcessor.class);

    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private FacturaService facturaService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private NotaCreditoService notaCreditoService;
    @Setter private ClienteService clienteService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(ec.gob.sri.comprobantes.modelo.factura.Factura.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = (ec.gob.sri.comprobantes.modelo.factura.Factura) unmarshaller.unmarshal(sr);

        logger.info("Updating solicitud and ofertas with factura nro {}", factura.getInfoTributaria().getSecuencial());
        Factura facturaGuardada = guardarFactura(factura, exchange);
        if(BaseFacturaProcessor.debeEmitirNotaCredito(clienteService.readCliente(facturaGuardada.getFacturaEmpresa()))){
            guardarNuevaNotaCredito(facturaGuardada.getFacturaId());
        }
        for (ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle detalle : factura.getDetalles().getDetalle()) {
            guardarFacturaDetalle(facturaGuardada.getFacturaId(), Integer.valueOf(detalle.getCodigoAuxiliar()));
            actualizarSolicitudEnvio(detalle.getCodigoPrincipal());
        }
        logger.info("Finish Facturacion");

        llenarExchange(exchange, facturaGuardada);
    }

    private Factura guardarFactura(ec.gob.sri.comprobantes.modelo.factura.Factura factura, Exchange exchange){
        Factura facturaPersist = new Factura();
        String facturaId = factura.getInfoTributaria().getEstab().concat("-").concat(factura.getInfoTributaria().getPtoEmi().concat("-").concat(factura.getInfoTributaria().getSecuencial()));
        facturaPersist.setFacturaId(facturaId);
        facturaPersist.setFacturaNro(facturaId);
        facturaPersist.setFacturaUsuarioId("1234567890001");
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        facturaPersist.setFacturaFechaAutorizacion(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        facturaPersist.setFacturaFechaEmision(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        facturaPersist.setFacturaNroAutorizacion(String.valueOf(exchange.getIn().getHeader("numeroAutorizacion")));
        facturaPersist.setFacturaTipoFactura(10);
        facturaPersist.setFacturaEstado(Constant.INVOICE_RECEIVABLE);
        facturaPersist.setFacturaSubtotal(factura.getInfoFactura().getTotalSinImpuestos().doubleValue());
        facturaPersist.setFacturaTotal(factura.getInfoFactura().getTotalSinImpuestos().doubleValue());
        facturaPersist.setFacturaComision(factura.getInfoFactura().getTotalSinImpuestos().doubleValue());
        facturaPersist.setFacturaEmpresa(factura.getInfoFactura().getIdentificacionComprador());
        facturaPersist.setFacturaXml(String.valueOf(exchange.getIn().getHeader("comprobante")));
        facturaService.create(facturaPersist);
        return facturaPersist;
    }

    private void guardarFacturaDetalle(String facturaId, Integer ofertaId ){
        FacturaDetalle facturaDetalle = new FacturaDetalle();
        facturaDetalle.setFacturaDetalleFacturaId(facturaId);
        facturaDetalle.setFacturaDetalleOfertaId(ofertaId);
        facturaDetalleService.create(facturaDetalle);
    }

    private void actualizarSolicitudEnvio(String solicitudEnvioId){
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicitudEnvioId);
        solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_TO_PAY);
        solicitudEnvioService.update(solicitudEnvio);
    }

    private void guardarNuevaNotaCredito(String facturaId){
        NotaCredito notaCredito = new NotaCredito();
        notaCredito.setNotaCreditoFacturaId(facturaId);
        notaCreditoService.create(notaCredito);
    }

    private void llenarExchange(Exchange exchange, Factura facturaGuardada){
        exchange.getIn().setHeader("ruc", facturaGuardada.getFacturaEmpresa());
        exchange.getIn().setHeader("numeroDocumento", facturaGuardada.getFacturaId());
        exchange.getIn().setHeader("fechaEmision", TasOnUtil.getStringFromDate(
                TasOnUtil.getDateFromTimeStamp(facturaGuardada.getFacturaFechaEmision()), new SimpleDateFormat("dd/MM/yyyy")));
        exchange.getIn().setHeader("clave", String.valueOf(facturaGuardada.getFacturaNroAutorizacion()));
        exchange.getIn().setHeader("subtotal", String.valueOf(facturaGuardada.getFacturaSubtotal()));
        exchange.getIn().setHeader("total", String.valueOf(facturaGuardada.getFacturaTotal()));
        exchange.getIn().setBody(exchange.getIn().getHeader("comprobante"));
    }

}
