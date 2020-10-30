package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CalificacionTransportistaDTO {

    private Integer id;
    private String empresaRazonSocial;
    private Integer calificacion;
    private String comentario;
    private String solicitudEnvioId;
    private String solicitudEnvioOrigen;
    private String solicitudEnvioDestino;
    private Double solicitudEnvioPeso;
    private String solicitudEnvioUnidadPeso;
    private Integer solicitudEnvioPiezas;
    private Timestamp ofertaFechaEntrega;
    private String numeroDocumento;

}
