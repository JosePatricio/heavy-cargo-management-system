package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.ClienteDAO;
import ec.net.redcode.tas.on.persistence.entities.Cliente;

import javax.persistence.Query;
import java.util.List;

public class ClienteDAOImpl extends GenericDAOImpl<Cliente, String> implements ClienteDAO {

    public ClienteDAOImpl(){
        super(Cliente.class);
    }

    @Override
    public List<Cliente> getAllCliente() {
        Query query = em.createNamedQuery("Cliente.ClienteGetAllCliente");
        List<Cliente> clientes = query.getResultList();
        return clientes;
    }

    @Override
    public List<Cliente> getClienteByTipo(int tipoCliente) {
        Query query = em.createNamedQuery("Cliente.ClienteByTipo");
        query.setParameter("clienteTipoCliente", tipoCliente);
        List<Cliente> clientes = query.getResultList();
        return clientes;    }
}
