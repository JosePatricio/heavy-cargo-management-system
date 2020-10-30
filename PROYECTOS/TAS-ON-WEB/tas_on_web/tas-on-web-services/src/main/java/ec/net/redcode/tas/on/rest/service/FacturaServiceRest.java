package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.*;
import ec.net.redcode.tas.on.persistence.entities.Factura;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/factura/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class FacturaServiceRest {

    @POST
    @Path("/create/")
    public Map<String, String> createFactura(Factura factura){
        return null;
    }

    @GET
    @Path("/code-factura/")
    public String getCodeFactura(){
        return null;
    }

    @GET
    @Path("/can-create-preinvoice/")
    public Map<String, String> puedeCrearPrefactura(){
        return null;
    }

    @POST
    @Path("/create-preinvoice/")
    public Map<String, String> createPreInvoice(Invoice invoice){
        return null;
    }

    @GET
    @Path("/invoices/{estado}")
    public Invoices getInvoicesByUserEstado(@PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/invoice-detail/{facturaId}")
    public InvoiceDetail getInvoiceDetail(@PathParam("facturaId") String facturaId){
        return null;
    }

    @GET
    @Path("/invoices-all/{estado}")
    public Invoices getInvoicesByEstado(@PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/invoices-all-by")
    public Invoices getAllInvoicesBy(
            @QueryParam("estado") int estado,
            @QueryParam("cliente") String cliente,
            @QueryParam("facturaNro") String facturaNro,
            @QueryParam("valorDesde") Double valorDesde,
            @QueryParam("valorHasta") Double valorHasta,
            @QueryParam("fechaCobroDesde") String fechaDesde,
            @QueryParam("fechaCobroHasta") String fechaHasta) {
        return null;
    }


    @PUT
    @Path("/invoice-update/")
    public Map<String, String> updateInvoice(Invoice invoice){
        return null;
    }

    @POST
    @Path("/generate-manual")
    public Map<String, String> createManualInvoice(ec.gob.sri.comprobantes.modelo.factura.Factura factura){
        return null;
    }

    @GET
    @Path("/manual-by")
    public FacturaManualDTO getAllManualInvoicesBy(
            @QueryParam("secuencial") String secuencial,
            @QueryParam("razonSocial") String razonSocial,
            @QueryParam("ruc") String ruc,
            @QueryParam("fechaEmisionDesde") String fechaDesde,
            @QueryParam("fechaEmisionHasta") String fechaHasta) {
        return null;
    }

    @GET
    @Path("/manual/download/XML/{claveAcceso}")
    @Produces("application/xml")
    public Response downloadFacturaManualXML(@PathParam("claveAcceso") String claveAcceso){
        return null;
    }

    @GET
    @Path("/retencion-by")
    public List<FacturaRetencionDTO> getFacturaRetencionBy(
            @QueryParam("razonSocialCliente") String razonSocialCliente,
            @QueryParam("numeroFacturaCliente") String numeroFacturaCliente,
            @QueryParam("prefactura") String prefactura,
            @QueryParam("fechaAutorizacionDesde") String fechaAutorizacionDesde,
            @QueryParam("fechaAutorizacionHasta") String fechaAutorizacionHasta) {
        return null;
    }

    @GET
    @Path("/retencion/download/XML/{claveAcceso}")
    @Produces("application/xml")
    public Response downloadRetencionXML(@PathParam("claveAcceso") String claveAcceso){
        return null;
    }

}
