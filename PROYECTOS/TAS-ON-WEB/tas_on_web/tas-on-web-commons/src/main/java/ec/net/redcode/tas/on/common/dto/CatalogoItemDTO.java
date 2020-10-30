package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class CatalogoItemDTO {
    private Integer catalogoItemId;
    private Integer catalogoItemIdCatalogo;
    private String catalogoItemDescripcion;
    private String catalogoItemValor;
    private Integer catalogoItemEstado;
}
