package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.SolicitudEnvioAuditDAO;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvioAudit;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioAuditService;
import lombok.Setter;

import java.util.List;

public class SolicitudEnvioAuditServiceImpl implements SolicitudEnvioAuditService {

    @Setter private SolicitudEnvioAuditDAO solicitudEnvioAuditDAO;

    @Override
    public List<SolicitudEnvioAudit> getSolicitudEnvioAuditBySolictudId(String solicitud) {
        return solicitudEnvioAuditDAO.getSolicitudEnvioAuditBySolictudId(solicitud);
    }

}
