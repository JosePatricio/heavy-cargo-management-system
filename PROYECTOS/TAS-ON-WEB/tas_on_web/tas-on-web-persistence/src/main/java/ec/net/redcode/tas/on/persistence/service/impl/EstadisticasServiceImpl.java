package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.Registro;
import ec.net.redcode.tas.on.persistence.dao.LocalidadDAO;
import ec.net.redcode.tas.on.persistence.dao.SolicitudEnvioDAO;
import ec.net.redcode.tas.on.persistence.service.EstadisticasService;
import lombok.Setter;

import java.util.List;

public class EstadisticasServiceImpl implements EstadisticasService {

    @Setter private LocalidadDAO localidadDAO;
    @Setter private SolicitudEnvioDAO solicitudEnvioDAO;

    @Override
    public List<Registro<Integer, String>> getLocalidadesDestinoSolicitudesCompletadas() {
        return localidadDAO.getLocalidadesDestinoSolicitudesCompletadas();
    }

    @Override
    public List<Registro<Integer, String>> getLocalidadesOrigenSolicitudesCompletadas() {
        return localidadDAO.getLocalidadesOrigenSolicitudesCompletadas();
    }

    @Override
    public double getToneladasEnviadasSolicitudesCompletadas() {
        return solicitudEnvioDAO.getToneladasEnviadasSolicitudesCompletadas();
    }

    @Override
    public double getTotalAhorradoSolicitudesCompletadas() {
        return solicitudEnvioDAO.getTotalAhorradoSolicitudesCompletadas();
    }

}
