package ec.net.redcode.tas.on.common.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class EnvioDTO {
    private Integer envioId;
    private String envioNumeroDocumento;
    private String envioClienteRazonSocial;
    private String envioTipo;
    private String envioOrigen;
    private String envioDireccionOrigen;
    private Timestamp envioFechaRecoleccion;
    private String envioDestino;
    private String envioDireccionDestino;
    private Timestamp envioFechaEntrega;
    private Integer envioNumeroPiezas;
    private Integer envioNumeroEstibadores;
    private String envioVehiculo;
    private String envioConductor;
    private String envioObservaciones;
    private String envioEstado;
    private int envioEstadoId;
    private Double envioPago;
}
