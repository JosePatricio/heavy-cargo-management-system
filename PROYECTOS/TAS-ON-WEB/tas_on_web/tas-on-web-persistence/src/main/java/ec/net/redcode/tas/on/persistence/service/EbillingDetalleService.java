package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.EbillingDetalle;

import java.util.List;

public interface EbillingDetalleService {

    EbillingDetalle read(int ebillingDetalleId);
    List<EbillingDetalle> readByEbillingId(int ebillingId);
    void create(EbillingDetalle ebillingDetalle);
    void update(EbillingDetalle ebillingDetalle);
    void delete(EbillingDetalle ebillingDetalle);

}
