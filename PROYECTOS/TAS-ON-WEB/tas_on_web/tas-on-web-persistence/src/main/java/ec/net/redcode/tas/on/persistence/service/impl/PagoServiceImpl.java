package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.PagoDAO;
import ec.net.redcode.tas.on.persistence.entities.Pago;
import ec.net.redcode.tas.on.persistence.service.PagoService;
import lombok.Setter;

import java.util.List;

public class PagoServiceImpl implements PagoService {

    @Setter private PagoDAO pagoDAO;

    @Override
    public void create(Pago pago) {
        pagoDAO.create(pago);
    }

    @Override
    public Pago read(int pagoId) {
        return pagoDAO.read(pagoId);
    }

    @Override
    public void update(Pago pago) {
        pagoDAO.update(pago);
    }

    @Override
    public void delete(Pago pago) {
        pagoDAO.delete(pago);
    }

    @Override
    public List<Pago> getPayByFactura(String facturaId) {
        return pagoDAO.getPayByFactura(facturaId);
    }

}
