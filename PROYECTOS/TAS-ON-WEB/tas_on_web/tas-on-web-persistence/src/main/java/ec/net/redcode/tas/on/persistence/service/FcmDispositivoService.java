package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;

import java.util.List;

public interface FcmDispositivoService {

    void create(FcmDispositivo fcmDispositivo);
    List<FcmDispositivo> getAllFcmDispositivo();
}
