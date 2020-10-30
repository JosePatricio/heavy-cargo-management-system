package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class DetalleFactura {
    private Integer numeroPiezas;
    private CatalogoItemDTO unidadPiezas;
    private LocalidadDTO ciudadOrigen;
    private LocalidadDTO ciudadDestino;
    private String detallesAdicionales;
    private Double precioUnitario;
    private Double descuento;
}