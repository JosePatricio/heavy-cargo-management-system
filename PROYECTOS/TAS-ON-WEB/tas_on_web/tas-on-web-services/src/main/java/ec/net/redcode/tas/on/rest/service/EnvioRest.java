package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.Documents;
import ec.net.redcode.tas.on.common.dto.EnvioDTO;
import ec.net.redcode.tas.on.common.dto.FotoEnvioDTO;
import ec.net.redcode.tas.on.common.dto.IdFotosDTO;
import ec.net.redcode.tas.on.persistence.entities.Envio;
import ec.net.redcode.tas.on.persistence.entities.File;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/envio/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class EnvioRest {

    @POST
    @Path("/archivo/validar")
    public List<Envio> validarArchivo(Documents archivo){
        return null;
    }

    @POST
    @Path("/archivo/cargar")
    public Map<String, String> cargarArchivo(Documents archivo){
        return null;
    }

    @GET
    @Path("/by")
    public List<EnvioDTO> consultarEnviosBy(
            @QueryParam("estado") Integer estado,
            @QueryParam("razonSocial") String razonSocial,
            @QueryParam("documento") String documento,
            @QueryParam("fechaRecoleccionDesde") String fechaDesde,
            @QueryParam("fechaRecoleccionHasta") String fechaHasta,
            @QueryParam("conductor") Integer conductor
    ){
        return null;
    }

    @GET
    @Path("/pendientes")
    public List<EnvioDTO> consultarEnviosPendientes(){
        return null;
    }

    @POST
    @Path("/fotos/subir")
    public List<Envio> actualizarFotosEnvio(FotoEnvioDTO fotos){
        return null;
    }

    @GET
    @Path("/fotos")
    public IdFotosDTO consultarFotosEnvio(@QueryParam("envioId") int envioId){
        return null;
    }

    @GET
    @Path("/fotos/ver")
    public File getFotoEnvio(@QueryParam("fotoId") int fotoId){
        return null;
    }

}
