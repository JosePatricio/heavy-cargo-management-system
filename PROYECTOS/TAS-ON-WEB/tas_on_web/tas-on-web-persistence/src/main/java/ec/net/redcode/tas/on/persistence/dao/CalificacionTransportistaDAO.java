package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.persistence.entities.CalificacionTransportista;

import java.util.List;
import java.util.Map;

public interface CalificacionTransportistaDAO extends GenericDAO<CalificacionTransportista, Integer> {

    List<CalificacionTransportistaDTO> getCalificacionTransportistasByUser(String nombreUsuario);
    boolean puedeCalificarTransportista(int calificacionId, String nombreUsuario);
    Map<String, Number> getResultadoCalificacionesTransportista(int conductorId);

}
