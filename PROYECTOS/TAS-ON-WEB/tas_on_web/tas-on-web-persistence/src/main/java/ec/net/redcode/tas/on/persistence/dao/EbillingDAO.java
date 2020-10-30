package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Ebilling;

import java.util.List;

public interface EbillingDAO extends GenericDAO<Ebilling, Integer> {

    List<Ebilling> readByUserEbillingId(String userEbillingId);
    List<Ebilling> readByUserId(String userEbillingId);
    Ebilling readEbilling(String ebillingNumero, String userEbillingId);
    Ebilling readByClaveAcceso(String claveAcceso);

}
