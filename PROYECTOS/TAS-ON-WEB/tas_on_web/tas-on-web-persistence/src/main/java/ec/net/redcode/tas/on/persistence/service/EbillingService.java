package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Ebilling;

import java.util.List;

public interface EbillingService {

    Ebilling read(Integer ebillingId);
    Ebilling readByClaveAcceso(String claveAcceso);
    void create(Ebilling ebilling);
    void update(Ebilling ebilling);
    void delete(Ebilling ebilling);
    List<Ebilling> readByUserEbillingId(String userEbillingId);
    List<Ebilling> readByUserId(String userId);

}
