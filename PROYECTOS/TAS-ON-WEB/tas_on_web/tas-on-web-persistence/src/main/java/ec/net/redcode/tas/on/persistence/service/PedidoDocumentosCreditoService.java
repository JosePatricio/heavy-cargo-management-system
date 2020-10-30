package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.PedidoDocumentosCredito;

public interface PedidoDocumentosCreditoService {

    void create(PedidoDocumentosCredito pedidoDocumentosCredito);
    PedidoDocumentosCredito read(int pedidoDocumentosCreditoId);
    void update(PedidoDocumentosCredito pedidoDocumentosCredito);

}
