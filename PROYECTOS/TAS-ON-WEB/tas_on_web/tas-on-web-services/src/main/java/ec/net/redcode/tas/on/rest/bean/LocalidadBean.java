package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.persistence.entities.Localidad;

import java.util.List;

public class LocalidadBean extends CommonBean {

    public void createLocalidad(Localidad localidad){
        localidadService.createLocalidad(localidad);
    }

    public Localidad readLocalidad(int idLocalidad){
        Localidad localidad = this.localidadService.readLocalidad(idLocalidad);
        return localidad;
    }

    public void updateLocalidad(Localidad localidad){
        localidadService.updateLocalidad(localidad);
    }

    public void deleteLocalidad(Localidad localidad){
        localidadService.deleteLocalidad(localidad);
    }

    public List<Localidad> getAllCiudades(){
        return localidadService.getAllCiudades();
    }

}
