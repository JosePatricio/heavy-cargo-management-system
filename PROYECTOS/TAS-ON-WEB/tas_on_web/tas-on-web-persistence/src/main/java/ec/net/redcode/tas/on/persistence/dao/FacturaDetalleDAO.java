package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.FacturaDetalle;

import java.util.List;

public interface FacturaDetalleDAO extends GenericDAO<FacturaDetalle, Integer> {

    List<FacturaDetalle> getFacturaDetalleByFactura(String idFactura);
    FacturaDetalle getFacturaDetalleByOfertaId(int idOferta, int tipo);

}
