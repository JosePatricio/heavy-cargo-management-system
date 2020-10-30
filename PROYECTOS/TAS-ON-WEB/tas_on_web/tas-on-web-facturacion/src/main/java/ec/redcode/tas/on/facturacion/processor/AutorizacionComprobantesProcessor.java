package ec.redcode.tas.on.facturacion.processor;

import autorizacion.ws.sri.gob.ec.Autorizacion;
import autorizacion.ws.sri.gob.ec.AutorizacionComprobantesOffline;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.entities.Ebilling;
import ec.net.redcode.tas.on.persistence.entities.FacturaManual;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import ec.net.redcode.tas.on.persistence.service.EbillingService;
import ec.net.redcode.tas.on.persistence.service.FacturaManualService;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AutorizacionComprobantesProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(RecepcionComprobanteProcessor.class);

    private AutorizacionComprobantesOffline service;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private EbillingService ebillingService;
    @Setter private FacturaManualService facturaManualService;

    public void init(){
        List<CatalogoItem> catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AutorizacionComprobantesOffline.class);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress(catalogoItems.stream().filter(c -> "ENDPOINT AUTH".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        service = (AutorizacionComprobantesOffline) factory.create();
    }

    @Override
    public void process(Exchange exchange){
        try{
            autorizarComprobante(exchange);
        }catch (WebServiceException e){
            logger.warn("Error en AutorizacionComprobantesProcessor.java:" + e.getMessage());
            logger.info("Se intenta de nuevo autorizar el comprobante, intento 2");
            try{
                autorizarComprobante(exchange);
            }catch (WebServiceException e2){
                logger.warn("Error en AutorizacionComprobantesProcessor.java:" + e2.getMessage());
                logger.info("Se intenta de nuevo autorizar el comprobante, intento 3");
                autorizarComprobante(exchange);
            }
        }

    }

    private void autorizarComprobante(Exchange exchange){
        String claveAcceso = exchange.getIn().getHeader("claveAcceso").toString();
        RespuestaComprobante response = service.autorizacionComprobante(claveAcceso);
        analizarRespuestaComprobante(response, exchange);

    }

    private void analizarRespuestaComprobante(RespuestaComprobante response, Exchange exchange){
        Object ebillingIdHeader = exchange.getIn().getHeader("ebilling");
        Object facturaManualHeader = exchange.getIn().getHeader("facturaManual");
        logger.info("Repuesta de la Solicitud de autorizacion de comprobante - estado {}", response.getAutorizaciones());
        RespuestaComprobante.Autorizaciones autorizaciones = response.getAutorizaciones();
        for (Autorizacion autorizacion: autorizaciones.getAutorizacion()) {
            logger.info("Autorizacion estado: {}", autorizacion.getEstado());
            if ("AUTORIZADO".equals(autorizacion.getEstado())) {
                exchange.getIn().setHeader("fechaAutorizacion", autorizacion.getFechaAutorizacion());
                exchange.getIn().setHeader("numeroAutorizacion", autorizacion.getNumeroAutorizacion());
                exchange.getIn().setHeader("estado", autorizacion.getEstado());
                exchange.getIn().setHeader("comprobante", autorizacion.getComprobante());
                if(ebillingIdHeader != null) actualizarEbilling((Integer)ebillingIdHeader, autorizacion.getFechaAutorizacion(), autorizacion.getComprobante());
                if(facturaManualHeader != null) actualizarFacturaManual((String) exchange.getIn().getHeader("claveAcceso"), autorizacion.getFechaAutorizacion(), autorizacion.getComprobante());
            }  else {
                AtomicReference<String> error = new AtomicReference<>();
                autorizacion.getMensajes().getMensaje().forEach(m -> error.set(m.getIdentificador().concat(" ").concat(m.getInformacionAdicional()).concat(" ").concat(m.getMensaje())));
                logger.error(error.get());
                if(ebillingIdHeader != null) actualizarEbilling((Integer)ebillingIdHeader, autorizacion.getEstado(), error.get());
                if(facturaManualHeader != null) actualizarFacturaManual((String) exchange.getIn().getHeader("claveAcceso"), autorizacion.getEstado(), error.get());
            }
        }
    }

    private void actualizarEbilling(Integer ebillingId, XMLGregorianCalendar fechaAutorizacion, String comprobante){
        Ebilling ebilling = ebillingService.read(ebillingId);
        ebilling.setEbillingEstado("AUTORIZADO");
        ebilling.setEbillingFechaAutorizacion(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        ebilling.setEbillingXml(comprobante);
        ebillingService.update(ebilling);
    }

    private void actualizarFacturaManual(String claveAcceso, XMLGregorianCalendar fechaAutorizacion, String comprobante){
        FacturaManual facturaManual = facturaManualService.read(claveAcceso);
        facturaManual.setFacturaManualEstado("AUTORIZADO");
        facturaManual.setFacturaManualFechaAutorizacion(new Timestamp(fechaAutorizacion.toGregorianCalendar().getTime().getTime()));
        facturaManual.setFacturaManualXml(comprobante);
        facturaManualService.update(facturaManual);
    }

    private void actualizarEbilling(Integer ebillingId, String estado, String mensaje){
        Ebilling ebilling = ebillingService.read(ebillingId);
        ebilling.setEbillingEstado(estado + " - " + mensaje);
        ebillingService.update(ebilling);
    }

    private void actualizarFacturaManual(String claveAcceso, String estado, String mensaje){
        FacturaManual facturaManual = facturaManualService.read(claveAcceso);
        facturaManual.setFacturaManualEstado(estado + " - " + mensaje);
        facturaManualService.update(facturaManual);
    }

}
