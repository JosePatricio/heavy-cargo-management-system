package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.SolicitudEnvioAuditDAO;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvioAudit;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class SolicitudEnvioAuditDAOImpl extends GenericDAOImpl<SolicitudEnvioAudit, Integer> implements SolicitudEnvioAuditDAO {

    public SolicitudEnvioAuditDAOImpl(){
        super(SolicitudEnvioAudit.class);
    }

    @Override
    public List<SolicitudEnvioAudit> getSolicitudEnvioAuditBySolictudId(String solicitud) {
        Query query = em.createNamedQuery("SolicitudEnvioAudit.SolicitudEnvioAuditBySolicitudId");
        query.setParameter("solicitud", solicitud);
        List<SolicitudEnvioAudit> solicitudEnvioAudits;
        try {
            solicitudEnvioAudits = query.getResultList();
        } catch (Exception e) {
            solicitudEnvioAudits = Collections.emptyList();
        }
        return solicitudEnvioAudits;
    }
}
