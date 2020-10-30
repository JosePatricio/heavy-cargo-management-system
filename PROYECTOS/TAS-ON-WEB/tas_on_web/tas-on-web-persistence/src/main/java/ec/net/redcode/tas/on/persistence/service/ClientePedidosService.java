package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.ClientePedidos;

import java.util.List;

public interface ClientePedidosService {

    void create(ClientePedidos clientePedidos);
    ClientePedidos read(int clientePedidosId);
    void update(ClientePedidos clientePedidos);
    List<ClientePedidos> getClientes(String clienteTasOnRuc);
    List<ClientePedidos> getClientesBy(String razonSocial, String clientePedidosRuc, String vendedorId, Integer diaVisita, String clienteTasOnRuc);

}
