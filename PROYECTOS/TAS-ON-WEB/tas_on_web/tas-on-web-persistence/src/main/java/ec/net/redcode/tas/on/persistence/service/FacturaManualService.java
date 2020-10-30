package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.FacturaManual;

import java.util.List;

public interface FacturaManualService {

    FacturaManual read(String claveAcceso);
    void create(FacturaManual adquiriente);
    void update(FacturaManual adquiriente);
    void delete(FacturaManual adquiriente);
    List<FacturaManual> getAllFacturaManual();

}
