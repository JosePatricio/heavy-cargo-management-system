package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.OfertaService;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.*;

public class FCMOfertaCambioProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudOfertaProcessor.class);

    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private OfertaService ofertaService;
    @Setter private UsuarioService usuarioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        if(idSolicitud==null || idSolicitud.isEmpty() || idSolicitud.equals("null"))return;
        logger.info("Sending notification push cambio oferta {}", idSolicitud);
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        Usuario usuario = usuarioService.readUsuario(solicitudEnvio.getSolicitudEnvioUsuarioId());
        List<Usuario> usuarios = usuarioService.getUsuarioByRuc(usuario.getUsuarioRuc());
        List<String> tokens = new ArrayList<>();
        for (Usuario u: usuarios){
            if (u.getUsuarioFcmToken() != null && u.getUsuarioEstado() == 1)
                tokens.add(u.getUsuarioFcmToken());
        }
        Notification notification = new Notification();
        notification.setTitle("Solicitud en curso");
        Oferta oferta = ofertaService.read(solicitudEnvio.getSolicitudEnvioOfertaId());
        FcmMessage message = new FcmMessage();
        message.setRegistration_ids(tokens);
        Response response;
        switch (oferta.getOfertaEstado()) {
            case Constant.OFERTA_DELIVER:
                notification.setBody("La solicitud ".concat(idSolicitud).concat(" ha sido despachada"));
                message.setNotification(notification);
                response = callService(message);
                logger.info("Enviada notificacion cambio de oferta {}", response.readEntity(String.class));
                break;
            case Constant.OFERTA_GENERATE_BILL:
                notification.setBody("La solicitud ".concat(idSolicitud).concat(" ha sido entregada"));
                message.setNotification(notification);
                response = callService(message);
                logger.info("Enviada notificacion cambio de oferta {}", response.readEntity(String.class));
                break;
            case Constant.OFERTA_FINISH:
                notification.setTitle("Solicitud pagada");
                notification.setBody("La solicitud ".concat(idSolicitud).concat(" ha sido pagada"));
                usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
                usuarios = usuarioService.getUsuariosByEmpresaTipoUsuario(usuario.getUsuarioRuc(), Constant.USER_DRIVER_ADMIN, Constant.USER_ACTIVE);
                if(usuarios.isEmpty()) break;
                Usuario usuarioOferta = usuarios.get(0);
                if(usuarioOferta.getUsuarioFcmToken()!=null && !usuarioOferta.getUsuarioFcmToken().isEmpty()){
                    tokens.clear();
                    tokens.add(usuarioOferta.getUsuarioFcmToken());
                    message.setRegistration_ids(tokens);
                    response = callService(message);
                    logger.info("Enviada notificacion oferta pagada {}", response.readEntity(String.class));
                }
                exchange.getIn().setHeader("solicitudPagadaId", idSolicitud);
                Map<String, String> data = new HashMap<>();
                data.put("template", Constant.EMAIL_PAGO_OFERTA);
                exchange.getIn().setBody(data);
                break;
        }
    }

}
