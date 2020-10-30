package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.User;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.UsuarioBean;
import ec.net.redcode.tas.on.rest.dto.Password;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuariosProcessor extends UsuarioBean implements Processor   {

    private static final Logger logger = LoggerFactory.getLogger(UsuariosProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        try {
            MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
            switch (operationName) {
                case MethodConstant.USUARIO_LOGIN:
                    String user = messageList.get(0).toString();
                    logger.info("Intento de login con el usuario "+user);
                    String password = messageList.get(1).toString();
                    Map<String, String> response = new HashMap<>();
                    try{
                        response = login(user, password, null,  exchange);
                        response.put("response", Constant.RESPONSE_OK);
                    } catch (TasOnException e){
                        response.put("response", e.getMessage());
                        response.put("responseMessage", e.getDeveloperMessage());
                    }
                    exchange.getIn().setBody(response(response));
                    logger.info("Login con el usuario "+user+" response: " + response);
                    break;
                case MethodConstant.USUARIO_LOGIN_MOVIL:
                    String userMovil = messageList.get(0).toString();
                    logger.info("Intento de login movil con el usuario "+userMovil);
                    String passwordMovil = messageList.get(1).toString();
                    String fcmToken = messageList.get(2).toString();
                    Map<String, String> responseMovil = new HashMap<>();
                    try{
                        responseMovil = login(userMovil, passwordMovil, fcmToken, exchange);
                    } catch (TasOnException e){
                        responseMovil.put("response", e.getMessage());
                        responseMovil.put("responseMessage", e.getDeveloperMessage());
                    }
                    exchange.getIn().setBody(response(responseMovil));
                    logger.info("Login con el usuario "+userMovil+" response: " + responseMovil);
                    break;
                case MethodConstant.USUARIO_CREATE:
                    Usuario usuarioCreate = exchange.getIn().getBody(Usuario.class);
                    usuarioCreate.setUsuarioEstado(Constant.USER_CREATED);
                    createUsuario(usuarioCreate);
                    Map<String, String> responseCreate = new HashMap<>();
                    responseCreate.put("response", Constant.RESPONSE_OK);
                    responseCreate.put("responseMessage", "Usuario creado correctamente");
                    exchange.getIn().setHeader("idDocumento", usuarioCreate.getUsuarioIdDocumento());
                    exchange.getIn().setHeader("email", Boolean.TRUE);
                    exchange.getIn().setBody(response(responseCreate));
                    break;
                case MethodConstant.USUARIO_READ:
                    Usuario usuario = readUsuario(exchange.getIn().getBody(String.class));
                    if (usuario == null){
                        Map<String, String> errors = new HashMap<>();
                        errors.put("response", Constant.RESPONSE_ERROR);
                        errors.put("responseMessage", "Usuario no existe");
                        exchange.getIn().setBody(response(errors));
                    }else {
                        exchange.getIn().setBody(response(usuario));
                    }
                    break;
                case MethodConstant.USUARIO_UPDATE:
                    Map<String, String> responseUpdate = new HashMap<>();
                    responseUpdate.put("response", Constant.RESPONSE_ERROR);
                    responseUpdate.put("responseMessage", "Error al actualizar");
                    exchange.getIn().setBody(response(responseUpdate));
                    break;
                case MethodConstant.USUARIO_DELETE:
                    Map<String, String> responseDelete = new HashMap<>();
                    responseDelete.put("response", Constant.RESPONSE_ERROR);
                    responseDelete.put("responseMessage", "El usuario no ha podido ser borrado");
                    exchange.getIn().setBody(response(responseDelete));
                    break;
                case MethodConstant.USUARIO_ALL_BY_TIPO:
                    int tipoUsuario = Integer.valueOf(messageList.get(0).toString());
                    int estado = Integer.valueOf(messageList.get(1).toString());
                    List<Usuario> usuarios = getAllUsuariosByTipoUsuario(tipoUsuario, Integer.valueOf(estado));
                    exchange.getIn().setBody(response(usuarios));
                    break;
                case MethodConstant.USUARIO_BY_USERNAME:
                    String username = exchange.getIn().getBody(String.class);
                    Usuario usuarioss = getUsarioByUsername(username);
                    if (usuarioss == null){
                        Map<String, String> errors = new HashMap<>();
                        errors.put("response", Constant.RESPONSE_ERROR);
                        errors.put("responseMessage", "Usuario no existe");
                        exchange.getIn().setBody(response(errors));
                    }else {
                        exchange.getIn().setBody(response(usuarioss));
                    }
                    break;
                case MethodConstant.USUARIO_UPDATE_PASSWORD:
                    Password passwordUpdate = exchange.getIn().getBody(Password.class);
                    logger.info("Intento de cambio de password del usuario "+passwordUpdate.getUsername());
                    updatePassword(passwordUpdate);
                    Map<String, String> mapResponse = new HashMap<>();
                    mapResponse.put("response", Constant.RESPONSE_OK);
                    mapResponse.put("responseMessage", "Contraseña actualizada correctamente");
                    exchange.getIn().setBody(response(mapResponse));
                    logger.info("Cambio de password del usuario "+passwordUpdate.getUsername()+" response: " + mapResponse);
                    break;
                case MethodConstant.USUARIO_BY_EMAIL:
                    String email = exchange.getIn().getBody(String.class);
                    Usuario usuarioEmail = getUsuarioByEmail(email);
                    if ( usuarioEmail == null){
                        Map<String, String> errors = new HashMap<>();
                        errors.put("response", Constant.RESPONSE_ERROR);
                        errors.put("responseMessage", "Usuario no existe");
                        exchange.getIn().setBody(response(errors));
                    }else {
                        exchange.getIn().setBody(response(usuarioEmail));
                    }
                    break;
                case MethodConstant.USUARIO_ALL_BY_TIPO_ESTADO:
                    int tipoUsuarios = Integer.valueOf(messageList.get(0).toString());
                    int estados = Integer.valueOf(messageList.get(1).toString());
                    List<User> users = getUsuariosByTipoUsuarioAndEstado(tipoUsuarios, estados);
                    exchange.getIn().setBody(users);
                    break;
                case MethodConstant.USUARIO_ALL_BY_EMPRESA_TIPO_ESTADO:
                    String rucEmpresa = messageList.get(0).toString();
                    int usuarioTipo = Integer.valueOf(messageList.get(1).toString());
                    int usuarioEstado = Integer.valueOf(messageList.get(2).toString());
                    List<User> usuariosList = getUsuariosByEmpresaTipoUsuarioAndEstado(rucEmpresa, usuarioTipo, usuarioEstado);
                    exchange.getIn().setBody(usuariosList);
                    break;
                case MethodConstant.USUARIO_ACTIVATE:
                    Usuario usuarioActivate = exchange.getIn().getBody(Usuario.class);
                    activateUser(usuarioActivate, exchange);
                    Map<String, String> responseActivate = new HashMap<>();
                    responseActivate.put("response", Constant.RESPONSE_OK);
                    responseActivate.put("responseMessage", "Operación exitosa");
                    exchange.getIn().setBody(response(responseActivate));
                    break;
                case MethodConstant.USUARIO_EMPRESA:
                    String empresa = messageList.get(0).toString();
                    int estadosss = Integer.valueOf(messageList.get(1).toString());
                    List<Usuario> usuariosEmpresa = getUsuarioByEmpresaAndEstado(empresa, estadosss);
                    exchange.getIn().setBody(response(usuariosEmpresa));
                    break;
            }
        }catch (TasOnException e){
            webFault(e, exchange, Boolean.TRUE);
        }
    }

}
