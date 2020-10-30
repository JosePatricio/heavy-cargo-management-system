package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Adquiriente;

import java.util.List;

public interface AdquirienteService {

    Adquiriente read(String numDocumento);
    void create(Adquiriente adquiriente);
    void update(Adquiriente adquiriente);
    void delete(Adquiriente adquiriente);
    List<Adquiriente> getAllAdquiriente();

}
