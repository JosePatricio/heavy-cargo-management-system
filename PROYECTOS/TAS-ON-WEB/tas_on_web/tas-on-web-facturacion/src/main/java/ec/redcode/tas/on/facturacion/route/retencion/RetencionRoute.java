package ec.redcode.tas.on.facturacion.route.retencion;

import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import javax.xml.bind.JAXBContext;

public class RetencionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        JaxbDataFormat dataFormat = new JaxbDataFormat(JAXBContext.newInstance(ComprobanteRetencion.class));
        dataFormat.setPrettyPrint(Boolean.TRUE);
        dataFormat.setIgnoreJAXBElement(false);

        from("activemq:queue:ec.net.redcode.tason.RetencionJMS")
                .log("Retencion Prefactura No ${body}")
                .to("retencionProcessor")
                .marshal(dataFormat)
                .to("signXmlProcessor")
                .to("recepcionComprobanteProcessor")
                .delay(10000)
                .to("autorizacionComprobantesProcessor")
                .to("retencionUpdateProcessor")
                .wireTap("seda:ec.net.redcode.tason.RetencionWriterJMS");;

        from("seda:ec.net.redcode.tason.RetencionWriterJMS")
                .to("retencionWriterProcessor")
                .to("xMLWriterProcessor")
                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");

    }

}
