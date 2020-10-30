package ec.net.redcode.tas.on.persistence.entities;

import ec.net.redcode.tas.on.persistence.component.JsonDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public class SolicitudEnvioCommon {

    protected String solicitudEnvioUsuarioId;
    protected Double solicitudEnvioPeso;
    protected Integer solicitudEnvioUnidadPeso;
    protected Double solicitudEnvioVolumen;
    protected Integer solicitudEnvioUnidadVolumen;
    protected Integer solicitudEnvioNumeroPiesas;
    protected String solicitudEnvioDireccionOrigen;
    protected Integer solicitudEnvioLocalidadOrigen;
    protected String solicitudEnvioDireccionDestino;
    protected Integer solicitudEnvioLocalidadDestino;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    protected Timestamp solicitudEnvioFechaRecoleccion;
    protected Integer solicitudEnvioNumeroEstibadores;
    protected Integer solicitudEnvioDiasValides;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    protected Timestamp solicitudEnvioFechaEntrega;
    protected Integer solicitudEnvioEstado;
    protected Integer solicitudEnvioOfertaId;
    protected String solicitudEnvioPersonaEntrega;
    protected String solicitudEnvioPersonaRecibe;
    protected Timestamp solicitudEnvioFechaCreacion;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    protected Timestamp solicitudEnvioFechaCaducidad;
    protected Timestamp solicitudEnvioFechaPago;
    protected Timestamp solicitudEnvioFechaFacturacion;
    protected String solicitudEnvioComentario;
    protected String solicitudEnvioObservaciones;
    protected String solicitudEnvioNumeroDocCliente;
    protected Double solicitudEnvioValorObjetivo;
    protected Double solicitudEnvioAhorroValorObjetivo;
    protected Integer solicitudEnvioTipoSubasta;

    @Basic
    @Column(name = "solicitud_envio_usuario_id")
    public String getSolicitudEnvioUsuarioId() {
        return solicitudEnvioUsuarioId;
    }

    public void setSolicitudEnvioUsuarioId(String solicitudEnvioUsuarioId) {
        this.solicitudEnvioUsuarioId = solicitudEnvioUsuarioId;
    }

    @Basic
    @Column(name = "solicitud_envio_peso")
    public Double getSolicitudEnvioPeso() {
        return solicitudEnvioPeso;
    }

    public void setSolicitudEnvioPeso(Double solicitudEnvioPeso) {
        this.solicitudEnvioPeso = solicitudEnvioPeso;
    }

    @Basic
    @Column(name = "solicitud_envio_unidad_peso")
    public Integer getSolicitudEnvioUnidadPeso() {
        return solicitudEnvioUnidadPeso;
    }

    public void setSolicitudEnvioUnidadPeso(Integer solicitudEnvioUnidadPeso) {
        this.solicitudEnvioUnidadPeso = solicitudEnvioUnidadPeso;
    }

    @Basic
    @Column(name = "solicitud_envio_volumen")
    public Double getSolicitudEnvioVolumen() {
        return solicitudEnvioVolumen;
    }

    public void setSolicitudEnvioVolumen(Double solicitudEnvioVolumen) {
        this.solicitudEnvioVolumen = solicitudEnvioVolumen;
    }

    @Basic
    @Column(name = "solicitud_envio_unidad_volumen")
    public Integer getSolicitudEnvioUnidadVolumen() {
        return solicitudEnvioUnidadVolumen;
    }

    public void setSolicitudEnvioUnidadVolumen(Integer solicitudEnvioUnidadVolumen) {
        this.solicitudEnvioUnidadVolumen = solicitudEnvioUnidadVolumen;
    }

    @Basic
    @Column(name = "solicitud_envio_numero_piesas")
    public Integer getSolicitudEnvioNumeroPiesas() {
        return solicitudEnvioNumeroPiesas;
    }

    public void setSolicitudEnvioNumeroPiesas(Integer solicitudEnvioNumeroPiesas) {
        this.solicitudEnvioNumeroPiesas = solicitudEnvioNumeroPiesas;
    }

    @Basic
    @Column(name = "solicitud_envio_direccion_origen")
    public String getSolicitudEnvioDireccionOrigen() {
        return solicitudEnvioDireccionOrigen;
    }

    public void setSolicitudEnvioDireccionOrigen(String solicitudEnvioDireccionOrigen) {
        this.solicitudEnvioDireccionOrigen = solicitudEnvioDireccionOrigen;
    }

    @Basic
    @Column(name = "solicitud_envio_localidad_origen")
    public Integer getSolicitudEnvioLocalidadOrigen() {
        return solicitudEnvioLocalidadOrigen;
    }

    public void setSolicitudEnvioLocalidadOrigen(Integer solicitudEnvioLocalidadOrigen) {
        this.solicitudEnvioLocalidadOrigen = solicitudEnvioLocalidadOrigen;
    }

    @Basic
    @Column(name = "solicitud_envio_direccion_destino")
    public String getSolicitudEnvioDireccionDestino() {
        return solicitudEnvioDireccionDestino;
    }

    public void setSolicitudEnvioDireccionDestino(String solicitudEnvioDireccionDestino) {
        this.solicitudEnvioDireccionDestino = solicitudEnvioDireccionDestino;
    }

    @Basic
    @Column(name = "solicitud_envio_localidad_destino")
    public Integer getSolicitudEnvioLocalidadDestino() {
        return solicitudEnvioLocalidadDestino;
    }

    public void setSolicitudEnvioLocalidadDestino(Integer solicitudEnvioLocalidadDestino) {
        this.solicitudEnvioLocalidadDestino = solicitudEnvioLocalidadDestino;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_recoleccion")
    public Timestamp getSolicitudEnvioFechaRecoleccion() {
        return solicitudEnvioFechaRecoleccion;
    }

    public void setSolicitudEnvioFechaRecoleccion(Timestamp solicitudEnvioFechaRecoleccion) {
        this.solicitudEnvioFechaRecoleccion = solicitudEnvioFechaRecoleccion;
    }

    @Basic
    @Column(name = "solicitud_envio_numero_estibadores")
    public Integer getSolicitudEnvioNumeroEstibadores() {
        return solicitudEnvioNumeroEstibadores;
    }

    public void setSolicitudEnvioNumeroEstibadores(Integer solicitudEnvioNumeroEstibadores) {
        this.solicitudEnvioNumeroEstibadores = solicitudEnvioNumeroEstibadores;
    }

    @Basic
    @Column(name = "solicitud_envio_dias_valides")
    public Integer getSolicitudEnvioDiasValides() {
        return solicitudEnvioDiasValides;
    }

    public void setSolicitudEnvioDiasValides(Integer solicitudEnvioDiasValides) {
        this.solicitudEnvioDiasValides = solicitudEnvioDiasValides;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_entrega")
    public Timestamp getSolicitudEnvioFechaEntrega() {
        return solicitudEnvioFechaEntrega;
    }

    public void setSolicitudEnvioFechaEntrega(Timestamp solicitudEnvioFechaEntrega) {
        this.solicitudEnvioFechaEntrega = solicitudEnvioFechaEntrega;
    }

    @Basic
    @Column(name = "solicitud_envio_estado")
    public Integer getSolicitudEnvioEstado() {
        return solicitudEnvioEstado;
    }

    public void setSolicitudEnvioEstado(Integer solicitudEnvioEstado) {
        this.solicitudEnvioEstado = solicitudEnvioEstado;
    }

    @Basic
    @Column(name = "solicitud_envio_oferta_id")
    public Integer getSolicitudEnvioOfertaId() {
        return solicitudEnvioOfertaId;
    }

    public void setSolicitudEnvioOfertaId(Integer solicitudEnvioOfertaId) {
        this.solicitudEnvioOfertaId = solicitudEnvioOfertaId;
    }

    @Basic
    @Column(name = "solicitud_envio_persona_entrega")
    public String getSolicitudEnvioPersonaEntrega() {
        return solicitudEnvioPersonaEntrega;
    }

    public void setSolicitudEnvioPersonaEntrega(String solicitudEnvioPersonaEntrega) {
        this.solicitudEnvioPersonaEntrega = solicitudEnvioPersonaEntrega;
    }


    @Basic
    @Column(name = "solicitud_envio_persona_recibe")
    public String getSolicitudEnvioPersonaRecibe() {
        return solicitudEnvioPersonaRecibe;
    }

    public void setSolicitudEnvioPersonaRecibe(String solicitudEnvioPersonaRecibe) {
        this.solicitudEnvioPersonaRecibe = solicitudEnvioPersonaRecibe;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_creacion")
    public Timestamp getSolicitudEnvioFechaCreacion() {
        return solicitudEnvioFechaCreacion;
    }

    public void setSolicitudEnvioFechaCreacion(Timestamp solicitudEnvioFechaCreacion) {
        this.solicitudEnvioFechaCreacion = solicitudEnvioFechaCreacion;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_caducidad")
    public Timestamp getSolicitudEnvioFechaCaducidad() {
        return solicitudEnvioFechaCaducidad;
    }

    public void setSolicitudEnvioFechaCaducidad(Timestamp solicitudEnvioFechaCaducidad) {
        this.solicitudEnvioFechaCaducidad = solicitudEnvioFechaCaducidad;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_pago")
    public Timestamp getSolicitudEnvioFechaPago() {
        return solicitudEnvioFechaPago;
    }

    public void setSolicitudEnvioFechaPago(Timestamp solicitudEnvioFechaPago) {
        this.solicitudEnvioFechaPago = solicitudEnvioFechaPago;
    }

    @Basic
    @Column(name = "solicitud_envio_fecha_facturacion")
    public Timestamp getSolicitudEnvioFechaFacturacion() {
        return solicitudEnvioFechaFacturacion;
    }

    public void setSolicitudEnvioFechaFacturacion(Timestamp solicitudEnvioFechaFacturacion) {
        this.solicitudEnvioFechaFacturacion = solicitudEnvioFechaFacturacion;
    }

    @Basic
    @Column(name = "solicitud_envio_comentario")
    public String getSolicitudEnvioComentario() {
        return solicitudEnvioComentario;
    }

    public void setSolicitudEnvioComentario(String solicitudEnvioComentario) {
        this.solicitudEnvioComentario = solicitudEnvioComentario;
    }

    @Basic
    @Column(name = "solicitud_envio_observaciones")
    public String getSolicitudEnvioObservaciones() {
        return solicitudEnvioObservaciones;
    }

    public void setSolicitudEnvioObservaciones(String solicitudEnvioObservaciones) {
        this.solicitudEnvioObservaciones = solicitudEnvioObservaciones;
    }

    @Column(name="solicitud_envio_numero_doc_cliente")
    public String getSolicitudEnvioNumeroDocCliente() {
        return solicitudEnvioNumeroDocCliente;
    }

    public void setSolicitudEnvioNumeroDocCliente(String solicitudEnvioNumeroDocCliente) {
        this.solicitudEnvioNumeroDocCliente = solicitudEnvioNumeroDocCliente;
    }

    @Column(name="solicitud_envio_valor_objetivo")
    public Double getSolicitudEnvioValorObjetivo() {
        return solicitudEnvioValorObjetivo;
    }

    public void setSolicitudEnvioValorObjetivo(Double solicitudEnvioValorObjetivo) {
        this.solicitudEnvioValorObjetivo = solicitudEnvioValorObjetivo;
    }

    @Column(name="solicitud_envio_ahorro_valor_objetivo")
    public Double getSolicitudEnvioAhorroValorObjetivo() {
        return solicitudEnvioAhorroValorObjetivo;
    }

    public void setSolicitudEnvioAhorroValorObjetivo(Double solicitudEnvioAhorroValorObjetivo) {
        this.solicitudEnvioAhorroValorObjetivo = solicitudEnvioAhorroValorObjetivo;
    }

    @Column(name="solicitud_envio_tipo_subasta")
    public Integer getSolicitudEnvioTipoSubasta() {
        return solicitudEnvioTipoSubasta;
    }

    public void setSolicitudEnvioTipoSubasta(Integer solicitudEnvioTipoSubasta) {
        this.solicitudEnvioTipoSubasta = solicitudEnvioTipoSubasta;
    }
}
