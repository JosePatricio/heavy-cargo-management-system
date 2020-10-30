package ec.redcode.tas.on.android.dto;

import java.sql.Timestamp;
import java.util.List;

public class SolicitudDTO {
    private String idSolicitud;
    private int idOrigen;
    private String origen;
    private int idDestino;
    private String destino;
    private Long fechaEnvio;
    private Long fechaCreacion;
    private Long fechaCaducidad;
    private String direccion;
    private String tipo;
    private int numeroPiezas;
    private Double volumen;
    private String tipoVolumen;
    private Double peso;
    private String tipoPeso;
    private int numeroEstibadores;
    private Long fechaEntrega;
    private String direccionEntrega;
    private String personaEntrega;
    private String personaRecibe;
    private Double bestOffer;
    private Double averageOffer;
    private long totalOffer;
    private Double maxValorOferta;
    private Double porcentajeAhorre;
    private int diasValidez;
    private int estado;
    private String comments;
    private String observaciones;
    private String usuario;
    private int diasPago;
    private OfertDTO offer;
    private List<OfertDTO> offers;

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public long getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(long fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(long fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(int numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public String getTipoVolumen() {
        return tipoVolumen;
    }

    public void setTipoVolumen(String tipoVolumen) {
        this.tipoVolumen = tipoVolumen;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getTipoPeso() {
        return tipoPeso;
    }

    public void setTipoPeso(String tipoPeso) {
        this.tipoPeso = tipoPeso;
    }

    public int getNumeroEstibadores() {
        return numeroEstibadores;
    }

    public void setNumeroEstibadores(int numeroEstibadores) {
        this.numeroEstibadores = numeroEstibadores;
    }

    public long getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(long fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getPersonaEntrega() {
        return personaEntrega;
    }

    public void setPersonaEntrega(String personaEntrega) {
        this.personaEntrega = personaEntrega;
    }

    public String getPersonaRecibe() {
        return personaRecibe;
    }

    public void setPersonaRecibe(String personaRecibe) {
        this.personaRecibe = personaRecibe;
    }

    public Double getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(Double bestOffer) {
        this.bestOffer = bestOffer;
    }

    public Double getAverageOffer() {
        return averageOffer;
    }

    public void setAverageOffer(Double averageOffer) {
        this.averageOffer = averageOffer;
    }

    public long getTotalOffer() {
        return totalOffer;
    }

    public void setTotalOffer(long totalOffer) {
        this.totalOffer = totalOffer;
    }

    public Double getMaxValorOferta() {
        return maxValorOferta;
    }

    public void setMaxValorOferta(Double maxValorOferta) {
        this.maxValorOferta = maxValorOferta;
    }

    public Double getPorcentajeAhorre() {
        return porcentajeAhorre;
    }

    public void setPorcentajeAhorre(Double porcentajeAhorre) {
        this.porcentajeAhorre = porcentajeAhorre;
    }

    public int getDiasValidez() {
        return diasValidez;
    }

    public void setDiasValidez(int diasValidez) {
        this.diasValidez = diasValidez;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getDiasPago() {
        return diasPago;
    }

    public void setDiasPago(int diasPago) {
        this.diasPago = diasPago;
    }

    public OfertDTO getOffer() {
        return offer;
    }

    public void setOffer(OfertDTO offer) {
        this.offer = offer;
    }

    public List<OfertDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<OfertDTO> offers) {
        this.offers = offers;
    }
}
