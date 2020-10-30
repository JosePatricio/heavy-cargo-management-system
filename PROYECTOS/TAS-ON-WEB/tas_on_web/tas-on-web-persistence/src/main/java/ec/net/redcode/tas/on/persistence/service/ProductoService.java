package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Producto;

import java.util.List;

public interface ProductoService {

    void create(Producto producto);
    Producto read(int productoId);
    void update(Producto producto);
    List<Producto> getProductos(String rucEmpresa);

}
