package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Catalogo;

import java.util.List;

public interface CatalogoService {

    void create(Catalogo catalogo);
    Catalogo read(int idCatalogo);
    void update(Catalogo catalogo);
    void delete(Catalogo catalogo);
    List<Catalogo> getAllCatalogos();

}
