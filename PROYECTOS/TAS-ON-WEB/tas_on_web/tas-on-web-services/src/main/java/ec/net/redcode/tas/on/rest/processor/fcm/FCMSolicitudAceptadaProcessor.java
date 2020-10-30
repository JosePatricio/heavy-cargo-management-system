package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class FCMSolicitudAceptadaProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudAceptadaProcessor.class);

    private SolicitudEnvioService solicitudEnvioService;
    private OfertaService ofertaService;
    private UsuarioService usuarioService;
    private ClienteService clienteService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        logger.info("Enviando notificacion push {}, ofertas ganadoras y perdedoras ", idSolicitud);
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        Usuario usuario;
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(idSolicitud);

        //envio de la notificacion ganadora
        Oferta oferta = ofertaService.read(solicitudEnvio.getSolicitudEnvioOfertaId());
        FcmMessage message = new FcmMessage();
        Notification notification = new Notification();
        notification.setTitle("Ganador solicitud ".concat(idSolicitud));
        notification.setBody("Se ha seleccionado su oferta como la ganadora de la subasta!");
        message.setNotification(notification);
        List<String> tokens = new ArrayList<>();
        usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
        tokens.add(usuario.getUsuarioFcmToken());
        message.setRegistration_ids(tokens);
        message.setNotification(notification);
        Response response = callService(message);
        logger.info("Envio Notificacion oferta ganadora" + response.readEntity(String.class));

        //Envio ofertas perdedoras
        tokens = new ArrayList<>();
        for (Oferta o: ofertas){
            if (o.getOfertaId() != oferta.getOfertaId()) {
                usuario = usuarioService.readUsuario(o.getOfertaIdUsuario());
                if (usuario.getUsuarioFcmToken() != null) {
                    tokens.add(usuario.getUsuarioFcmToken());
                }
            }
        }
        message = new FcmMessage();
        message.setRegistration_ids(tokens);
        notification.setTitle("No aprobado solicitud " + idSolicitud);
        notification.setBody("Se seleccion\u00f3 a " +
                "otra oferta como ganadora de la subasta. \u00c1nimo, suerte en la siguiente oferta!");
        message.setNotification(notification);
        response = callService(message);
        logger.info("Envio Notificacion de la ofertas no aprobadas" + response.readEntity(String.class));

    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
}
