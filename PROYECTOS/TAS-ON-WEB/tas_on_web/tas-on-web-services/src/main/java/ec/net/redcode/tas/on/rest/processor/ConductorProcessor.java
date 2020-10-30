package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.persistence.entities.Conductor;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.ConductorBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConductorProcessor extends ConductorBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String username = exchange.getIn().getHeader("user").toString();
        Usuario usuarioPeticion = usuarioService.getUsuariosByUserName(username);
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName){
            case MethodConstant.CONDUCTOR_CREATE:
                Conductor conductor = exchange.getIn().getBody(Conductor.class);
                create(usuarioPeticion, conductor);
                Map<String, String> response = new HashMap<>();
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Conductor creado correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.CONDUCTOR_READ:
                int idConductor = exchange.getIn().getBody(Integer.class);
                Conductor conductorRead = read(idConductor);
                exchange.getIn().setBody(response(conductorRead));
                break;
            case MethodConstant.CONDUCTOR_UPDATE:
                Conductor conductorUpdate = exchange.getIn().getBody(Conductor.class);
                update(usuarioPeticion, conductorUpdate);
                Map<String, String> responseUpdate = new HashMap<>();
                responseUpdate.put("response", Constant.RESPONSE_OK);
                responseUpdate.put("responseMessage", "Conductor actualizado correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.CONDUCTOR_DELETE:
                Conductor conductorDelete = exchange.getIn().getBody(Conductor.class);
                conductorDelete.setConductorEstado(0);
                update(usuarioPeticion, conductorDelete);
                Map<String, String> responseDelete = new HashMap<>();
                responseDelete.put("response", Constant.RESPONSE_OK);
                responseDelete.put("responseMessage", "Conductor eliminado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.CONDUCTOR_CONDUCTORES:
                List<Conductor> conductors = getConductoresByUser(usuarioPeticion);
                exchange.getIn().setBody(response(conductors));
                break;
            case MethodConstant.CONDUCTOR_GET_BY_USUARIO_Y_SOLICITUD:
                String solicitudId = exchange.getIn().getBody(String.class);
                List<Conductor> conductores = getConductoresByUsuarioYSolicitud(usuarioPeticion, solicitudId);
                exchange.getIn().setBody(response(conductores));
                break;
        }
    }

}
