package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.Offer;
import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.persistence.entities.File;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.rest.dto.Solicitud;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/oferta/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class OfertaServiceRest {

    @GET
    @Path("/oferta/{idOferta}")
    public Solicitud getOferta(@PathParam("idOferta") int idOferta){
        return null;
    }

    @POST
    @Path("/ofertar/")
    public String offerValue(Offer oferta){
        return null;
    }

    @POST
    @Path("update-oferta")
    public String updateOffer(Offer offer){
        return null;
    }

    @GET
    @Path("/ofertas/{estado}")
    public List<Offers> getOfertasByUserAndEstado(@PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/ofertas-estado/{estado}")
    public List<Offers> getOfertasByEstado(@PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/ofertas-by/")
    public List<Offers> getOfertasBy(
            @QueryParam("estado") int estado,
            @QueryParam("tipo") int tipo,
            @QueryParam("razonSocial") String razonSocial,
            @QueryParam("solicitud") String solicitud,
            @QueryParam("facturaNro") String facturaNro,
            @QueryParam("fechaDesde") String fechaDesde,
            @QueryParam("fechaHasta") String fechaHasta){
        return null;
    }

    @GET
    @Path("/ofertas-solicitud/{solicitud}/{estado}")
    public List<Oferta> getOfertasBySolicitud(@PathParam("solicitud") String solicitud, @PathParam("estado") int estado){
        return null;
    }
    //VER FOTOS DE RECOLECCION O ENTREGA
    //TODO eliminar este metodo cuando ya se lo elimine de la app movil - lo reemplaza getFoto
    @GET
    @Path("/documentos/{idOferta}/{estado}")
    public Map getDocumentos(@PathParam("idOferta") int idOferta, @PathParam("estado") int estado){
        return null;
    }

    @GET
    @Path("/foto/{fotoId}")
    public File getFoto(@PathParam("fotoId") int fotoId){
        return null;
    }

    @PUT
    @Path("/oferta-generate-cash-file/")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response generateCashManagementFile(List<Offer> offers){
        return null;
    }

    @POST
    @Path("/download-cash-management-file/")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response downloadCashManagementFile(List<Offer> offers){
        return null;
    }

}
