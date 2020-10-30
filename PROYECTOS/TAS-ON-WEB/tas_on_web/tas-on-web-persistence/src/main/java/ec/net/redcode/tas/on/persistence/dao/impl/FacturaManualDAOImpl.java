package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaManualDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaManual;

import javax.persistence.Query;
import java.util.List;

public class FacturaManualDAOImpl extends GenericDAOImpl<FacturaManual, String> implements FacturaManualDAO {

    public FacturaManualDAOImpl() {
        super(FacturaManual.class);
    }

    @Override
    public List<FacturaManual> getAllFacturaManual() {
        Query query = em.createQuery("select o from FacturaManual o order by o.facturaManualFechaEmision desc");
        return query.getResultList();
    }

    @Override
    public FacturaManual read(String claveAcceso) {
        Query query = em.createQuery("select o from FacturaManual o where o.facturaManualClaveAcceso = :claveAcceso");
        query.setParameter("claveAcceso", claveAcceso);
        try{
            return (FacturaManual) query.getSingleResult();
        } catch (Exception e){
            return null;
        }

    }
}
