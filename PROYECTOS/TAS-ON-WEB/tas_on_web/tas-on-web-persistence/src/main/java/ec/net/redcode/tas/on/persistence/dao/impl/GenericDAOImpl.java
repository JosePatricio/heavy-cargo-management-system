package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.GenericDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class GenericDAOImpl <T, PK extends Serializable> implements GenericDAO<T, PK> {

    @PersistenceContext
    EntityManager em;

    private Class<T> type;

    public GenericDAOImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T newInstance) {
        em.persist(newInstance);
        em.flush();
    }

    @Override
    public T read(Object id) {
        return em.find(type, id);
    }

    @Override
    public void update(T persistentObject) {
        em.merge(persistentObject);
        em.flush();
    }

    @Override
    public void delete(T persistentObject) {
        em.remove(em.contains(persistentObject) ? persistentObject : em.merge(persistentObject));
        em.flush();
    }

}
