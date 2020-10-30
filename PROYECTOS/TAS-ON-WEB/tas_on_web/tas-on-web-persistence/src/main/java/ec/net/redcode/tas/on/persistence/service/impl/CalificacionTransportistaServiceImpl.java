package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.persistence.dao.CalificacionTransportistaDAO;
import ec.net.redcode.tas.on.persistence.entities.CalificacionTransportista;
import ec.net.redcode.tas.on.persistence.service.CalificacionTransportistaService;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class CalificacionTransportistaServiceImpl implements CalificacionTransportistaService {

    @Setter private CalificacionTransportistaDAO calificacionTransportistaDAO;

    @Override
    public List<CalificacionTransportistaDTO> getCalificacionTransportistasByUser(String nombreUsuario){
        return calificacionTransportistaDAO.getCalificacionTransportistasByUser(nombreUsuario);
    }

    @Override
    public boolean puedeCalificarTransportista(int calificacionId, String nombreUsuario){
        return calificacionTransportistaDAO.puedeCalificarTransportista(calificacionId, nombreUsuario);
    }

    @Override
    public CalificacionTransportista read(int id){
        return calificacionTransportistaDAO.read(id);
    }

    @Override
    public void create(CalificacionTransportista calificacionTransportista){
        calificacionTransportistaDAO.create(calificacionTransportista);
    }

    @Override
    public Map<String, Number> getResultadoCalificacionesTransportista(int conductorId){
       return calificacionTransportistaDAO.getResultadoCalificacionesTransportista(conductorId);
    }

}