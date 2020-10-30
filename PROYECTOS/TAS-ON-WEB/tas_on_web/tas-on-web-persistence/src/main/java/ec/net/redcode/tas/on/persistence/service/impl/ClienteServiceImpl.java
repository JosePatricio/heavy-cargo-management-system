package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.ClienteDAO;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
import lombok.Setter;

import java.util.List;

public class ClienteServiceImpl implements ClienteService {

    @Setter private ClienteDAO clienteDAO;

    @Override
    public Cliente readCliente(String ruc) {
        return clienteDAO.read(ruc);
    }

    @Override
    public void createCliente(Cliente cliente) {
        clienteDAO.create(cliente);
    }

    @Override
    public void updateCliente(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    @Override
    public void deleteCliente(Cliente cliente) {
        clienteDAO.delete(cliente);
    }

    @Override
    public List<Cliente> getAllCiente() {
        return clienteDAO.getAllCliente();
    }

    @Override
    public List<Cliente> getCienteByTipo(int tipoCliente) {
        return clienteDAO.getClienteByTipo(tipoCliente);
    }
}
