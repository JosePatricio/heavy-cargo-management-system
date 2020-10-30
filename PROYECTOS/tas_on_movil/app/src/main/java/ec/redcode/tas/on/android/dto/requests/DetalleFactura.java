package ec.redcode.tas.on.android.dto.requests;

import java.math.BigDecimal;

import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import lombok.Data;

@Data
public class DetalleFactura {
    private Integer numeroPiezas;
    private CatalogoItemDTO unidadPiezas;
    private LocalidadDTO ciudadOrigen;
    private LocalidadDTO ciudadDestino;
    private String detallesAdicionales;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
}