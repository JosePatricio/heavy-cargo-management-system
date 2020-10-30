package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.ContratoDAO;
import ec.net.redcode.tas.on.persistence.entities.Contrato;
import ec.net.redcode.tas.on.persistence.service.ContratoService;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

public class ContratoServiceImpl implements ContratoService {

    @Setter private ContratoDAO contratoDAO;

    @Override
    public void createContrato(Contrato contrato) {
        contratoDAO.create(contrato);
    }

    @Override
    public Contrato readContrato(int contratoId) {
    return contratoDAO.read(contratoId);
    }

    @Override
    public void updateContrato(Contrato contrato) {
         contratoDAO.update(contrato);
    }

    @Override
    public void deleteContrato(Contrato contrato) {
        contratoDAO.delete(contrato);
    }

    @Override
    public List<Contrato> getContratoByContratoDocumentoCliente(String contratoDocumentoCliente, Integer contratoEstado) {
        return contratoDAO.getContratoByContratoDocumentoCliente(contratoDocumentoCliente,contratoEstado);
    }

    @Override
    public List<Contrato> getContratoByContratoDocumentoConductor(String contratoDocumentoConductor, Integer contratoEstado) {
        return contratoDAO.getContratoByContratoDocumentoConductor(contratoDocumentoConductor,contratoEstado);
    }

    @Override
    public List<Contrato> getContratoByContratoIdSolicitud(Integer contratoIdSolicitud, Integer contratoEstado) {
        return contratoDAO.getContratoByContratoIdSolicitud(contratoIdSolicitud,contratoEstado);
    }

    @Override
    public List<Contrato> getContratoByContratoFechaContrato(Timestamp fechaInicio, Timestamp fechaFin, Integer contratoEstado) {
        return contratoDAO.getContratoByContratoFechaContrato(fechaInicio,fechaFin,contratoEstado);
    }

}
