package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.FacturaRetencionDTO;
import ec.net.redcode.tas.on.persistence.dao.FacturaRetencionDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaRetencion;
import ec.net.redcode.tas.on.persistence.service.FacturaRetencionService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class FacturaRetencionServiceImpl implements FacturaRetencionService {

    @Setter private FacturaRetencionDAO facturaRetencionDAO;

    @Override
    public FacturaRetencion read(String facturaRetencionId){
        return facturaRetencionDAO.read(facturaRetencionId);
    }

    @Override
    public void create(FacturaRetencion facturaRetencion) {
        facturaRetencionDAO.create(facturaRetencion);
    }

    @Override
    public List<FacturaRetencionDTO> getRetencionesBy(String razonSocialCliente, String numeroFacturaCliente,
                                                      String prefactura, Date fechaAutorizacionDesde, Date fechaAutorizacionHasta){
        return facturaRetencionDAO.getRetencionesBy(razonSocialCliente, numeroFacturaCliente, prefactura,
                fechaAutorizacionDesde, fechaAutorizacionHasta);
    }

}
