package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private int productoId;
    private String productoCodigo;
    private String productoCategoria;
    private Integer productoCategoriaId;
    private String productoNombre;
    private String productoDescripcion;
    private Double productoVolumen;
    private String productoUnidadVolumen;
    private Integer productoUnidadVolumenId;
    private Integer productoUnidadesPorCaja;
    private double productoPrecioSinImp;
    private double productoPrecioConImp;
    private double productoPrecioPvpSinImp;
    private double productoPrecioPvpConImp;

}
