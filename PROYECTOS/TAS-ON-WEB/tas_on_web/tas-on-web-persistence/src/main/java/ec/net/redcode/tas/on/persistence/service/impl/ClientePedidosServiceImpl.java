package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.ClientePedidosDAO;
import ec.net.redcode.tas.on.persistence.entities.ClientePedidos;
import ec.net.redcode.tas.on.persistence.service.ClientePedidosService;
import lombok.Setter;

import java.util.List;

public class ClientePedidosServiceImpl implements ClientePedidosService {

    @Setter private ClientePedidosDAO clientePedidosDAO;

    @Override
    public void create(ClientePedidos clientePedidos) {
        clientePedidosDAO.create(clientePedidos);
    }

    @Override
    public ClientePedidos read(int clientePedidosId) {
        return clientePedidosDAO.read(clientePedidosId);
    }

    @Override
    public void update(ClientePedidos clientePedidos) {
        clientePedidosDAO.update(clientePedidos);
    }

    @Override
    public List<ClientePedidos> getClientes(String clienteTasOnRuc){
        return clientePedidosDAO.getClientes(clienteTasOnRuc);
    }

    @Override
    public List<ClientePedidos> getClientesBy(String razonSocial, String clientePedidosRuc, String vendedorId, Integer diaVisita, String clienteTasOnRuc){
        return clientePedidosDAO.getClientesBy(razonSocial, clientePedidosRuc, vendedorId, diaVisita, clienteTasOnRuc);
    }

}
