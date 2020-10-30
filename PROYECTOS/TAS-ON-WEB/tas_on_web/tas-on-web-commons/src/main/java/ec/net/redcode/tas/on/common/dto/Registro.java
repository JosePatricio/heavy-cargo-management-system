package ec.net.redcode.tas.on.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Registro<T, G> {
    private T valor;
    private G detalle;

    public Registro(T valor, G detalle){
        this.valor = valor;
        this.detalle = detalle;
    }

}
