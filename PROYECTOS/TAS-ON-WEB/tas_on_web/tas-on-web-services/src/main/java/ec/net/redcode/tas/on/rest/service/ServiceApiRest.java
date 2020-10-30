package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;

import javax.ws.rs.*;

@Path("/service-api/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class ServiceApiRest {

    @GET
    @Path("/token/")
    public String getToken() {
        return null;
    }

    @POST
    @Path("/create-solicitud/")
    public String createSolicitudEnvio(SolicitudEnvio solicitudEnvio){
        return null;
    }

    @GET
    @Path("/data-solicitud/{idSolicitud}")
    public String getSolicitudEnvio(@PathParam("idSolicitud") String idSolicitud){
        return null;
    }

}
