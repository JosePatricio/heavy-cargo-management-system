package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Catalogo;

import java.util.List;

public interface CatalogoDAO extends  GenericDAO<Catalogo, Integer>  {

    List<Catalogo> getAllCatalogo();

}


