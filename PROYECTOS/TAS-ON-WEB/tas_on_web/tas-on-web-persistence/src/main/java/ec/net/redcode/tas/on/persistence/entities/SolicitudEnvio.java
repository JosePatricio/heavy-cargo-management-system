package ec.net.redcode.tas.on.persistence.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "solicitud_envio")
@NamedQueries({
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByOrigenDestino", query = "select s from SolicitudEnvio s " +
                "where s.solicitudEnvioLocalidadOrigen = :solicitudEnvioLocalidadOrigen " +
                "and s.solicitudEnvioLocalidadDestino = :solicitudEnvioLocalidadDestino " +
                "and s.solicitudEnvioEstado = :solicitudEnvioEstado"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioBySolicitudEnvioOferta", query = "select s from SolicitudEnvio s " +
                "where s.solicitudEnvioOfertaId = :solicitudEnvioOfertaId " +
                "and s.solicitudEnvioEstado = :solicitudEnvioEstado"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioBySolicitudEstado", query = "select s from SolicitudEnvio s " +
                "where s.solicitudEnvioEstado = :solicitudEnvioEstado order by s.solicitudEnvioFechaCreacion desc"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByEstadoAndFechaEntrega", query = "select s from SolicitudEnvio s where s.solicitudEnvioEstado = :estado " +
                "and s.solicitudEnvioFechaEntrega between :fechaInicio and :fechaFin"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByEstadoAndFechaCaducidad", query = "select s from SolicitudEnvio s " +
                "where s.solicitudEnvioEstado = :solicitudEnvioEstado and s.solicitudEnvioFechaCaducidad >= current_date order by s.solicitudEnvioFechaCreacion desc"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByFechaCaducidad", query = "select s from SolicitudEnvio s where s.solicitudEnvioFechaCaducidad <= current_date " +
                "order by s.solicitudEnvioFechaCreacion desc"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByFechaCaducidadNowEstado", query = "SELECT s from SolicitudEnvio s where s.solicitudEnvioFechaCaducidad <= current_timestamp " +
                "and s.solicitudEnvioEstado = :estado"),
        @NamedQuery(name = "SolicitudEnvio.SolicitudEnvioByOferta", query = "select s from SolicitudEnvio s where s.solicitudEnvioOfertaId = :oferta")
  })

public class SolicitudEnvio extends SolicitudEnvioCommon {

    private String solicitudEnvioId;
    @Ignore private Collection<Contrato> contratoesBySolicitudEnvioId;
    @Ignore private Collection<Oferta> ofertasBySolicitudEnvioId;
    @Ignore private CatalogoItem catalogoItemBySolicitudEnvioUnidadPeso;
    @Ignore private CatalogoItem catalogoItemBySolicitudEnvioUnidadVolumen;
    @Ignore private Localidad localidadBySolicitudEnvioLocalidadOrigen;
    @Ignore private Localidad localidadBySolicitudEnvioLocalidadDestino;
    @Ignore private CatalogoItem catalogoBySolicitudEnvioEstado;
    @Ignore private Oferta ofertaBySolicitudEnvioOfertaId;
    @Ignore private Usuario usuarioBySolicitudEnvioUsuarioId;
    @Ignore private CatalogoItem catalogoItemBySolicitudEnvioTipoSubasta;

    @Id
    @Column(name = "solicitud_envio_id", nullable = false)
    public String getSolicitudEnvioId() {
        return solicitudEnvioId;
    }

    public void setSolicitudEnvioId(String solicitudEnvioId) {
        this.solicitudEnvioId = solicitudEnvioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitudEnvio that = (SolicitudEnvio) o;
        return Objects.equals(solicitudEnvioId, that.solicitudEnvioId) &&
                Objects.equals(solicitudEnvioUsuarioId, that.solicitudEnvioUsuarioId) &&
                Objects.equals(solicitudEnvioPeso, that.solicitudEnvioPeso) &&
                Objects.equals(solicitudEnvioUnidadPeso, that.solicitudEnvioUnidadPeso) &&
                Objects.equals(solicitudEnvioVolumen, that.solicitudEnvioVolumen) &&
                Objects.equals(solicitudEnvioUnidadVolumen, that.solicitudEnvioUnidadVolumen) &&
                Objects.equals(solicitudEnvioNumeroPiesas, that.solicitudEnvioNumeroPiesas) &&
                Objects.equals(solicitudEnvioDireccionOrigen, that.solicitudEnvioDireccionOrigen) &&
                Objects.equals(solicitudEnvioLocalidadOrigen, that.solicitudEnvioLocalidadOrigen) &&
                Objects.equals(solicitudEnvioDireccionDestino, that.solicitudEnvioDireccionDestino) &&
                Objects.equals(solicitudEnvioLocalidadDestino, that.solicitudEnvioLocalidadDestino) &&
                Objects.equals(solicitudEnvioFechaRecoleccion, that.solicitudEnvioFechaRecoleccion) &&
                Objects.equals(solicitudEnvioNumeroEstibadores, that.solicitudEnvioNumeroEstibadores) &&
                Objects.equals(solicitudEnvioDiasValides, that.solicitudEnvioDiasValides) &&
                Objects.equals(solicitudEnvioFechaEntrega, that.solicitudEnvioFechaEntrega) &&
                Objects.equals(solicitudEnvioEstado, that.solicitudEnvioEstado) &&
                Objects.equals(solicitudEnvioOfertaId, that.solicitudEnvioOfertaId) &&
                Objects.equals(solicitudEnvioPersonaEntrega, that.solicitudEnvioPersonaEntrega) &&
                Objects.equals(solicitudEnvioPersonaRecibe, that.solicitudEnvioPersonaRecibe) &&
                Objects.equals(solicitudEnvioFechaCreacion, that.solicitudEnvioFechaCreacion) &&
                Objects.equals(solicitudEnvioFechaCaducidad, that.solicitudEnvioFechaCaducidad) &&
                Objects.equals(solicitudEnvioFechaPago, that.solicitudEnvioFechaPago) &&
                Objects.equals(solicitudEnvioFechaFacturacion, that.solicitudEnvioFechaFacturacion) &&
                Objects.equals(solicitudEnvioComentario, that.solicitudEnvioComentario) &&
                Objects.equals(solicitudEnvioObservaciones, that.solicitudEnvioObservaciones) &&
                Objects.equals(contratoesBySolicitudEnvioId, that.contratoesBySolicitudEnvioId) &&
                Objects.equals(ofertasBySolicitudEnvioId, that.ofertasBySolicitudEnvioId) &&
                Objects.equals(catalogoItemBySolicitudEnvioUnidadPeso, that.catalogoItemBySolicitudEnvioUnidadPeso) &&
                Objects.equals(catalogoItemBySolicitudEnvioUnidadVolumen, that.catalogoItemBySolicitudEnvioUnidadVolumen) &&
                Objects.equals(localidadBySolicitudEnvioLocalidadOrigen, that.localidadBySolicitudEnvioLocalidadOrigen) &&
                Objects.equals(localidadBySolicitudEnvioLocalidadDestino, that.localidadBySolicitudEnvioLocalidadDestino) &&
                Objects.equals(catalogoBySolicitudEnvioEstado, that.catalogoBySolicitudEnvioEstado) &&
                Objects.equals(ofertaBySolicitudEnvioOfertaId, that.ofertaBySolicitudEnvioOfertaId) &&
                Objects.equals(usuarioBySolicitudEnvioUsuarioId, that.usuarioBySolicitudEnvioUsuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solicitudEnvioId, solicitudEnvioUsuarioId, solicitudEnvioPeso, solicitudEnvioUnidadPeso, solicitudEnvioVolumen,
                solicitudEnvioUnidadVolumen, solicitudEnvioNumeroPiesas, solicitudEnvioDireccionOrigen, solicitudEnvioLocalidadOrigen,
                solicitudEnvioDireccionDestino, solicitudEnvioLocalidadDestino, solicitudEnvioFechaRecoleccion, solicitudEnvioNumeroEstibadores,
                solicitudEnvioDiasValides, solicitudEnvioFechaEntrega, solicitudEnvioEstado, solicitudEnvioOfertaId, solicitudEnvioPersonaEntrega,
                solicitudEnvioPersonaRecibe, solicitudEnvioFechaCreacion, solicitudEnvioFechaCaducidad, solicitudEnvioFechaPago,
                solicitudEnvioFechaFacturacion, solicitudEnvioComentario, solicitudEnvioObservaciones, contratoesBySolicitudEnvioId,
                ofertasBySolicitudEnvioId, catalogoItemBySolicitudEnvioUnidadPeso, catalogoItemBySolicitudEnvioUnidadVolumen,
                localidadBySolicitudEnvioLocalidadOrigen, localidadBySolicitudEnvioLocalidadDestino, catalogoBySolicitudEnvioEstado,
                ofertaBySolicitudEnvioOfertaId, usuarioBySolicitudEnvioUsuarioId);
    }

    @OneToMany(mappedBy = "solicitudEnvioByContratoIdSolicitud")
    public Collection<Contrato> getContratoesBySolicitudEnvioId() {
        return contratoesBySolicitudEnvioId;
    }

    public void setContratoesBySolicitudEnvioId(Collection<Contrato> contratoesBySolicitudEnvioId) {
        this.contratoesBySolicitudEnvioId = contratoesBySolicitudEnvioId;
    }

    @OneToMany(mappedBy = "solicitudEnvioByOfertaIdSolicitud")
    public Collection<Oferta> getOfertasBySolicitudEnvioId() {
        return ofertasBySolicitudEnvioId;
    }

    public void setOfertasBySolicitudEnvioId(Collection<Oferta> ofertasBySolicitudEnvioId) {
        this.ofertasBySolicitudEnvioId = ofertasBySolicitudEnvioId;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_unidad_peso", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemBySolicitudEnvioUnidadPeso() {
        return catalogoItemBySolicitudEnvioUnidadPeso;
    }

    public void setCatalogoItemBySolicitudEnvioUnidadPeso(CatalogoItem catalogoItemBySolicitudEnvioUnidadPeso) {
        this.catalogoItemBySolicitudEnvioUnidadPeso = catalogoItemBySolicitudEnvioUnidadPeso;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_unidad_volumen", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemBySolicitudEnvioUnidadVolumen() {
        return catalogoItemBySolicitudEnvioUnidadVolumen;
    }

    public void setCatalogoItemBySolicitudEnvioUnidadVolumen(CatalogoItem catalogoItemBySolicitudEnvioUnidadVolumen) {
        this.catalogoItemBySolicitudEnvioUnidadVolumen = catalogoItemBySolicitudEnvioUnidadVolumen;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_tipo_subasta", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemBySolicitudEnvioTipoSubasta() {
        return catalogoItemBySolicitudEnvioTipoSubasta;
    }

    public void setCatalogoItemBySolicitudEnvioTipoSubasta(CatalogoItem catalogoItemBySolicitudEnvioTipoSubasta) {
        this.catalogoItemBySolicitudEnvioTipoSubasta = catalogoItemBySolicitudEnvioTipoSubasta;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_localidad_origen", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadBySolicitudEnvioLocalidadOrigen() {
        return localidadBySolicitudEnvioLocalidadOrigen;
    }

    public void setLocalidadBySolicitudEnvioLocalidadOrigen(Localidad localidadBySolicitudEnvioLocalidadOrigen) {
        this.localidadBySolicitudEnvioLocalidadOrigen = localidadBySolicitudEnvioLocalidadOrigen;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_localidad_destino", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadBySolicitudEnvioLocalidadDestino() {
        return localidadBySolicitudEnvioLocalidadDestino;
    }

    public void setLocalidadBySolicitudEnvioLocalidadDestino(Localidad localidadBySolicitudEnvioLocalidadDestino) {
        this.localidadBySolicitudEnvioLocalidadDestino = localidadBySolicitudEnvioLocalidadDestino;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoBySolicitudEnvioEstado() {
        return catalogoBySolicitudEnvioEstado;
    }

    public void setCatalogoBySolicitudEnvioEstado(CatalogoItem catalogoBySolicitudEnvioEstado) {
        this.catalogoBySolicitudEnvioEstado = catalogoBySolicitudEnvioEstado;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_oferta_id", referencedColumnName = "oferta_id", insertable = false, updatable = false)
    public Oferta getOfertaBySolicitudEnvioOfertaId() {
        return ofertaBySolicitudEnvioOfertaId;
    }

    public void setOfertaBySolicitudEnvioOfertaId(Oferta ofertaBySolicitudEnvioOfertaId) {
        this.ofertaBySolicitudEnvioOfertaId = ofertaBySolicitudEnvioOfertaId;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_envio_usuario_id", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioBySolicitudEnvioUsuarioId() {
        return usuarioBySolicitudEnvioUsuarioId;
    }

    public void setUsuarioBySolicitudEnvioUsuarioId(Usuario usuarioBySolicitudEnvioUsuarioId) {
        this.usuarioBySolicitudEnvioUsuarioId = usuarioBySolicitudEnvioUsuarioId;
    }
}
