package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/calificacion-transportista/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class CalificacionTransportistaRest {

    @GET
    @Path("/all/")
    public List<CalificacionTransportistaDTO> getCalificacionesByUser(){
        return null;
    }

    @PUT
    @Path("/update/")
    public Map<String, String> updateCalificacionTransportista(CalificacionTransportistaDTO calificacionTransportistaDTO){
        return null;
    }

}
