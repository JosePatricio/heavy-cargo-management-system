package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "pedido")
public class Pedido {
    private int pedidoId;
    private Timestamp pedidoFechaEntregaDesde;
    private Timestamp pedidoFechaEntregaHasta;
    private String pedidoPersonaRecibe;
    private Integer pedidoSolicitaCredito;
    private Double pedidoTotal;
    private String pedidoLat;
    private String pedidoLng;
    private int pedidoVisitaId;
    private Timestamp pedidoFechaCreacion;
    private String pedidoUsuarioCrea;
    @JsonIgnore private Visita visitaByPedidoVisitaId;
    @JsonIgnore private Usuario usuarioByPedidoUsuarioCrea;
    @JsonIgnore private Collection<PedidoDetalle> pedidoDetallesByPedidoId;
    @JsonIgnore private Collection<PedidoDocumentosCredito> pedidoDocumentosCreditosByPedidoId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "pedido_id")
    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    @Basic
    @Column(name = "pedido_fecha_entrega_desde")
    public Timestamp getPedidoFechaEntregaDesde() {
        return pedidoFechaEntregaDesde;
    }

    public void setPedidoFechaEntregaDesde(Timestamp pedidoFechaEntregaDesde) {
        this.pedidoFechaEntregaDesde = pedidoFechaEntregaDesde;
    }

    @Basic
    @Column(name = "pedido_fecha_entrega_hasta")
    public Timestamp getPedidoFechaEntregaHasta() {
        return pedidoFechaEntregaHasta;
    }

    public void setPedidoFechaEntregaHasta(Timestamp pedidoFechaEntregaHasta) {
        this.pedidoFechaEntregaHasta = pedidoFechaEntregaHasta;
    }

    @Basic
    @Column(name = "pedido_persona_recibe")
    public String getPedidoPersonaRecibe() {
        return pedidoPersonaRecibe;
    }

    public void setPedidoPersonaRecibe(String pedidoPersonaRecibe) {
        this.pedidoPersonaRecibe = pedidoPersonaRecibe;
    }

    @Basic
    @Column(name = "pedido_solicita_credito")
    public Integer getPedidoSolicitaCredito() {
        return pedidoSolicitaCredito;
    }

    public void setPedidoSolicitaCredito(Integer pedidoSolicitaCredito) {
        this.pedidoSolicitaCredito = pedidoSolicitaCredito;
    }

    @Basic
    @Column(name = "pedido_total")
    public Double getPedidoTotal() {
        return pedidoTotal;
    }

    public void setPedidoTotal(Double pedidoTotal) {
        this.pedidoTotal = pedidoTotal;
    }

    @Basic
    @Column(name = "pedido_lat")
    public String getPedidoLat() {
        return pedidoLat;
    }

    public void setPedidoLat(String pedidoLat) {
        this.pedidoLat = pedidoLat;
    }

    @Basic
    @Column(name = "pedido_lng")
    public String getPedidoLng() {
        return pedidoLng;
    }

    public void setPedidoLng(String pedidoLng) {
        this.pedidoLng = pedidoLng;
    }

    @Basic
    @Column(name = "pedido_visita_id")
    public int getPedidoVisitaId() {
        return pedidoVisitaId;
    }

    public void setPedidoVisitaId(int pedidoVisitaId) {
        this.pedidoVisitaId = pedidoVisitaId;
    }

    @Basic
    @Column(name = "pedido_fecha_creacion")
    public Timestamp getPedidoFechaCreacion() {
        return pedidoFechaCreacion;
    }

    public void setPedidoFechaCreacion(Timestamp pedidoFechaCreacion) {
        this.pedidoFechaCreacion = pedidoFechaCreacion;
    }

    @Basic
    @Column(name = "pedido_usuario_crea")
    public String getPedidoUsuarioCrea() {
        return pedidoUsuarioCrea;
    }

    public void setPedidoUsuarioCrea(String pedidoUsuarioCrea) {
        this.pedidoUsuarioCrea = pedidoUsuarioCrea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return pedidoId == pedido.pedidoId &&
                pedidoVisitaId == pedido.pedidoVisitaId &&
                Objects.equals(pedidoFechaEntregaDesde, pedido.pedidoFechaEntregaDesde) &&
                Objects.equals(pedidoFechaEntregaHasta, pedido.pedidoFechaEntregaHasta) &&
                Objects.equals(pedidoPersonaRecibe, pedido.pedidoPersonaRecibe) &&
                Objects.equals(pedidoSolicitaCredito, pedido.pedidoSolicitaCredito) &&
                Objects.equals(pedidoTotal, pedido.pedidoTotal) &&
                Objects.equals(pedidoLat, pedido.pedidoLat) &&
                Objects.equals(pedidoLng, pedido.pedidoLng) &&
                Objects.equals(pedidoFechaCreacion, pedido.pedidoFechaCreacion) &&
                Objects.equals(pedidoUsuarioCrea, pedido.pedidoUsuarioCrea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, pedidoFechaEntregaDesde, pedidoFechaEntregaHasta, pedidoPersonaRecibe, pedidoSolicitaCredito, pedidoTotal, pedidoLat, pedidoLng, pedidoVisitaId, pedidoFechaCreacion, pedidoUsuarioCrea);
    }

    @ManyToOne
    @JoinColumn(name = "pedido_visita_id", referencedColumnName = "visita_id", nullable = false, insertable = false, updatable = false)
    public Visita getVisitaByPedidoVisitaId() {
        return visitaByPedidoVisitaId;
    }

    public void setVisitaByPedidoVisitaId(Visita visitaByPedidoVisitaId) {
        this.visitaByPedidoVisitaId = visitaByPedidoVisitaId;
    }

    @ManyToOne
    @JoinColumn(name = "pedido_usuario_crea", referencedColumnName = "usuario_id_documento", nullable = false, insertable = false, updatable = false)
    public Usuario getUsuarioByPedidoUsuarioCrea() {
        return usuarioByPedidoUsuarioCrea;
    }

    public void setUsuarioByPedidoUsuarioCrea(Usuario usuarioByPedidoUsuarioCrea) {
        this.usuarioByPedidoUsuarioCrea = usuarioByPedidoUsuarioCrea;
    }

    @OneToMany(mappedBy = "pedidoByPedidoDetallePedidoId")
    public Collection<PedidoDetalle> getPedidoDetallesByPedidoId() {
        return pedidoDetallesByPedidoId;
    }

    public void setPedidoDetallesByPedidoId(Collection<PedidoDetalle> pedidoDetallesByPedidoId) {
        this.pedidoDetallesByPedidoId = pedidoDetallesByPedidoId;
    }

    @OneToMany(mappedBy = "pedidoByPedidoDocumentosCreditoPedidoId")
    public Collection<PedidoDocumentosCredito> getPedidoDocumentosCreditosByPedidoId() {
        return pedidoDocumentosCreditosByPedidoId;
    }

    public void setPedidoDocumentosCreditosByPedidoId(Collection<PedidoDocumentosCredito> pedidoDocumentosCreditosByPedidoId) {
        this.pedidoDocumentosCreditosByPedidoId = pedidoDocumentosCreditosByPedidoId;
    }
}
