package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.ConductorDAO;
import ec.net.redcode.tas.on.persistence.entities.Conductor;
import ec.net.redcode.tas.on.persistence.service.ConductorService;
import lombok.Setter;

import java.util.List;

public class ConductorServiceImpl implements ConductorService {

    @Setter private ConductorDAO conductorDAO;

    @Override
    public void create(Conductor conductor) {
        conductorDAO.create(conductor);
    }

    @Override
    public Conductor read(int idConductor) {
        return conductorDAO.read(idConductor);
    }

    @Override
    public void update(Conductor conductor) {
        conductorDAO.update(conductor);
    }

    @Override
    public void delete(Conductor conductor) {
        conductorDAO.delete(conductor);
    }

    @Override
    public List<Conductor> getConductoresByUser(String user) {
        return conductorDAO.getConductorsByUser(user);
    }

    @Override
    public List<Conductor> getConductoresByUserAndEstado(String user, int estado) {
        return conductorDAO.getConductorsByUserAndEstado(user, estado);
    }

    @Override
    public List<Conductor> getConductoresAcreditados(String rucClienteOferta, String rucClienteSolicitud){
        return conductorDAO.getConductoresAcreditados(rucClienteOferta, rucClienteSolicitud);
    }

    @Override
    public Conductor readConductorAcreditado(int idConductor, String rucClienteSolicitud){
        return conductorDAO.readConductorAcreditado(idConductor, rucClienteSolicitud);
    }

}
