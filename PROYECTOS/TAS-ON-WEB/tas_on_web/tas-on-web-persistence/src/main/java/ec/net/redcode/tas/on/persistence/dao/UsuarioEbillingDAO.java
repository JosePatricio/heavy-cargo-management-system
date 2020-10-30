package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;


public interface UsuarioEbillingDAO extends GenericDAO<UsuarioEbilling, String> {

    UsuarioEbilling readByUserId(String userID);

}
