package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.CipherHash;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;

import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

public class SecurityBean extends TasOnResponse {

    @Setter protected ClienteService clienteService;
    @Setter protected UsuarioService usuarioService;

    protected void solicitarActualizarInfoBancaria(Usuario usuario, String password) throws TasOnException{
        try{
            Cliente cliente = usuario.getClienteByUsuarioRuc();
            int intentoActualizar = cliente.getClienteBancoIntentoActualizar() == null ? 0 : cliente.getClienteBancoIntentoActualizar();
            CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
            if(!usuario.getUsuarioContrasenia().equals(cipherHash.hashString(password))){
                cliente.setClienteBancoIntentoActualizar(++intentoActualizar);
                cliente.setClienteBancoSolicActualizar(null);
                clienteService.updateCliente(cliente);
                validarBloqueaUsuario(usuario, intentoActualizar);
                throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Contraseña incorrecta. Intento "+intentoActualizar+" de 3");
            }
            if(usuario.getUsuarioTipoUsuario() != Constant.USER_DRIVER_ADMIN){
                cliente.setClienteBancoIntentoActualizar(++intentoActualizar);
                cliente.setClienteBancoSolicActualizar(null);
                clienteService.updateCliente(cliente);
                validarBloqueaUsuario(usuario, 3);
                throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Solo el usuario administrador puede actualizar esta información");
            }
            cliente.setClienteBancoSolicActualizar(new Timestamp(new Date().getTime()));
            clienteService.updateCliente(cliente);
        }catch (NoSuchPaddingException | NoSuchAlgorithmException e){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No se pudo aprobar la actualización de los datos. Intente más tarde");
        }

    }
    private void validarBloqueaUsuario(Usuario usuario, int intento){
        if(intento >= 3){
            usuario.setUsuarioEstado(Constant.USER_BLOCKED);
            usuarioService.updateUsuario(usuario);
            throw ExceptionUtils.toForbiddenException(null, null);
        }
    }

}
