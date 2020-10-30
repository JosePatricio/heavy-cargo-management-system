package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaProveedorDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class FacturaProveedorDAOImpl extends GenericDAOImpl<FacturaProveedor, Integer> implements FacturaProveedorDAO {

    public FacturaProveedorDAOImpl() {
        super(FacturaProveedor.class);
    }

    @Override
    public List<FacturaProveedor> getByEstado(int estado) {
        Query query = em.createNamedQuery("FacturaProveedorByFacturaProveedorEstado");
        query.setParameter("estado", estado);
        List<FacturaProveedor> facturaProveedors;
        try {
            facturaProveedors = query.getResultList();
        } catch (Exception e) {
            facturaProveedors = Collections.emptyList();
        }
        return facturaProveedors;
    }

    @Override
    public FacturaProveedor getByNumeroFactura(String numeroFactura) {
        Query query = em.createNamedQuery("FacturaProveedorByfacturaProveedorNumero");
        query.setParameter("numero", numeroFactura);
        FacturaProveedor facturaProveedor;
        try {
            facturaProveedor = (FacturaProveedor) query.getSingleResult();
        } catch (Exception e) {
            facturaProveedor = new FacturaProveedor();
        }
        return facturaProveedor;
    }

    @Override
    public FacturaProveedor getByNumeroFacturaAndRuc(String numeroFactura, String ruc) {
        Query query = em.createNamedQuery("FacturaProveedorByfacturaProveedorNumeroAndFacturaProveedorRucCliente");
        query.setParameter("numero", numeroFactura);
        query.setParameter("ruc", ruc);
        FacturaProveedor facturaProveedor;
        try {
            facturaProveedor = (FacturaProveedor) query.getSingleResult();
        } catch (Exception e) {
            facturaProveedor = new FacturaProveedor();
        }
        return facturaProveedor;
    }
}
