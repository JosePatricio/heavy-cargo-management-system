package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.VisitaDAO;
import ec.net.redcode.tas.on.persistence.entities.Visita;
import ec.net.redcode.tas.on.persistence.service.VisitaService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class VisitaServiceImpl implements VisitaService {

    @Setter private VisitaDAO visitaDAO;

    @Override
    public void create(Visita visita) {
        visitaDAO.create(visita);
    }

    @Override
    public Visita read(int visitaId) {
        return visitaDAO.read(visitaId);
    }

    @Override
    public void update(Visita visita) {
        visitaDAO.update(visita);
    }

    @Override
    public List<Visita> getVisitasBy(String vendedorId, Date fechaVisita){
        return visitaDAO.getVisitasBy(vendedorId, fechaVisita);
    }

    @Override
    public List<Visita> getVisitasEmpresaBy(String rucEmpresa, Date fechaVisita){
        return visitaDAO.getVisitasEmpresaBy(rucEmpresa, fechaVisita);
    }

}
