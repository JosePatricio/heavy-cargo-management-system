package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Visita;

import java.util.Date;
import java.util.List;

public interface VisitaDAO extends GenericDAO<Visita, Integer> {

    List<Visita> getVisitasBy(String vendedorId, Date fechaVisita);
    List<Visita> getVisitasEmpresaBy(String rucEmpresa, Date fechaVisita);

}
