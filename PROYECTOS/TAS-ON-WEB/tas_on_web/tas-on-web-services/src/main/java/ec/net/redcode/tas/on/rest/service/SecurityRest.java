package ec.net.redcode.tas.on.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("/security/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class SecurityRest {

    @POST
    @Path("/banco/solicitud/update")
    public Map<String, String> solicitarActualizarInfoBancaria(String password){
        return null;
    }

}
