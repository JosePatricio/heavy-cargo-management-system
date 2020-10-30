package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.PagoDAO;
import ec.net.redcode.tas.on.persistence.entities.Pago;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class PagoDAOImpl extends GenericDAOImpl<Pago, Integer> implements PagoDAO {

    public PagoDAOImpl(){
        super(Pago.class);
    }

    @Override
    public List<Pago> getPayByFactura(String facturaId) {
        Query query = em.createNamedQuery("Pago.PagoByFactura");
        query.setParameter("facturaId", facturaId);
        List<Pago> pagos;
        try {
            pagos = query.getResultList();
        } catch (Exception e){
            pagos = Collections.emptyList();
        }
        return pagos;
    }

}
