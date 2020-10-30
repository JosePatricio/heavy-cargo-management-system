package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.EbillingDAO;
import ec.net.redcode.tas.on.persistence.entities.Ebilling;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class EbillingDAOImpl extends GenericDAOImpl<Ebilling, Integer> implements EbillingDAO {

    public EbillingDAOImpl() {
        super(Ebilling.class);
    }

    @Override
    public List<Ebilling> readByUserEbillingId(String userEbillingId){
        Query query = em.createQuery("select o from Ebilling o where o.ebillingUsuarioEbilling = :userEbillingId order by o.ebillingFechaEmision desc");
        return query.setParameter("userEbillingId", userEbillingId).getResultList();
    }

    @Override
    public List<Ebilling> readByUserId(String userId){
        Query query = em.createQuery("select o from Ebilling o where o.ebillingUsuarioId = :userId order by o.ebillingFechaEmision desc");
        return query.setParameter("userId", userId).getResultList();
    }

    @Override
    public Ebilling readEbilling(String ebillingNumero, String userEbillingId){
        Query query = em.createQuery("select o from Ebilling o where o.ebillingNumero = :ebillingNumero and" +
                " o.ebillingUsuarioEbilling = :userEbillingId");
        query.setParameter("ebillingNumero", ebillingNumero);
        query.setParameter("userEbillingId", userEbillingId);
        try{
            return (Ebilling)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Ebilling readByClaveAcceso(String claveAcceso){
        Query query = em.createQuery("select o from Ebilling o where o.ebillingClaveAcceso = :claveAcceso");
        query.setParameter("claveAcceso", claveAcceso);
        try{
            return (Ebilling)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }


}
