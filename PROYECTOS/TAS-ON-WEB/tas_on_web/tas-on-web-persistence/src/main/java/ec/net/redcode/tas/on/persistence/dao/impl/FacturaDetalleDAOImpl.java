package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.FacturaDAO;
import ec.net.redcode.tas.on.persistence.dao.FacturaDetalleDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaDetalle;
import ec.net.redcode.tas.on.persistence.service.FacturaService;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class FacturaDetalleDAOImpl extends GenericDAOImpl<FacturaDetalle, Integer> implements FacturaDetalleDAO {

    public FacturaDetalleDAOImpl(){
        super(FacturaDetalle.class);
    }

    @Override
    public List<FacturaDetalle> getFacturaDetalleByFactura(String idFactura) {
        Query query = em.createNamedQuery("FacturaDetalle.FacturaDetalleByFactura");
        query.setParameter("idFactura", idFactura);
        List<FacturaDetalle> facturaDetalles;
        try {
            facturaDetalles = query.getResultList();
        } catch (Exception e){
            facturaDetalles = Collections.emptyList();;
        }
        return facturaDetalles;
    }

    @Override
    public FacturaDetalle getFacturaDetalleByOfertaId(int idOferta, int tipo) {
        Query query = em.createNamedQuery("FacturaDetalle.FacturaDetalleByOferta");
        query.setParameter("idOferta", idOferta);
        query.setParameter("tipo", tipo);
        FacturaDetalle facturaDetalle;
        try {
            facturaDetalle = (FacturaDetalle) query.getSingleResult();
        } catch (Exception e){
            return null;
        }
        return facturaDetalle;
    }
}
