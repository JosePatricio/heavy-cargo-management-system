package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.PedidoDetalle;

public class PedidoDetalleDAOImpl extends GenericDAOImpl<PedidoDetalle, Integer> implements PedidoDetalleDAO {

    public PedidoDetalleDAOImpl(){
        super(PedidoDetalle.class);
    }

}
