package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EBillingRequest implements Serializable {
    private String usuarioEbillingID;
    private String idUsuario;
    private String guiaRemision;
    private Adquiriente adquiriente;
    private List<DetalleFactura> detalles;
    private Integer formaPago;
    private String claveFirma;
    private int diasPlazo;
    private String claveAcceso;
}
