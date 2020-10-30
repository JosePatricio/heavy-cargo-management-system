package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Pedido;

import java.util.Date;
import java.util.List;

public interface PedidoDAO extends GenericDAO<Pedido, Integer> {

    Pedido read(int pedidoId, String empresaRuc);
    Pedido getByVisitaId(Integer visitaId, String empresaRuc);
    List<Pedido> getAllPedidosBy(Date fechaPedido, Date fechaEntrega, String empresaRuc);

}
