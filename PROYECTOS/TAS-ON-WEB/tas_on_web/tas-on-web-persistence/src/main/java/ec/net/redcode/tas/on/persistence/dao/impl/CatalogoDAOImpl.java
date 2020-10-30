
package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.CatalogoDAO;
import ec.net.redcode.tas.on.persistence.entities.Catalogo;

import javax.persistence.Query;
import java.util.List;

public class CatalogoDAOImpl extends GenericDAOImpl <Catalogo, Integer> implements CatalogoDAO{

    public CatalogoDAOImpl(){
        super(Catalogo.class);
    }

    @Override
    public List<Catalogo> getAllCatalogo() {
        Query query = em.createNamedQuery("Catalogo.CatalogoAll");
        List<Catalogo> catalogos = query.getResultList();
        return catalogos;
    }

}