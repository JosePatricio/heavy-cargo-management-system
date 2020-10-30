package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.EbillingDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.EbillingDetalle;
import ec.net.redcode.tas.on.persistence.service.EbillingDetalleService;
import lombok.Setter;

import java.util.List;

public class EbillingDetalleServiceImpl implements EbillingDetalleService {

    @Setter private EbillingDetalleDAO ebillingDetalleDAO;

    @Override
    public void create(EbillingDetalle ebillingDetalle) {
        ebillingDetalleDAO.create(ebillingDetalle);
    }

    @Override
    public EbillingDetalle read(int ebillingDetalleId) {
        return ebillingDetalleDAO.read(ebillingDetalleId);
    }

    @Override
    public List<EbillingDetalle> readByEbillingId(int ebillingId){
        return ebillingDetalleDAO.readByEbillingId(ebillingId);
    }

    @Override
    public void update(EbillingDetalle ebillingDetalle) {
        ebillingDetalleDAO.update(ebillingDetalle);
    }

    @Override
    public void delete(EbillingDetalle ebillingDetalle) {
        ebillingDetalleDAO.delete(ebillingDetalle);
    }

}
