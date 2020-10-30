package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class EBillingDTO implements Serializable {
    private String ebillingId;
    private Timestamp emisionDate;
    private Timestamp authorizationDate;
    private String claveAcceso;
    private String state;
    private Double total;
    private String adquiriente;
    private String razonSocialAdquiriente;
    private String usuarioCreador;
}
