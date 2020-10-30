package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.persistence.entities.Vehiculo;

import javax.ws.rs.*;
import java.util.List;

@Path("/vehiculo/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class VehiculoServiceRest {

    @POST
    @Path("/create/")
    public String createVehiculo(Vehiculo vehiculo){
        return null;
    }

    @GET
    @Path("/read/{idVehiculo}")
    public Vehiculo readVehiculo(@PathParam("idVehiculo") int idVehiculo){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateVehiculo(Vehiculo vehiculo){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteVehiculo(Vehiculo vehiculo){
        return null;
    }

    @GET
    @Path("/vehiculos/")
    public List<Vehiculo> getVehiculosByUser(){
        return null;
    }

    @GET
    @Path("/vehiculos/{solicitudId}")
    public List<Vehiculo> getVehiculosByUsuarioYSolicitud(@PathParam("solicitudId") String solicitudId){
        return null;
    }

    @GET
    @Path("/vehiculo-placa/{placa}")
    public Vehiculo getVehiculoByPlaca(@PathParam("placa") String placa) {
        return null;
    }

}
