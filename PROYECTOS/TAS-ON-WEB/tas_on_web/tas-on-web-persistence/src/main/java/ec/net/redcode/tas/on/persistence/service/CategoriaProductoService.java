package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.CategoriaProducto;

import java.util.List;

public interface CategoriaProductoService {

    void create(CategoriaProducto categoriaProducto);
    CategoriaProducto read(int categoriaProductoId);
    void update(CategoriaProducto categoriaProducto);
    List<CategoriaProducto> getCategoriasByCliente(String empresaRuc);

}
