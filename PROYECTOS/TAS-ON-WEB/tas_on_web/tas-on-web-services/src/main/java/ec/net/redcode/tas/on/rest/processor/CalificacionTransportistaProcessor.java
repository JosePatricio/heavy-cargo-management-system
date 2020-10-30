package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.rest.bean.CalificacionTansportistaBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CalificacionTransportistaProcessor extends CalificacionTansportistaBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String username = exchange.getIn().getHeader("user").toString();
        switch (operationName) {
            case MethodConstant.CALIFICACION_TRANSPORTISTA_BY_USER:
                List<CalificacionTransportistaDTO> catalogos = getCalificacionTransportistasByUser(username);
                exchange.getIn().setBody(response(catalogos));
                break;
            case MethodConstant.CALIFICACION_TRANSPORTISTA_UPDATE:
                CalificacionTransportistaDTO calificacionTransportistaDTO = exchange.getIn().getBody(CalificacionTransportistaDTO.class);
                Map<String, String> response = new ConcurrentHashMap<>();
                if(puedeCalificarTransportista(calificacionTransportistaDTO, username)){
                    actualizarCalificacion(calificacionTransportistaDTO, username);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Calificación actualizada correctamente");
                }else {
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", "No tiene permitido actualizar la calificación");
                }
                exchange.getIn().setBody(response(response));
                break;
        }
    }

}
