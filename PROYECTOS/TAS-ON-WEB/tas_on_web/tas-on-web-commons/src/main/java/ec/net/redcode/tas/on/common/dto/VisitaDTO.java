package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitaDTO {

    private int visitaId;
    private ClientePedidosDTO cliente;
    private VendedorClienteDTO vendedor;
    private Timestamp visitaFecha;
    private String visitaEstado;
    private Integer visitaEstadoId;

}
