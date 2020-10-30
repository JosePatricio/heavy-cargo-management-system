package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.NotaCredito;

import java.util.List;

public interface NotaCreditoDAO extends GenericDAO<NotaCredito, Integer> {

    List<NotaCredito> getNCPendientesEmitir();
    NotaCredito readByNotaCreditoFacturaId(String facturaId);

}
