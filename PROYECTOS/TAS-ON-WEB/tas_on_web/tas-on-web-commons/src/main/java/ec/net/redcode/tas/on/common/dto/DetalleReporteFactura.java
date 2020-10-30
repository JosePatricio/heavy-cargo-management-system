package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class DetalleReporteFactura {

    private String noOferta;
    private String origen;
    private String destino;
    private String transportista;
    private String fechaEntrega;
    private String fechaRequerida;
    private Double valor;
    private int numeroCajas;
    private int numeroOfertas;
    private Double ofertaAlta;
    private Double ahorroOfertaAlta;
    private Double ahorroOfertaPromedio;
    private int posicionOferta;
    private Double costoPromedio;

}
