package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.SecurityBean;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public class SecurityRestProcessor extends SecurityBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String userName = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(userName);
        Map<String, String> response = new ConcurrentHashMap<>();
        switch (operationName) {
            case MethodConstant.SECURITY_SOLICITAR_ACTUALIZAR_INFO_BANCARIA:
                String password = exchange.getIn().getBody(String.class);
                log.info("Intento de aprobar solicitud de actualizacion de datos de cuenta bancaria con el usuario "+userName);
                try{
                    solicitarActualizarInfoBancaria(usuario, password);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Puede modificar la información bancaria dentro de los próximos 15 minutos");
                } catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                }
                exchange.getIn().setBody(response(response));
                log.info("Intento de aprobar solicitud de actualizacion de datos de cuenta bancaria con el usuario "+userName+" response: " + response);
                break;
        }
    }

}
