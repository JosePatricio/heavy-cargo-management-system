package ec.net.redcode.tas.on.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DireccionDTO {
    private String persona;
    private String direccion;

    public DireccionDTO(String persona, String direccion){
        this.persona = persona;
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DireccionDTO)
            return this.persona.equals(((DireccionDTO) obj).getPersona());
        return false;
    }

}
