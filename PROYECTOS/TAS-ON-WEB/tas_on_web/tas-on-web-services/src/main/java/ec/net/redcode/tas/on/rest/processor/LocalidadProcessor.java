package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.persistence.entities.Localidad;
import ec.net.redcode.tas.on.rest.bean.LocalidadBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalidadProcessor extends LocalidadBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName) {
            case MethodConstant.LOCALIDAD_CREATE:
                Localidad localidadCreate = exchange.getIn().getBody(Localidad.class);
                createLocalidad(localidadCreate);
                Map<String, String> responseCreate = new HashMap<>();
                responseCreate.put("response", "Catalogo creado correctamente");
                exchange.getIn().setBody(response(responseCreate));
                break;
            case MethodConstant.LOCALIDAD_READ:
                int idLocalidad = exchange.getIn().getBody(Integer.class);
                Localidad localidad = readLocalidad(idLocalidad);
                exchange.getIn().setBody(response(localidad));
                break;
            case MethodConstant.LOCALIDAD_UPDATE:
                Localidad localidadUpdate = exchange.getIn().getBody(Localidad.class);
                updateLocalidad(localidadUpdate);
                Map<String, String> responseUpdate = new HashMap<>();
                responseUpdate.put("response", "Catalogo actualizado correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.LOCALIDAD_DELETE:
                Localidad localidadDelete = exchange.getIn().getBody(Localidad.class);
                deleteLocalidad(localidadDelete);
                Map<String, String> responseDelete = new HashMap<>();
                responseDelete.put("response", "Catalogo borrado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.LOCALIDAD_ALL_BY_PADRE:
                MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
                int idLocalidadPadre = Integer.valueOf(messageList.get(0).toString());
                int estado = Integer.valueOf(messageList.get(1).toString());
                List<Localidad> localidads = getLocalidadByPadre(idLocalidadPadre, estado);
                exchange.getIn().setBody(response(localidads));
                break;
            case MethodConstant.LOCALIDAD_ALL_CIUDADES:
                List<Localidad> ciudadesList = getAllCiudades();
                exchange.getIn().setBody(response(ciudadesList));
                break;
        }
    }

}
