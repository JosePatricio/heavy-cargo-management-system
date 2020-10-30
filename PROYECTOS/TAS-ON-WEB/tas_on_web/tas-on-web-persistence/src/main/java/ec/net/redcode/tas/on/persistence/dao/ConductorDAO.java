package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Conductor;

import java.util.List;

public interface ConductorDAO extends GenericDAO<Conductor, Integer> {

    List<Conductor> getConductorsByUser(String user);
    List<Conductor> getConductorsByUserAndEstado(String user, int estado);
    List<Conductor> getConductoresAcreditados(String rucClienteOferta, String rucClienteSolicitud);
    Conductor readConductorAcreditado(int idConductor, String rucClienteSolicitud);
}
