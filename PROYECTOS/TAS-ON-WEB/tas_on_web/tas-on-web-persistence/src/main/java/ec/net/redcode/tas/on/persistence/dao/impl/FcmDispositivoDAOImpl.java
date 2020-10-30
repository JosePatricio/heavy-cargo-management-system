package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.FcmDispositivoDAO;
import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class FcmDispositivoDAOImpl extends GenericDAOImpl<FcmDispositivo, String> implements FcmDispositivoDAO {

    public FcmDispositivoDAOImpl() {
        super(FcmDispositivo.class);
    }

    @Override
    public List<FcmDispositivo> getAllFcmDispositivo() {
        Query query = em.createNamedQuery("FcmDispositivo.FcmDispositivoAll");
        List<FcmDispositivo> fcmDispositivos;
        try {
            fcmDispositivos = query.getResultList();
        } catch (Exception e) {
            fcmDispositivos = Collections.emptyList();
        }
        return fcmDispositivos;
    }

}
