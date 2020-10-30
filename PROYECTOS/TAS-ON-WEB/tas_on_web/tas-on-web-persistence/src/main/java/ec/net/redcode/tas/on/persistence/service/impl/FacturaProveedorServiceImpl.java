package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaProveedorDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;
import ec.net.redcode.tas.on.persistence.service.FacturaProveedorService;
import lombok.Setter;

import java.util.List;

public class FacturaProveedorServiceImpl implements FacturaProveedorService {

    @Setter private FacturaProveedorDAO facturaProveedorDAO;

    @Override
    public void create(FacturaProveedor facturaProveedor) {
        facturaProveedorDAO.create(facturaProveedor);
    }

    @Override
    public FacturaProveedor read(int idFacturaProveedor) {
        return facturaProveedorDAO.read(idFacturaProveedor);
    }

    @Override
    public void update(FacturaProveedor facturaProveedor) {
        facturaProveedorDAO.update(facturaProveedor);
    }

    @Override
    public void delete(FacturaProveedor facturaProveedor) {
        facturaProveedorDAO.delete(facturaProveedor);
    }

    @Override
    public List<FacturaProveedor> getByEstado(int estado) {
        return facturaProveedorDAO.getByEstado(estado);
    }

    @Override
    public FacturaProveedor getByNumeroFactura(String numeroFactura) {
        return facturaProveedorDAO.getByNumeroFactura(numeroFactura);
    }

    @Override
    public FacturaProveedor getByNumeroFacturaAndRuc(String numeroFactura, String ruc) {
        return facturaProveedorDAO.getByNumeroFacturaAndRuc(numeroFactura, ruc);
    }
}
