package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PedidoDTO {

    private Integer pedidoId;
    private Timestamp fechaEntregaDesde;
    private Timestamp fechaEntregaHasta;
    private String clienteRazonSocial;
    private String personaRecibe;
    private Boolean solicitaCredito;
    private Double total;
    private String lat;
    private String lng;
    private Integer visitaId;
    private Timestamp fechaCreacion;
    private String usuarioCrea;
    private List<PedidoDetalleDTO> detalle;
    private List<Documents> documentosCredito;

}
