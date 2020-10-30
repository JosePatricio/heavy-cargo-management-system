package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstadisticaHome {
    private double ahorroGenerado;
    private double toneladasTrasladadas;
    private int destinos;
    private LocalDate fechaConsulta;
}
