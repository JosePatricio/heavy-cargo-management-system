package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.FcmDispositivoDAO;
import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;
import ec.net.redcode.tas.on.persistence.service.FcmDispositivoService;
import lombok.Setter;

import java.util.List;

public class FcmDispositivoServiceImpl implements FcmDispositivoService {

    @Setter private FcmDispositivoDAO fcmDispositivoDAO;

    @Override
    public void create(FcmDispositivo fcmDispositivo) {
        fcmDispositivoDAO.create(fcmDispositivo);
    }

    @Override
    public List<FcmDispositivo> getAllFcmDispositivo() {
        return fcmDispositivoDAO.getAllFcmDispositivo();
    }

}
