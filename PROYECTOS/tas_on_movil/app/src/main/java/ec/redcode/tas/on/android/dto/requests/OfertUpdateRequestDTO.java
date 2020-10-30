package ec.redcode.tas.on.android.dto.requests;

import java.util.List;

import lombok.Data;

@Data
public class OfertUpdateRequestDTO {

    private String idOferta;
    private String idConductor;
    private String idVehiculo;
    private String state;
    private String idSolicitud;
    private String amount;
    private String comments;
    private List<DocumentRequestDTO> fotos;

}
