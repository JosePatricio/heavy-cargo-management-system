package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "visita")
public class Visita {
    private int visitaId;
    private int visitaClientePedidos;
    private String visitaVendedorUsuario;
    private int visitaEstado;
    private Timestamp visitaFecha;
    private Timestamp visitaFechaCreacion;
    private String visitaUsuarioCrea;
    @JsonIgnore private Collection<Pedido> pedidosByVisitaId;
    @JsonIgnore private ClientePedidos clientePedidosByVisitaClientePedidos;
    @JsonIgnore private Usuario usuarioByVisitaVendedorUsuario;
    @JsonIgnore private CatalogoItem catalogoItemByVisitaEstado;
    @JsonIgnore private Usuario usuarioByVisitaUsuarioCrea;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "visita_id")
    public int getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(int visitaId) {
        this.visitaId = visitaId;
    }

    @Basic
    @Column(name = "visita_cliente_pedidos")
    public int getVisitaClientePedidos() {
        return visitaClientePedidos;
    }

    public void setVisitaClientePedidos(int visitaClientePedidos) {
        this.visitaClientePedidos = visitaClientePedidos;
    }

    @Basic
    @Column(name = "visita_vendedor_usuario")
    public String getVisitaVendedorUsuario() {
        return visitaVendedorUsuario;
    }

    public void setVisitaVendedorUsuario(String visitaVendedorUsuario) {
        this.visitaVendedorUsuario = visitaVendedorUsuario;
    }

    @Basic
    @Column(name = "visita_estado")
    public int getVisitaEstado() {
        return visitaEstado;
    }

    public void setVisitaEstado(int visitaEstado) {
        this.visitaEstado = visitaEstado;
    }

    @Basic
    @Column(name = "visita_fecha")
    public Timestamp getVisitaFecha() {
        return visitaFecha;
    }

    public void setVisitaFecha(Timestamp visitaFecha) {
        this.visitaFecha = visitaFecha;
    }

    @Basic
    @Column(name = "visita_fecha_creacion")
    public Timestamp getVisitaFechaCreacion() {
        return visitaFechaCreacion;
    }

    public void setVisitaFechaCreacion(Timestamp visitaFechaCreacion) {
        this.visitaFechaCreacion = visitaFechaCreacion;
    }

    @Basic
    @Column(name = "visita_usuario_crea")
    public String getVisitaUsuarioCrea() {
        return visitaUsuarioCrea;
    }

    public void setVisitaUsuarioCrea(String visitaUsuarioCrea) {
        this.visitaUsuarioCrea = visitaUsuarioCrea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visita visita = (Visita) o;
        return visitaId == visita.visitaId &&
                visitaClientePedidos == visita.visitaClientePedidos &&
                visitaEstado == visita.visitaEstado &&
                Objects.equals(visitaVendedorUsuario, visita.visitaVendedorUsuario) &&
                Objects.equals(visitaFecha, visita.visitaFecha) &&
                Objects.equals(visitaFechaCreacion, visita.visitaFechaCreacion) &&
                Objects.equals(visitaUsuarioCrea, visita.visitaUsuarioCrea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitaId, visitaClientePedidos, visitaVendedorUsuario, visitaEstado, visitaFecha, visitaFechaCreacion, visitaUsuarioCrea);
    }

    @OneToMany(mappedBy = "visitaByPedidoVisitaId")
    public Collection<Pedido> getPedidosByVisitaId() {
        return pedidosByVisitaId;
    }

    public void setPedidosByVisitaId(Collection<Pedido> pedidosByVisitaId) {
        this.pedidosByVisitaId = pedidosByVisitaId;
    }

    @ManyToOne
    @JoinColumn(name = "visita_cliente_pedidos", referencedColumnName = "cliente_pedidos_id", nullable = false, insertable = false, updatable = false)
    public ClientePedidos getClientePedidosByVisitaClientePedidos() {
        return clientePedidosByVisitaClientePedidos;
    }

    public void setClientePedidosByVisitaClientePedidos(ClientePedidos clientePedidosByVisitaClientePedidos) {
        this.clientePedidosByVisitaClientePedidos = clientePedidosByVisitaClientePedidos;
    }

    @ManyToOne
    @JoinColumn(name = "visita_vendedor_usuario", referencedColumnName = "usuario_id_documento", nullable = false, insertable = false, updatable = false)
    public Usuario getUsuarioByVisitaVendedorUsuario() {
        return usuarioByVisitaVendedorUsuario;
    }

    public void setUsuarioByVisitaVendedorUsuario(Usuario usuarioByVisitaVendedorUsuario) {
        this.usuarioByVisitaVendedorUsuario = usuarioByVisitaVendedorUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "visita_estado", referencedColumnName = "catalogo_item_id", nullable = false, insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByVisitaEstado() {
        return catalogoItemByVisitaEstado;
    }

    public void setCatalogoItemByVisitaEstado(CatalogoItem catalogoItemByVisitaEstado) {
        this.catalogoItemByVisitaEstado = catalogoItemByVisitaEstado;
    }

    @ManyToOne
    @JoinColumn(name = "visita_usuario_crea", referencedColumnName = "usuario_id_documento", nullable = false, insertable = false, updatable = false)
    public Usuario getUsuarioByVisitaUsuarioCrea() {
        return usuarioByVisitaUsuarioCrea;
    }

    public void setUsuarioByVisitaUsuarioCrea(Usuario usuarioByVisitaUsuarioCrea) {
        this.usuarioByVisitaUsuarioCrea = usuarioByVisitaUsuarioCrea;
    }
}
