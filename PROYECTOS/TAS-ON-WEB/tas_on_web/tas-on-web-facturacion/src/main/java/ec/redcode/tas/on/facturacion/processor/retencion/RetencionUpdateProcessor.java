package ec.redcode.tas.on.facturacion.processor.retencion;

import ec.net.redcode.tas.on.persistence.entities.FacturaRetencion;
import ec.net.redcode.tas.on.persistence.service.FacturaRetencionService;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;

public class RetencionUpdateProcessor implements Processor {

    @Setter private FacturaRetencionService facturaRetencionService;

    @Override
    public void process(Exchange exchange) throws Exception {
        FacturaRetencion facturaRetencion = new FacturaRetencion();
        facturaRetencion.setFacturaRetencionId(exchange.getIn().getHeader("claveAcceso").toString());
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        facturaRetencion.setFacturaRetencionFechaAutorizacion(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        if ("factura".equals(exchange.getIn().getHeader("tipo")))
            facturaRetencion.setFacturaRetencionFacturaId((String) exchange.getIn().getHeader("prefactura"));
        else{
            try{
                facturaRetencion.setFacturaRetencionFacturaProveedorId((Integer) exchange.getIn().getHeader("prefactura"));
            } catch (Exception e){
                facturaRetencion.setFacturaRetencionFacturaProveedorId(Integer.parseInt((String) exchange.getIn().getHeader("prefactura")));
            }
        }
        facturaRetencion.setFacturaRetencionEstado(exchange.getIn().getHeader("estado").toString());
        facturaRetencion.setFacturaRetencionXml(exchange.getIn().getHeader("comprobante").toString());
        facturaRetencionService.create(facturaRetencion);
    }

}
