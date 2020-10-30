package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDocumentosCreditoDAO;
import ec.net.redcode.tas.on.persistence.entities.PedidoDocumentosCredito;

public class PedidoDocumentosCreditoDAOImpl extends GenericDAOImpl<PedidoDocumentosCredito, Integer> implements PedidoDocumentosCreditoDAO {

    public PedidoDocumentosCreditoDAOImpl(){
        super(PedidoDocumentosCredito.class);
    }

}
