package ec.net.redcode.tas.on.rest.processor.fcm;

import ec.net.redcode.tas.on.common.Constant;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FCMSolicitudNuevaProcessor extends FCMCommon implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FCMSolicitudNuevaProcessor.class);

    private SolicitudEnvioService solicitudEnvioService;
    private LocalidadService localidadService;
    private UsuarioService usuarioService;
    private ClienteService clienteService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String idSolicitud = exchange.getIn().getBody(String.class);
        logger.info("Sending notification push by id {}",idSolicitud);

        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);

        List<Usuario> usuarios = usuarioService.getUsuariosByTipoUsuario(Constant.USER_DRIVER_ADMIN, Constant.USER_ACTIVE);
        usuarios.addAll(usuarioService.getUsuariosByTipoUsuario(Constant.USER_DRIVER, Constant.USER_ACTIVE));
        Set<String> tokens = new HashSet<>();
        usuarios.forEach(user -> {
            if(user.getUsuarioFcmToken() != null && !user.getUsuarioFcmToken().isEmpty())
                tokens.add(user.getUsuarioFcmToken());
        });

        FcmMessage message = new FcmMessage();
        message.setRegistration_ids(new ArrayList<>(tokens));
        Notification notification = new Notification();
        notification.setTitle(idSolicitud);
        Localidad localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
        String origen = localidad.getLocalidadDescripcion();
        localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
        String destino = localidad.getLocalidadDescripcion();
        notification.setBody("Se requiere el traslado de ".concat(solicitudEnvio.getSolicitudEnvioNumeroPiesas().toString()).concat(" (cajas, tambores,etc), ").concat(origen)
                .concat(" > ").concat(destino));
        message.setNotification(notification);

        Response response = callService(message);
        logger.info(response.readEntity(String.class));
    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public void setLocalidadService(LocalidadService localidadService) {
        this.localidadService = localidadService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

}
