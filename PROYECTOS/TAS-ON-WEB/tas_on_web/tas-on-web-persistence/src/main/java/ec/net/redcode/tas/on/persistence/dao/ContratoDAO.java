package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Contrato;
import java.util.List;
import java.sql.Timestamp;

public interface ContratoDAO extends  GenericDAO<Contrato, String> {

     List<Contrato>  getContratoByContratoDocumentoCliente(String contratoDocumentoCliente, Integer contratoEstado);
     List<Contrato>  getContratoByContratoDocumentoConductor(String contratoDocumentoConductor, Integer contratoEstado);
     List<Contrato>  getContratoByContratoIdSolicitud(Integer contratoIdSolicitud,Integer contratoEstado);
     List<Contrato>  getContratoByContratoFechaContrato(Timestamp fechaInicio,Timestamp fechaFin ,Integer contratoEstado);


}

