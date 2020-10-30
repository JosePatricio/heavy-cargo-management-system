package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Offer;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.SolicitudEnvioBean;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Log4j
public class SolicitudEnvioProcessor extends SolicitudEnvioBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String idUsuario = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(idUsuario);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        try {
            switch (operationName) {
                case MethodConstant.SOLICITUD_ENVIO_CREATE:
                    SolicitudEnvio solicitudEnvioCreate = exchange.getIn().getBody(SolicitudEnvio.class);
                    Map<String, String> responseCreate = new HashMap<>();
                    try{
                        create(solicitudEnvioCreate, idUsuario);
                        responseCreate.put("response", Constant.RESPONSE_OK);
                        responseCreate.put("responseMessage", "Solicitud creada correctamente");
                        exchange.getIn().setHeader("solicitud", solicitudEnvioCreate.getSolicitudEnvioId());
                    } catch (TasOnException e){
                        responseCreate.put("response", Constant.RESPONSE_ERROR);
                        responseCreate.put("responseMessage", e.getDeveloperMessage());
                        exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                    }
                    exchange.getIn().setBody(response(responseCreate));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_READ:
                    String idSolicitudEnvio = exchange.getIn().getBody(String.class);
                    SolicitudEnvio solicitudEnvio = read(idSolicitudEnvio);
                    exchange.getIn().setBody(response(solicitudEnvio));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_UPDATE:
                    SolicitudEnvio solicitudEnvioUpdate = exchange.getIn().getBody(SolicitudEnvio.class);
                    update(solicitudEnvioUpdate, idUsuario);
                    Map<String, String> responseUpdate = new HashMap<>();
                    responseUpdate.put("response", Constant.RESPONSE_OK);
                    responseUpdate.put("responseMessage", "Solicitud de envio actualizada correctamente");
                    exchange.getIn().setHeader("solicitud", solicitudEnvioUpdate.getSolicitudEnvioId());
                    exchange.getIn().setBody(response(responseUpdate));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_DELETE:
                    SolicitudEnvio solicitudEnvioDelete = exchange.getIn().getBody(SolicitudEnvio.class);
                    solicitudEnvioService.delete(solicitudEnvioDelete);
                    Map<String, String> responseDelete = new HashMap<>();
                    responseDelete.put("response", "Solicitud envio borrado correctamente");
                    exchange.getIn().setBody(response(responseDelete));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_ORIGEN_DESTINO:
                    int origen = Integer.valueOf(messageList.get(0).toString());
                    int destino = Integer.valueOf(messageList.get(1).toString());
                    int estado = Integer.valueOf(messageList.get(2).toString());
                    exchange.getIn().setBody(response(solicitudEnvioService.getSolicitudEnvioByOrigenDestino(origen, destino, estado)));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_OFERTA:
                    int idSolicitudEnvioOferta = exchange.getIn().getBody(Integer.class);
                    int estados = Integer.valueOf(messageList.get(1).toString());
                    exchange.getIn().setBody(response(solicitudEnvioService.getSolicitudEnvioBySolicitudEnvioOferta(idSolicitudEnvioOferta, estados)));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_ESTADO:
                    int estadoss = exchange.getIn().getBody(Integer.class);
                    exchange.getIn().setBody(response(solicitudEnvioService.getSolicitudEnvioBySolicitudEstado(estadoss)));
                    break;
                case MethodConstant.SOLICITUD_SOLICITUDES:
                    estado = exchange.getIn().getBody(Integer.class);
                    exchange.getIn().setBody(response(getSolicitudesEnvioByOferta(estado, usuario)));
                    break;
                case MethodConstant.SOLICITUD_ALL_SOLICITUDES:
                    estado = exchange.getIn().getBody(Integer.class);
                    List<Solicitudes> solicitudesList;
                    if(estado == Constant.ESTADO_TODAS_SOLICITUDES_ENTREGADAS){
                        solicitudesList = getAllSolicitudesEntregadas(idUsuario);
                    } else{
                        solicitudesList = getSolicitudesEnvio(estado, idUsuario);
                    }
                    exchange.getIn().setBody(response(solicitudesList));
                    break;
                case MethodConstant.SOLICITUD_SOLICITUD:
                    String idSolicitud = exchange.getIn().getBody(String.class);
                    Solicitud solicitud = getSolicitud(idSolicitud, usuario);
                    exchange.getIn().setBody(response(solicitud));
                    break;
                case MethodConstant.SOLICITUD_ACCEPT_OFERTA:
                    Offer offer = exchange.getIn().getBody(Offer.class);
                    updateAcceptOferta(offer.getIdSolicitud(), offer.getIdOferta(), offer.getAmount());
                    Map<String, String> responseAccept = new HashMap<>();
                    responseAccept.put("response", Constant.RESPONSE_OK);
                    responseAccept.put("responseMessage", "Solicitud actualizada correctamente");
                    exchange.getIn().setHeader("solicitud", offer.getIdSolicitud());
                    exchange.getIn().setBody(response(responseAccept));
                    break;
                case MethodConstant.SOLICITUD_WITH_OFERTAS:
                    exchange.getIn().setBody(response(getSolictudEnvioWithOfertas(usuario)));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_CANCEL:
                    SolicitudEnvio solicitudCancel = exchange.getIn().getBody(SolicitudEnvio.class);
                    cancelSolicitud(solicitudCancel);
                    exchange.getIn().setHeader("solicitud", solicitudCancel.getSolicitudEnvioId());
                    exchange.getIn().setHeader("comentario", solicitudCancel.getSolicitudEnvioComentario());
                    Map<String, String> responseCancel = new HashMap<>();
                    responseCancel.put("response", Constant.RESPONSE_OK);
                    responseCancel.put("responseMessage", "Solicitud cancelada correctamente");
                    exchange.getIn().setBody(response(responseCancel));
                    break;
                case MethodConstant.SOLICITUD_ENVIO_RANDOM:
                    boolean ejecutar = new Random().nextBoolean();
                    exchange.getIn().setBody(ejecutar);
                    LocalDate localDate = LocalDate.now();
                    boolean isWeekend = localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
                    if(ejecutar && !isWeekend) exchange.getIn().setHeader("solicitud", crearSolicitudEnvioAleatoria());
                    break;
                case MethodConstant.SOLICITUD_ESTADOS:
                    String rucCliente = TasOnUtil.getStringFromObject(messageList.get(0));
                    if(usuario.getUsuarioTipoUsuario() != Constant.USER_ADMIN)
                        exchange.getIn().setBody(response(new ArrayList<>()));
                    else
                        exchange.getIn().setBody(response(solicitudEnvioService.getEstadoSolicitudes(rucCliente)));
                    break;
                case MethodConstant.SOLICITUD_ADMIN_BY:
                    if(usuario.getUsuarioTipoUsuario() != Constant.USER_ADMIN){
                        exchange.getIn().setBody(response(new ArrayList<>()));
                        break;
                    }
                    Integer estadoInteger = TasOnUtil.getIntegerFromObject(messageList.get(0));
                    rucCliente = TasOnUtil.getStringFromObject(messageList.get(1));
                    exchange.getIn().setBody(response(solicitudEnvioService.getSolicitudesAdminBy(estadoInteger, rucCliente)));
                    break;
                case MethodConstant.SOLICITUD_NUEVA:
                    exchange.getIn().setBody(response(getDefaultValuesNuevaSolicitud(usuario)));
                    break;
                case MethodConstant.SOLICITUD_ULTIMOS_DATOS_ORIGEN:
                    Integer idOrigen = TasOnUtil.getIntFromObject(messageList.get(0));
                    exchange.getIn().setBody(response(getDatosOrigenUltimosEnvios(idOrigen, usuario)));
                    break;
                case MethodConstant.SOLICITUD_ULTIMOS_DATOS_DESTINO:
                    Integer idDestino = TasOnUtil.getIntFromObject(messageList.get(0));
                    exchange.getIn().setBody(response(solicitudEnvioService.getDatosDestinoUltimosEnvios(idDestino, usuario.getUsuarioRuc())));
                    break;
            }
        }catch (TasOnException e){
            Map<String, String> response = new HashMap<>();
            response.put("response", e.getMessage());
            response.put("responseMessage", e.getDeveloperMessage());
            exchange.getIn().setBody(response(response));
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
    }

}
