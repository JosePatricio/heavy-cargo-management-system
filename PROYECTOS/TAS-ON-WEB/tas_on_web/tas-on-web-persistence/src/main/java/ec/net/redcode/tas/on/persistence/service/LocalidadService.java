package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Localidad;

import java.util.List;

public interface LocalidadService {

    void createLocalidad(Localidad localidad);

    Localidad readLocalidad(int idLocalidad);

    void updateLocalidad(Localidad localidad);

    void deleteLocalidad(Localidad localidad);

    Localidad getLocalidadByLocalidadId(Integer localidadId,Integer localidadEstado);

    List<Localidad> getLocalidadByLocalidadIdPadre(Integer localidadIdPadre, Integer localidadEstado);

    List<Localidad> getAllCiudades();

}
