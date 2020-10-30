package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "oferta")
@NamedQueries({
        @NamedQuery(name = "Oferta.OfertaByUsuarioAndEstado", query = "select o from Oferta o " +
                "where o.ofertaIdUsuario = :usuario " +
                "and o.ofertaEstado = :estado"),
        @NamedQuery(name = "Oferta.OfertaBySolicitudAndEstado", query = "select o from Oferta o " +
                "where o.ofertaIdSolicitud = :idSolicitudEnvio and o.ofertaEstado = :estado order by o.ofertaValor"),
        @NamedQuery(name = "Oferta.OfertaBySolicitudUsuarioAndEstado", query = "select o from Oferta o " +
                "where o.ofertaIdSolicitud = :idSolicitud and o.ofertaIdUsuario = :usuario and (o.ofertaEstado = :estado or o.ofertaEstado = 34)"),
        @NamedQuery(name = "Oferta.OfertaBySolicitudUsuario", query = "select o from Oferta o " +
                "where o.ofertaIdSolicitud = :idSolicitud and o.ofertaIdUsuario = :usuario"),
        @NamedQuery(name = "Oferta.OfertaBySolicitudOrder", query = "select o from Oferta  o " +
                "where o.ofertaIdSolicitud = :idSolicitud order by o.ofertaValor asc"),
        @NamedQuery(name = "Oferta.OfertaByEstado", query = "select o from Oferta o where o.ofertaEstado = :estado"),
        @NamedQuery(name = "Oferta.OfertaByMax", query = "select MAX(o.ofertaValor) from Oferta o where o.ofertaIdSolicitud = :idSolicitud"),
        @NamedQuery(name = "Oferta.OfertaBySolicitudOfertas", query = "select o from Oferta o where o.ofertaIdSolicitud = :solicitud order by o.ofertaValor"),
        @NamedQuery(name = "Oferta.OfertaPorCobrar", query = "select o from Oferta o where o.ofertaEstado in (35, 84, 74) and o.ofertaIdUsuario = :usuario")
})

public class Oferta {
    private int ofertaId;
    private Double ofertaValor;
    private String ofertaObservacion;
    private String ofertaIdUsuario;
    private String ofertaIdSolicitud;
    private int ofertaIdConductor;
    private int ofertaIdVehiculo;
    private Timestamp ofertaFecha;
    private Timestamp ofertaFechaAceptada;
    private Timestamp ofertaFechaFinalizada;
    private Timestamp ofertaFechaRecepcion;
    private Timestamp ofertaFechaEntrega;
    private Timestamp ofertaFechaCancelada;
    private Integer ofertaEstado;
    private String ofertaNroTransferencial;
    private Double ofertaComision;
    private Double ofertaRetencion;
    private Double ofertaIva;
    private Double ofertaDescuento;
    @JsonIgnore private Usuario usuarioByOfertaIdConductor;
    @JsonIgnore private SolicitudEnvio solicitudEnvioByOfertaIdSolicitud;
    @JsonIgnore private CatalogoItem catalogoByOfertaEstado;
    @JsonIgnore private Collection<SolicitudEnvio> solicitudEnviosByOfertaId;
    @JsonIgnore private Collection<OfertaFile> fotos;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "oferta_id", nullable = false)
    public int getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(int ofertaId) {
        this.ofertaId = ofertaId;
    }

    @Basic
    @Column(name = "oferta_valor", nullable = true, precision = 0)
    public Double getOfertaValor() {
        return ofertaValor;
    }

    public void setOfertaValor(Double ofertaValor) {
        this.ofertaValor = ofertaValor;
    }

    @Basic
    @Column(name = "oferta_observacion", nullable = true, length = 100)
    public String getOfertaObservacion() {
        return ofertaObservacion;
    }

    public void setOfertaObservacion(String ofertaObservacion) {
        this.ofertaObservacion = ofertaObservacion;
    }

    @Basic
    @Column(name = "oferta_id_usuario", nullable = true, length = 13)
    public String getOfertaIdUsuario() {
        return ofertaIdUsuario;
    }

    public void setOfertaIdUsuario(String ofertaIdUsuario) {
        this.ofertaIdUsuario = ofertaIdUsuario;
    }

    @Basic
    @Column(name = "oferta_id_solicitud", nullable = true)
    public String getOfertaIdSolicitud() {
        return ofertaIdSolicitud;
    }

    public void setOfertaIdSolicitud(String ofertaIdSolicitud) {
        this.ofertaIdSolicitud = ofertaIdSolicitud;
    }

    @Basic
    @Column(name = "oferta_id_conductor", nullable = true)
    public int getOfertaIdConductor() {
        return ofertaIdConductor;
    }

    public void setOfertaIdConductor(int ofertaIdConductor) {
        this.ofertaIdConductor = ofertaIdConductor;
    }

    @Basic
    @Column(name = "oferta_id_vehiculo", nullable = true)
    public int getOfertaIdVehiculo() {
        return ofertaIdVehiculo;
    }

    public void setOfertaIdVehiculo(int ofertaIdVehiculo) {
        this.ofertaIdVehiculo = ofertaIdVehiculo;
    }

    @Basic
    @Column(name = "oferta_fecha", nullable = true)
    public Timestamp getOfertaFecha() {
        return ofertaFecha;
    }

    public void setOfertaFecha(Timestamp ofertaFecha) {
        this.ofertaFecha = ofertaFecha;
    }

    @Basic
    @Column(name = "oferta_fecha_aceptada", nullable = true)
    public Timestamp getOfertaFechaAceptada() {
        return ofertaFechaAceptada;
    }

    public void setOfertaFechaAceptada(Timestamp ofertaFechaAceptada) {
        this.ofertaFechaAceptada = ofertaFechaAceptada;
    }

    @Basic
    @Column(name = "oferta_fecha_finalizada", nullable = true)
    public Timestamp getOfertaFechaFinalizada() {
        return ofertaFechaFinalizada;
    }

    public void setOfertaFechaFinalizada(Timestamp ofertaFechaFinalizada) {
        this.ofertaFechaFinalizada = ofertaFechaFinalizada;
    }

    @Basic
    @Column(name = "oferta_fecha_recepcion", nullable = true)
    public Timestamp getOfertaFechaRecepcion() {
        return ofertaFechaRecepcion;
    }

    public void setOfertaFechaRecepcion(Timestamp ofertaFechaRecepcion) {
        this.ofertaFechaRecepcion = ofertaFechaRecepcion;
    }

    @Basic
    @Column(name = "oferta_fecha_entrega", nullable = true)
    public Timestamp getOfertaFechaEntrega() {
        return ofertaFechaEntrega;
    }

    public void setOfertaFechaEntrega(Timestamp ofertaFechaEntrega) {
        this.ofertaFechaEntrega = ofertaFechaEntrega;
    }

    @Basic
    @Column(name = "oferta_fecha_cancelada", nullable = true)
    public Timestamp getOfertaFechaCancelada() {
        return ofertaFechaCancelada;
    }

    public void setOfertaFechaCancelada(Timestamp ofertaFechaCancelada) {
        this.ofertaFechaCancelada = ofertaFechaCancelada;
    }

    @Basic
    @Column(name = "oferta_estado", nullable = true)
    public Integer getOfertaEstado() {
        return ofertaEstado;
    }

    public void setOfertaEstado(Integer ofertaEstado) {
        this.ofertaEstado = ofertaEstado;
    }

    @Basic
    @Column(name = "oferta_nro_transferencia", nullable = true, length = 30)
    public String getOfertaNroTransferencial() {
        return ofertaNroTransferencial;
    }

    public void setOfertaNroTransferencial(String ofertaNroTransferencial) {
        this.ofertaNroTransferencial = ofertaNroTransferencial;
    }

    @Basic
    @Column(name = "oferta_comision", nullable = true)
    public Double getOfertaComision() {
        return ofertaComision;
    }

    public void setOfertaComision(Double ofertaComision) {
        this.ofertaComision = ofertaComision;
    }

    @Basic
    @Column(name = "oferta_retencion", nullable = true)
    public Double getOfertaRetencion() {
        return ofertaRetencion;
    }

    public void setOfertaRetencion(Double ofertaRetencion) {
        this.ofertaRetencion = ofertaRetencion;
    }

    @Basic
    @Column(name = "oferta_iva", nullable = true)
    public Double getOfertaIva() {
        return ofertaIva;
    }

    public void setOfertaIva(Double ofertaIva) {
        this.ofertaIva = ofertaIva;
    }

    @Basic
    @Column(name = "oferta_descuento", nullable = true)
    public Double getOfertaDescuento() {
        return ofertaDescuento;
    }

    public void setOfertaDescuento(Double ofertaDescuento) {
        this.ofertaDescuento = ofertaDescuento;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ofertaId, ofertaValor, ofertaObservacion, ofertaIdConductor, ofertaIdSolicitud, ofertaFecha, ofertaEstado);
    }

    @ManyToOne
    @JoinColumn(name = "oferta_id_usuario", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByOfertaIdConductor() {
        return usuarioByOfertaIdConductor;
    }

    public void setUsuarioByOfertaIdConductor(Usuario usuarioByOfertaIdConductor) {
        this.usuarioByOfertaIdConductor = usuarioByOfertaIdConductor;
    }

    @ManyToOne
    @JoinColumn(name = "oferta_id_solicitud", referencedColumnName = "solicitud_envio_id", insertable = false, updatable = false)
    public SolicitudEnvio getSolicitudEnvioByOfertaIdSolicitud() {
        return solicitudEnvioByOfertaIdSolicitud;
    }

    public void setSolicitudEnvioByOfertaIdSolicitud(SolicitudEnvio solicitudEnvioByOfertaIdSolicitud) {
        this.solicitudEnvioByOfertaIdSolicitud = solicitudEnvioByOfertaIdSolicitud;
    }

    @ManyToOne
    @JoinColumn(name = "oferta_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoByOfertaEstado() {
        return catalogoByOfertaEstado;
    }

    public void setCatalogoByOfertaEstado(CatalogoItem catalogoByOfertaEstado) {
        this.catalogoByOfertaEstado = catalogoByOfertaEstado;
    }

    @OneToMany(mappedBy = "ofertaBySolicitudEnvioOfertaId")
    public Collection<SolicitudEnvio> getSolicitudEnviosByOfertaId() {
        return solicitudEnviosByOfertaId;
    }

    public void setSolicitudEnviosByOfertaId(Collection<SolicitudEnvio> solicitudEnviosByOfertaId) {
        this.solicitudEnviosByOfertaId = solicitudEnviosByOfertaId;
    }

    @OneToMany(mappedBy = "ofertaByFoto")
    public Collection<OfertaFile> getFotos() {
        return fotos;
    }

    public void setFotos(Collection<OfertaFile> fotos) {
        this.fotos = fotos;
    }
}
