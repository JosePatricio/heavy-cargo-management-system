package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OfertaNotificacion  implements Serializable {

    private String solicitud;
    private String empresa;
    private String origen;
    private String destino;
    private String tipo;
    private String comentario;
    private String correo;

}
