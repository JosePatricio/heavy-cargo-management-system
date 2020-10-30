package ec.redcode.tas.on.android.dto.requests;

import lombok.Data;

@Data
public class Adquiriente {

    private String adquirienteIdDocumento;
    private Integer adquirienteTipoDocumento;
    private String adquirienteRazonSocial;
    private String adquirienteDireccion;
    private String adquirienteTelefono;
    private String adquirienteMail;
    private String adquirientePersonaContacto;

}
