package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.OfertaService;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class FCMSolicitudCanceladaJobProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudCanceladaJobProcessor.class);

    private SolicitudEnvioService solicitudEnvioService;
    private UsuarioService usuarioService;
    private OfertaService ofertaService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        logger.info("Enviando notificacion push {} solicitud cancelada job", idSolicitud);
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        Usuario usuario = usuarioService.readUsuario(solicitudEnvio.getSolicitudEnvioUsuarioId());
        List<Usuario> usuarios = usuarioService.getUsuarioByRuc(usuario.getUsuarioRuc());
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudOfertas(idSolicitud);
        Notification notification = new Notification();
        if (ofertas.size() != 0){
            notification.setTitle("Aprobar");
            notification.setBody("La solicitud ".concat(idSolicitud).concat(" caduca el dia de hoy a las 23:59 y no ha sido aprobada."));
        } else {
            notification.setTitle("Sin oferta");
            notification.setBody("La solicitud ".concat(idSolicitud).concat(" caduca el dia de hoy a las 23:59 y no cuenta con ofertas."));
        }
        List<String> tokens = new ArrayList<>();
        for (Usuario u: usuarios){
            if (u.getUsuarioFcmToken() != null && u.getUsuarioEstado() == 1)
                tokens.add(u.getUsuarioFcmToken());
        }
        FcmMessage message = new FcmMessage();
        message.setNotification(notification);
        message.setRegistration_ids(tokens);
        Response response = callService(message);
        logger.info("Notificaciones push cancaledas job {}", response.readEntity(String.class));
    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }
}
