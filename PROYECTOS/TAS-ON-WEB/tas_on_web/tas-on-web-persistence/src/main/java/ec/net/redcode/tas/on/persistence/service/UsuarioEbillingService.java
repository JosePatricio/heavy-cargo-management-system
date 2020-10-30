package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;

public interface UsuarioEbillingService {

    UsuarioEbilling read(String numDocumento);
    UsuarioEbilling readByUserID(String numDocumento);
    void create(UsuarioEbilling usuarioEbilling);
    void update(UsuarioEbilling usuarioEbilling);
    void delete(UsuarioEbilling usuarioEbilling);

}
