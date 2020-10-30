package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDAO;
import ec.net.redcode.tas.on.persistence.entities.Pedido;
import ec.net.redcode.tas.on.persistence.service.PedidoService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class PedidoServiceImpl implements PedidoService {

    @Setter private PedidoDAO pedidoDAO;

    @Override
    public void create(Pedido pedido) {
        pedidoDAO.create(pedido);
    }

    @Override
    public Pedido read(int pedidoId) {
        return pedidoDAO.read(pedidoId);
    }

    @Override
    public Pedido read(int pedidoId, String empresaRuc) {
        return pedidoDAO.read(pedidoId, empresaRuc);
    }

    @Override
    public void update(Pedido pedido) {
        pedidoDAO.update(pedido);
    }

    @Override
    public Pedido getByVisitaId(Integer visitaId, String empresaRuc){
        return pedidoDAO.getByVisitaId(visitaId, empresaRuc);
    }

    @Override
    public List<Pedido> getAllPedidosBy(Date fechaPedido, Date fechaEntrega, String empresaRuc){
        return pedidoDAO.getAllPedidosBy(fechaPedido, fechaEntrega, empresaRuc);
    }

}
