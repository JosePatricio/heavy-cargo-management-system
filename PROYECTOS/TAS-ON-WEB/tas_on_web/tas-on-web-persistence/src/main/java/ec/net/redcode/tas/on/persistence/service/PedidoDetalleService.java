package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.PedidoDetalle;

public interface PedidoDetalleService {

    void create(PedidoDetalle pedidoDetalle);
    PedidoDetalle read(int pedidoDetalleId);
    void update(PedidoDetalle pedidoDetalle);

}
