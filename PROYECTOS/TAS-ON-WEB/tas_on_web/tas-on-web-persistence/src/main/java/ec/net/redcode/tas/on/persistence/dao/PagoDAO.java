package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Pago;

import java.util.List;

public interface PagoDAO extends GenericDAO<Pago, Integer> {

    List<Pago> getPayByFactura(String facturaId);

}
