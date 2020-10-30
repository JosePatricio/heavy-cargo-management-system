package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.DireccionDTO;
import ec.net.redcode.tas.on.common.dto.EstadoSolicitudesDTO;
import ec.net.redcode.tas.on.common.dto.SolicitudAdminDTO;
import ec.net.redcode.tas.on.persistence.dao.SolicitudEnvioDAO;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import lombok.Setter;

import java.util.List;

public class SolicitudEnvioServiceImpl implements SolicitudEnvioService {

    @Setter private SolicitudEnvioDAO solicitudEnvioDAO ;

    @Override
    public void create(SolicitudEnvio solicitudEnvio) {
        solicitudEnvioDAO.create(solicitudEnvio);
    }

    @Override
    public SolicitudEnvio read(String idSolicitudEnvio) {
        return solicitudEnvioDAO.read(idSolicitudEnvio);
    }

    @Override
    public void update(SolicitudEnvio solicitudEnvio) {
        solicitudEnvioDAO.update(solicitudEnvio);
    }

    @Override
    public void delete(SolicitudEnvio solicitudEnvio) {
        solicitudEnvioDAO.delete(solicitudEnvio);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByOrigenDestino(Integer origen, Integer destino, Integer estado) {
        return solicitudEnvioDAO.getSolicitudEnvioByOrigenDestino(origen,destino,estado);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEnvioOferta(Integer solicitudEnvioOfertaId, Integer estado) {
        return solicitudEnvioDAO.getSolicitudEnvioBySolicitudEnvioOferta(solicitudEnvioOfertaId,estado);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEstado(Integer estado) {
        return solicitudEnvioDAO.getSolicitudEnvioBySolicitudEstado(estado);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByUsuarioAndEstado(String usuario, int estado) {
        return solicitudEnvioDAO.getSolicitudEnvioByUsuarioAndEstado(usuario, estado);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioPorFacturarse(String clienteRuc) {
        return solicitudEnvioDAO.getSolicitudEnvioPorFacturarse(clienteRuc);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioCaducadas() {
        return solicitudEnvioDAO.getSolicitudEnvioCaducadas();
    }

    @Override
    public List<SolicitudEnvio> getSolictudEnvioWithOfertas(String usuario) {
        return solicitudEnvioDAO.getSolictudEnvioWithOfertas(usuario);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByFechaCaducidadNowEstado(Integer estado) {
        return solicitudEnvioDAO.getSolicitudEnvioByFechaCaducidadNowEstado(estado);
    }

    @Override
    public SolicitudEnvio getSolicitudEnvioByOferta(Integer oferta) {
        return solicitudEnvioDAO.getSolicitudEnvioByOferta(oferta);
    }

    @Override
    public List<EstadoSolicitudesDTO> getEstadoSolicitudes(String rucCliente) {
        return solicitudEnvioDAO.getEstadoSolicitudes(rucCliente);
    }

    @Override
    public List<SolicitudAdminDTO> getSolicitudesAdminBy(Integer estado, String rucCliente) {
        return solicitudEnvioDAO.getSolicitudesAdminBy(estado, rucCliente);
    }

    @Override
    public SolicitudEnvio getUltimaSolicitud(String rucCliente){
        return solicitudEnvioDAO.getUltimaSolicitud(rucCliente);
    }

    @Override
    public List<DireccionDTO> getDatosOrigenUltimosEnvios(Integer idOrigen, String rucCliente){
        return solicitudEnvioDAO.getDatosOrigenUltimosEnvios(idOrigen, rucCliente);
    }

    @Override
    public List<DireccionDTO> getDatosDestinoUltimosEnvios(Integer idDestino, String rucCliente){
        return solicitudEnvioDAO.getDatosDestinoUltimosEnvios(idDestino, rucCliente);
    }

}
