package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.PedidoDetalle;
import ec.net.redcode.tas.on.persistence.service.PedidoDetalleService;
import lombok.Setter;

public class PedidoDetalleServiceImpl implements PedidoDetalleService {

    @Setter private PedidoDetalleDAO pedidoDetalleDAO;

    @Override
    public void create(PedidoDetalle pedidoDetalle) {
        pedidoDetalleDAO.create(pedidoDetalle);
    }

    @Override
    public PedidoDetalle read(int pedidoDetalleId) {
        return pedidoDetalleDAO.read(pedidoDetalleId);
    }

    @Override
    public void update(PedidoDetalle pedidoDetalle) {
        pedidoDetalleDAO.update(pedidoDetalle);
    }

}
