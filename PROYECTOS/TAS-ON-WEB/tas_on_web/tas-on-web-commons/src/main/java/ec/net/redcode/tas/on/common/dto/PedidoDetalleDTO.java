package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class PedidoDetalleDTO {

    private Integer productoId;
    private String productoCodigo;
    private String productoNombre;
    private Integer productoCantidad;
    private Double productoPrecioSinImp;
    private Double productoPrecioConImp;
    private Double precioTotalSinImp;
    private Double precioTotalConImp;

}
