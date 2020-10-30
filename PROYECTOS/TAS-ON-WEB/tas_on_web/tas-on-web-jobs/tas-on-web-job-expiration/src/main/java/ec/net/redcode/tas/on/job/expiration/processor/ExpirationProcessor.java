package ec.net.redcode.tas.on.job.expiration.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

@Log4j
public class ExpirationProcessor implements Processor {

    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private OfertaService ofertaService;
    @Setter protected ClienteService clienteService;

    @Override
    public void process(Exchange exchange) {
        List<SolicitudEnvio> solicitudEnvios = solicitudEnvioService.getSolicitudEnvioByFechaCaducidadNowEstado(Constant.SOLICITUD_ENVIO_CREATE);
        log.info("Solicitudes por cancelar: " + solicitudEnvios.size());
        for (SolicitudEnvio solicitudEnvio: solicitudEnvios){
            solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_CANCEL);
            solicitudEnvio.setSolicitudEnvioComentario("Cancelaci\u00f3n por caducidad");
            solicitudEnvioService.update(solicitudEnvio);
            log.info("Se cancela la solicitud "+solicitudEnvio.getSolicitudEnvioId());
            List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(solicitudEnvio.getSolicitudEnvioId());
            for (Oferta oferta: ofertas){
                oferta.setOfertaEstado(Constant.OFERTA_CANCEL_ADMINISTRATION);
                ofertaService.update(oferta);
            }
        }
    }

}
