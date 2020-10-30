package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.EbillingDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.EbillingDetalle;

import javax.persistence.Query;
import java.util.List;

public class EbillingDetalleDAOImpl extends GenericDAOImpl<EbillingDetalle, Integer> implements EbillingDetalleDAO {

    public EbillingDetalleDAOImpl() {
        super(EbillingDetalle.class);
    }

    @Override
    public List<EbillingDetalle> readByEbillingId(int ebillingId){
        Query query = em.createQuery("select o from EbillingDetalle o where o.ebillingDetalleEbillingId = :ebillingId")
                .setParameter("ebillingId", ebillingId);
        return query.getResultList();
    }
}
