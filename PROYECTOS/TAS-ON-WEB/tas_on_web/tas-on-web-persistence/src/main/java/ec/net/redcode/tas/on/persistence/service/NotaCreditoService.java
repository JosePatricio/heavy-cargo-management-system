package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.NotaCredito;

import java.util.List;

public interface NotaCreditoService {

    void create(NotaCredito notaCredito);
    NotaCredito read(int notaCreditoId);
    NotaCredito readByNotaCreditoFacturaId(String facturaId);
    void update(NotaCredito notaCredito);
    List<NotaCredito> getNCPendientesEmitir();

}
