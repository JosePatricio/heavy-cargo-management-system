package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.CategoriaProductoDAO;
import ec.net.redcode.tas.on.persistence.entities.CategoriaProducto;
import ec.net.redcode.tas.on.persistence.service.CategoriaProductoService;
import lombok.Setter;

import java.util.List;

public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    @Setter private CategoriaProductoDAO categoriaProductoDAO;

    @Override
    public void create(CategoriaProducto categoriaProducto) {
        categoriaProductoDAO.create(categoriaProducto);
    }

    @Override
    public CategoriaProducto read(int categoriaProductoId) {
        return categoriaProductoDAO.read(categoriaProductoId);
    }

    @Override
    public void update(CategoriaProducto categoriaProducto) {
        categoriaProductoDAO.update(categoriaProducto);
    }

    @Override
    public List<CategoriaProducto> getCategoriasByCliente(String empresaRuc){
        return categoriaProductoDAO.getCategoriasByCliente(empresaRuc);
    }
}
