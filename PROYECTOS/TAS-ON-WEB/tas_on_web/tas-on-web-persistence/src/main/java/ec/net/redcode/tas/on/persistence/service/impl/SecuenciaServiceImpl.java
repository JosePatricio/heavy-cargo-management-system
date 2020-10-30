package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.SecuenciaDAO;
import ec.net.redcode.tas.on.persistence.entities.Secuencia;
import ec.net.redcode.tas.on.persistence.service.SecuenciaService;
import lombok.Setter;

public class SecuenciaServiceImpl implements SecuenciaService {

    @Setter SecuenciaDAO secuenciaDAO;

    @Override
    public void create(Secuencia secuencia) {
        secuenciaDAO.create(secuencia);
    }

    @Override
    public Secuencia read(int idSecuencia) {
        return secuenciaDAO.read(idSecuencia);
    }

    @Override
    public void update(Secuencia secuencia) {
        secuenciaDAO.update(secuencia);
    }

    @Override
    public Secuencia getSecuenciaByCliente(String idCliente) {
        return secuenciaDAO.getSecuenciaByCliente(idCliente);
    }

    @Override
    public Secuencia getSecuenciaByNemonico(String nemonico) {
        return secuenciaDAO.getSecuenciaByNemonico(nemonico);
    }

}
