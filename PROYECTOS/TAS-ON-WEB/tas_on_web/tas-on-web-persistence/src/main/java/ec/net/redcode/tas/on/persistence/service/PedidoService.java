package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Pedido;

import java.util.Date;
import java.util.List;

public interface PedidoService {

    void create(Pedido pedido);
    Pedido read(int pedidoId);
    Pedido read(int pedidoId, String empresaRuc);
    void update(Pedido pedido);
    Pedido getByVisitaId(Integer visitaId, String empresaRuc);
    List<Pedido> getAllPedidosBy(Date fechaPedido, Date fechaEntrega, String empresaRuc);

}
