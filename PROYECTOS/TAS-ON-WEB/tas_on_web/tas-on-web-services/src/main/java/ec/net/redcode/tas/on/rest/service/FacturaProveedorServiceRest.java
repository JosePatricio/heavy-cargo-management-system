package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.InvoiceProvider;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/factura-proveedor/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class FacturaProveedorServiceRest {

    @POST
    @Path("/create/")
    public Map<String, String> create(FacturaProveedor facturaProveedor){
        return null;
    }

    @GET
    @Path("/read/{idFacturaProveedor}")
    public FacturaProveedor read(@PathParam("idFacturaProveedor") String idFacturaProveedor){
        return null;
    }

    @GET
    @Path("/read-by-numero-ruc/{numeroFactura}/{ruc}")
    public FacturaProveedor getByNumeroFacturaAndRuc(@PathParam("numeroFactura") String numeroFactura, @PathParam("ruc") String ruc){
        return null;
    }

    @PUT
    @Path("/update/")
    public Map<String, String> update(List<InvoiceProvider> facturaProveedor){
        return null;
    }

    @GET
    @Path("/by-estado/{estado}")
    public List<InvoiceProvider> getByEstado(@PathParam("estado") int estado) {
        return null;
    }

}
