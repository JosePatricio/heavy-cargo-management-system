package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.CatalogoItemDAO;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;

import javax.persistence.Query;
import java.util.List;

public class CatalogoItemDAOImpl extends GenericDAOImpl<CatalogoItem, Integer> implements CatalogoItemDAO {

    public CatalogoItemDAOImpl(){
        super(CatalogoItem.class);
    }

    @Override
    public List<CatalogoItem> getCatalogoItemByCatalogo(int idCatalogoItem) {
        Query query = em.createNamedQuery("CatalogoItem.CatalogoItemByCatalogo");
        query.setParameter("idCatalogo", idCatalogoItem);
        List<CatalogoItem> catalogoItems = query.getResultList();
        return catalogoItems;
    }
}
