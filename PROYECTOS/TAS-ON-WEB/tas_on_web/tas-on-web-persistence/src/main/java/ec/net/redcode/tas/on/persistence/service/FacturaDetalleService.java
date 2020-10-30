package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.FacturaDetalle;

import java.util.List;

public interface FacturaDetalleService {

    void create(FacturaDetalle facturaDetalle);
    List<FacturaDetalle> getFacturaDetalleByFactura(String idFactura);
    FacturaDetalle getFacturaDetalleByOfertaId(int idOferta, int tipo);

}
