package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.ResponseAPI;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.rest.bean.ServiceApiBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.Map;

public class ServiceApiProcessor extends ServiceApiBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String username = exchange.getIn().getHeader("user").toString();
        try {
            switch (operationName) {
                case "getToken":
                    String token = getToken(username);
                    Map<String, String> responseToken = new HashMap<>();
                    responseToken.put("token", token);
                    exchange.getIn().setBody(response(responseToken));
                    break;
                case "createSolicitudEnvio":
                    SolicitudEnvio solicitudEnvio = exchange.getIn().getBody(SolicitudEnvio.class);
                    String solicitud = createSolicitudEnvio(solicitudEnvio, username);
                    Map<String, String> response = new HashMap<>();
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("solicitud", solicitud);
                    exchange.getIn().setBody(response(response));
                    break;
                case "getSolicitudEnvio":
                    String idSolicitud = exchange.getIn().getBody(String.class);
                    ResponseAPI responseAPI = getSolicitudEnvio(idSolicitud);
                    exchange.getIn().setBody(response(responseAPI));
                    break;
            }
        } catch (TasOnException e){
            webFault(e, exchange, Boolean.TRUE);
        }
    }

}
