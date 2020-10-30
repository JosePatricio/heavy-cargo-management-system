package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;

import java.util.List;

public interface CatalogoItemService {

    void create(CatalogoItem catalogoItem);
    CatalogoItem read(int idCatalogoItem);
    void update(CatalogoItem catalogoItem);
    void delete(CatalogoItem catalogoItem);
    List<CatalogoItem> getCatalogoItemByCatalogo(int idCatalogoItem);

}
