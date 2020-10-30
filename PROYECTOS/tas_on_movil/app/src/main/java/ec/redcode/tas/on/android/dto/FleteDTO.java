package ec.redcode.tas.on.android.dto;

import lombok.Data;

/**
 * Created by User on 29/12/2017.
 */

@Data
public class FleteDTO {

    private String idSolicitud;
    private String idOrigen;
    private String origen;
    private String idDestino;
    private String destino;
    private String fechaEnvio;
    private String direccion;
    private String tipo;
    private String numeroPiezas;
    private String volumen;
    private String tipoVolumen;
    private String peso;
    private String tipoPeso;
    private String numeroEstibadores;
    private String fechaEntrega;
    private String direccionEntrega;
    private String personaEntrega;
    private String personaRecibe;
    private String bestOffer;
    private String averageOffer;
    private String totalOffer;
    private Integer diasPago;
    private String telefonoContacto;
    private String correoContacto;
    private OfertDTO offer;
    private String observaciones;

}
