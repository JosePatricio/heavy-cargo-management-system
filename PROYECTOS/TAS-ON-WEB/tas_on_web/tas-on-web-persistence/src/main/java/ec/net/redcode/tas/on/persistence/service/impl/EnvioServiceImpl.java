package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.EnvioDAO;
import ec.net.redcode.tas.on.persistence.entities.Envio;
import ec.net.redcode.tas.on.persistence.service.EnvioService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class EnvioServiceImpl implements EnvioService {

    @Setter private EnvioDAO envioDAO ;

    @Override
    public void create(Envio envio) {
        envioDAO.create(envio);
    }

    @Override
    public void create(List<Envio> envios) {
        envios.forEach(envioDAO::create);
    }

    @Override
    public Envio read(int envioId) {
        return envioDAO.read(envioId);
    }

    @Override
    public void update(Envio envio) {
        envioDAO.update(envio);
    }

    @Override
    public List<Envio> getBy(List<Integer> estados, String razonSocial, String ruc, String nroDocumento, Date fechaRecoleccionDesde, Date fechaRecoleccionHasta, Integer conductorId){
        return envioDAO.getBy(estados, razonSocial, ruc, nroDocumento, fechaRecoleccionDesde, fechaRecoleccionHasta, conductorId);
    }

}
