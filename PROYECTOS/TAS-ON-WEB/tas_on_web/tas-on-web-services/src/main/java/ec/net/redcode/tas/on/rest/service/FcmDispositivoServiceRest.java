package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("/fcm-dispositivo/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class FcmDispositivoServiceRest {

    @POST
    @Path("/create/")
    public Map<String, String> create(FcmDispositivo fcmDispositivo){
        return null;
    }

}
