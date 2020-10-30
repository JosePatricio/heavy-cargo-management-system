package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Contrato;

import java.sql.Timestamp;
import java.util.List;

public interface ContratoService  {
    void createContrato(Contrato contrato);

    Contrato readContrato(int contratoId);

    void updateContrato(Contrato contrato);

    void deleteContrato(Contrato contrato);

    List<Contrato> getContratoByContratoDocumentoCliente(String contratoDocumentoCliente, Integer contratoEstado);
    List<Contrato>  getContratoByContratoDocumentoConductor(String contratoDocumentoConductor, Integer contratoEstado);
    List<Contrato>  getContratoByContratoIdSolicitud(Integer contratoIdSolicitud,Integer contratoEstado);
    List<Contrato>  getContratoByContratoFechaContrato(Timestamp fechaInicio, Timestamp fechaFin , Integer contratoEstado);

}
