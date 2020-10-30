package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Envio;

import java.util.Date;
import java.util.List;

public interface EnvioDAO extends  GenericDAO<Envio, Integer> {

    List<Envio> getBy(List<Integer> estados, String razonSocial, String ruc, String nroDocumento, Date fechaRecoleccionDesde, Date fechaRecoleccionHasta, Integer conductorId);

}
