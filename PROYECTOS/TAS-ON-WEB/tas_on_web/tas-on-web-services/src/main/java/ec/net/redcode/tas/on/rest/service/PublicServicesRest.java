package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.EstadisticaHome;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/public/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class PublicServicesRest {

    @GET
    @Path("/home")
    public EstadisticaHome getHome(){
        return null;
    }

    @GET
    @Path("/localidad-all/{idLocalidadPadre}/{estado}")
    public List<Localidad> getLocalidadByPadre(@PathParam("idLocalidadPadre") int idLocalidadPadre, @PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/catalogo-item-all/{idCatalogo}")
    public List<CatalogoItem> getCatalogoItemByCatalogo(@PathParam("idCatalogo") int idCatalogo){
        return null;
    }

    @GET
    @Path("/catalogo-item-read/{idCatalogoItem}")
    public CatalogoItem readCatalogoItem(@PathParam("idCatalogoItem") String idCatalogoItem){
        return null;
    }

    @POST
    @Path("/empresa-create/")
    public Map<String, String> createEmpresaCliente(Map<String, Object> data){
        return null;
    }

    @GET
    @Path("/empresa-clientes/{idTipoCliente}")
    public List<Cliente> getAllClienteByTipo(@PathParam("idTipoCliente") int idTipoCliente){
        return null;
    }

    @POST
    @Path("/usuario-create/")
    public String createUsuario(Usuario usuarios){
        return null;
    }

    @GET
    @Path("/user/{username}")
    public Usuario getUsuarioByUsername(@PathParam("username") String username){
        return null;
    }

    @GET
    @Path("/email/{email}")
    public Usuario getUsuarioByEmail(@PathParam("email") String email){
        return null;
    }

    @GET
    @Path("/empresa-read/{ruc}")
    public Cliente readCliente(@PathParam("ruc") String ruc){
        return null;
    }

    @GET
    @Path("/empresa-transporte-read/{ruc}")
    public Cliente readTransporteCliente(@PathParam("ruc") String ruc){
        return null;
    }

    @GET
    @Path("/usuario-read/{identificador}")
    public Usuario readUsuario(@PathParam("identificador") String identificador){
        return null;
    }

    @GET
    @Path("/solicitudes-all/{estado}")
    public List<Solicitudes> getAllSolicitudes(@PathParam("estado") Integer estado){
        return null;
    }

    @GET
    @Path("/solicitud/{idSolicitud}")
    public Solicitud getSolicitud(@PathParam("idSolicitud") String idSolicitud){
        return null;
    }

    @GET
    @Path("/vehiculo-placa/{placa}")
    public Vehiculo getVehiculoByPlaca(@PathParam("placa") String placa) {
        return null;
    }

    @GET
    @Path("/usuario-restablecer/{email}/{identificador}")
    public Map<String, String> restablecerPassword(@PathParam("email") String email, @PathParam("identificador") String identificador) {
        return null;
    }


}
