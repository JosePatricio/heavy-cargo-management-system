package ec.redcode.tas.on.facturacion.route;

import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito;
import ec.net.redcode.tas.on.common.dto.EBillingRequest;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import lombok.Setter;
import org.apache.camel.LoggingLevel;
import com.google.gson.Gson;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import javax.xml.bind.JAXBContext;
import java.util.List;

public class FacturacionRoute extends RouteBuilder {

    @Setter private CatalogoItemService catalogoItemService;
    private String policyFacturacion;
    private String policyEmisionNotasCredito;
    private String pathXML;
    private String pathXMLProcesed;

    public void initCron(){
        List<CatalogoItem> catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        policyFacturacion = catalogoItems.stream().filter(c -> "POLICY".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        policyEmisionNotasCredito = catalogoItems.stream().filter(c -> "POLICY-NC".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        pathXML = TasOnUtil.getComprobantesInboxPath();
        pathXMLProcesed = TasOnUtil.getComprobantesOutboxPath();
    }

    @Override
    public void configure() throws Exception {

        JaxbDataFormat dataFormatFactura = new JaxbDataFormat(JAXBContext.newInstance(Factura.class));
        dataFormatFactura.setPrettyPrint(Boolean.TRUE);
        dataFormatFactura.setIgnoreJAXBElement(false);

        JaxbDataFormat dataFormatNotaCredito = new JaxbDataFormat(JAXBContext.newInstance(NotaCredito.class));
        dataFormatNotaCredito.setPrettyPrint(Boolean.TRUE);
        dataFormatNotaCredito.setIgnoreJAXBElement(false);

        //from("quartz2://myGroup/myTimerName?cron=0+0/5+*+*+*+?")
        from("quartz2://myGroup/myTimerName?cron=" + policyFacturacion)
                .transacted()
                .to("facturasProcessor")
                .split(body())
                    .process(exchange -> {
                        Factura factura = exchange.getIn().getBody(Factura.class);
                        exchange.getIn().setHeader("claveAcceso", factura.getInfoTributaria().getClaveAcceso());
                        exchange.getIn().setHeader("tipo", "factura");
                    })
                    .marshal(dataFormatFactura)
                    .to("signXmlProcessor")
                    .to("recepcionComprobanteProcessor")
                    .delay(10000)
                    .to("autorizacionComprobantesProcessor")
                    .to("updateFacturaProcessor")
                    .wireTap("seda:ec.net.redcode.tason.FacturasJMS");

        from("seda:ec.net.redcode.tason.FacturasJMS")
                .to("rideWriterProcessor")
                .to("reporteFacturaProcessor")
                .to("xMLWriterProcessor")
                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");


        from("quartz2://myGroup/generateNCTimer?cron=" + policyEmisionNotasCredito)
                .transacted()
                .to("notaCreditoProcessor")
                .split(body())
                    .process(exchange -> {
                        NotaCredito notaCredito = exchange.getIn().getBody(NotaCredito.class);
                        exchange.getIn().setHeader("claveAcceso", notaCredito.getInfoTributaria().getClaveAcceso());
                    })
                    .marshal(dataFormatNotaCredito)
                    .to("signXmlProcessor")
                    .to("recepcionComprobanteProcessor")
                    .delay(10000)
                    .to("autorizacionComprobantesProcessor")
                    .to("updateNotaCreditoProcessor")
                    .wireTap("seda:ec.net.redcode.tason.NCJMS");

        from("seda:ec.net.redcode.tason.NCJMS")
                .to("rideNCWriterProcessor")
                .to("xMLWriterProcessor")
                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");

        from("file:" + pathXML).errorHandler(deadLetterChannel("activemq:queue:dlq").maximumRedeliveries(2)
                .redeliveryDelay(15000).useOriginalMessage().logStackTrace(true).loggingLevel(LoggingLevel.ERROR))
                .log("Starting generate Invoice")
                .unmarshal(dataFormatFactura)
                .to("facturaXMLProcessor")
                .to("signXmlProcessor")
                .to("recepcionComprobanteProcessor")
                .delay(10000)
                .to("autorizacionComprobantesProcessor")
                .log("Finish generate Invoice ")
                .to("file:" + pathXMLProcesed);

        from("activemq:queue:ec.net.redcode.tason.FacturacionManual")
                .errorHandler(deadLetterChannel("activemq:queue:dlq").maximumRedeliveries(2)
                .redeliveryDelay(15000).useOriginalMessage().logStackTrace(true).loggingLevel(LoggingLevel.ERROR))
                .process(exchange -> {
                    String contenido = (String) exchange.getIn().getHeader("facturaManual");
                    Factura factura = new Gson().fromJson(contenido, Factura.class);
                    exchange.getIn().setBody(factura);
                })
                .log("Starting generating manual invoice")
                .to("facturaXMLProcessor")
                .to("signXmlProcessor")
                .to("recepcionComprobanteProcessor")
                .delay(10000)
                .to("autorizacionComprobantesProcessor")
                .log("Finish generating manual invoice");

        /*
        * Recibe en el header: contenido -> JSON de EbillingRequest
        */
        from("activemq:queue:ec.net.redcode.tason.Ebilling")
                .process(exchange -> {
                    String contenido = (String) exchange.getIn().getHeader("contenido");
                    EBillingRequest eBillingRequest = new Gson().fromJson(contenido, EBillingRequest.class);
                    exchange.getIn().setBody(eBillingRequest);
                })
                .to("eBillingProcessor")
                .marshal(dataFormatFactura)
                .wireTap("activemq:queue:ec.net.redcode.tason.SignAndSendEbilling");


        /*
        * Recibe en header: claveFirma, ebilling -> ebillingId, claveAcceso, tipo -> factura, signature -> firma,
        *                   adquiriente -> razon social, xml (opcional si el xml no esta en el body)
        * Recibe en el body un String -> xml de la factura a enviar (lo puede recibir en el header)
        */
        from("activemq:queue:ec.net.redcode.tason.SignAndSendEbilling")
                .process(exchange -> {
                    Object xmlObject = exchange.getIn().getHeader("xml");
                    if(xmlObject!=null){
                        exchange.getIn().setBody(xmlObject);
                    }
                })
                .to("signEBillingProcessor")
                .to("recepcionComprobanteProcessor")
                .delay(10000)
                .to("autorizacionComprobantesProcessor")
                .to("ebillingRideWriterProcessor")
                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");

    }

}
