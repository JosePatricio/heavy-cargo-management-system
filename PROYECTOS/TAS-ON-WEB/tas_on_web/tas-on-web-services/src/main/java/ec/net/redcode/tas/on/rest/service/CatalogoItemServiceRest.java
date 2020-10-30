package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;

import javax.ws.rs.*;
import java.util.List;

@Path("/catalogo-item/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class CatalogoItemServiceRest {

    @POST
    @Path("/create/")
    public String createCatalogoItem(CatalogoItem catalogoItem){
        return null;
    }

    @GET
    @Path("/read/{idCatalogoItem}")
    public CatalogoItem readCatalogoItem(@PathParam("idCatalogoItem") String idCatalogoItem){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateCatalogoItem(CatalogoItem catalogoItem){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteCatalogoItem(CatalogoItem catalogoItem){
        return null;
    }

    @GET
    @Path("/all/{idCatalogo}")
    public List<CatalogoItem> getCatalogoItemByCatalogo(@PathParam("idCatalogo") int idCatalogo){
        return null;
    }

}
