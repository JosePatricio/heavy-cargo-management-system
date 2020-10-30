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

public class FCMSolicitudOfertaProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudOfertaProcessor.class);

    private OfertaService ofertaService;
    private UsuarioService usuarioService;
    private SolicitudEnvioService solicitudEnvioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        if(idSolicitud==null || idSolicitud.isEmpty() || idSolicitud.equals("null")) return;
        logger.info("Sending notification push ofertas {}", idSolicitud);
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudOfertas(idSolicitud);
        Usuario usuario;
        FcmMessage message;
        List<String> token;
        Response response;
        Notification notification = new Notification();
        if (ofertas.size() != 0 || ofertas.size() != 1){
            int i = 0;
            notification = new Notification();
            notification.setTitle("TAS-ON Posici\u00f3n en la subasta");
            for (Oferta oferta: ofertas) {
                i++;
                if (i != 1) {
                    message = new FcmMessage();
                    token = new ArrayList<>();
                    notification.setBody("Para la subasta ".concat(idSolicitud).concat(" has pasado a la posici\u00f3n ").concat(String.valueOf(i))
                            .concat(". ¡Puede llegar a ser la primera opci\u00f3n si mejora su oferta ahora!"));
                    usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
                    if (usuario.getUsuarioFcmToken() != null) {
                        message.setNotification(notification);
                        token.add(usuario.getUsuarioFcmToken());
                        message.setRegistration_ids(token);
                        response = callService(message);
                        logger.info("Notificacion de oferta enviada {}", response.readEntity(String.class));
                    }
                }
            }
        }

        //notificacion para el cliente
        logger.info("Sending notification push ofertas al cliente {}", idSolicitud);
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        notification.setTitle("Oferta");
        notification.setBody("La solicitud ".concat(idSolicitud).concat(" tiene ").concat(String.valueOf(ofertas.size())).concat(" oferta(s)"));
        message = new FcmMessage();
        message.setNotification(notification);
        usuario = usuarioService.readUsuario(solicitudEnvio.getSolicitudEnvioUsuarioId());
        token = new ArrayList<>();
        if (usuario.getUsuarioFcmToken() != null) {
            token.add(usuario.getUsuarioFcmToken());
            message.setRegistration_ids(token);
            response = callService(message);
            logger.info("Notificacion de nueva oferta al cliente {}", response.readEntity(String.class));
        }
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }
}
