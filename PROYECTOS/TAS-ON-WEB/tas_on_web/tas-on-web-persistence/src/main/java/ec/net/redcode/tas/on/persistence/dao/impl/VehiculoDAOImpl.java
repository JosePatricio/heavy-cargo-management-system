package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.VehiculoDAO;
import ec.net.redcode.tas.on.persistence.entities.Vehiculo;

import javax.persistence.Query;
import java.util.List;

public class VehiculoDAOImpl extends GenericDAOImpl<Vehiculo, Integer> implements VehiculoDAO {

    public VehiculoDAOImpl(){
        super(Vehiculo.class);
    }

    @Override
    public List<Vehiculo> getVehiculoByUser(String user) {
        Query query = em.createNamedQuery("Vehiculo.VehiculoByUsuario");
        query.setParameter("usuario", user);
        List<Vehiculo> vehiculos = query.getResultList();
        return vehiculos;
    }

    @Override
    public List<Vehiculo> getVehiculoByUserAndEstado(String user, int estado) {
        Query query = em.createNamedQuery("Vehiculo.VehiculoByUsuarioAndEstado");
        query.setParameter("usuario", user);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @Override
    public Vehiculo getVehiculoByPlaca(String placa) {
        Query query = em.createNamedQuery("Vehiculo.VehiculoByPlaca");
        query.setParameter("placa", placa);
        Vehiculo vehiculo;
        try {
            vehiculo = (Vehiculo) query.getSingleResult();
        } catch (Exception e){
            vehiculo = null;
        }
        return vehiculo;
    }

    @Override
    public List<Vehiculo> getVehiculosAcreditados(String rucClienteOferta, String rucClienteSolicitud){
        Query query = em.createQuery("select v from Vehiculo v, VehiculoAcreditado va, Usuario u " +
                " where va.vehiculoAcreditadoClienteRuc = :rucClienteSolicitud " +
                " and va.vehiculoAcreditadoVehiculoId = v.vehiculoId " +
                " and v.vehiculoUsuario = u.usuarioIdDocumento " +
                " and v.vehiculoEstado = :estado " +
                " and u.usuarioRuc = :rucClienteOferta");
        query.setParameter("rucClienteSolicitud", rucClienteSolicitud);
        query.setParameter("rucClienteOferta", rucClienteOferta);
        query.setParameter("estado", 1);
        return query.getResultList();
    }

    @Override
    public Vehiculo readVehiculoAcreditado(int idVehiculo, String rucClienteSolicitud){
        Query query = em.createQuery("select v from Vehiculo v, VehiculoAcreditado va " +
                " where v.vehiculoId = va.vehiculoAcreditadoVehiculoId " +
                " and va.vehiculoAcreditadoClienteRuc = :rucClienteSolicitud" +
                " and va.vehiculoAcreditadoVehiculoId = :idVehiculo" +
                " and v.vehiculoEstado = :estado ");
        query.setParameter("rucClienteSolicitud", rucClienteSolicitud);
        query.setParameter("idVehiculo", idVehiculo);
        query.setParameter("estado", 1);
        try{
            return (Vehiculo) query.getResultList().get(0);
        }catch (Exception e){
            return null;
        }
    }
}
