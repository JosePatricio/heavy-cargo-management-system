package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Secuencia;

public interface SecuenciaDAO extends GenericDAO<Secuencia, Integer> {

    Secuencia getSecuenciaByCliente(String idCliente);
    Secuencia getSecuenciaByNemonico(String nemonico);

}
