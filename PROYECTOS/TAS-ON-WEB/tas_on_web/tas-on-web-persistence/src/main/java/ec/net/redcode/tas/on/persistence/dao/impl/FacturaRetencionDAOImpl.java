package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.dto.FacturaRetencionDTO;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.FacturaRetencionDAO;
import ec.net.redcode.tas.on.persistence.entities.FacturaRetencion;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaRetencionDAOImpl extends GenericDAOImpl<FacturaRetencion, String> implements FacturaRetencionDAO {

    public FacturaRetencionDAOImpl() {
        super(FacturaRetencion.class);
    }

    @Override
    public List<FacturaRetencionDTO> getRetencionesBy(String razonSocialCliente, String numeroFacturaCliente, String prefactura,
                                                      Date fechaAutorizacionDesde, Date fechaAutorizacionHasta){
        StringBuilder queryFacturasRetencionSB = new StringBuilder("select fr.facturaRetencionId, fr.facturaRetencionFechaAutorizacion," +
                " fr.facturaRetencionFacturaId, f.facturaNro, fr.facturaRetencionEstado," +
                " f.facturaTotal, f.facturaRetencion, c.clienteRazonSocial" +
                " from FacturaRetencion fr, Factura f, Cliente c" +
                " where fr.facturaRetencionFacturaId = f.facturaId" +
                " and f.facturaEmpresa = c.clienteRuc");
        StringBuilder queryFacturaProveedorRetencionSB = new StringBuilder("select fr.facturaRetencionId, fr.facturaRetencionFechaAutorizacion," +
                " fr.facturaRetencionFacturaId, fp.facturaProveedorNumero, fr.facturaRetencionEstado," +
                " fp.facturaProveedorTotal, fp.facturaProveedorRetencion, c.clienteRazonSocial" +
                " from FacturaRetencion fr, FacturaProveedor fp, Cliente c" +
                " where fr.facturaRetencionFacturaProveedorId = fp.facturaProveedorId" +
                " and fr.facturaRetencionFacturaProveedorId != 0" +
                " and fp.facturaProveedorRucCliente = c.clienteRuc");

        if(!TasOnUtil.isStringNullOrEmpty(razonSocialCliente)){
            queryFacturasRetencionSB.append(" and c.clienteRazonSocial like :razonSocialCliente");
            queryFacturaProveedorRetencionSB.append(" and c.clienteRazonSocial like :razonSocialCliente");
        }
        if(!TasOnUtil.isStringNullOrEmpty(numeroFacturaCliente)){
            queryFacturasRetencionSB.append(" and f.facturaNro like :numeroFacturaCliente");
            queryFacturaProveedorRetencionSB.append(" and fp.facturaProveedorNumero like :numeroFacturaCliente");
        }
        if(!TasOnUtil.isStringNullOrEmpty(prefactura)){
            queryFacturasRetencionSB.append(" and fr.facturaRetencionFacturaId like :prefactura");
            queryFacturaProveedorRetencionSB.append(" and fr.facturaRetencionFacturaId like :prefactura");
        }
        if(fechaAutorizacionDesde != null){
            queryFacturasRetencionSB.append(" and date(fr.facturaRetencionFechaAutorizacion) >= :fechaAutorizacionDesde");
            queryFacturaProveedorRetencionSB.append(" and date(fr.facturaRetencionFechaAutorizacion) >= :fechaAutorizacionDesde");
        }
        if(fechaAutorizacionHasta != null){
            queryFacturasRetencionSB.append(" and date(fr.facturaRetencionFechaAutorizacion) <= :fechaAutorizacionHasta");
            queryFacturaProveedorRetencionSB.append(" and date(fr.facturaRetencionFechaAutorizacion) <= :fechaAutorizacionHasta");
        }

        Query queryFacturasRetencion = em.createQuery(queryFacturasRetencionSB.toString());
        Query queryFacturaProveedorRetencion = em.createQuery(queryFacturaProveedorRetencionSB.toString());

        if(!TasOnUtil.isStringNullOrEmpty(razonSocialCliente)){
            queryFacturasRetencion.setParameter("razonSocialCliente", "%"+razonSocialCliente+"%");
            queryFacturaProveedorRetencion.setParameter("razonSocialCliente","%"+razonSocialCliente+"%");
        }
        if(!TasOnUtil.isStringNullOrEmpty(numeroFacturaCliente)){
            queryFacturasRetencion.setParameter("numeroFacturaCliente","%"+numeroFacturaCliente+"%");
            queryFacturaProveedorRetencion.setParameter("numeroFacturaCliente","%"+numeroFacturaCliente+"%");
        }
        if(!TasOnUtil.isStringNullOrEmpty(prefactura)){
            queryFacturasRetencion.setParameter("prefactura","%"+prefactura+"%");
            queryFacturaProveedorRetencion.setParameter("prefactura","%"+prefactura+"%");
        }
        if(fechaAutorizacionDesde != null){
            queryFacturasRetencion.setParameter("fechaAutorizacionDesde",fechaAutorizacionDesde);
            queryFacturaProveedorRetencion.setParameter("fechaAutorizacionDesde",fechaAutorizacionDesde);
        }
        if(fechaAutorizacionHasta != null){
            queryFacturasRetencion.setParameter("fechaAutorizacionHasta",fechaAutorizacionHasta);
            queryFacturaProveedorRetencion.setParameter("fechaAutorizacionHasta",fechaAutorizacionHasta);
        }

        List<FacturaRetencionDTO> result = getFacturaRetencionDTOListFromQueryResult(queryFacturasRetencion.getResultList());
        result.addAll(getFacturaRetencionDTOListFromQueryResult(queryFacturaProveedorRetencion.getResultList()));
        return result;
    }

    private List<FacturaRetencionDTO> getFacturaRetencionDTOListFromQueryResult(List<Object[]> resultList){
        List<FacturaRetencionDTO> facturaRetencionDTOList = new ArrayList<>();
        for(Object[] o : resultList){
            int i = 0;
            FacturaRetencionDTO facturaRetencionDTO = new FacturaRetencionDTO();
            facturaRetencionDTO.setClaveAccesoRetencion(TasOnUtil.getStringFromObject(o[i++]));
            facturaRetencionDTO.setFechaAutorizacionRetencion(TasOnUtil.getTimeStampFromObject(o[i++]));
            facturaRetencionDTO.setNumeroPrefactura(TasOnUtil.getStringFromObject(o[i++]));
            facturaRetencionDTO.setNumeroFacturaCliente(TasOnUtil.getStringFromObject(o[i++]));
            facturaRetencionDTO.setEstado(TasOnUtil.getStringFromObject(o[i++]));
            facturaRetencionDTO.setTotal(TasOnUtil.getDoubleFromObject(o[i++]));
            facturaRetencionDTO.setRetencion(TasOnUtil.getDoubleFromObject(o[i++]));
            facturaRetencionDTO.setCliente(TasOnUtil.getStringFromObject(o[i++]));
            facturaRetencionDTOList.add(facturaRetencionDTO);
        }
        return facturaRetencionDTOList;
    }

}
