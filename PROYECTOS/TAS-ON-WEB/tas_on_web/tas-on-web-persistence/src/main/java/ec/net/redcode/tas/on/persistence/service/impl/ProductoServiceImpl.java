package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.ProductoDAO;
import ec.net.redcode.tas.on.persistence.entities.Producto;
import ec.net.redcode.tas.on.persistence.service.ProductoService;
import lombok.Setter;

import java.util.List;

public class ProductoServiceImpl implements ProductoService {

    @Setter private ProductoDAO productoDAO;

    @Override
    public void create(Producto producto) {
        productoDAO.create(producto);
    }

    @Override
    public Producto read(int productoId) {
        return productoDAO.read(productoId);
    }

    @Override
    public void update(Producto producto) {
        productoDAO.update(producto);
    }

    @Override
    public List<Producto> getProductos(String rucEmpresa){
        return productoDAO.getProductos(rucEmpresa);
    }

}
