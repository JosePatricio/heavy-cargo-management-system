package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.FacturaManual;

import java.util.List;

public interface FacturaManualDAO extends GenericDAO<FacturaManual, String> {

    List<FacturaManual> getAllFacturaManual();
    FacturaManual read(String claveAcceso);

}
