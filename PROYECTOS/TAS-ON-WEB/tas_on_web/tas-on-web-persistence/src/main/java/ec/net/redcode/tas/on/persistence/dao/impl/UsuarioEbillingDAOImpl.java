package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.UsuarioEbillingDAO;
import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UsuarioEbillingDAOImpl extends GenericDAOImpl<UsuarioEbilling, String> implements UsuarioEbillingDAO {

    public UsuarioEbillingDAOImpl() {
        super(UsuarioEbilling.class);
    }

    @Override
    public UsuarioEbilling readByUserId(String userID){
        Query query = em.createNamedQuery("UsuarioEbilling.getByUserID");
        query.setParameter("idUsuario", userID);
        try{
            return (UsuarioEbilling)query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
