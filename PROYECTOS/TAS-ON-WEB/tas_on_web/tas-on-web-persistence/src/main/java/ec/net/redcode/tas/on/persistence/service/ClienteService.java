package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente readCliente(String ruc);
    void createCliente(Cliente cliente);
    void updateCliente(Cliente cliente);
    void deleteCliente(Cliente cliente);
    List<Cliente> getAllCiente();
    List<Cliente> getCienteByTipo(int tipoCliente);

}
