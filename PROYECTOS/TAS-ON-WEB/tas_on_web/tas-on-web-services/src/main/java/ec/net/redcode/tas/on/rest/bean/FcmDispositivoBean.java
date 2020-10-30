package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;
import ec.net.redcode.tas.on.persistence.service.FcmDispositivoService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;

import java.util.List;

public class FcmDispositivoBean extends TasOnResponse {

    private FcmDispositivoService fcmDispositivoService;

    protected void create(FcmDispositivo fcmDispositivo){
        fcmDispositivoService.create(fcmDispositivo);
    }

    public void setFcmDispositivoService(FcmDispositivoService fcmDispositivoService) {
        this.fcmDispositivoService = fcmDispositivoService;
    }
}
