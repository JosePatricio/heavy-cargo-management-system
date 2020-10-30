package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FacturaRetencionDTO {

    private String claveAccesoRetencion;
    private Timestamp fechaAutorizacionRetencion;
    private String numeroPrefactura;
    private String numeroFacturaCliente;
    private String estado;
    private Double total;
    private Double retencion;
    private String cliente;

}
