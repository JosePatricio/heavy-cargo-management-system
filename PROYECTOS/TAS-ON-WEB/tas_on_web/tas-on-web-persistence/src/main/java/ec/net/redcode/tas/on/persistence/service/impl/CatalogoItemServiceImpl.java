package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.CatalogoItemDAO;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import lombok.Setter;

import java.util.List;

public class CatalogoItemServiceImpl implements CatalogoItemService {

    @Setter private CatalogoItemDAO catalogoItemDAO;

    @Override
    public void create(CatalogoItem catalogoItem) {
        catalogoItemDAO.create(catalogoItem);
    }

    @Override
    public CatalogoItem read(int idCatalogoItem) {
        return catalogoItemDAO.read(idCatalogoItem);
    }

    @Override
    public void update(CatalogoItem catalogoItem) {
        catalogoItemDAO.update(catalogoItem);
    }

    @Override
    public void delete(CatalogoItem catalogoItem) {
        catalogoItemDAO.delete(catalogoItem);
    }

    @Override
    public List<CatalogoItem> getCatalogoItemByCatalogo(int idCatalogoItem) {
        return catalogoItemDAO.getCatalogoItemByCatalogo(idCatalogoItem);
    }

}
