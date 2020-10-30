package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaManualDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaManual;
import ec.net.redcode.tas.on.persistence.service.FacturaManualService;
import lombok.Setter;

import java.util.List;

public class FacturaManualServiceImpl implements FacturaManualService {

    @Setter private FacturaManualDAO facturaManualDAO;

    @Override
    public void create(FacturaManual facturaManual) {
        facturaManualDAO.create(facturaManual);
    }

    @Override
    public FacturaManual read(String claveAcceso) {
        return facturaManualDAO.read(claveAcceso);
    }

    @Override
    public void update(FacturaManual facturaManual) {
        facturaManualDAO.update(facturaManual);
    }

    @Override
    public void delete(FacturaManual facturaManual) {
        facturaManualDAO.delete(facturaManual);
    }

    @Override
    public List<FacturaManual> getAllFacturaManual() {
        return facturaManualDAO.getAllFacturaManual();
    }

}
