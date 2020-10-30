package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.AdquirienteDAO;
import ec.net.redcode.tas.on.persistence.entities.Adquiriente;

import javax.persistence.Query;
import java.util.List;

public class AdquirienteDAOImpl extends GenericDAOImpl<Adquiriente, String> implements AdquirienteDAO {

    public AdquirienteDAOImpl() {
        super(Adquiriente.class);
    }

    @Override
    public List<Adquiriente> getAllAdquiriente() {
        Query query = em.createNamedQuery("Adquiriente.getAll");
        return query.getResultList();
    }
}
