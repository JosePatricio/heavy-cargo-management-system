package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Company;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.rest.bean.CommonBean;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class PublicServiceProcessor extends CommonBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName) {
            case MethodConstant.LOCALIDAD_ALL_BY_PADRE:
                int idLocalidadPadre = Integer.valueOf(messageList.get(0).toString());
                int estado = Integer.valueOf(messageList.get(1).toString());
                List<Localidad> localidads = getLocalidadByPadre(idLocalidadPadre, estado);
                exchange.getIn().setBody(response(localidads));
                break;
            case MethodConstant.CATALOGO_ITEM_ALL_BY_CATALOGO:
                int idCatalogo = exchange.getIn().getBody(Integer.class);
                List<CatalogoItem> catalogoItems = getCatalogoItemByCatalogo(idCatalogo);
                exchange.getIn().setBody(response(catalogoItems));
                break;
            case MethodConstant.CATALOGO_ITEM_READ:
                int idCatalogoItem = exchange.getIn().getBody(Integer.class);
                CatalogoItem catalogoItem = readCatalogo(idCatalogoItem);
                exchange.getIn().setBody(response(catalogoItem));
                break;
            case MethodConstant.PUBLIC_CREATE_EMPRESA_CLIENTE:
                Map<String, Object> data = exchange.getIn().getBody(Map.class);
                Map<String, String> mapResponse = new HashMap<>();
                try{
                    createEmpresaCliente(data);
                    mapResponse.put("response", Constant.RESPONSE_OK);
                    mapResponse.put("responseMessage", "Usuario creado correctamente");
                    exchange.getIn().setBody(response(mapResponse));
                    ObjectMapper mapper = new ObjectMapper();
                    Usuario usuario = mapper.convertValue( data.get("cliente"), Usuario.class);
                    exchange.getIn().setHeader("idDocumento", usuario.getUsuarioIdDocumento());
                    if (usuario.getUsuarioPrincipal())
                        exchange.getIn().setHeader("email", Boolean.TRUE);
                    else
                        exchange.getIn().setHeader("email", Boolean.FALSE);
                }catch (TasOnException e){
                    mapResponse.put("response", Constant.RESPONSE_ERROR);
                    mapResponse.put("responseMessage", e.getDeveloperMessage());
                    exchange.getIn().setBody(response(mapResponse));
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }
                break;
            case MethodConstant.CLIENT_GET_CLIENTE_BY_TIPO:
                int tipoCliente = exchange.getIn().getBody(Integer.class);
                List<Company> clientesByTipo = getAllClienteByTipo(tipoCliente);
                exchange.getIn().setBody(response(clientesByTipo));
                break;
            case MethodConstant.USUARIO_CREATE:
                Usuario usuarioCreate = exchange.getIn().getBody(Usuario.class);
                usuarioCreate.setUsuarioPrincipal(Boolean.FALSE);
                usuarioCreate.setUsuarioEstado(Constant.USER_PENDING);
                createUsuario(usuarioCreate);
                Map<String, String> responseCreate = new HashMap<>();
                responseCreate.put("response", Constant.RESPONSE_OK);
                responseCreate.put("responseMessage", "Usuario creado correctamente");
                exchange.getIn().setBody(response(responseCreate));
                exchange.getIn().setHeader("idDocumento", usuarioCreate.getUsuarioIdDocumento());
                if (usuarioCreate.getUsuarioPrincipal())
                    exchange.getIn().setHeader("email", Boolean.TRUE);
                else
                    exchange.getIn().setHeader("email", Boolean.FALSE);
                break;
            case MethodConstant.USUARIO_BY_USERNAME:
                String username = exchange.getIn().getBody(String.class);
                Usuario usuarioss = getUsarioByUsername(username);
                setMessageToResponse(usuarioss, exchange, "Usuario no existe");
                break;
            case MethodConstant.USUARIO_BY_EMAIL:
                String email = exchange.getIn().getBody(String.class);
                Usuario usuarioEmail = getUsuarioByEmail(email);
                setMessageToResponse(usuarioEmail, exchange, "Usuario no existe");
                break;
            case MethodConstant.CLIENT_READ:
                String ruc = exchange.getIn().getBody(String.class);
                Cliente clienteRead = readCliente(ruc);
                setMessageToResponse(clienteRead, exchange, "Empresa no existe");
                break;
            case MethodConstant.CLIENT_TRANSPORTE_READ:
                String rucEmpresaTransporte = exchange.getIn().getBody(String.class);
                Cliente clienteTransporteRead = readCliente(rucEmpresaTransporte);
                if(clienteTransporteRead != null && clienteTransporteRead.getClienteTipoCliente() != Constant.CLIENTE_EMPRESA_TRANSPORTISTA &&
                        clienteTransporteRead.getClienteTipoCliente() != Constant.CLIENTE_EMPRESA_TRANSPORTISTA_INDEPENDIENTE &&
                        clienteTransporteRead.getClienteTipoCliente() != Constant.CLIENTE_EMPRESA_TRANSPORTISTA_ENVIOS)
                    clienteTransporteRead = null;
                setMessageToResponse(clienteTransporteRead, exchange, "Empresa no existe");
                break;
            case MethodConstant.USUARIO_READ:
                Usuario usuarioRead = readUsuario(exchange.getIn().getBody(String.class));
                setMessageToResponse(usuarioRead, exchange, "Usuario no existe");
                break;
            case MethodConstant.SOLICITUD_ALL_SOLICITUDES:
                int estadossss = exchange.getIn().getBody(Integer.class);
                List<Solicitudes> solicitudss = getSolicitudesEnvio(estadossss, Constant.PUBLIC_USER);
                exchange.getIn().setBody(response(solicitudss));
                break;
            case MethodConstant.SOLICITUD_SOLICITUD:
                String idSolicitud = exchange.getIn().getBody(String.class);
                Solicitud solicitud = getSolicitud(idSolicitud, null);
                exchange.getIn().setBody(response(solicitud));
                break;
            case MethodConstant.VEHICULO_PLACA:
                String placa = exchange.getIn().getBody(String.class);
                Vehiculo vehiculoPlaca = getVehiculoByPlaca(placa);
                exchange.getIn().setBody(response(vehiculoPlaca));
                break;
            case MethodConstant.USUARIO_RESTABLECER:
                String correo = messageList.get(0).toString();
                String identificador = messageList.get(1).toString();
                Map<String, String> response = new HashMap<>();
                try{
                    validarCambioPassword(identificador, correo);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Se enviará a su correo los datos de recuperación");
                    exchange.getIn().setBody(response(response));
                    exchange.getIn().setHeader("idDocumento", identificador);
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                    exchange.getIn().setBody(response(response));
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }
                break;
            case MethodConstant.GET_HOME:
                exchange.getIn().setBody(response(getEstadisticaHome()));
                break;
        }
    }

    //Permite devolver el objeto de la consulta
    private void setMessageToResponse(Object object, Exchange exchange, String message){
        if (object == null){
            Map<String, String> errors = new HashMap<>();
            errors.put("response", Constant.RESPONSE_ERROR);
            errors.put("responseMessage", message);
            exchange.getIn().setBody(response(errors));
        }else {
            exchange.getIn().setBody(response(object));
        }
    }

}
