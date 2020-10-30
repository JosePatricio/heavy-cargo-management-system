package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Adquiriente implements Serializable {

    private String adquirienteIdDocumento;
    private Integer adquirienteTipoDocumento;
    private String adquirienteRazonSocial;
    private String adquirienteDireccion;
    private String adquirienteTelefono;
    private String adquirienteMail;
    private String adquirientePersonaContacto;

}
