package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Visita;

import java.util.Date;
import java.util.List;

public interface VisitaService {

    void create(Visita visita);
    Visita read(int visitaId);
    void update(Visita visita);
    List<Visita> getVisitasBy(String vendedorId, Date fechaVisita);
    List<Visita> getVisitasEmpresaBy(String rucEmpresa, Date fechaVisita);

}
