package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.Catalogo;

import javax.ws.rs.*;
import java.util.List;

@Path("/catalogo/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class CatalogoServiceRest {

    @POST
    @Path("/create/")
    public String createCatalogo(Catalogo catalogo){
        return null;
    }

    @GET
    @Path("/read/{idCatalogo}")
    public Catalogo readCatalogo(@PathParam("idCatalogo") String idCatalogo){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateCatalogo(Catalogo catalogo){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteCatalogo(Catalogo catalogo){
        return null;
    }

    @GET
    @Path("/all/")
    public List<Catalogo> getAllCatalogo(){
        return null;
    }

}
