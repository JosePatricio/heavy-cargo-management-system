package ec.net.redcode.tas.on.rest.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Solicitudes {

    private String idSolicitud;
    private int idOrigen;
    private String origen;
    private String provinciaOrigen;
    private int idDestino;
    private String destino;
    private String provinciaDestino;
    private Double peso;
    private String medida;
    private int dias; //TODO eliminar cuando se lance la nueva app movil
    private int numeroPiezas;
    private Double ofertaValor;
    private Timestamp fechaEntrega;
    private Timestamp fechaRecepcion;
    private Timestamp fechaFacturacion;
    private Timestamp fechaCancelada;
    private String cliente;
    private String transportista;
    private String facturaId;
    private Double saldo;
    private int diasVencidaFactura;
    private int diasPagos;
    private Double comision;
    private String personaRecibe;
    private String numeroDocCliente;
    private Double descuento;

}
