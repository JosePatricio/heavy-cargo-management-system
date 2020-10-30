package ec.redcode.tas.on.android.dto.requests;

import lombok.Data;

@Data
public class MyEBillingDTO {
    private String ebillingId;
    private String emisionDate;
    private String authorizationDate;
    private String claveAcceso;
    private String state;
    private Double total;
    private String adquiriente;
    private String razonSocialAdquiriente;
}