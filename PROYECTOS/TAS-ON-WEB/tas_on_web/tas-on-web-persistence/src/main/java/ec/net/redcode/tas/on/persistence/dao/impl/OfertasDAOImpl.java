package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.OfertaDAO;
import ec.net.redcode.tas.on.persistence.entities.Oferta;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OfertasDAOImpl extends GenericDAOImpl<Oferta, String> implements OfertaDAO{

    public OfertasDAOImpl(){
        super(Oferta.class);
    }
    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    @Override
    public List<Oferta> getOfertasByUsuarioAndEstado(String username, int estado) {
        Query query = em.createNamedQuery("Oferta.OfertaByUsuarioAndEstado");
        query.setParameter("usuario", username);
        query.setParameter("estado", estado);
        List<Oferta> ofertas = query.getResultList();
        return ofertas;
    }

    @Override
    public List<Oferta> getOfertasBySolicitudAndEstado(String idSolicitud, int estado) {
        Query query = em.createNamedQuery("Oferta.OfertaBySolicitudAndEstado");
        query.setParameter("idSolicitudEnvio", idSolicitud);
        query.setParameter("estado", estado);
        List<Oferta> ofertas = query.getResultList();
        return ofertas;
    }

    @Override
    public Oferta getOfertaBySolicitudUserAndEstado(String idSolicitud, String usuario, int estado) {
        Query query = em.createNamedQuery("Oferta.OfertaBySolicitudUsuarioAndEstado");
        query.setParameter("idSolicitud", idSolicitud);
        query.setParameter("usuario", usuario);
        query.setParameter("estado", estado);
        Oferta oferta;
        try {
            oferta = (Oferta) query.getSingleResult();
        } catch (Exception e){
            oferta = null;
        }
        return oferta;
    }

    @Override
    public Oferta getOfertaBySolicitudUser(String idSolicitud, String usuario) {
        Query query = em.createNamedQuery("Oferta.OfertaBySolicitudUsuario");
        query.setParameter("idSolicitud", idSolicitud);
        query.setParameter("usuario", usuario);
        Oferta oferta;
        try {
            oferta = (Oferta) query.getSingleResult();
        } catch (Exception e){
            oferta = null;
        }
        return oferta;
    }

    @Override
    public Oferta getOfertaBySolicitudEstado(String idSolicitud, int estado) {
        return null;
    }

    @Override
    public List<Oferta> getOfertaBySolicitudAOrder(String idSolicitud) {
        Query query = em.createNamedQuery("Oferta.OfertaBySolicitudOrder");
        query.setParameter("idSolicitud", idSolicitud);
        List<Oferta> ofertas;
        try {
            ofertas = query.getResultList();
        } catch (Exception e){
            ofertas = Collections.emptyList();
        }
        return ofertas;
    }

    @Override
    public long getOfertaCount(String idSolicitud) {
        Query query = em.createQuery("select count(o) from Oferta o where o.ofertaIdSolicitud = :idSolicitud and o.ofertaEstado = 31");
        query.setParameter("idSolicitud", idSolicitud);
        long count = (long) query.getSingleResult();
        return count;
    }

    @Override
    public Double getOfertaAvg(String idSolicitud) {
        Query query = em.createQuery("select avg(o.ofertaValor) from Oferta o where o.ofertaIdSolicitud = :idSolicitud and o.ofertaEstado = 31");
        query.setParameter("idSolicitud", idSolicitud);
        Double avg = (Double) query.getSingleResult();
        return avg;
    }

    @Override
    public Double getOfertaMin(String idSolicitud) {
        Query query = em.createQuery("select min(o.ofertaValor) from Oferta o where o.ofertaIdSolicitud = :idSolicitud and o.ofertaEstado = 31");
        query.setParameter("idSolicitud", idSolicitud);
        Double min = (Double) query.getSingleResult();
        return min;
    }

    @Override
    public Double getOfertaMax(String idSolicitud) {
        Query query = em.createQuery("select max(o.ofertaValor + o.ofertaComision) from Oferta o where o.ofertaIdSolicitud = :idSolicitud");
        query.setParameter("idSolicitud", idSolicitud);
        Double max = (Double) query.getSingleResult();
        return max;
    }

    @Override
    public List<Oferta> getOfertasByEstado(int estado) {
        Query query = em.createNamedQuery("Oferta.OfertaByEstado");
        query.setParameter("estado", estado);
        List<Oferta> ofertas;
        try {
            ofertas = query.getResultList();
        } catch (Exception e){
            ofertas = Collections.emptyList();
        }
        return ofertas;
    }

    //Es necesario un estado y un tipoFactura
    @Override
    public List<Offers> getOfertasBy(int estado, int tipoFactura, String razonSocial, String solicitud, String facturaNro,
                                     Date fechaDesde, Date fechaHasta) {
        StringBuilder querySB = new StringBuilder("select c.clienteRazonSocial, " +
                "fd.facturaDetalleFacturaId, " +
                "f.facturaNro," +
                "s.solicitudEnvioId, " +
                "o.ofertaId, " +
                "(select localidadDescripcion from Localidad where localidadId = s.solicitudEnvioLocalidadOrigen), " +
                "(select localidadDescripcion from Localidad where localidadId = s.solicitudEnvioLocalidadDestino), " +
                "CASE WHEN f.facturaTipoPago=0 THEN 'NORMAL' ELSE (select catalogoItemDescripcion from CatalogoItem where catalogoItemId = f.facturaTipoPago) END, " +
                "o.ofertaValor, " +
                "o.ofertaRetencion, " +
                "o.ofertaIva, " +
                "o.ofertaDescuento, " +
                "o.ofertaFecha, " +
                "s.solicitudEnvioFechaPago " +
                "   from SolicitudEnvio s, Oferta o, " +
                "        FacturaDetalle fd, Factura f, " +
                "        Usuario u, Cliente c " +
                "   where o.ofertaEstado = :estado " +
                "         and s.solicitudEnvioOfertaId = o.ofertaId " +
                "         and f.facturaTipoFactura = :tipoFactura " +
                "         and fd.facturaDetalleOfertaId = o.ofertaId " +
                "         and f.facturaId = fd.facturaDetalleFacturaId " +
                "         and u.usuarioIdDocumento = o.ofertaIdUsuario " +
                "         and u.usuarioRuc = c.clienteRuc");
        if(razonSocial != null && !razonSocial.isEmpty()){
            querySB.append(" and c.clienteRazonSocial like :razonSocial");
        }
        if(solicitud != null && !solicitud.isEmpty()){
            querySB.append(" and s.solicitudEnvioId like :solicitud");
        }
        if(facturaNro != null && !facturaNro.isEmpty()){
            querySB.append(" and f.facturaNro like :facturaNro");
        }
        if(fechaDesde != null){
            querySB.append(" and DATE(s.solicitudEnvioFechaPago) >= :fechaDesde");
        }
        if(fechaHasta != null){
            querySB.append(" and DATE(s.solicitudEnvioFechaPago) <= :fechaHasta");
        }
        Query query = em.createQuery(querySB.toString());
        query.setParameter("estado", estado);
        query.setParameter("tipoFactura", tipoFactura);
        if(razonSocial != null && !razonSocial.isEmpty()){
            query.setParameter("razonSocial", "%"+razonSocial+"%");
        }
        if(solicitud != null && !solicitud.isEmpty()){
            query.setParameter("solicitud", "%"+solicitud+"%");
        }
        if(facturaNro != null && !facturaNro.isEmpty()){
            query.setParameter("facturaNro", "%"+facturaNro+"%");
        }
        if(fechaDesde != null){
            query.setParameter("fechaDesde", fechaDesde);
        }
        if(fechaHasta != null){
            query.setParameter("fechaHasta", fechaHasta);
        }
        return getOffersFromResultList(query.getResultList());
    }

    private List<Offers> getOffersFromResultList(List<Object[]> offersObj){
        List<Offers> offersList = new ArrayList<>();
        if(offersObj == null) return offersList;
        offersObj.forEach( o -> {
            int i = 0;
            Offers offers = new Offers();
            offers.setSupplier((String)o[i++]);
            offers.setNumeroPrefactura((String)o[i++]);
            offers.setInvoiceId((String)o[i++]);
            offers.setIdSolicitud((String)o[i++]);
            offers.setIdOferta((Integer)o[i++]);
            offers.setOrigen((String)o[i++]);
            offers.setDestino((String)o[i++]);
            offers.setTypePay((String)o[i++]);
            double valor = TasOnUtil.getDoubleFromObject(o[i++]);
            double retencion = TasOnUtil.getDoubleFromObject(o[i++]);
            double iva = TasOnUtil.getDoubleFromObject(o[i++]);
            double descuento = TasOnUtil.getDoubleFromObject(o[i++]);
            offers.setAmount(TasOnUtil.roundDouble(valor-retencion-iva-descuento,2));
            Timestamp fechaOferta = (Timestamp) o[i++];
            Date date = new Date(fechaOferta.getTime());
            offers.setDate(simpleDateFormat.format(date));
            Timestamp fechaPago = (Timestamp) o[i++];
            offers.setDatePay(fechaPago);
            offers.setExpiredDay(TasOnUtil.getExpiredDays(fechaPago));

            offersList.add(offers);
        });
        return offersList;
    }

    @Override
    public Double getOfertaMaxBySolicitud(String idSolicitud) {
        Query query = em.createNamedQuery("Oferta.OfertaByMax");
        query.setParameter("idSolicitud", idSolicitud);
        Double max = (Double) query.getSingleResult();
        return max;
    }

    @Override
    public List<Oferta> getOfertaBySolicitudOfertas(String idSolicitud) {
        Query query = em.createNamedQuery("Oferta.OfertaBySolicitudOfertas");
        query.setParameter("solicitud", idSolicitud);
        List<Oferta> ofertas;
        try {
            ofertas = query.getResultList();
        } catch (Exception e){
            ofertas = Collections.emptyList();
        }
        return ofertas;
    }

    @Override
    public List<Oferta> getOfertasPorCobrar(String usuario) {
        Query query = em.createNamedQuery("Oferta.OfertaPorCobrar");
        query.setParameter("usuario", usuario);
        List<Oferta> ofertas;
        try {
            ofertas = query.getResultList();
        } catch (Exception e){
            ofertas = Collections.emptyList();
        }
        return ofertas;
    }
}


