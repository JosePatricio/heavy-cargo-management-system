package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.common.dto.FacturaManualDTO;
import ec.net.redcode.tas.on.common.dto.Invoices;
import ec.net.redcode.tas.on.persistence.dao.FacturaDAO;
import ec.net.redcode.tas.on.persistence.entities.Factura;
import ec.net.redcode.tas.on.persistence.service.FacturaService;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class FacturaServiceImpl implements FacturaService {

    @Setter private FacturaDAO facturaDAO;

    @Override
    public void create(Factura factura) {
        facturaDAO.create(factura);
    }

    @Override
    public Factura read(String idFactura) {
        return facturaDAO.read(idFactura);
    }

    @Override
    public void update(Factura factura) {
        facturaDAO.update(factura);
    }

    @Override
    public List<Factura> getFacturasByUsuarioAndEstado(String usuario, int estado) {
        return facturaDAO.getFacturasByUsuarioAndEstado(usuario, estado);
    }

    @Override
    public List<Factura> getFacturasByEstado(int estado) {
        return facturaDAO.getFacturasByEstado(estado);
    }

    @Override
    public List<Invoices> getFacturasBy(int estado, String cliente, String facturaNro, Double valorDesde,
                                        Double valorHasta, Date fechaCobroDesde, Date fechaCobroHasta){
        return facturaDAO.getFacturasBy(estado, cliente, facturaNro, valorDesde, valorHasta, fechaCobroDesde, fechaCobroHasta);
    }

    @Override
    public Date fechaPagoFacturaOferta(String prefactura, int ofertaId){
        return facturaDAO.fechaPagoFacturaOferta(prefactura, ofertaId);
    }

    @Override
    public List<FacturaManualDTO> getFacturasManualesBy(String secuencial, String razonSocial, String ruc, Date fechaEmisionDesde, Date fechaEmisionHasta){
        return facturaDAO.getFacturasManualesBy(secuencial, razonSocial, ruc, fechaEmisionDesde, fechaEmisionHasta);
    }

}
