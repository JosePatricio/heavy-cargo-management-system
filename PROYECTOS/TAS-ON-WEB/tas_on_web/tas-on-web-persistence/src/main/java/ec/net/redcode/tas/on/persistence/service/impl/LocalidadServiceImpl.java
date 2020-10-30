package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.LocalidadDAO;
import ec.net.redcode.tas.on.persistence.entities.Localidad;
import ec.net.redcode.tas.on.persistence.service.LocalidadService;
import lombok.Setter;

import java.util.List;

public class LocalidadServiceImpl implements LocalidadService {

    @Setter private LocalidadDAO localidadDAO;

    @Override
    public void createLocalidad(Localidad localidad) {
        localidadDAO.create(localidad);
    }

    @Override
    public Localidad readLocalidad(int idLocalidad) {
        return localidadDAO.read(idLocalidad);
    }

    @Override
    public void updateLocalidad(Localidad localidad) {
        localidadDAO.update(localidad);
    }

    @Override
    public void deleteLocalidad(Localidad localidad) {
        localidadDAO.delete(localidad);
    }

    @Override
    public Localidad getLocalidadByLocalidadId(Integer localidadId, Integer localidadEstado) {
        return localidadDAO.getLocalidadByLocalidadId(localidadId, localidadEstado);
    }

    @Override
    public List<Localidad> getLocalidadByLocalidadIdPadre(Integer localidadIdPadre, Integer localidadEstado) {
        return localidadDAO.getLocalidadByLocalidadIdPadre(localidadIdPadre, localidadEstado);
    }

    @Override
    public List<Localidad> getAllCiudades() {
        return localidadDAO.getAllCiudades();
    }

}
