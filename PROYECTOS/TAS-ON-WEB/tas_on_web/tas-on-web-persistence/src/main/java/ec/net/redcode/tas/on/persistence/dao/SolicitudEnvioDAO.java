package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.DireccionDTO;
import ec.net.redcode.tas.on.common.dto.EstadoSolicitudesDTO;
import ec.net.redcode.tas.on.common.dto.SolicitudAdminDTO;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;

import java.util.List;

public interface SolicitudEnvioDAO extends  GenericDAO<SolicitudEnvio, Integer> {

    List<SolicitudEnvio> getSolicitudEnvioByOrigenDestino(Integer origen, Integer destino , Integer estado);
    List<SolicitudEnvio> getSolicitudEnvioBySolicitudEnvioOferta(Integer solicitudEnvioOfertaId, Integer estado);
    List<SolicitudEnvio> getSolicitudEnvioBySolicitudEstado (Integer estado);
    List<SolicitudEnvio> getSolicitudEnvioByUsuarioAndEstado(String usuario, int estado);
    List<SolicitudEnvio> getSolicitudEnvioPorFacturarse(String cliente);
    List<SolicitudEnvio> getSolicitudEnvioCaducadas();
    List<SolicitudEnvio> getSolictudEnvioWithOfertas(String usuario);
    List<SolicitudEnvio> getSolicitudEnvioByFechaCaducidadNowEstado(Integer estado);
    SolicitudEnvio getSolicitudEnvioByOferta(Integer oferta);
    double getToneladasEnviadasSolicitudesCompletadas();
    double getTotalAhorradoSolicitudesCompletadas();
    List<EstadoSolicitudesDTO> getEstadoSolicitudes(String rucCliente);
    List<SolicitudAdminDTO> getSolicitudesAdminBy(Integer estado, String rucCliente);
    SolicitudEnvio getUltimaSolicitud(String rucCliente);
    List<DireccionDTO> getDatosOrigenUltimosEnvios(Integer idOrigen, String rucCliente);
    List<DireccionDTO> getDatosDestinoUltimosEnvios(Integer idDestino, String rucCliente);

}
