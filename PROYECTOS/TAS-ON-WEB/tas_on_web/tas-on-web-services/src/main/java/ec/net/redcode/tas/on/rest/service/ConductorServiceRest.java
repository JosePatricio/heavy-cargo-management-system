package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.Conductor;

import javax.ws.rs.*;
import java.util.List;

@Path("/conductor/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class ConductorServiceRest {

    @POST
    @Path("/create/")
    public String createConductor(Conductor conductor){
        return null;
    }

    @GET
    @Path("/read/{idConductor}")
    public Conductor readConductor(@PathParam("idConductor") int idConductor){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateConductor(Conductor conductor){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteConductor(Conductor conductor){
        return null;
    }

    @GET
    @Path("/conductores/")
    public List<Conductor> getConductoresByUser(){
        return null;
    }

    @GET
    @Path("/conductores/{solicitudId}")
    public List<Conductor> getConductoresByUsuarioYSolicitud(@PathParam("solicitudId") String solicitudId){
        return null;
    }

}
