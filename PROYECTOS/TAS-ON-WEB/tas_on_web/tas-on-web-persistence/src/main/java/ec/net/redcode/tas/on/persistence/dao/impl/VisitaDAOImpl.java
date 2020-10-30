package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.VisitaDAO;
import ec.net.redcode.tas.on.persistence.entities.Visita;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class VisitaDAOImpl extends GenericDAOImpl<Visita, Integer> implements VisitaDAO {

    public VisitaDAOImpl(){
        super(Visita.class);
    }

    @Override
    public List<Visita> getVisitasBy(String vendedorId, Date fechaVisita){
        Query query = em.createQuery("select o from Visita o where o.visitaVendedorUsuario = :vendedorId" +
                " and date(o.visitaFecha) = :fechaVisita");
        query.setParameter("vendedorId", vendedorId);
        query.setParameter("fechaVisita", fechaVisita);
        return query.getResultList();
    }

    @Override
    public List<Visita> getVisitasEmpresaBy(String rucEmpresa, Date fechaVisita){
        Query query = em.createQuery("select o from Visita o where o.usuarioByVisitaUsuarioCrea.usuarioRuc = :rucEmpresa" +
                " and date(o.visitaFecha) = :fechaVisita");
        query.setParameter("rucEmpresa", rucEmpresa);
        query.setParameter("fechaVisita", fechaVisita);
        return query.getResultList();
    }

}
