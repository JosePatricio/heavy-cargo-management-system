package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Conductor;

import java.util.List;

public interface ConductorService {

    void create(Conductor conductor);
    Conductor read(int idConductor);
    void update(Conductor conductor);
    void delete(Conductor conductor);
    List<Conductor> getConductoresByUser(String user);
    List<Conductor> getConductoresByUserAndEstado(String user, int estado);
    List<Conductor> getConductoresAcreditados(String rucClienteOferta, String rucClienteSolicitud);
    Conductor readConductorAcreditado(int idConductor, String rucClienteSolicitud);

}
