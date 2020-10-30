package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.entities.Vehiculo;
import ec.net.redcode.tas.on.rest.bean.VehiculoBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiculoProcessor extends VehiculoBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String username = exchange.getIn().getHeader("user").toString();
        Usuario usuarioPeticion = usuarioService.getUsuariosByUserName(username);
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName){
            case MethodConstant.VEHICULO_CREATE:
                Vehiculo vehiculo = exchange.getIn().getBody(Vehiculo.class);
                create(usuarioPeticion, vehiculo);
                Map<String, String> response = new HashMap<>();
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Vehiculo creado correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.VEHICULO_READ:
                int idVehiculo = exchange.getIn().getBody(Integer.class);
                Vehiculo vehiculoRead = read(idVehiculo);
                exchange.getIn().setBody(response(vehiculoRead));
                break;
            case MethodConstant.VEHICULO_UPDATE:
                Vehiculo vehiculoUpdate = exchange.getIn().getBody(Vehiculo.class);
                update(usuarioPeticion, vehiculoUpdate);
                Map<String, String> responseUpdate = new HashMap<>();
                responseUpdate.put("response", Constant.RESPONSE_OK);
                responseUpdate.put("responseMessage", "Vehiculo actualizado correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.VEHICULO_DELETE:
                Vehiculo vehiculoDelete = exchange.getIn().getBody(Vehiculo.class);
                vehiculoDelete.setVehiculoEstado(0);
                update(usuarioPeticion, vehiculoDelete);
                Map<String, String> responseDelete = new HashMap<>();
                responseDelete.put("response", Constant.RESPONSE_OK);
                responseDelete.put("responseMessage", "Vehiculo borrado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.VEHICULO_PLACA:
                String placa = exchange.getIn().getBody(String.class);
                Vehiculo vehiculoPlaca = getVehiculoByPlaca(placa);
                exchange.getIn().setBody(response(vehiculoPlaca));
                break;
            case MethodConstant.VEHICULO_VEHICULOS:
                List<Vehiculo> vehiculos = getVehiculosByUser(usuarioPeticion);
                exchange.getIn().setBody(response(vehiculos));
                break;
            case MethodConstant.VEHICULO_GET_BY_USUARIO_Y_SOLICITUD:
                String solicitudId = exchange.getIn().getBody(String.class);
                vehiculos = getVehiculosByUsuarioYSolicitud(usuarioPeticion, solicitudId);
                exchange.getIn().setBody(response(vehiculos));
                break;

        }
    }

}
