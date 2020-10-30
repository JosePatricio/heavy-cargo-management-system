package ec.net.redcode.tas.on.rest.dto;

import ec.net.redcode.tas.on.common.dto.Offer;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Solicitud {

    private String idSolicitud;
    private String numeroDocCliente;
    private String tipo;
    private int numeroPiezas;
    private Double volumen;
    private String tipoVolumen;
    private Double peso;
    private String tipoPeso;
    private int numeroEstibadores;
    private String observaciones;
    private int estado;

    private int idOrigen;
    private String origen;
    private String provinciaOrigen;
    private String direccion;
    private String personaEntrega;
    private String telefonoContacto;
    private String correoContacto;
    private Timestamp fechaEnvio;
    private List<Integer> fotosRecoleccion;

    private int idDestino;
    private String destino;
    private String provinciaDestino;
    private String direccionEntrega;
    private String personaRecibe;
    private Timestamp fechaEntrega;
    private List<Integer> fotosEntrega;

    private Timestamp fechaCreacion;
    private Timestamp fechaCaducidad;

    private long totalOffer;
    private Double bestOffer;
    private Double averageOffer;
    private Double maxValorOferta;
    private Double porcentajeAhorro;
    private Offer offer;
    private List<Offer> offers;

    private int diasValidez;
    private String usuario;
    private int diasPago;
    private String comments;

    private Integer tipoSubasta;
    private String tipoSubastaDescripcion;
    private Double valorObjetivo;

}
