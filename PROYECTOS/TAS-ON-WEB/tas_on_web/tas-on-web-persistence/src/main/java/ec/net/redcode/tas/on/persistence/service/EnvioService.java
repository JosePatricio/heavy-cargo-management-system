package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Envio;

import java.util.Date;
import java.util.List;

public interface EnvioService {
    void create(Envio envio);
    void create(List<Envio> envios);
    Envio read(int envioId);
    void update(Envio envio);
    List<Envio> getBy(List<Integer> estados, String razonSocial, String ruc, String nroDocumento, Date fechaRecoleccionDesde, Date fechaRecoleccionHasta, Integer conductorId);
}
