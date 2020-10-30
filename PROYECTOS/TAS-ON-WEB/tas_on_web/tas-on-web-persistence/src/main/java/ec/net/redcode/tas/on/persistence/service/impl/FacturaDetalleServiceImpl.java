package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaDetalle;
import ec.net.redcode.tas.on.persistence.service.FacturaDetalleService;
import lombok.Setter;

import java.util.List;

public class FacturaDetalleServiceImpl implements FacturaDetalleService {

    @Setter private FacturaDetalleDAO facturaDetalleDAO;

    @Override
    public void create(FacturaDetalle facturaDetalle) {
        facturaDetalleDAO.create(facturaDetalle);
    }

    @Override
    public List<FacturaDetalle> getFacturaDetalleByFactura(String idFactura) {
        return facturaDetalleDAO.getFacturaDetalleByFactura(idFactura);
    }

    @Override
    public FacturaDetalle getFacturaDetalleByOfertaId(int idOferta, int tipo) {
        return facturaDetalleDAO.getFacturaDetalleByOfertaId(idOferta, tipo);
    }

}
