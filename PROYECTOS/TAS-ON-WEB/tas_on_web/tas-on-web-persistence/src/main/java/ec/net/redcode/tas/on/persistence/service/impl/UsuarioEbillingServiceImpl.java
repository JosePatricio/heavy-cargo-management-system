package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.UsuarioEbillingDAO;
import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;
import ec.net.redcode.tas.on.persistence.service.UsuarioEbillingService;
import lombok.Setter;

public class UsuarioEbillingServiceImpl implements UsuarioEbillingService {

    @Setter private UsuarioEbillingDAO usuarioEbillingDAO;

    @Override
    public void create(UsuarioEbilling usuarioEbilling) {
        usuarioEbillingDAO.create(usuarioEbilling);
    }

    @Override
    public UsuarioEbilling read(String numDocumento) {
        return usuarioEbillingDAO.read(numDocumento);
    }

    @Override
    public UsuarioEbilling readByUserID(String numDocumento){
        return usuarioEbillingDAO.readByUserId(numDocumento);
    }

    @Override
    public void update(UsuarioEbilling usuarioEbilling) {
        usuarioEbillingDAO.update(usuarioEbilling);
    }

    @Override
    public void delete(UsuarioEbilling usuarioEbilling) {
        usuarioEbillingDAO.delete(usuarioEbilling);
    }

}
