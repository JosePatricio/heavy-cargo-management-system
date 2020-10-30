package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Documents;
import ec.net.redcode.tas.on.common.dto.FotoEnvioDTO;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.SecuenciaService;
import ec.net.redcode.tas.on.rest.bean.EnvioBean;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Log4j
public class EnvioProcessor extends EnvioBean implements Processor {

    @Setter SecuenciaService secuenciaService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String idUsuario = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(idUsuario);
        Documents archivo;
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        Map<String, String> response = new ConcurrentHashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        switch (operationName) {
            case MethodConstant.ENVIO_VALIDAR_ARCHIVO:
                archivo = exchange.getIn().getBody(Documents.class);
                if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN_ENVIOS)  && archivo != null){
                    exchange.getIn().setBody(response(getEnviosListFromFile(usuario, archivo.getFile())));
                } else{
                    exchange.getIn().setBody(response(Collections.emptyList()));
                }
                break;
            case MethodConstant.ENVIO_CARGAR_ARCHIVO:
                archivo = exchange.getIn().getBody(Documents.class);
                if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN_ENVIOS) && archivo != null){
                    try{
                        guardarEnvios(usuario, archivo.getFile());
                        response.put("response", Constant.RESPONSE_OK);
                        response.put("responseMessage", "Operación realizada exitosamente");
                    }catch (Exception e){
                        response.put("response", Constant.RESPONSE_ERROR);
                        response.put("responseMessage", "Ocurrió un error al guardar los envíos");
                    }

                }
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.ENVIO_PENDIENTES:
                exchange.getIn().setBody(response(consultarEnvios(Arrays.asList(Constant.ESTADO_ENVIO_POR_RECIBIR, Constant.ESTADO_ENVIO_POR_ENTREGAR),
                        null, null, null, null, null, usuario)));
                break;
            case MethodConstant.ENVIO_BY:
                int i = 0;
                Integer estado = TasOnUtil.getIntegerFromObject(messageList.get(i++));
                String razonSocial = TasOnUtil.getStringFromObject(messageList.get(i++));
                String nroDocumento = TasOnUtil.getStringFromObject(messageList.get(i++));
                String fechaRecoleccionDesdeStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaRecoleccionDesde = fechaRecoleccionDesdeStr != null ? formatter.parse(fechaRecoleccionDesdeStr): null;
                String fechaRecoleccionHastaStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaRecoleccionHasta = fechaRecoleccionHastaStr != null ? formatter.parse(fechaRecoleccionHastaStr) : null;
                Integer conductor = TasOnUtil.getIntegerFromObject(messageList.get(i++));
                exchange.getIn().setBody(response(consultarEnvios(estado!=null?Arrays.asList(estado):null, razonSocial, nroDocumento,
                        fechaRecoleccionDesde, fechaRecoleccionHasta, conductor, usuario)));
                break;
            case MethodConstant.ENVIO_ACTUALIZAR_FOTOS:
                FotoEnvioDTO fotoEnvioDTO = exchange.getIn().getBody(FotoEnvioDTO.class);
                try{
                    if(fotoEnvioDTO.getFotos().size()>0 ){
                        guardarFotos(fotoEnvioDTO, usuario);
                    }
                    if(fotoEnvioDTO.getEstadoCompletado() != null && fotoEnvioDTO.getEstadoCompletado()){
                        cambiarEstado(fotoEnvioDTO, usuario);
                    }
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Operación realizada exitosamente");
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.ENVIO_CONSULTAR_FOTOS:
                int envioId = TasOnUtil.getIntFromObject(messageList.get(0));
                exchange.getIn().setBody(response(consultarFotosEnvio(envioId, usuario)));
                break;
            case MethodConstant.ENVIO_GET_FOTO:
                int fotoId = TasOnUtil.getIntFromObject(messageList.get(0));
                exchange.getIn().setBody(response(getFotoEnvio(fotoId, usuario)));
                break;
        }
    }

}
