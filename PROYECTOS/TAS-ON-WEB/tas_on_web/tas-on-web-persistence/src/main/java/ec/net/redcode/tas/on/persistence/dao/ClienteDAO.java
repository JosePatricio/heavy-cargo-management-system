package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Cliente;

import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente, String> {

    List<Cliente> getAllCliente();
    List<Cliente> getClienteByTipo(int tipoCliente);

}
