package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.NotaCreditoDAO;
import ec.net.redcode.tas.on.persistence.entities.NotaCredito;
import ec.net.redcode.tas.on.persistence.service.NotaCreditoService;
import lombok.Setter;

import java.util.List;

    public class NotaCreditoServiceImpl implements NotaCreditoService {

    @Setter private NotaCreditoDAO notaCreditoDAO;

    @Override
    public void create(NotaCredito notaCredito) {
        notaCreditoDAO.create(notaCredito);
    }

    @Override
    public NotaCredito read(int notaCreditoId) {
        return notaCreditoDAO.read(notaCreditoId);
    }

    @Override
    public NotaCredito readByNotaCreditoFacturaId(String facturaId) {
        return notaCreditoDAO.readByNotaCreditoFacturaId(facturaId);
    }

    @Override
    public void update(NotaCredito notaCredito) {
        notaCreditoDAO.update(notaCredito);
    }

    @Override
    public List<NotaCredito> getNCPendientesEmitir(){
        return notaCreditoDAO.getNCPendientesEmitir();
    }

}
