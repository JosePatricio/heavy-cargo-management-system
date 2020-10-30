package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Producto;

import java.util.List;

public interface ProductoDAO extends GenericDAO<Producto, Integer> {

    List<Producto> getProductos(String rucEmpresa);

}
