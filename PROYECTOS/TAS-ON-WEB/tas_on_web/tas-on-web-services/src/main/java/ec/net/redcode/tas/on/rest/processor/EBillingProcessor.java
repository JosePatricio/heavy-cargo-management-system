package ec.net.redcode.tas.on.rest.processor;

import com.google.gson.Gson;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.EBillingDTO;
import ec.net.redcode.tas.on.common.dto.EBillingInfo;
import ec.net.redcode.tas.on.common.dto.EBillingRequest;
import ec.net.redcode.tas.on.common.dto.UsuarioEbillingDTO;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.rest.bean.EbillingBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EBillingProcessor extends EbillingBean implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(EBillingProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        Map<String, String> response = new ConcurrentHashMap<>();
        Usuario usuarioTasOn;
        UsuarioEbilling usuarioEbilling;
        UsuarioEbillingDTO usuarioEbillingDTO;
        String idUsuario;
        Adquiriente adquiriente;
        Cliente cliente;
        List<EBillingDTO> eBillingDTOList;
        String claveAcceso;
        EBillingRequest eBillingRequest;
        KeyStore keyStore;
        byte[] decodedBytes;
        InputStream keystoreStream;
        switch (operationName) {
            case MethodConstant.EBILLING_CREATE_ADQUIRIENTE:
                adquiriente = exchange.getIn().getBody(Adquiriente.class);
                String operacion = "";
                int createAdquirienteResponse = createAdquiriente(adquiriente);
                response.put("response", Constant.RESPONSE_OK);
                if(createAdquirienteResponse==0) operacion = "Creado correctamente";
                if(createAdquirienteResponse==1) operacion = "Actualizado correctamente";
                response.put("responseMessage", "Acreedor " + operacion + " ");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.EBILLING_GET_ADQUIRIENTE:
                String numDocumento = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(response(readAdquiriente(numDocumento)));
                break;
            case MethodConstant.EBILLING_GET_USUARIO:
                MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
                String numeroDoc = messageList.get(0).toString();
                usuarioEbilling = readUsuario(numeroDoc);
                if(usuarioEbilling == null) {
                    exchange.getIn().setBody(responseNoContent());
                }
                else {
                    UsuarioEbilling usuarioEbillingResponse = new UsuarioEbilling();
                    usuarioEbillingResponse.setUsuarioEBillingActividadComercial(usuarioEbilling.getUsuarioEBillingActividadComercial());
                    usuarioEbillingResponse.setUsuarioEbillingPuntoEmision(usuarioEbilling.getUsuarioEbillingPuntoEmision());
                    usuarioEbillingResponse.setUsuarioEbillingLogo(usuarioEbilling.getUsuarioEbillingLogo());
                    usuarioEbillingResponse.setUsuarioEbillingFechaFirma(usuarioEbilling.getUsuarioEbillingFechaFirma());
                    usuarioEbillingResponse.setUsuarioEbillingId(usuarioEbilling.getUsuarioEbillingId());
                    usuarioEbillingResponse.setUsuarioEbillingFirma("");
                    exchange.getIn().setBody(response(usuarioEbillingResponse));
                }
                break;
            case MethodConstant.EBILLING_UPDATE_USUARIO:
                usuarioEbillingDTO = exchange.getIn().getBody(UsuarioEbillingDTO.class);
                createUsuario(usuarioEbillingDTO);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Datos actualizados correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.EBILLING_GET_USER_INFO:
                EBillingInfo eBillingInfo = new EBillingInfo();
                usuarioTasOn  = usuarioService.getUsuariosByUserName(exchange.getIn().getHeader("user").toString());
                usuarioEbilling = usuarioEbillingService.read(usuarioTasOn.getUsuarioRuc());
                cliente = clienteService.readCliente(usuarioTasOn.getUsuarioRuc());
                if(usuarioEbilling == null || cliente == null){
                    exchange.getIn().setBody(responseNoContent());
                    break;
                }
                eBillingInfo.setDireccion(cliente.getClienteDireccion());
                eBillingInfo.setRazonSocial(cliente.getClienteRazonSocial());
                eBillingInfo.setRuc(cliente.getClienteRuc());
                eBillingInfo.setFacturaNro(usuarioEbilling.getUsuarioEbillingEstablecimiento()+"-"+
                        usuarioEbilling.getUsuarioEbillingPuntoEmision()+"-"+
                        TasOnUtil.getDigitsToSecuence(usuarioEbilling.getUsuarioEbillingSecuencia()) + usuarioEbilling.getUsuarioEbillingSecuencia());
                exchange.getIn().setBody(response(eBillingInfo));
                break;
            case MethodConstant.EBILLING_GENERATE:
                eBillingRequest = exchange.getIn().getBody(EBillingRequest.class);
                idUsuario = exchange.getIn().getHeader("user").toString();
                usuarioTasOn  = usuarioService.getUsuariosByUserName(idUsuario);
                usuarioEbilling = usuarioEbillingService.read(usuarioTasOn.getUsuarioRuc());
                //validar contraseña
                keyStore = KeyStore.getInstance("pkcs12");
                decodedBytes = Base64.getDecoder().decode(usuarioEbilling.getUsuarioEbillingFirma());
                keystoreStream = new ByteArrayInputStream(decodedBytes);
                try{
                    keyStore.load(keystoreStream, eBillingRequest.getClaveFirma().toCharArray());
                    eBillingRequest.setIdUsuario(usuarioTasOn.getUsuarioIdDocumento());
                    exchange.getIn().setHeader("contenido", new Gson().toJson(eBillingRequest));
                    response.put("response", Constant.RESPONSE_OK);
                    exchange.getIn().setBody(response(response));
                    crearAdquiriente(eBillingRequest.getAdquiriente()); //actualiza o crea adquiriente
                }catch (IOException e){
                    //contraseña incorrecta
                    logger.error(e.getMessage());
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", "Contraseña incorrecta!");
                    exchange.getIn().setBody(response(response));
                } catch (Exception e){
                    logger.error(e.getMessage());
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", "Contraseña incorrecta!");
                    exchange.getIn().setBody(responseGeneric(response, Response.Status.CONFLICT));
                }
                break;
            case MethodConstant.EBILLING_SEND:
                eBillingRequest = exchange.getIn().getBody(EBillingRequest.class);
                idUsuario = exchange.getIn().getHeader("user").toString();
                usuarioTasOn  = usuarioService.getUsuariosByUserName(idUsuario);
                usuarioEbilling = usuarioEbillingService.read(usuarioTasOn.getUsuarioRuc());
                Ebilling ebilling = ebillingService.readByClaveAcceso(eBillingRequest.getClaveAcceso());
                if(ebilling.getEbillingEstado()!=null && !ebilling.getEbillingEstado().isEmpty() &&
                        ebilling.getEbillingEstado().trim().equalsIgnoreCase("AUTORIZADO")){
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "La factura ya se encuentra en estado Autorizado");
                    return;
                }
                //validar contraseña
                keyStore = KeyStore.getInstance("pkcs12");
                decodedBytes = Base64.getDecoder().decode(usuarioEbilling.getUsuarioEbillingFirma());
                keystoreStream = new ByteArrayInputStream(decodedBytes);
                try{
                    keyStore.load(keystoreStream, eBillingRequest.getClaveFirma().toCharArray());
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Factura enviada correctamente, espere unos minutos para visualizar el nuevo estado");
                    exchange.getIn().setBody(response(response));
                    exchange.getIn().setHeader("ebilling", ebilling.getEbillingId());
                    exchange.getIn().setHeader("claveAcceso", ebilling.getEbillingClaveAcceso());
                    exchange.getIn().setHeader("tipo", "factura");
                    exchange.getIn().setHeader("signature", usuarioEbilling.getUsuarioEbillingFirma());
                    exchange.getIn().setHeader("claveFirma", eBillingRequest.getClaveFirma());
                    exchange.getIn().setHeader("xml", ebilling.getEbillingXml());
                    ebilling.setEbillingEstado("ENVIADO");
                }catch (IOException e){
                    //contraseña incorrecta
                    logger.error(e.getMessage());
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", "Contraseña incorrecta!");
                    exchange.getIn().setBody(response(response));
                } catch (Exception e){
                    logger.error(e.getMessage());
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getMessage());
                    exchange.getIn().setBody(response(response));
                }
                break;
            case MethodConstant.EBILLING_GET_ALL:
                idUsuario = exchange.getIn().getHeader("user").toString();
                usuarioTasOn  = usuarioService.getUsuariosByUserName(idUsuario);
                eBillingDTOList = getEbillings(usuarioTasOn.getUsuarioRuc(), true);
                exchange.getIn().setBody(response(eBillingDTOList));
                break;
            case MethodConstant.EBILLING_GET_MY:
                idUsuario = exchange.getIn().getHeader("user").toString();
                usuarioTasOn  = usuarioService.getUsuariosByUserName(idUsuario);
                eBillingDTOList = getEbillings(usuarioTasOn.getUsuarioIdDocumento(), false);
                exchange.getIn().setBody(response(eBillingDTOList));
                break;
            case MethodConstant.EBILLING_DOWNLOAD_RIDE:
                claveAcceso = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(response(getRIDE(claveAcceso)));
                break;
            case MethodConstant.EBILLING_DOWNLOAD_XML:
                claveAcceso = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(response(getXML(claveAcceso)));
                break;
            case MethodConstant.EBILLING_SEND_MAIL:
                MessageContentsList inputList = exchange.getIn().getBody(MessageContentsList.class);
                claveAcceso = inputList.get(0).toString();
                String correo = inputList.get(1).toString();
                Map<String, Object> data = new HashMap<>();
                ebilling = ebillingService.readByClaveAcceso(claveAcceso);
                if(ebilling != null){
                    if(ebilling.getEbillingRide() != null && !ebilling.getEbillingRide().isEmpty()){
                        usuarioEbilling = usuarioEbillingService.read(ebilling.getEbillingUsuarioEbilling());
                        Usuario usuario = usuarioService.readUsuario(usuarioEbilling.getUsuarioEbillingIdUsuario());
                        adquiriente = adquirienteService.read(ebilling.getEbillingAdquiriente());
                        data.put("template", Constant.EMAIL_EBILLING);
                        data.put("nombreUsuarioEbilling", getNombresUsuario(usuario));
                        data.put("telefonoUsuarioEbilling", usuario.getUsuarioCelular());
                        data.put("mailUsuarioEbilling", usuario.getUsuarioMail());
                        data.put("correos", correo);
                        data.put("nroDocumento", ebilling.getEbillingNumero());
                        data.put("adquiriente", adquiriente.getAdquirienteRazonSocial());
                        exchange.getIn().setBody(data);
                        exchange.getIn().setHeader("ebilling", ebilling.getEbillingId());
                    } else {
                        data = new HashMap<>();
                        data.put("response", Constant.RESPONSE_ERROR);
                        data.put("responseMessage", "La factura aún no ha generado el archivo RIDE");
                        exchange.getIn().setBody(data);
                    }
                } else {
                    data = new HashMap<>();
                    data.put("response", Constant.RESPONSE_ERROR);
                    data.put("responseMessage", "Clave de acceso incorrecta");
                    exchange.getIn().setBody(data);
                }
                break;
        }
    }

    private void crearAdquiriente(ec.net.redcode.tas.on.common.dto.Adquiriente adquirienteDTO){
        if(adquirienteDTO==null) return;
        Adquiriente adquiriente = new Adquiriente();
        adquiriente.setAdquirienteDireccion(adquirienteDTO.getAdquirienteDireccion());
        adquiriente.setAdquirienteIdDocumento(adquirienteDTO.getAdquirienteIdDocumento());
        adquiriente.setAdquirienteMail(adquirienteDTO.getAdquirienteMail());
        adquiriente.setAdquirientePersonaContacto(adquirienteDTO.getAdquirientePersonaContacto());
        adquiriente.setAdquirienteRazonSocial(adquirienteDTO.getAdquirienteRazonSocial());
        adquiriente.setAdquirienteTelefono(adquirienteDTO.getAdquirienteTelefono());
        adquiriente.setAdquirienteTipoDocumento(adquirienteDTO.getAdquirienteTipoDocumento());
        createAdquiriente(adquiriente);
    }

}
