package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.ProductoDAO;
import ec.net.redcode.tas.on.persistence.entities.Producto;

import javax.persistence.Query;
import java.util.List;

public class ProductoDAOImpl extends GenericDAOImpl<Producto, Integer> implements ProductoDAO {

    public ProductoDAOImpl(){
        super(Producto.class);
    }

    @Override
    public List<Producto> getProductos(String rucEmpresa){
        Query query = em.createQuery("select o from Producto o where o.usuarioByProductoUsuarioCrea.usuarioRuc = :rucEmpresa");
        query.setParameter("rucEmpresa", rucEmpresa);
        return query.getResultList();
    }

}
