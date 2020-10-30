package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Pago;

import java.util.List;

public interface PagoService {

    void create(Pago pago);
    Pago read(int pagoId);
    void update(Pago pago);
    void delete(Pago pago);
    List<Pago> getPayByFactura(String facturaId);

}
