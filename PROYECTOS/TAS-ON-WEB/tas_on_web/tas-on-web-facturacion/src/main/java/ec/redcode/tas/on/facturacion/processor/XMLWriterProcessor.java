package ec.redcode.tas.on.facturacion.processor;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLWriterProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(XMLWriterProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String numeroAutorizacion = (String) exchange.getIn().getHeader("numeroAutorizacion"); //lo agrega la clase AutorizacionComprobantesProcessor
        String xml = exchange.getIn().getBody(String.class);
        String path = TasOnUtil.getComprobantesPath() + exchange.getIn().getHeader("ruta") + File.separator; //"ruta" la agrega la clase RetencionWriterProcessor, RideWriterProcessor o RideNCWriterProcessor
        logger.info("Starting writing XML in {}", path);
        Path file = Paths.get(path, numeroAutorizacion.concat(".xml"));
        Files.write(file, xml.getBytes());
        llenarExchange(exchange);
        logger.info("Finish XML Reporte");
    }

    private void llenarExchange(Exchange exchange){
        Map<String, String> data = new ConcurrentHashMap<>();
        data.put("template", (String) exchange.getIn().getHeader("template")); //lo agrega la clase RetencionWriterProcessor, RideWriterProcessor o RideNCWriterProcessor
        data.put("ruc", (String) exchange.getIn().getHeader("ruc")); //lo agrega RetencionWriterProcessor, UpdateFacturaProcessor o UpdateNotaCreditoProcessor
        data.put("autorizacion", (String) exchange.getIn().getHeader("numeroAutorizacion")); //lo agrega la clase AutorizacionComprobantesProcessor
        data.put("tipo", (String) exchange.getIn().getHeader("tipo")); //lo agrega FacturacionRoute, FacturaProcessor, FacturaProveedorProcessor, EBillingProcessor, RetencionProcessor, UpdateNotaCreditoProcessor
        try{ //estos valores se agregan a la factura o NC para enviarlos como datos del correo
            data.put("numeroDocumento", (String) exchange.getIn().getHeader("numeroDocumento")); //lo agrega UpdateFacturaProcessor, UpdateNotaCreditoProcessor
            data.put("fechaEmision", (String) exchange.getIn().getHeader("fechaEmision")); // lo agrega UpdateFacturaProcessor, UpdateNotaCreditoProcessor
            data.put("clave", (String) exchange.getIn().getHeader("clave")); //lo agrega UpdateFacturaProcessor, UpdateNotaCreditoProcessor
            data.put("total", (String) exchange.getIn().getHeader("total")); //lo agrega UpdateFacturaProcessor, UpdateNotaCreditoProcessor
        }catch (Exception e){
            //e.printStackTrace();
        }
        if((exchange.getIn().getHeader("tipo")).equals("notaCredito")) //lo agrega UpdateNotaCreditoProcessor
        {
            data.put("numeroDocumentoModificado", (String) exchange.getIn().getHeader("numeroDocumentoModificado")); //lo agrega UpdateNotaCreditoProcessor

        }
        exchange.getIn().setBody(data);
    }

}
