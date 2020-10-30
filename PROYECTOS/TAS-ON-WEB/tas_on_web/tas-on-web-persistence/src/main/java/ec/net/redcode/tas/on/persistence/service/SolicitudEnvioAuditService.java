package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvioAudit;

import java.util.List;

public interface SolicitudEnvioAuditService {

    List<SolicitudEnvioAudit> getSolicitudEnvioAuditBySolictudId(String solicitud);

}
