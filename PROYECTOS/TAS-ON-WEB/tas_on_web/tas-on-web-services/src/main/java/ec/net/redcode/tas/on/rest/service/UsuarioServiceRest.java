package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.User;
import ec.net.redcode.tas.on.persistence.entities.Usuario;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/usuario/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class UsuarioServiceRest {

    //TODO borrar este metodo despues de que los usuarios actualicen la app movil - el metodo estara en LoginServiceRest
    @POST
    @Path("/login-movil/")
    public Usuario loginMovil(@FormParam("username") String username, @FormParam("password")String password, @FormParam("fcmToken")String fcmToken){
        return null;
    }

    @GET
    @Path("/all/{tipoUsuario}/{estado}")
    public List<Usuario> getAllUserByTipoPersona(@PathParam("tipoUsuario") int tipoUsuario, @PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/user/{username}")
    public Usuario getUsuarioByUsername(@PathParam("username") String username){
        return null;
    }

    @GET
    @Path("/email/{email}")
    public Usuario getUsuarioByEmail(@PathParam("email") String email){
        return null;
    }

    @POST
    @Path("/create/")
    public String createUsuario(Usuario usuarios){
        return null;
    }

    @GET
    @Path("/read/{identificador}")
    public Usuario readUsuario(@PathParam("identificador") String identificador){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateUsuario(Usuario usuarios){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteUsuario(Usuario usuarios){
        return null;
    }

    @GET
    @Path("/all-usuario/{tipoUsuario}/{estado}")
    public List<User> getAllUserByTipoUsuarioAndEstado(@PathParam("tipoUsuario") int tipoUsuario, @PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/all-usuario-empresa/{rucEmpresa}/{tipoUsuario}/{estado}")
    public List<User> getAllUserByEmpresaTipoUsuarioAndEstado(@PathParam("rucEmpresa") String rucEmpresa, @PathParam("tipoUsuario") int tipoUsuario, @PathParam("estado") int estado){
        return null;
    }

    @POST
    @Path("/activate-user/")
    public Map<String, String> activateUsuario(Usuario usuario){
        return null;
    }

    @GET
    @Path("/empresa-usuario/{idEmpresa}/{estado}")
    public List<Usuario> getUsuarioByEmpresaAndEstado(@PathParam("idEmpresa") String idEmpresa, @PathParam("estado") int estado){
        return null;
    }

}
