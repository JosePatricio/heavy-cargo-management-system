package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.Registro;
import ec.net.redcode.tas.on.persistence.entities.Localidad;

import java.util.List;

public interface LocalidadDAO extends  GenericDAO<Localidad, Integer> {

    Localidad getLocalidadByLocalidadId(Integer localidadId,Integer localidadEstado);
    List<Localidad> getLocalidadByLocalidadIdPadre(Integer localidadIdPadre, Integer localidadEstado);
    List<Localidad> getAllCiudades();
    List<Registro<Integer, String>> getLocalidadesDestinoSolicitudesCompletadas();
    List<Registro<Integer, String>> getLocalidadesOrigenSolicitudesCompletadas();

}
