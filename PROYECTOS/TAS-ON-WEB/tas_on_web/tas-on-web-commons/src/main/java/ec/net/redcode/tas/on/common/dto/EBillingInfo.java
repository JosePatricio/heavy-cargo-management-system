package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EBillingInfo implements Serializable {
    private String razonSocial;
    private String ruc;
    private String direccion;
    private String facturaNro;
}
