package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.persistence.entities.Oferta;

import java.util.Date;
import java.util.List;

public interface OfertaDAO extends  GenericDAO<Oferta, String> {

    List <Oferta> getOfertasByUsuarioAndEstado(String username, int estado);
    List <Oferta>  getOfertasBySolicitudAndEstado(String idSolicitud, int estado);
    List<Oferta> getOfertaBySolicitudAOrder(String idSolicitud);
    Oferta getOfertaBySolicitudUserAndEstado(String idSolicitud, String usuario, int estado);
    Oferta getOfertaBySolicitudUser(String idSolicitud, String usuario);
    Oferta getOfertaBySolicitudEstado(String idSolicitud, int estado);
    long getOfertaCount(String idSolicitud);
    Double getOfertaAvg(String idSolicitud);
    Double getOfertaMin(String idSolicitud);
    Double getOfertaMax(String idSolicitud);
    List<Oferta> getOfertasByEstado(int estado);
    List<Offers> getOfertasBy(int estado, int tipoFactura, String razonSocial, String solicitud, String facturaNro,
                              Date fechaDesde, Date fechaHasta);
    Double getOfertaMaxBySolicitud(String idSolicitud);
    List<Oferta> getOfertaBySolicitudOfertas(String idSolicitud);
    List<Oferta> getOfertasPorCobrar(String usuario);

}
