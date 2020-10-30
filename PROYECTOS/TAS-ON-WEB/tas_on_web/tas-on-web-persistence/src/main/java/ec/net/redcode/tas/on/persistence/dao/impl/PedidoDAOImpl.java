package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDAO;
import ec.net.redcode.tas.on.persistence.entities.Pedido;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class PedidoDAOImpl extends GenericDAOImpl<Pedido, Integer> implements PedidoDAO {

    public PedidoDAOImpl(){
        super(Pedido.class);
    }

    @Override
    public Pedido read(int pedidoId, String empresaRuc){
        Query query = em.createQuery("select o from Pedido o where o.pedidoId = :pedidoId " +
                " and o.usuarioByPedidoUsuarioCrea.usuarioRuc = :empresaRuc");
        query.setParameter("pedidoId", pedidoId);
        query.setParameter("empresaRuc", empresaRuc);
        try{
            return (Pedido) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public Pedido getByVisitaId(Integer visitaId, String empresaRuc){
        Query query = em.createQuery("select o from Pedido o where o.pedidoVisitaId = :visitaId " +
                " and o.visitaByPedidoVisitaId.usuarioByVisitaUsuarioCrea.usuarioRuc = :empresaRuc");
        query.setParameter("visitaId", visitaId);
        query.setParameter("empresaRuc", empresaRuc);
        try{
            return (Pedido)query.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Pedido> getAllPedidosBy(Date fechaPedido, Date fechaEntrega, String empresaRuc){
        StringBuilder querySB = new StringBuilder("select o from Pedido o where o.visitaByPedidoVisitaId.usuarioByVisitaUsuarioCrea.usuarioRuc = :empresaRuc ");
        if(fechaPedido != null) querySB.append(" and date(o.pedidoFechaCreacion) = :fechaPedido ");
        if(fechaEntrega != null) querySB.append(" and date(o.pedidoFechaEntregaDesde) = :fechaEntrega ");
        Query query = em.createQuery(querySB.toString());
        query.setParameter("empresaRuc", empresaRuc);
        if(fechaPedido != null) query.setParameter("fechaPedido", fechaPedido);
        if(fechaEntrega != null) query.setParameter("fechaEntrega", fechaEntrega);
        return query.getResultList();
    }

}
