package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Secuencia;

public interface SecuenciaService {

    void create(Secuencia secuencia);
    Secuencia read(int idSecuencia);
    void update(Secuencia secuencia);
    Secuencia getSecuenciaByCliente(String idCliente);
    Secuencia getSecuenciaByNemonico(String nemonico);

}
