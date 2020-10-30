package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.dto.OfertaNotificacion;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

public class SolicitudEnvioCancelProcessor implements Processor {

    private OfertaService ofertaService;
    private LocalidadService localidadService;
    private SolicitudEnvioService solicitudEnvioService;
    private UsuarioService usuarioService;
    private ClienteService clienteService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String solicitud = (String) exchange.getIn().getHeader("solicitud");
        String comentario = (String) exchange.getIn().getHeader("comentario");
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicitud);
        Localidad localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
        String origen = localidad.getLocalidadDescripcion();
        localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
        String destino = localidad.getLocalidadDescripcion();
        String tipo = solicitudEnvio.getSolicitudEnvioNumeroPiesas() + " (Cajas, Tambores, Tanques, Bidones, etc)";
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(solicitud);
        List<OfertaNotificacion> ofertaNotificacions = new ArrayList<>();
        Usuario usuario;
        Cliente cliente;
        OfertaNotificacion ofertaNotificacion;
        for (Oferta oferta: ofertas){
            ofertaNotificacion = new OfertaNotificacion();
            ofertaNotificacion.setSolicitud(solicitud);
            ofertaNotificacion.setOrigen(origen);
            ofertaNotificacion.setDestino(destino);
            ofertaNotificacion.setTipo(tipo);
            usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
            ofertaNotificacion.setCorreo(usuario.getUsuarioMail());
            cliente = clienteService.readCliente(usuario.getUsuarioRuc());
            ofertaNotificacion.setEmpresa(cliente.getClienteRazonSocial());
            ofertaNotificacions.add(ofertaNotificacion);
            ofertaNotificacion.setComentario(comentario);
        }
        exchange.getIn().setBody(ofertaNotificacions);
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    public void setLocalidadService(LocalidadService localidadService) {
        this.localidadService = localidadService;
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
}
