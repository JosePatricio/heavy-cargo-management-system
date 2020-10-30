package ec.redcode.tas.on.android.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Walter on 21/3/18.
 */

@Data @NoArgsConstructor
public class LocalidadDTO {
    private int localidadId;
    private String localidadDescripcion;
    private int localidadCode;
    private Integer localidadLocalidadPadre;
    private Integer localidadEstado;

    public LocalidadDTO(String localidadDescripcion) {
        this.localidadDescripcion = localidadDescripcion;
    }

    @Override
    public String toString(){
        return localidadDescripcion;
    }
}
