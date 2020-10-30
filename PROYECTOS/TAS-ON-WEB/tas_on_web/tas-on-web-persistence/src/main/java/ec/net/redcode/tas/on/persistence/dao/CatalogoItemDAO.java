package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;

import java.util.List;

public interface CatalogoItemDAO extends GenericDAO<CatalogoItem, Integer> {

    List<CatalogoItem> getCatalogoItemByCatalogo(int idCatalogoItem);

}
