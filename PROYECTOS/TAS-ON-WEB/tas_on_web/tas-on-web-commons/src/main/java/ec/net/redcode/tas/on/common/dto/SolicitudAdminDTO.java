package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SolicitudAdminDTO {
    private String idSolicitud;
    private String cliente;
    private double peso;

    private String unidadPeso;
    private double volumen;
    private String unidadVolumen;

    private int piezas;
    private String tipoProducto;
    private int estibadores;

    private String origen;
    private String destino;
    private Timestamp fechaCreacion;

    private Timestamp fechaCaducidad;
    private Timestamp fechaRecoleccion;
    private Timestamp fechaEntrega;

    private String observaciones;
    private int ofertasRecibidas;
    private Double valorMaximaOferta;

    private Double valorMinimaOferta;
    private Double valorOfertaGanadora;
    private String transportistaGanador;

    private String estado;
}
