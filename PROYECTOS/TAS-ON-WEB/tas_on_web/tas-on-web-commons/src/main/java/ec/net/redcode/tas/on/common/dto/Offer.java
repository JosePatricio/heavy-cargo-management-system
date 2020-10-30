package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Offer {

    private String idSolicitud;
    private Double amount;
    private String comments;
    private Timestamp date;
    private int idOferta;
    private int state;
    private int idConductor;
    private int idVehiculo;
    private String conductor;
    private String vehiculo;
    private String nroTransferencia;
    private String empresaTransportista;
    private List<Documents> fotos;
    private Double comision;
    private String correoContacto;
    private String telefonoContacto;

}
