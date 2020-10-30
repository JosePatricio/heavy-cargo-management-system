package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.dto.Password;

import javax.ws.rs.*;
import java.util.Map;

@Path("/login/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class LoginServiceRest {

    @POST
    @Path("/web/")
    public Map<String, String> login(@FormParam("username") String username, @FormParam("password") String password){
        return null;
    }

    @POST
    @Path("/movil/")
    public Usuario loginMovil(@FormParam("username") String username, @FormParam("password")String password, @FormParam("fcmToken")String fcmToken){
        return null;
    }

    @POST
    @Path("/change-password/")
    public Map<String, String> updatePassword(Password password){
        return null;
    }

}
