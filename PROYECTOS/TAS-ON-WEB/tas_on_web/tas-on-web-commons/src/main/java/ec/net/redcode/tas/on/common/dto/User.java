package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class User {

    private String idDocumento;
    private String usuario;
    private String nombres;
    private String apellidos;
    private int idTipoUsuario;
    private String tipoUsuario;
    private int idEstado;
    private String estado;

}
