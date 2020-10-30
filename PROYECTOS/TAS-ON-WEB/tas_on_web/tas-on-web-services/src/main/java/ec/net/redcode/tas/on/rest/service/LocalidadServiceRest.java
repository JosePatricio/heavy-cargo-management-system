package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.Localidad;

import javax.ws.rs.*;
import java.util.List;

@Path("/localidad/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class LocalidadServiceRest {

    @POST
    @Path("/create/")
    public String createLocalidad(Localidad localidad){
        return null;
    }

    @GET
    @Path("/read/{idLocalidadPadre}")
    public Localidad readLocalidad(@PathParam("idLocalidadPadre") String idLocalidadPadre){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateLocalidad(Localidad localidad){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteLocalidad(Localidad localidad){
        return null;
    }

    @GET
    @Path("/all/{idLocalidadPadre}/{estado}")
    public List<Localidad> getLocalidadByPadre(@PathParam("idLocalidadPadre") int idLocalidadPadre, @PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/all-ciudades/")
    public List<Localidad> getAllCiudades(){
        return null;
    }

}
