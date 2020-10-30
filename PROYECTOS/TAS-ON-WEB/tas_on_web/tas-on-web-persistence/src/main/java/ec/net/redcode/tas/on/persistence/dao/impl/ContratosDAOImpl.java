package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.ContratoDAO;
import ec.net.redcode.tas.on.persistence.entities.Contrato;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

public class ContratosDAOImpl extends  GenericDAOImpl <Contrato, String> implements ContratoDAO {
    public ContratosDAOImpl(){super(Contrato.class);
    }

    @Override
    public List<Contrato> getContratoByContratoDocumentoCliente(String contratoDocumentoCliente, Integer contratoEstado) {
        Query query = em.createNamedQuery("Contrato.ContratoByContratoDocumentoCliente");
        query.setParameter("contratoDocumentoCliente", contratoDocumentoCliente);
        query.setParameter("contratoEstado", contratoEstado);
        List<Contrato> contratos = query.getResultList() ;
        return contratos;

    }

    @Override
    public List<Contrato> getContratoByContratoDocumentoConductor(String contratoDocumentoConductor, Integer contratoEstado) {
        Query query = em.createNamedQuery("Contrato.ContratoByContratoDocumentoConductor");
        query.setParameter("contratoDocumentoConductor", contratoDocumentoConductor);
        query.setParameter("contratoEstado", contratoEstado);
        List<Contrato> contratos = query.getResultList() ;
        return contratos;
    }

    @Override
    public List<Contrato> getContratoByContratoIdSolicitud(Integer contratoIdSolicitud, Integer contratoEstado) {
        Query query = em.createNamedQuery("Contrato.ContratoByContratoIdSolicitud");
        query.setParameter("contratoIdSolicitud", contratoIdSolicitud);
        query.setParameter("contratoEstado", contratoEstado);
        List<Contrato> contratos = query.getResultList() ;
        return contratos;
    }

    @Override
    public List<Contrato> getContratoByContratoFechaContrato(Timestamp fechaInicio, Timestamp fechaFin, Integer contratoEstado) {
        Query query = em.createNamedQuery("Contrato.ContratoByContratoFechaContrato");
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        query.setParameter("contratoEstado", contratoEstado);
        List<Contrato> contratos = query.getResultList() ;
        return contratos;
    }
}
