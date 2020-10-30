package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.EBillingDTO;
import ec.net.redcode.tas.on.common.dto.EBillingInfo;
import ec.net.redcode.tas.on.common.dto.EBillingRequest;
import ec.net.redcode.tas.on.common.dto.UsuarioEbillingDTO;
import ec.net.redcode.tas.on.persistence.entities.Adquiriente;
import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/ebilling/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class EbillingServiceRest {

    @POST
    @Path("/create/adquiriente")
    public Map<String, String> createAdquiriente(Adquiriente adquiriente){
        return null;
    }

    @GET
    @Path("/adquiriente/{adquirienteId}")
    public Adquiriente getAdquiriente(@PathParam("adquirienteId") String numDocumento){
        return null;
    }

    @GET
    @Path("/usuario/{usuarioEbillingId}")
    public UsuarioEbilling getUsuarioEbilling(@PathParam("usuarioEbillingId") String numDocumento){
        return null;
    }

    @POST
    @Path("/update/usuario")
    public Map<String, String> updateUsuarioEbilling(UsuarioEbillingDTO usuarioEbillingDTO){
        return null;
    }

    @POST
    @Path("/generate/")
    public Map<String, String> generateEbilling(EBillingRequest eBillingRequest){
        return null;
    }

    @POST
    @Path("/send/")
    public Map<String, String> sendEbilling(EBillingRequest eBillingRequest){
        return null;
    }

    @GET
    @Path("/user-info/")
    public EBillingInfo getEbillingUserInfo(){
        return null;
    }

    @GET
    @Path("/all/")
    public List<EBillingDTO> getAllEbillings(){
        return null;
    }

    @GET
    @Path("/my/")
    public List<EBillingDTO> getMyEbillings(){
        return null;
    }

    @GET
    @Path("/send-mail/{claveAcceso}/{correo}")
    public Map<String, String> sendEbillingMail(@PathParam("claveAcceso") String claveAcceso,
                                                @PathParam("correo") String correo){
        return null;
    }

    @GET
    @Path("/download/RIDE/{claveAcceso}")
    @Produces("application/pdf")
    public Response downloadRIDE(@PathParam("claveAcceso") String claveAcceso){
        return null;
    }

    @GET
    @Path("/download/XML/{claveAcceso}")
    @Produces("application/xml")
    public Response downloadXML(@PathParam("claveAcceso") String claveAcceso){
        return null;
    }

}
