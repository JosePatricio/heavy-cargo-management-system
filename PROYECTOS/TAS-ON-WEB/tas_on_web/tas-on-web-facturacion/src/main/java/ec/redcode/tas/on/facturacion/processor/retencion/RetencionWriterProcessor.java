package ec.redcode.tas.on.facturacion.processor.retencion;

import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import ec.gob.sri.comprobantes.modelo.reportes.ComprobanteRetencionReporte;
import ec.net.redcode.tas.on.common.Constant;
import ec.redcode.tas.on.facturacion.reporte.ReportUtilTasOn;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringReader;

public class RetencionWriterProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(ComprobanteRetencion.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ComprobanteRetencion retencion = (ComprobanteRetencion) unmarshaller.unmarshal(sr);
        ComprobanteRetencionReporte reporte = new ComprobanteRetencionReporte(retencion);
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        ReportUtilTasOn report = new ReportUtilTasOn();
        String numeroAutorizacion = (String) exchange.getIn().getHeader("numeroAutorizacion");
        String path = System.getProperty("user.home") + "/comprobantes/retenciones/" + numeroAutorizacion + ".pdf";
        report.generateComprobante(path, reporte, numeroAutorizacion, fechaAutorizacion.toString() );
        exchange.getIn().setHeader("ruta", "retenciones");
        exchange.getIn().setHeader("template", Constant.EMAIL_RETENCION);
        exchange.getIn().setHeader("ruc", retencion.getInfoCompRetencion().getIdentificacionSujetoRetenido());
    }

}
