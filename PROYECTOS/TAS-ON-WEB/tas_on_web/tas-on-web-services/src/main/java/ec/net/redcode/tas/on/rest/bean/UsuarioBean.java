package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.CipherHash;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.User;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.dto.Password;
import org.apache.camel.Exchange;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;

import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UsuarioBean extends CommonBean {

    private OAuthDataProvider oAuthDataProvider;

    protected Map<String, String> login(String username, String password, String fcmToken, Exchange exchange) throws TasOnException {
        Usuario usuario;
        Map<String, String> response = new HashMap<>();
        try {
            CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
            usuario = usuarioService.getUsuarioByUserNameAndPassword(username, cipherHash.hashString(password));
            if (usuario == null) {
                throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Usuario o contraseña incorrectos");
            }
            if (Constant.USER_INACTIVE == usuario.getUsuarioEstado())
                throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Usuario inactivo");
            if (Constant.USER_BLOCKED == usuario.getUsuarioEstado())
                throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Usuario bloqueado");
            if (fcmToken != null) {
                if (!fcmToken.equals(usuario.getUsuarioFcmToken()))
                    usuario.setUsuarioFcmToken(fcmToken);
            }
            Date date = new Date();
            response.put("ultimaSesion", usuario.getUsuarioUltimaSesion() != null ?
                    simpleDateFormat.format(usuario.getUsuarioUltimaSesion()) : simpleDateFormat.format(date));
            usuario.setUsuarioUltimaSesion(new Timestamp(new Date().getTime()));
            usuarioService.updateUsuario(usuario);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new TasOnException(Response.Status.NOT_IMPLEMENTED.getStatusCode(), Constant.RESPONSE_ERROR, e);
        }

        response.put("rucEmpresa", usuario.getUsuarioRuc());
        response.put("tipoUsuarioDesc", this.catalogoItemService.read(usuario.getUsuarioTipoUsuario()).getCatalogoItemDescripcion());
        response.put("nombres", usuario.getUsuarioNombres() + " " + usuario.getUsuarioApellidos());
        response.put("email", usuario.getUsuarioMail());
        response.put("tipoUsuario", usuario.getUsuarioTipoUsuario().toString());
        response.put("estadoUsuario", usuario.getUsuarioEstado().toString());
        response.put("token", generateToken(usuario, Constant.SUBJECT, oAuthDataProvider));
        Integer notaCredito = clienteService.readCliente(usuario.getUsuarioRuc()).getClienteNotaCredito();
        response.put("notaCredito", notaCredito != null && notaCredito == 1 ? "true" : "false");
        response.put("horasCaducidadSolicitud", getHorasCaducidadSolicitud().toString());
        return response;
    }


    protected List<Usuario> getAllUsuariosByTipoUsuario(int tipoUsuario, int estado){
        return usuarioService.getUsuariosByTipoUsuario(tipoUsuario, estado);
    }

    protected void updatePassword(Password password) throws TasOnException{
        try {
            CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
            String passHashed = cipherHash.hashString(password.getOldPassword());
            Usuario usuario = usuarioService.getUsuarioByUserNameAndPassword(password.getUsername(), passHashed);
            if (usuario != null){
                String newPassword = cipherHash.hashString(password.getNewPassword());
                usuario.setUsuarioContrasenia(newPassword);
                usuario.setUsuarioEstado(Constant.USER_ACTIVE);
                usuarioService.updateUsuario(usuario);
            }else {
                throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), "Error changing password", "Password no coincide");
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new TasOnException(Response.Status.NOT_IMPLEMENTED.getStatusCode(), "Error changing password", e);
        }
    }

    protected List<User> getUsuariosByTipoUsuarioAndEstado(int tipoUsuario, int estado){
        List<Usuario> usuarios = usuarioService.getUsuariosByTipoUsuario(tipoUsuario, estado);
        return usuarios.stream().map(userFunction).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<User> getUsuariosByEmpresaTipoUsuarioAndEstado(String rucEmpresa, int tipoUsuario, int estado){
        List<Usuario> usuarios = usuarioService.getUsuariosByEmpresaTipoUsuario(rucEmpresa, tipoUsuario, estado);
        return usuarios.stream().map(userFunction).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<Usuario> getUsuarioByEmpresaAndEstado(String idEmpresa, int estado){
        List<Usuario> usuarios;
        if (estado == 0)
            usuarios = usuarioService.getUsuarioByRuc(idEmpresa);
        else
            usuarios = usuarioService.getUsuarioByEmpresaAndEstado(idEmpresa, estado);
        return usuarios;
    }

    private Function<Usuario, User> userFunction = usuario -> {
        User user = new User();
        user.setIdDocumento(usuario.getUsuarioIdDocumento());
        user.setUsuario(usuario.getUsuarioNombreUsuario());
        user.setNombres(usuario.getUsuarioNombres());
        user.setApellidos(usuario.getUsuarioApellidos());
        CatalogoItem catalogoItem = this.catalogoItemService.read(usuario.getUsuarioTipoUsuario());
        user.setIdTipoUsuario(usuario.getUsuarioTipoUsuario());
        user.setTipoUsuario(catalogoItem.getCatalogoItemDescripcion());
        user.setIdEstado(usuario.getUsuarioEstado());
        catalogoItem = this.catalogoItemService.read(usuario.getUsuarioEstado());
        user.setEstado(catalogoItem.getCatalogoItemDescripcion());
        return user;
    };

    protected void activateUser(Usuario usuario, Exchange exchange){
        int estado = usuario.getUsuarioEstado();
        Integer tipoUsuario = usuario.getUsuarioTipoUsuario();
        usuario = usuarioService.readUsuario(usuario.getUsuarioIdDocumento());
        switch (estado){
            case Constant.USER_CREATED:
                usuario.setUsuarioEstado(Constant.USER_CREATED);
                if(tipoUsuario != null && tipoUsuario != 0) usuario.setUsuarioTipoUsuario(tipoUsuario);
                updateUsuario(usuario);
                Map<String, String> notifications = new HashMap<>();
                notifications.put("template", Constant.EMAIL_NUEVO);
                notifications.put("idDocumento", usuario.getUsuarioIdDocumento());
                exchange.getContext().createProducerTemplate().requestBody("activemq:queue:ec.net.redcode.tason.NotificationJMS", notifications);
                break;
            case Constant.USER_ELIMINATED:
                usuario.setUsuarioEstado(Constant.USER_ELIMINATED);
                updateUsuario(usuario);
                break;
        }
    }

    private void updateUsuario(Usuario usuario) {
        usuarioService.updateUsuario(usuario);
    }

    public void setoAuthDataProvider(OAuthDataProvider oAuthDataProvider) {
        this.oAuthDataProvider = oAuthDataProvider;
    }
}
