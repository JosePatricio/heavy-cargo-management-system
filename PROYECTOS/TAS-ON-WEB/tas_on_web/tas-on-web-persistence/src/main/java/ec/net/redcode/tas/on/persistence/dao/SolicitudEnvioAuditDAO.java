package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvioAudit;

import java.util.List;

public interface SolicitudEnvioAuditDAO extends GenericDAO<SolicitudEnvioAudit, Integer> {

    List<SolicitudEnvioAudit> getSolicitudEnvioAuditBySolictudId(String solicitud);

}
