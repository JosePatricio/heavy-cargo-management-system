package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FCMSolicitudModificadaProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudModificadaProcessor.class);

    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private SolicitudEnvioAuditService solicitudEnvioAuditService;
    @Setter private UsuarioService usuarioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        logger.info("Sending notification push modificada {}", exchange.getIn().getBody());
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        List<SolicitudEnvioAudit> solicitudEnvioAudits = solicitudEnvioAuditService.getSolicitudEnvioAuditBySolictudId(idSolicitud);
        SolicitudEnvioAudit solicitudEnvioAudit = solicitudEnvioAudits.get(1);
        StringBuilder camposBuilder = new StringBuilder();
        if (!solicitudEnvio.getSolicitudEnvioPeso().equals(solicitudEnvioAudit.getSolicitudEnvioPeso()))
            camposBuilder.append("peso, ");
        if (!solicitudEnvio.getSolicitudEnvioVolumen().equals(solicitudEnvioAudit.getSolicitudEnvioVolumen()))
            camposBuilder.append("volumen, ");
        if (!solicitudEnvio.getSolicitudEnvioNumeroPiesas().equals(solicitudEnvioAudit.getSolicitudEnvioNumeroPiesas()))
            camposBuilder.append("piezas, ");
        if (!solicitudEnvio.getSolicitudEnvioLocalidadOrigen().equals(solicitudEnvioAudit.getSolicitudEnvioLocalidadOrigen()))
            camposBuilder.append("origen, ");
        if (!solicitudEnvio.getSolicitudEnvioLocalidadDestino().equals(solicitudEnvioAudit.getSolicitudEnvioLocalidadDestino()))
            camposBuilder.append("destino, ");
        if (!solicitudEnvio.getSolicitudEnvioNumeroEstibadores().equals(solicitudEnvioAudit.getSolicitudEnvioNumeroEstibadores()))
            camposBuilder.append("estibadores, ");
        if (!solicitudEnvio.getSolicitudEnvioFechaRecoleccion().equals(solicitudEnvioAudit.getSolicitudEnvioFechaRecoleccion()))
            camposBuilder.append("fecha de recolecci\u00f3n, ");
        if (!solicitudEnvio.getSolicitudEnvioFechaEntrega().equals(solicitudEnvioAudit.getSolicitudEnvioFechaEntrega()))
            camposBuilder.append("fecha de entrega, ");
        if (!solicitudEnvio.getSolicitudEnvioPersonaEntrega().equals(solicitudEnvioAudit.getSolicitudEnvioPersonaEntrega()))
            camposBuilder.append("persona entrega, ");
        if (!solicitudEnvio.getSolicitudEnvioPersonaRecibe().equals(solicitudEnvioAudit.getSolicitudEnvioPersonaRecibe()))
            camposBuilder.append("persona recibe, ");
        if (!solicitudEnvio.getSolicitudEnvioFechaCaducidad().equals(solicitudEnvioAudit.getSolicitudEnvioFechaCaducidad()))
            camposBuilder.append("fecha de caducidad, ");
        if (!solicitudEnvio.getSolicitudEnvioObservaciones().equals(solicitudEnvioAudit.getSolicitudEnvioObservaciones()))
            camposBuilder.append("observaciones, ");
        String campos = camposBuilder.toString();
        if (!campos.isEmpty()) {
            Notification notification = new Notification();
            notification.setTitle("Modificaci\u00f3n solicitud " + idSolicitud);
            String mensaje = "Se ha modificado los campos ".concat(campos).concat(" en la solicitud ").concat(idSolicitud);
            notification.setBody(TasOnUtil.eliminarEspacios(mensaje));
            FcmMessage message = new FcmMessage();
            message.setNotification(notification);

            List<Usuario> usuarios = usuarioService.getUsuariosByTipoUsuario(Constant.USER_DRIVER_ADMIN, Constant.USER_ACTIVE);
            usuarios.addAll(usuarioService.getUsuariosByTipoUsuario(Constant.USER_DRIVER, Constant.USER_ACTIVE));
            Set<String> tokens = new HashSet<>();
            usuarios.forEach(user -> {
                if (user.getUsuarioFcmToken() != null)
                    tokens.add(user.getUsuarioFcmToken());
            });

            logger.info("Enviando el mensaje: '{}' via notificaciones push por solicitudes modificadas a {} usuarios",notification.getBody(), tokens.size());
            message.setRegistration_ids(new ArrayList<>(tokens));
            Response response = callService(message);
            logger.info("Terminando notificaciones push de solicitudes modificadas {}", response.readEntity(String.class));
        }
    }

}
