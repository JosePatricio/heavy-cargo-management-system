package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.ConductorDAO;
import ec.net.redcode.tas.on.persistence.entities.Conductor;

import javax.persistence.Query;
import java.util.List;

public class ConductorDAOImpl extends GenericDAOImpl<Conductor, Integer> implements ConductorDAO {

    public ConductorDAOImpl(){
        super(Conductor.class);
    }

    @Override
    public List<Conductor> getConductorsByUser(String user) {
        Query query = em.createNamedQuery("Conductor.ConductorByConductorUsuario");
        query.setParameter("usuario", user);
        List<Conductor> conductors = query.getResultList();
        return conductors;
    }

    @Override
    public List<Conductor> getConductorsByUserAndEstado(String user, int estado) {
        Query query = em.createNamedQuery("Conductor.ConductorByConductorUsuarioAndEstado");
        query.setParameter("usuario", user);
        query.setParameter("estado", estado);
        return query.getResultList();
   }

   @Override
    public List<Conductor> getConductoresAcreditados(String rucClienteOferta, String rucClienteSolicitud){
        Query query = em.createQuery("select c from Conductor c, ConductorAcreditado ca, Usuario u " +
                " where ca.conductorAcreditadoClienteRuc = :rucClienteSolicitud " +
                " and ca.conductorAcreditadoConductorId = c.conductorId " +
                " and c.conductorEstado = :conductorEstado " +
                " and c.conductorUsuario = u.usuarioIdDocumento " +
                " and u.usuarioRuc = :rucClienteOferta");
        query.setParameter("rucClienteSolicitud", rucClienteSolicitud);
        query.setParameter("rucClienteOferta", rucClienteOferta);
        query.setParameter("conductorEstado", 1);
        return query.getResultList();
   }

   @Override
    public Conductor readConductorAcreditado(int idConductor, String rucClienteSolicitud){
       Query query = em.createQuery("select c from Conductor c, ConductorAcreditado ca " +
               " where  c.conductorId = ca.conductorAcreditadoConductorId " +
               " and ca.conductorAcreditadoClienteRuc = :rucClienteSolicitud " +
               " and ca.conductorAcreditadoConductorId = :idConductor " +
               " and c.conductorEstado = :conductorEstado");
       query.setParameter("rucClienteSolicitud", rucClienteSolicitud);
       query.setParameter("idConductor", idConductor);
       query.setParameter("conductorEstado", 1);
       try{
           return (Conductor) query.getResultList().get(0);
       }catch (Exception e){
           return null;
       }
   }

}
