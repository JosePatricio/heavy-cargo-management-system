package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.ClientePedidosDAO;
import ec.net.redcode.tas.on.persistence.entities.ClientePedidos;

import javax.persistence.Query;
import java.util.List;

public class ClientePedidosDAOImpl extends GenericDAOImpl<ClientePedidos, Integer> implements ClientePedidosDAO {

    public ClientePedidosDAOImpl(){
        super(ClientePedidos.class);
    }

    @Override
    public List<ClientePedidos> getClientes(String clienteTasOnRuc){
        Query query = em.createQuery("select o from ClientePedidos o " +
                "where o.usuarioByClientePedidosUsuarioCrea.usuarioRuc = :clienteTasOnRuc");
        query.setParameter("clienteTasOnRuc", clienteTasOnRuc);
        return query.getResultList();
    }

    @Override
    public List<ClientePedidos> getClientesBy(String razonSocial, String clientePedidosRuc, String vendedorId, Integer diaVisita, String clienteTasOnRuc){
        StringBuilder querySB = new StringBuilder("select o from ClientePedidos o where o.usuarioByClientePedidosUsuarioCrea.usuarioRuc = :clienteTasOnRuc ");
        if(razonSocial != null) querySB.append(" and o.clientePedidosRazonSocial like :razonSocial ");
        if(clientePedidosRuc != null ) querySB.append(" and o.clientePedidosRuc like :clientePedidosRuc ");
        if(vendedorId != null && !vendedorId.equals("-1")) querySB.append(" and o.clientePedidosVendedorAsignado = :vendedorId ");
        if(vendedorId != null && vendedorId.equals("-1")) querySB.append(" and o.clientePedidosVendedorAsignado is null ");
        if(diaVisita != null && diaVisita != -1) querySB.append(" and o.clientePedidosDiaSemanaVisita = :diaVisita ");
        if(diaVisita != null && diaVisita == -1) querySB.append(" and o.clientePedidosDiaSemanaVisita is null ");
        Query query = em.createQuery(querySB.toString());
        query.setParameter("clienteTasOnRuc", clienteTasOnRuc);
        if(razonSocial != null) query.setParameter("razonSocial", "%"+razonSocial+"%");
        if(clientePedidosRuc != null ) query.setParameter("clientePedidosRuc", "%"+clientePedidosRuc+"%");
        if(vendedorId != null && !vendedorId.equals("-1")) query.setParameter("vendedorId", vendedorId);
        if(diaVisita != null && diaVisita != -1) query.setParameter("diaVisita", diaVisita);
        return query.getResultList();
    }

}
