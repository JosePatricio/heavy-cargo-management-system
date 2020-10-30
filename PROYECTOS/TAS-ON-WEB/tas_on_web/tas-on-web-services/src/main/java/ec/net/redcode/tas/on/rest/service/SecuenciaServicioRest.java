package ec.net.redcode.tas.on.rest.service;

import javax.ws.rs.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Path("/secuencia/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class SecuenciaServicioRest {

    @GET
    @Path("/secuencia/{idCliente}")
    public Map<String, String> getSecuenciaByCliente(@PathParam("idCliente") String idCliente){
        return null;
    }

    @GET
    @Path("/dias-habiles/{caducidad}")
    public Map<String, String> getDiasHabiles(@PathParam("caducidad") String caducidad){
        return null;
    }

}
