package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.ClientePedidos;

import java.util.List;

public interface ClientePedidosDAO extends GenericDAO<ClientePedidos, Integer> {

    List<ClientePedidos> getClientes(String clienteTasOnRuc);
    List<ClientePedidos> getClientesBy(String razonSocial, String clientePedidosRuc, String vendedorId, Integer diaVisita, String clienteTasOnRuc);

}
