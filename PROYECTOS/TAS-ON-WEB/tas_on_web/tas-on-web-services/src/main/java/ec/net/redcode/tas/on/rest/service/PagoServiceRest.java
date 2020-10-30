package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.Pay;
import ec.net.redcode.tas.on.common.facturacion.ComprobanteRetencion;
import ec.net.redcode.tas.on.common.facturacion.InfoPago;
import ec.net.redcode.tas.on.persistence.entities.Pago;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/pago/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class PagoServiceRest {

    @POST
    @Path("/create/")
    public Map<String, String> createPago(Pago pago){
        return null;
    }

    @POST
    @Path("/create-retencion/")
    public Map<String, String> createPagoRetencion(InfoPago pago){
        return null;
    }

    @GET
    @Path("/pagos/{facturaId}")
    public List<Pay> getPagos(@PathParam("facturaId") String facturaId){
        return null;
    }

    @GET
    @Path("/pago-detail/{pagoId}")
    public Pay getPagoDetail(@PathParam("pagoId") int pagoId){
        return null;
    }

    @GET
    @Path("/pago-retencion/{codigoAutorizacion}/{facturaId}")
    public Map<String, Object> getRetencion(@PathParam("codigoAutorizacion") String codigoAutorizacion, @PathParam("facturaId") String facturaId) {
        return null;
    }

    @GET
    @Path("/pago-nota-credito/consultar/{facturaId}")
    public Map<String, Object> getNotaCreditoInfo(@PathParam("facturaId") String facturaId) {
        return null;
    }

}
