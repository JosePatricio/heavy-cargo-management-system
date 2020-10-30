package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.common.dto.Registro;

import java.util.List;

public interface EstadisticasService {

    List<Registro<Integer, String>> getLocalidadesDestinoSolicitudesCompletadas();
    List<Registro<Integer, String>> getLocalidadesOrigenSolicitudesCompletadas();
    double getToneladasEnviadasSolicitudesCompletadas();
    double getTotalAhorradoSolicitudesCompletadas();

}
