package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.*;
import ec.net.redcode.tas.on.persistence.entities.Envio;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;

import javax.ws.rs.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/solicitud-envio/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class SolicitudEnvioRest {

    @POST
    @Path("/create/")
    public String createSolicitudEnvio(SolicitudEnvio solicitudEnvio){
        return null;
    }

    @GET
    @Path("/read/{idSolicitudEnvio}")
    public SolicitudEnvio readSolicitudEnvio(@PathParam("idSolicitudEnvio") String idSolicitudEnvio){
        return null;
    }

    @PUT
    @Path("/update/")
    public String updateSolicitudEnvio(SolicitudEnvio solicitudEnvio){
        return null;
    }

    @DELETE
    @Path("/delete/")
    public String deleteSolicitudEnvio(SolicitudEnvio solicitudEnvio){
        return null;
    }

    @GET
    @Path("/origen-destino/{origen}/{destino}/{estado}")
    public List<SolicitudEnvio> getSolicitudEnvioByOrigenDestino(@PathParam("origen") Integer origen, @PathParam("destino") Integer destino , @PathParam("estado") Integer estado){
        return null;
    }

    @GET
    @Path("/envio-oferta/{solicitudEnvioOfertaId}/{estado}")
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEnvioOferta(@PathParam("solicitudEnvioOfertaId") Integer solicitudEnvioOfertaId, @PathParam("estado") Integer estado){
        return null;
    }

    @GET
    @Path("/estado/{estado}")
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEstado(@PathParam("estado") Integer estado){
        return null;
    }

    @GET
    @Path("/solicitudes/{estado}")
    public List<Solicitudes> getSolicitudes(@PathParam("estado") Integer estado){
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

    @POST
    @Path("/solicitud-oferta/")
    public String updateAcceptOferta(Offer offer){
        return null;
    }

    @GET
    @Path("/solicitudes-ofertas/")
    public List<Solicitudes> getSolicitudesOferta(){
        return null;
    }

    @POST
    @Path("/solicitud-cancel")
    public Map<String, String> cancelSolicitud(SolicitudEnvio solicitud){
        return null;
    }

    @GET
    @Path("/estado-solicitudes/")
    public List<EstadoSolicitudesDTO> getEstadoSolicitudes(@QueryParam("ruc") String ruc){
        return null;
    }

    @GET
    @Path("/solicitudes-by/")
    public List<SolicitudAdminDTO> getSolicitudesAdminBy(
            @QueryParam("estado") Integer estado,
            @QueryParam("ruc") String ruc){
        return null;
    }

    @POST
    @Path("/envios/validar-archivo/")
    public List<Envio> validarArchivo(Documents archivo){
        return null;
    }

    @POST
    @Path("/envios/cargar-archivo/")
    public List<Envio> cargarArchivo(Documents archivo){
        return null;
    }

    @GET
    @Path("/nueva/")
    public NuevaSolicitud getDatosNuevaSolicitud(){
        return null;
    }

    @GET
    @Path("/origen/{idOrigen}")
    public LinkedHashMap<String, String> getUltimosDatosOrigen(@PathParam("idOrigen") Integer idOrigen){
        return null;
    }

    @GET
    @Path("/destino/{idDestino}")
    public LinkedHashMap<String, String> getUltimosDatosDestino(@PathParam("idDestino") Integer idDestino){
        return null;
    }

}
