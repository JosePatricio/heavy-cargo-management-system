package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.persistence.entities.Oferta;

import java.util.Date;
import java.util.List;

public interface OfertaService {

    void create(Oferta oferta);
    Oferta read(int idOferta);
    void update(Oferta oferta);
    void delete(Oferta oferta);
    Oferta getOfertaBySolicitudUserAndEstado(String idSolicitud, String usuario, int estado);
    Oferta getOfertaBySolicitudUser(String idSolicitud, String usuario);
    List<Oferta> getOfertasByUsuarioAndEstado(String username, int estado);
    List<Oferta> getOfertasBySolicitudAndEstado(String solicitud, int estado);
    List<Oferta> getOfertaBySolicitudAOrder(String idSolicitud);
    long getOfertaCount(String idSolicitud);
    Double getOfertaAvg(String idSolicitud);
    Double getOfertaMin(String idSolicitud);
    Double getOfertaMax(String idSolicitud);
    List<Oferta> getOfertasyEstado(int estado);
    List<Offers> getOfertasBy(int estado, int tipoFactura, String razonSocial, String solicitud, String facturaNro,
                              Date fechaDesde, Date fechaHasta);
    Double getOfertaMaxBySolicitud(String idSolicitud);
    List<Oferta> getOfertaBySolicitudOfertas(String idSolicitud);
    List<Oferta> getOfertasPorCobrar(String usuario);

}
