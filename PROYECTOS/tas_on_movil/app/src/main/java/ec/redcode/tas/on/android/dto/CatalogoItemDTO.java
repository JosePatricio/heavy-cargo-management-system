package ec.redcode.tas.on.android.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Walter on 20/3/18.
 */

@Data @NoArgsConstructor
public class CatalogoItemDTO {
    private int catalogoItemId;
    private Integer catalogoItemIdCatalogo;
    private String catalogoItemDescripcion;
    private String catalogoItemValor;
    private Integer catalogoItemEstado;

    public CatalogoItemDTO(String catalogoItemDescripcion) {
        this.catalogoItemDescripcion = catalogoItemDescripcion;
    }

    @Override
    public String toString() {
        return catalogoItemDescripcion;
    }
}
