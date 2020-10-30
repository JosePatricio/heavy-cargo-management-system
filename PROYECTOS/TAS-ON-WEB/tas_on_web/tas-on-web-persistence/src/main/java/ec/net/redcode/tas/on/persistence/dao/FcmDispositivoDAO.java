package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;

import java.util.List;

public interface FcmDispositivoDAO extends GenericDAO<FcmDispositivo, String> {

    List<FcmDispositivo> getAllFcmDispositivo();

}
