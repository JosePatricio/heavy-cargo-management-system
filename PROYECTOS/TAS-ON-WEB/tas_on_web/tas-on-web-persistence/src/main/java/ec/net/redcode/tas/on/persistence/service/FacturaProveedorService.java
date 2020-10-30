package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;

import java.util.List;

public interface FacturaProveedorService {

    void create(FacturaProveedor facturaProveedor);
    FacturaProveedor read(int idFacturaProveedor);
    void update(FacturaProveedor facturaProveedor);
    void delete(FacturaProveedor facturaProveedor);
    List<FacturaProveedor> getByEstado(int estado);
    FacturaProveedor getByNumeroFactura(String numeroFactura);
    FacturaProveedor getByNumeroFacturaAndRuc(String numeroFactura, String ruc);

}
