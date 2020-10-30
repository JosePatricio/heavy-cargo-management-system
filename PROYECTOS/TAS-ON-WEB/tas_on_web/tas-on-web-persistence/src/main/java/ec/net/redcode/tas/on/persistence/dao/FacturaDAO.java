package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.common.dto.FacturaManualDTO;
import ec.net.redcode.tas.on.common.dto.Invoices;
import ec.net.redcode.tas.on.persistence.entities.Factura;

import java.util.Date;
import java.util.List;

public interface FacturaDAO extends GenericDAO<Factura, String> {

    List<Factura> getFacturasByEstado(int estado);
    List<Factura> getFacturasByUsuarioAndEstado(String usuario, int estado);
    List<Invoices> getFacturasBy(int estado, String cliente, String facturaNro, Double valorDesde,
                                 Double valorHasta, Date fechaCobroDesde, Date fechaCobroHasta);
    Date fechaPagoFacturaOferta(String prefactura, int ofertaId);
    List<FacturaManualDTO> getFacturasManualesBy(String secuencial, String razonSocial, String ruc, Date fechaEmisionDesde, Date fechaEmisionHasta);

}
