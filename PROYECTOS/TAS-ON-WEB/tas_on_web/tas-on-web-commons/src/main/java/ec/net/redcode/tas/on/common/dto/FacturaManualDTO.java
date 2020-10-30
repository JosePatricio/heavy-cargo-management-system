package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FacturaManualDTO {

    private String secuencial;
    private String claveAcceso;
    private Timestamp fechaEmision;
    private Timestamp fechaAutorizacion;
    private String comprador;
    private String razonSocialComprador;
    private String estado;
    private String usuario;
    private Double total;

}
