package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.NotaCreditoDAO;
import ec.net.redcode.tas.on.persistence.entities.NotaCredito;

import javax.persistence.Query;
import java.util.List;

public class NotaCreditoDAOImpl extends GenericDAOImpl<NotaCredito, Integer> implements NotaCreditoDAO {

    public NotaCreditoDAOImpl() {
        super(NotaCredito.class);
    }

    @Override
    public List<NotaCredito> getNCPendientesEmitir(){
        Query query = em.createQuery("select o from NotaCredito o where o.notaCreditoClaveAcceso is null");
        return query.getResultList();
    }

    @Override
    public NotaCredito readByNotaCreditoFacturaId(String facturaId) {
        Query query = em.createQuery("select o from NotaCredito o where o.notaCreditoFacturaId = :facturaId");
        query.setParameter("facturaId", facturaId);
        try{
            return (NotaCredito) query.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
}
