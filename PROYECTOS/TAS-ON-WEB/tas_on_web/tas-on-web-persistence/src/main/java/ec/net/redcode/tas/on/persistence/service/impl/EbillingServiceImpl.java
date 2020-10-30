package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.EbillingDAO;
import ec.net.redcode.tas.on.persistence.entities.Ebilling;
import ec.net.redcode.tas.on.persistence.service.EbillingService;
import lombok.Setter;

import java.util.List;

public class EbillingServiceImpl implements EbillingService {

    @Setter private EbillingDAO ebillingDAO;

    @Override
    public void create(Ebilling ebilling) {
        ebillingDAO.create(ebilling);
    }

    @Override
    public Ebilling read(Integer ebillingId) {
        return ebillingDAO.read(ebillingId);
    }

    @Override
    public Ebilling readByClaveAcceso(String claveAcceso){
        return ebillingDAO.readByClaveAcceso(claveAcceso);
    }

    @Override
    public void update(Ebilling ebilling) {
        ebillingDAO.update(ebilling);
    }

    @Override
    public void delete(Ebilling ebilling) {
        ebillingDAO.delete(ebilling);
    }

    @Override
    public List<Ebilling> readByUserEbillingId(String userEbillingId){
        return ebillingDAO.readByUserEbillingId(userEbillingId);
    }

    @Override
    public List<Ebilling> readByUserId(String userId){
        return ebillingDAO.readByUserId(userId);
    }


}
