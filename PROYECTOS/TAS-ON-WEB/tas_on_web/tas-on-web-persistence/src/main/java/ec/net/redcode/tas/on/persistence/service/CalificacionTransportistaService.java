package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.persistence.entities.CalificacionTransportista;

import java.util.List;
import java.util.Map;

public interface CalificacionTransportistaService {

    List<CalificacionTransportistaDTO> getCalificacionTransportistasByUser(String nombreUsuario);
    boolean puedeCalificarTransportista(int calificacionId,String nombreUsuario);
    CalificacionTransportista read(int id);
    void create(CalificacionTransportista calificacionTransportista);
    Map<String, Number> getResultadoCalificacionesTransportista(int conductorId);

}
