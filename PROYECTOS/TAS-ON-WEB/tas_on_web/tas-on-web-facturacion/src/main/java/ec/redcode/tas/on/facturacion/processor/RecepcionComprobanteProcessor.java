package ec.redcode.tas.on.facturacion.processor;

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
import recepcion.ws.sri.gob.ec.RecepcionComprobantesOffline;
import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

import javax.xml.ws.WebServiceException;
import java.util.List;

public class RecepcionComprobanteProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(RecepcionComprobanteProcessor.class);

    private RecepcionComprobantesOffline service;

    @Setter private CatalogoItemService catalogoItemService;
    @Setter private EbillingService ebillingService;
    @Setter private FacturaManualService facturaManualService;

    protected void init(){
        List<CatalogoItem> catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(RecepcionComprobantesOffline.class);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress(catalogoItems.stream().filter(c -> "ENDPOINT RECEP".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        service = (RecepcionComprobantesOffline) factory.create();
    }

    @Override
    public void process(Exchange exchange){
        Object facturaManualHeader = exchange.getIn().getHeader("facturaManual");
        String xml = exchange.getIn().getBody(String.class);
        logger.info("Xml Sign {}", xml);
        if(facturaManualHeader != null) guardarXMLenviarFacturaManual((String) exchange.getIn().getHeader("claveAcceso"), xml);
        try{
            validarComprobante(exchange, xml);
        }catch (WebServiceException e){
            logger.warn("Error en RecepcionComprobanteProcessor.java: {}", e.getMessage());
            logger.info("Se intenta de nuevo validar el comprobante, intento 2");
            try{
                validarComprobante(exchange, xml);
            }catch (WebServiceException e2){
                logger.warn("Error en RecepcionComprobanteProcessor.java: {}", e2.getMessage());
                logger.info("Se intenta de nuevo validar el comprobante, intento 3");
                validarComprobante(exchange, xml);
            }
        }
    }

    private void validarComprobante(Exchange exchange, String xml){
        RespuestaSolicitud response = service.validarComprobante(xml.getBytes());
        analizarRespuesta(response, exchange);
    }

    private void analizarRespuesta(RespuestaSolicitud response, Exchange exchange){
        logger.info("Repuesta de la Solicitud de recepcion - estado: {}", response.getEstado());
        Object ebillingIdHeader = exchange.getIn().getHeader("ebilling");
        Object facturaManualHeader = exchange.getIn().getHeader("facturaManual");

        if ("DEVUELTA".equals(response.getEstado())){
            String mensaje = response.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
            String informacion = response.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
            logger.error(mensaje + " " + informacion);

            if(ebillingIdHeader != null) guardarErrorEbilling((Integer)ebillingIdHeader, mensaje, informacion);
            if(facturaManualHeader != null) guardarErrorFacturaManual((String) exchange.getIn().getHeader("claveAcceso"), mensaje, informacion);
        }
    }

    private void guardarErrorEbilling(Integer ebillingId, String mensaje, String informacion){
        Ebilling ebilling = ebillingService.read(ebillingId);
        String estado = mensaje + " - " + informacion;
        ebilling.setEbillingEstado(estado.length()>250 ? estado.substring(250) : estado);
        ebillingService.update(ebilling);
    }

    private void guardarXMLenviarFacturaManual(String claveAcceso, String xml){
        FacturaManual facturaManual = facturaManualService.read(claveAcceso);
        facturaManual.setFacturaManualXml(xml);
        facturaManualService.update(facturaManual);
    }

    private void guardarErrorFacturaManual(String claveAcceso, String mensaje, String informacion){
        FacturaManual facturaManual = facturaManualService.read(claveAcceso);
        String estado = mensaje + " - " + informacion;
        facturaManual.setFacturaManualEstado(estado.length()>250 ? estado.substring(250) : estado);
        facturaManualService.update(facturaManual);
    }

}
