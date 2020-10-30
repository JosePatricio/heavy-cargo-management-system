package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.FacturaRetencionDTO;
import ec.net.redcode.tas.on.persistence.entities.FacturaRetencion;

import java.util.Date;
import java.util.List;

public interface FacturaRetencionDAO extends GenericDAO<FacturaRetencion, String> {

    List<FacturaRetencionDTO> getRetencionesBy(String razonSocialCliente, String numeroFacturaCliente, String prefactura,
                                               Date fechaAutorizacionDesde, Date fechaAutorizacionHasta);
}
