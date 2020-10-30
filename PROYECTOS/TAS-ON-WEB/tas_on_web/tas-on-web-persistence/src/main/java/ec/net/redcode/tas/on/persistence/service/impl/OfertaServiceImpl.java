package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.persistence.dao.OfertaDAO;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.persistence.service.OfertaService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class OfertaServiceImpl implements OfertaService {

    @Setter private OfertaDAO ofertaDAO;

    @Override
    public void create(Oferta oferta) {
        ofertaDAO.create(oferta);
    }

    @Override
    public Oferta read(int idOferta) {
        return ofertaDAO.read(idOferta);
    }

    @Override
    public void update(Oferta oferta) {
        ofertaDAO.update(oferta);
    }

    @Override
    public void delete(Oferta oferta) {
        ofertaDAO.delete(oferta);
    }

    @Override
    public Oferta getOfertaBySolicitudUserAndEstado(String idSolicitud, String usuario, int estado) {
        return ofertaDAO.getOfertaBySolicitudUserAndEstado(idSolicitud, usuario, estado);
    }

    @Override
    public Oferta getOfertaBySolicitudUser(String idSolicitud, String usuario) {
        return ofertaDAO.getOfertaBySolicitudUser(idSolicitud, usuario);
    }

    @Override
    public List<Oferta> getOfertasByUsuarioAndEstado(String username, int estado) {
        return ofertaDAO.getOfertasByUsuarioAndEstado(username, estado);
    }

    @Override
    public List<Oferta> getOfertasBySolicitudAndEstado(String solicitud, int estado) {
        return ofertaDAO.getOfertasBySolicitudAndEstado(solicitud, estado);
    }

    @Override
    public List<Oferta> getOfertaBySolicitudAOrder(String idSolicitud) {
        return ofertaDAO.getOfertaBySolicitudAOrder(idSolicitud);
    }

    @Override
    public long getOfertaCount(String idSolicitud) {
        return ofertaDAO.getOfertaCount(idSolicitud);
    }

    @Override
    public Double getOfertaAvg(String idSolicitud) {
        return ofertaDAO.getOfertaAvg(idSolicitud);
    }

    @Override
    public Double getOfertaMin(String idSolicitud) {
        return ofertaDAO.getOfertaMin(idSolicitud);
    }

    @Override
    public Double getOfertaMax(String idSolicitud) {
        return ofertaDAO.getOfertaMax(idSolicitud);
    }

    @Override
    public List<Oferta> getOfertasyEstado(int estado) {
        return ofertaDAO.getOfertasByEstado(estado);
    }

    @Override
    public List<Offers> getOfertasBy(int estado, int tipoFactura, String razonSocial, String solicitud, String facturaNro,
                                     Date fechaDesde, Date fechaHasta) {
        return ofertaDAO.getOfertasBy(estado, tipoFactura, razonSocial, solicitud, facturaNro, fechaDesde, fechaHasta);
    }

    @Override
    public Double getOfertaMaxBySolicitud(String idSolicitud) {
        return ofertaDAO.getOfertaMaxBySolicitud(idSolicitud);
    }

    @Override
    public List<Oferta> getOfertaBySolicitudOfertas(String idSolicitud) {
        return ofertaDAO.getOfertaBySolicitudOfertas(idSolicitud);
    }

    @Override
    public List<Oferta> getOfertasPorCobrar(String usuario) {
        return ofertaDAO.getOfertasPorCobrar(usuario);
    }

}
