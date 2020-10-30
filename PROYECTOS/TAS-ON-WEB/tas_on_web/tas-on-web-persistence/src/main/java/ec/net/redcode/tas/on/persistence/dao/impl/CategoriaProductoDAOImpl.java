package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.CategoriaProductoDAO;
import ec.net.redcode.tas.on.persistence.entities.CategoriaProducto;

import javax.persistence.Query;
import java.util.List;

public class CategoriaProductoDAOImpl extends GenericDAOImpl<CategoriaProducto, Integer> implements CategoriaProductoDAO {

    public CategoriaProductoDAOImpl(){
        super(CategoriaProducto.class);
    }

    @Override
    public List<CategoriaProducto> getCategoriasByCliente(String empresaRuc){
        Query query = em.createQuery("select o from CategoriaProducto o where o.usuarioByCategoriaProductoUsuarioCrea.usuarioRuc = :empresRuc");
        query.setParameter("empresRuc", empresaRuc);
        return query.getResultList();
    }

}
