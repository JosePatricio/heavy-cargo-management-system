package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.CategoriaProducto;

import java.util.List;

public interface CategoriaProductoDAO extends GenericDAO<CategoriaProducto, Integer> {

    List<CategoriaProducto> getCategoriasByCliente(String empresaRuc);

}
