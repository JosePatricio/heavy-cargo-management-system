package ec.redcode.tas.on.facturacion.processor;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.NotaCredito;
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

public class UpdateNotaCreditoProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(UpdateNotaCreditoProcessor.class);

    @Setter private NotaCreditoService notaCreditoService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito notaCredito = (ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito) unmarshaller.unmarshal(sr);
        actualizarNotaCredito(exchange, notaCredito);
        logger.info("Finish actualizacion nota de credito con facturaId: " + notaCredito.getInfoNotaCredito().getNumDocModificado());

        llenarExchange(exchange, notaCredito);
    }

    private NotaCredito actualizarNotaCredito(Exchange exchange, ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito notaCredito){
        String facturaId = notaCredito.getInfoNotaCredito().getNumDocModificado();
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        NotaCredito notaCreditoXActualizar = notaCreditoService.readByNotaCreditoFacturaId(facturaId);
        notaCreditoXActualizar.setNotaCreditoClaveAcceso(String.valueOf(exchange.getIn().getHeader("numeroAutorizacion")));
        notaCreditoXActualizar.setNotaCreditoEstado(String.valueOf(exchange.getIn().getHeader("estado")));
        notaCreditoXActualizar.setNotaCreditoFechaAutorizacion(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        notaCreditoXActualizar.setNotaCreditoValor(TasOnUtil.roundDouble(notaCredito.getInfoNotaCredito().getValorModificacion().doubleValue(), 2));
        notaCreditoXActualizar.setNotaCreditoXml(String.valueOf(exchange.getIn().getHeader("comprobante")));
        return notaCreditoXActualizar;
    }

    private void llenarExchange(Exchange exchange, ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito notaCredito){
        exchange.getIn().setHeader("ruc", notaCredito.getInfoNotaCredito().getIdentificacionComprador());
        exchange.getIn().setHeader("tipo", "notaCredito");
        exchange.getIn().setHeader("numeroDocumento", notaCredito.getInfoAdicional().getCampoAdicional().get(0).getValue());
        exchange.getIn().setHeader("numeroDocumentoModificado", notaCredito.getInfoNotaCredito().getNumDocModificado());
        exchange.getIn().setHeader("fechaEmision", notaCredito.getInfoNotaCredito().getFechaEmision());
        exchange.getIn().setHeader("clave", exchange.getIn().getHeader("numeroAutorizacion"));
        exchange.getIn().setHeader("total", String.valueOf(TasOnUtil.roundDouble(notaCredito.getInfoNotaCredito().getTotalSinImpuestos().doubleValue(),2)));
        exchange.getIn().setBody(exchange.getIn().getHeader("comprobante")); //lo agrega la clase AutorizacionComprobantesProcessor - comprobante es el XML enviado a autorizar
    }

}
