package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.CatalogoDAO;
import ec.net.redcode.tas.on.persistence.entities.Catalogo;
import ec.net.redcode.tas.on.persistence.service.CatalogoService;
import lombok.Setter;

import java.util.List;

public class CatalogoServiceImpl implements CatalogoService{

    @Setter private CatalogoDAO catalogoDAO;

    @Override
    public void create(Catalogo catalogo) {
        catalogoDAO.create(catalogo);
    }

    @Override
    public Catalogo read(int idCatalogo) {
        return catalogoDAO.read(idCatalogo);
    }

    @Override
    public void update(Catalogo catalogo) {
        catalogoDAO.update(catalogo);
    }

    @Override
    public void delete(Catalogo catalogo) {
        catalogoDAO.delete(catalogo);
    }

    @Override
    public List<Catalogo> getAllCatalogos() {
        return catalogoDAO.getAllCatalogo();
    }
}
