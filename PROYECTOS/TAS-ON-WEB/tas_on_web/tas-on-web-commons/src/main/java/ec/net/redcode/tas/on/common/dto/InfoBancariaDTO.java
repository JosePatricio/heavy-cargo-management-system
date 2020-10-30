package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class InfoBancariaDTO {

    private String bancoDesc;
    private Integer banco;
    private String tipoCuentaDesc;
    private Integer tipoCuenta;
    private String numeroCuenta;
    private String tipoIdentificacionDesc;
    private Integer tipoIdentificacion;
    private String identificacion;
    private String nombres;
    private Timestamp ultimaActualizacion;

}
