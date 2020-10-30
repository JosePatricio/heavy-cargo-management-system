package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class FotoEnvioDTO {
    private Integer envioId;
    private List<Documents> fotos;
    private Boolean estadoCompletado;
    private Double pago;
}
