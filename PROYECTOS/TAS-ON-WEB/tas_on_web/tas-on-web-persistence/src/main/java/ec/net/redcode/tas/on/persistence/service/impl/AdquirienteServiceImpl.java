package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.AdquirienteDAO;
import ec.net.redcode.tas.on.persistence.entities.Adquiriente;
import ec.net.redcode.tas.on.persistence.service.AdquirienteService;
import lombok.Setter;

import java.util.List;

public class AdquirienteServiceImpl implements AdquirienteService {

    @Setter private AdquirienteDAO adquirienteDAO;

    @Override
    public void create(Adquiriente adquiriente) {
        adquirienteDAO.create(adquiriente);
    }

    @Override
    public Adquiriente read(String numDocumento) {
        return adquirienteDAO.read(numDocumento);
    }

    @Override
    public void update(Adquiriente adquiriente) {
        adquirienteDAO.update(adquiriente);
    }

    @Override
    public void delete(Adquiriente adquiriente) {
        adquirienteDAO.delete(adquiriente);
    }

    @Override
    public List<Adquiriente> getAllAdquiriente() {
        return adquirienteDAO.getAllAdquiriente();
    }

}
