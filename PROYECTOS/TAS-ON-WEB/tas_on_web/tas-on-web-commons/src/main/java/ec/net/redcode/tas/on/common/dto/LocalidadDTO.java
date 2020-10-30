package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class LocalidadDTO {
    private int localidadId;
    private String localidadDescripcion;
    private int localidadCode;
    private Integer localidadLocalidadPadre;
    private Integer localidadEstado;
}
