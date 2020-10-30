package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
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

public class FCMSolicitudCanceladaProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudAceptadaProcessor.class);

    private SolicitudEnvioService solicitudEnvioService;
    private UsuarioService usuarioService;
    private ClienteService clienteService;
    private OfertaService ofertaService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        logger.info("Enviando notificacion push {} solicitud cancelada ", idSolicitud);
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(idSolicitud);
        if (ofertas.size() != 0) {
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
            Usuario usuario;
            Notification notification = new Notification();
            notification.setTitle("Cancelada solicitud ".concat(idSolicitud));
            notification.setBody("Se cancel\u00f3 la solicitud por "
                    .concat(solicitudEnvio.getSolicitudEnvioComentario()));
            FcmMessage message = new FcmMessage();
            message.setNotification(notification);
            List<String> tokens = new ArrayList<>();
            for (Oferta oferta : ofertas) {
                usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
                if (usuario.getUsuarioFcmToken() != null)
                    tokens.add(usuario.getUsuarioFcmToken());
            }
            message.setRegistration_ids(tokens);
            Response response = callService(message);
            logger.info("Notificaciones push cancaledas {}", response.readEntity(String.class));
        } else {
            logger.info("No se envio notificacion, la solicitud {} no tuvo ofertas", idSolicitud);
        }
    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }
}
