package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class AgendarVisitaDTO {

    private Timestamp fechaVisita;
    private List<Integer> clientesId;

}
