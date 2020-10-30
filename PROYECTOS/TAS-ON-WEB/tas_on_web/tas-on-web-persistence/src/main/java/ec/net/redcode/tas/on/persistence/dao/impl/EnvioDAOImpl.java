package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.EnvioDAO;
import ec.net.redcode.tas.on.persistence.entities.Envio;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class EnvioDAOImpl extends GenericDAOImpl<Envio, Integer> implements EnvioDAO {

    public EnvioDAOImpl() {
        super(Envio.class);
    }

    @Override
    public List<Envio> getBy(List<Integer> estados, String razonSocial, String ruc, String nroDocumento, Date fechaRecoleccionDesde, Date fechaRecoleccionHasta, Integer conductorId){
        StringBuilder querySB = new StringBuilder("select e from Envio e, Cliente c where e.envioCliente = c.clienteRuc");
        if(estados!= null && !estados.isEmpty()) querySB.append(" and e.envioEstado in :estados ");
        if(!TasOnUtil.isStringNullOrEmpty(razonSocial)) querySB.append(" and c.clienteRazonSocial like :razonSocial ");
        if(!TasOnUtil.isStringNullOrEmpty(ruc)) querySB.append(" and c.clienteRuc = :ruc ");
        if(!TasOnUtil.isStringNullOrEmpty(nroDocumento)) querySB.append(" and e.envioNumeroDocumento like :nroDocumento ");
        if(fechaRecoleccionDesde != null) querySB.append(" and date(e.envioFechaRecoleccion) >= :fechaRecoleccionDesde");
        if(fechaRecoleccionHasta != null) querySB.append(" and date(e.envioFechaRecoleccion) <= :fechaRecoleccionHasta");
        if(conductorId != null)  querySB.append(" and e.envioConductor = :conductor ");

        Query query = em.createQuery(querySB.toString());

        if(estados!= null && !estados.isEmpty()) query.setParameter("estados", estados);
        if(!TasOnUtil.isStringNullOrEmpty(razonSocial)) query.setParameter("razonSocial", "%"+ razonSocial +"%");
        if(!TasOnUtil.isStringNullOrEmpty(ruc)) query.setParameter("ruc", ruc);
        if(!TasOnUtil.isStringNullOrEmpty(nroDocumento)) query.setParameter("nroDocumento", "%"+ nroDocumento +"%");
        if(fechaRecoleccionDesde != null) query.setParameter("fechaRecoleccionDesde", fechaRecoleccionDesde);
        if(fechaRecoleccionHasta != null) query.setParameter("fechaRecoleccionHasta", fechaRecoleccionHasta);
        if(conductorId != null) query.setParameter("conductor", conductorId);
        return query.getResultList();
    }

}
