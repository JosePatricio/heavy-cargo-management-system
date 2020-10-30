package ec.redcode.tas.on.android.dto.requests;

import java.util.List;

import lombok.Data;

@Data
public class EBillingDTO {
    private String guiaRemision;
    private Adquiriente adquiriente;
    private List<DetalleFactura> detalles;
    private Integer formaPago;
    private String claveFirma;
    private int diasPlazo;
}