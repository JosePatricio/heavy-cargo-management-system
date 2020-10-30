package ec.redcode.tas.on.facturacion.reporte;

import ec.gob.sri.comprobantes.modelo.reportes.FormasPago;
import ec.gob.sri.comprobantes.modelo.reportes.InformacionAdicional;
import ec.gob.sri.comprobantes.modelo.reportes.TotalesComprobante;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DetallesAdicionalesReporte {

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private String cantidad;
    private String descripcion;
    private BigDecimal precioUnitario;
    private String precioTotalSinImpuesto;
    private String descuento;
    private String numeroComprobante;
    private String nombreComprobante;
    private String detalle1;
    private String detalle2;
    private String detalle3;
    private String fechaEmisionCcompModificado;
    private BigDecimal precioSinSubsidio;
    private List<InformacionAdicional> infoAdicional;
    private List<FormasPago> formasPago;
    private List<TotalesComprobante> totalesComprobante;
    private String razonModificacion;
    private String valorModificacion;
    private String baseImponible;
    private String nombreImpuesto;
    private String porcentajeRetener;
    private String valorRetenido;
    private String numeroDocCliente;

}
