package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class NuevaSolicitud {

    private String idSolicitud;
    private int ciudadOrigen;
    private int provinciaOrigen;
    private Integer ciudadDestino;
    private Integer provinciaDestino;
    private Integer unidadPeso;
    private Integer unidadVolumen;

}
