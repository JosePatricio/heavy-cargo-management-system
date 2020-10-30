package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.SecuenciaDAO;
import ec.net.redcode.tas.on.persistence.entities.Secuencia;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SecuenciaDAOImpl extends GenericDAOImpl<Secuencia, Integer> implements SecuenciaDAO{

    public SecuenciaDAOImpl(){
        super(Secuencia.class);
    }

    @Override
    public Secuencia getSecuenciaByCliente(String idCliente) {
        Query query = em.createNamedQuery("Secuencia.SecuenciaByCliente");
        query.setParameter("idCliente", idCliente);
        Secuencia secuencia = (Secuencia) query.getSingleResult();
        return secuencia;
    }

    @Override
    public Secuencia getSecuenciaByNemonico(String nemonico) {
        Query query = em.createNamedQuery("Secuencia.SecuenciaByNemonico");
        query.setParameter("nemonico", nemonico);
        try{
            return  (Secuencia) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
