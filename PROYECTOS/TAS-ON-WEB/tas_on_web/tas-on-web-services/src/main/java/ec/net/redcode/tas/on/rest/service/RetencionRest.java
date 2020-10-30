package ec.net.redcode.tas.on.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("/retencion/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class RetencionRest {

    @POST
    @Path("/enviar/sri")
    public void enviarRetencionSRI(Map<String, String> data){
        return;
    }

}
