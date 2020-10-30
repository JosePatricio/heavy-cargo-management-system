package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.InfoBancariaDTO;
import ec.net.redcode.tas.on.persistence.entities.Cliente;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/client/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class ClienteServiceRest {

    @GET
    @Path("/read/{ruc}")
    public Cliente readCliente(@PathParam("ruc") String ruc){
        return null;
    }

    @POST
    @Path("/create/")
    public Map<String, String> createCliente(Cliente cliente){
        return null;
    }

    @PUT
    @Path("/update/")
    public Map<String, String> updateCliente(Cliente cliente){
        return null;
    }

    @PUT
    @Path("/banco/update/")
    public Map<String, String> updateInfoBancaria(InfoBancariaDTO cliente){
        return null;
    }

    @GET
    @Path("/banco/info/")
    public InfoBancariaDTO getInfoBancaria(){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public Map<String, String> deleteCliente(Cliente cliente){
        return null;
    }

    @GET
    @Path("/read-auth")
    public Cliente getClienteByToken(){
        return null;
    }

    @GET
    @Path("/all/")
    public List<Cliente> getAllCliente(){
        return null;
    }

    @GET
    @Path("/clientes/{idTipoCliente}")
    public List<Cliente> getAllClienteByTipo(@PathParam("idTipoCliente") int idTipoCliente){
        return null;
    }



}
