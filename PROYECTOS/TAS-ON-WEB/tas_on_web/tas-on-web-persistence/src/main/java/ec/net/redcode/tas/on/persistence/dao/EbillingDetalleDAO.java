package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.EbillingDetalle;

import java.util.List;

public interface EbillingDetalleDAO extends GenericDAO<EbillingDetalle, Integer> {

    List<EbillingDetalle> readByEbillingId(int ebillingId);

}
