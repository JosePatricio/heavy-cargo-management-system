package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ReporteFactura {

    private String noFactura;
    private String clienteTason;
    private InputStream LOGO;
    private List<DetalleReporteFactura> detalleReporteFacturas;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;

}
