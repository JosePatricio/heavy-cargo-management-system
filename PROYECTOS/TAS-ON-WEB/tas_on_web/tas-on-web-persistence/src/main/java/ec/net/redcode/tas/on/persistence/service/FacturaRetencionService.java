package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.common.dto.FacturaRetencionDTO;
import ec.net.redcode.tas.on.persistence.entities.FacturaRetencion;

import java.util.Date;
import java.util.List;

public interface FacturaRetencionService {

    void create(FacturaRetencion facturaRetencion);
    List<FacturaRetencionDTO> getRetencionesBy(String razonSocialCliente, String numeroFacturaCliente, String prefactura,
                                               Date fechaAutorizacionDesde, Date fechaAutorizacionHasta);
    FacturaRetencion read(String facturaRetencionId);


}
