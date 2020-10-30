package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Adquiriente;

import java.util.List;

public interface AdquirienteDAO extends GenericDAO<Adquiriente, String> {

    List<Adquiriente> getAllAdquiriente();

}
