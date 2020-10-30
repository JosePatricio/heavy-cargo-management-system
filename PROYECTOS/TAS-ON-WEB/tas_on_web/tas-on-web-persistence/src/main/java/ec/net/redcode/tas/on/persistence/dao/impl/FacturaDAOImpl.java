package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.dto.FacturaManualDTO;
import ec.net.redcode.tas.on.common.dto.Invoices;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.FacturaDAO;
import ec.net.redcode.tas.on.persistence.entities.Factura;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FacturaDAOImpl extends GenericDAOImpl<Factura, String> implements FacturaDAO {

    public FacturaDAOImpl(){
        super(Factura.class);
    }

    @Override
    public List<Factura> getFacturasByEstado(int estado) {
        Query query = em.createNamedQuery("Factura.FacturaByEstado");
        query.setParameter("estado", estado);
        List<Factura> facturas;
        try {
            facturas = query.getResultList();
        } catch (Exception e){
            facturas = Collections.emptyList();;
        }
        return facturas;
    }

    @Override
    public List<Factura> getFacturasByUsuarioAndEstado(String usuario, int estado) {
        Query query = em.createNamedQuery("Factura.FacturaByUsuarioAndEstado");
        query.setParameter("usuario", usuario);
        query.setParameter("estado", estado);
        List<Factura> facturas;
        try {
            facturas = query.getResultList();
        } catch (Exception e){
            facturas = Collections.emptyList();;
        }
        return facturas;
    }

    @Override
    public List<Invoices> getFacturasBy(int estado, String cliente, String facturaNro, Double valorDesde,
                                        Double valorHasta, Date fechaCobroDesde, Date fechaCobroHasta){
        StringBuilder querySB = new StringBuilder("select factura_id," +
                " factura_nro," +
                " cliente_razon_social," +
                " factura_total," +
                " factura_descuento," +
                " factura_fecha_emision," +
                " DATE_ADD(factura_fecha_emision, INTERVAL cliente_dias_credito DAY) AS FECHA_COBRO," +
                " DATEDIFF(DATE_ADD(factura_fecha_emision, INTERVAL cliente_dias_credito DAY), NOW()) AS DIAS_FACTURA_VENCIDA," +
                " CASE WHEN factura_tipo_pago=0 THEN 'NORMAL' ELSE (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = factura_tipo_pago) END " +
                "  from factura, cliente" +
                "  where factura_estado = :estado" +
                "  and factura_empresa = cliente_ruc"
        );

        if(cliente != null && !cliente.isEmpty()) querySB.append(" and cliente_razon_social like :cliente");
        if(facturaNro !=null && !facturaNro.isEmpty()) querySB.append(" and factura_id like :facturaNro");
        if(valorDesde != null && valorDesde > 0) querySB.append(" and factura_total >= :valorDesde");
        if(valorHasta != null && valorHasta > 0) querySB.append(" and factura_total <= :valorHasta");
        if(fechaCobroDesde != null) querySB.append(" and DATE(DATE_ADD(factura_fecha_emision, INTERVAL cliente_dias_credito DAY)) >= :fechaCobroDesde");
        if(fechaCobroHasta != null) querySB.append(" and DATE(DATE_ADD(factura_fecha_emision, INTERVAL cliente_dias_credito DAY)) <= :fechaCobroHasta");

        Query query = em.createNativeQuery(querySB.toString());
        query.setParameter("estado", estado);

        if(cliente != null && !cliente.isEmpty()) query.setParameter("cliente", '%'+cliente+'%');
        if(facturaNro !=null && !facturaNro.isEmpty()) query.setParameter("facturaNro", '%'+facturaNro+'%');
        if(valorDesde != null && valorDesde > 0) query.setParameter("valorDesde", valorDesde);
        if(valorHasta != null && valorHasta > 0) query.setParameter("valorHasta", valorHasta);
        if(fechaCobroDesde != null) query.setParameter("fechaCobroDesde", fechaCobroDesde);
        if(fechaCobroHasta != null) query.setParameter("fechaCobroHasta", fechaCobroHasta);

        return getInvoicesFromResultQuery(query.getResultList());
    }

    private List<Invoices> getInvoicesFromResultQuery(List<Object[]> objects){
        List<Invoices> invoices = new ArrayList<>();
        for(Object[] o : objects){
            int i = 0;
            Invoices invoice = new Invoices();
            invoice.setInvoiceId(TasOnUtil.getStringFromObject(o[i++]));
            invoice.setInvoiceNumber(TasOnUtil.getStringFromObject(o[i++]));
            invoice.setCompany(TasOnUtil.getStringFromObject(o[i++]));
            invoice.setInvoiceAmount(TasOnUtil.getDoubleFromObject(o[i++]));
            invoice.setInvoiceDescuento(TasOnUtil.getDoubleFromObject(o[i++]));
            invoice.setInvoiceEmisionDate(TasOnUtil.getTimeStampFromObject(o[i++]));
            invoice.setInvoiceDate(TasOnUtil.getTimeStampFromObject(o[i++]));//FECHA COBRO
            invoice.setDiasFacturaVencida(TasOnUtil.getIntFromObject(o[i++]));
            invoice.setInvoicesTypePayDesc(TasOnUtil.getStringFromObject(o[i++]));
            invoices.add(invoice);
        }
        return invoices;
    }

    @Override
    public Date fechaPagoFacturaOferta(String prefactura, int ofertaId){
        Query query = em.createNativeQuery("select DATE(DATE_ADD(factura_fecha_emision, INTERVAL cliente_dias_credito DAY))" +
                " from factura, cliente" +
                " where factura_empresa = cliente_ruc" +
                " and factura_id = (select factura_detalle_factura_id" +
                " from factura_detalle where factura_detalle_oferta_id = :ofertaID" +
                " and factura_detalle_factura_id != :prefactura);");
        query.setParameter("ofertaID", ofertaId);
        query.setParameter("prefactura", prefactura);
        try{
            return (Date) query.getSingleResult();
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public List<FacturaManualDTO> getFacturasManualesBy(String secuencial, String razonSocial, String ruc, Date fechaEmisionDesde, Date fechaEmisionHasta){
        StringBuilder querySB = new StringBuilder("select fm.facturaManualSecuencial, fm.facturaManualClaveAcceso, " +
                "fm.facturaManualFechaEmision, fm.facturaManualFechaAutorizacion, ad.adquirienteIdDocumento, " +
                "ad.adquirienteRazonSocial, us.usuarioNombreUsuario, fm.facturaManualTotal, " +
                "fm.facturaManualEstado" +
                " from FacturaManual fm, Adquiriente ad, Usuario us" +
                " where fm.facturaManualAdquiriente = ad.adquirienteIdDocumento" +
                " and fm.facturaManualUsuarioId = us.usuarioIdDocumento");
        if(!TasOnUtil.isStringNullOrEmpty(secuencial)) querySB.append(" and fm.facturaManualSecuencial like :secuencial");
        if(!TasOnUtil.isStringNullOrEmpty(razonSocial)) querySB.append(" and ad.adquirienteRazonSocial like :razonSocial");
        if(!TasOnUtil.isStringNullOrEmpty(ruc)) querySB.append(" and ad.adquirienteIdDocumento like :ruc");
        if(fechaEmisionDesde != null) querySB.append(" and date(fm.facturaManualFechaEmision) >= :fechaEmisionDesde");
        if(fechaEmisionHasta != null) querySB.append(" and date(fm.facturaManualFechaEmision) <= :fechaEmisionHasta");
        Query query = em.createQuery(querySB.toString());
        if(!TasOnUtil.isStringNullOrEmpty(secuencial)) query.setParameter("secuencial", "%"+secuencial+"%");
        if(!TasOnUtil.isStringNullOrEmpty(razonSocial)) query.setParameter("razonSocial", "%"+razonSocial+"%");
        if(!TasOnUtil.isStringNullOrEmpty(ruc)) query.setParameter("ruc", "%"+ruc+"%");
        if(fechaEmisionDesde != null) query.setParameter("fechaEmisionDesde", fechaEmisionDesde);
        if(fechaEmisionHasta != null) query.setParameter("fechaEmisionHasta", fechaEmisionHasta);

        return getFacturasManualesFromResultQuery(query.getResultList());

    }

    private List<FacturaManualDTO> getFacturasManualesFromResultQuery(List<Object[]> objects){
        List<FacturaManualDTO> facturasManualesList = new ArrayList<>();
        for(Object[] o : objects){
            int i = 0;
            FacturaManualDTO facturaManual = new FacturaManualDTO();
            facturaManual.setSecuencial(TasOnUtil.getStringFromObject(o[i++]));
            facturaManual.setClaveAcceso(TasOnUtil.getStringFromObject(o[i++]));
            facturaManual.setFechaEmision(TasOnUtil.getTimeStampFromObject(o[i++]));
            facturaManual.setFechaAutorizacion(TasOnUtil.getTimeStampFromObject(o[i++]));
            facturaManual.setComprador(TasOnUtil.getStringFromObject(o[i++]));
            facturaManual.setRazonSocialComprador(TasOnUtil.getStringFromObject(o[i++]));
            facturaManual.setUsuario(TasOnUtil.getStringFromObject(o[i++]));
            facturaManual.setTotal(TasOnUtil.getDoubleFromObject(o[i++]));
            facturaManual.setEstado(TasOnUtil.getStringFromObject(o[i++]));
            facturasManualesList.add(facturaManual);
        }
        return facturasManualesList;
    }


}
