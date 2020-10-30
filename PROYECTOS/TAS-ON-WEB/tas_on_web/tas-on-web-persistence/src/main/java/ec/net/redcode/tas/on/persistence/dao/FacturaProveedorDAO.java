package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;

import java.util.List;

public interface FacturaProveedorDAO extends GenericDAO<FacturaProveedor, Integer> {

    List<FacturaProveedor> getByEstado(int estado);
    FacturaProveedor getByNumeroFactura(String numeroFactura);
    FacturaProveedor getByNumeroFacturaAndRuc(String numeroFactura, String ruc);

}
