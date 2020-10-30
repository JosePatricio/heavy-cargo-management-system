package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.PedidoDocumentosCreditoDAO;
import ec.net.redcode.tas.on.persistence.entities.PedidoDocumentosCredito;
import ec.net.redcode.tas.on.persistence.service.PedidoDocumentosCreditoService;
import lombok.Setter;

public class PedidoDocumentosCreditoServiceImpl implements PedidoDocumentosCreditoService {

    @Setter private PedidoDocumentosCreditoDAO pedidoDocumentosCreditoDAO;

    @Override
    public void create(PedidoDocumentosCredito pedidoDocumentosCredito) {
        pedidoDocumentosCreditoDAO.create(pedidoDocumentosCredito);
    }

    @Override
    public PedidoDocumentosCredito read(int pedidoDocumentosCreditoId) {
        return pedidoDocumentosCreditoDAO.read(pedidoDocumentosCreditoId);
    }

    @Override
    public void update(PedidoDocumentosCredito pedidoDocumentosCredito) {
        pedidoDocumentosCreditoDAO.update(pedidoDocumentosCredito);
    }

}
