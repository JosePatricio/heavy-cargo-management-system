package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "cliente_pedidos")
public class ClientePedidos {
    private int clientePedidosId;
    private String clientePedidosRuc;
    private String clientePedidosRazonSocial;
    private String clientePedidosNombre;
    private String clientePedidosTelefono;
    private String clientePedidosCorreo;
    private Integer clientePedidosLocalidadId;
    private String clientePedidosLat;
    private String clientePedidosLng;
    private String clientePedidosDireccion;
    private Integer clientePedidosFotoId;
    private Timestamp clientePedidosFechaCreacion;
    private String clientePedidosUsuarioCrea;
    private int clientePedidosEstado;
    private String clientePedidosVendedorAsignado;
    private Integer clientePedidosDiaSemanaVisita;
    @JsonIgnore private Localidad localidadByClientePedidosLocalidadId;
    @JsonIgnore private File fileByClientePedidosFotoId;
    @JsonIgnore private Usuario usuarioByClientePedidosUsuarioCrea;
    @JsonIgnore private Collection<Visita> visitasByClientePedidosId;
    @JsonIgnore private Usuario usuarioByClientePedidosVendedorAsignado;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "cliente_pedidos_id")
    public int getClientePedidosId() {
        return clientePedidosId;
    }

    public void setClientePedidosId(int clientePedidosId) {
        this.clientePedidosId = clientePedidosId;
    }

    @Basic
    @Column(name = "cliente_pedidos_ruc")
    public String getClientePedidosRuc() {
        return clientePedidosRuc;
    }

    public void setClientePedidosRuc(String clientePedidosRuc) {
        this.clientePedidosRuc = clientePedidosRuc;
    }

    @Basic
    @Column(name = "cliente_pedidos_razon_social")
    public String getClientePedidosRazonSocial() {
        return clientePedidosRazonSocial;
    }

    public void setClientePedidosRazonSocial(String clientePedidosRazonSocial) {
        this.clientePedidosRazonSocial = clientePedidosRazonSocial;
    }

    @Basic
    @Column(name = "cliente_pedidos_nombre")
    public String getClientePedidosNombre() {
        return clientePedidosNombre;
    }

    public void setClientePedidosNombre(String clientePedidosNombre) {
        this.clientePedidosNombre = clientePedidosNombre;
    }

    @Basic
    @Column(name = "cliente_pedidos_telefono")
    public String getClientePedidosTelefono() {
        return clientePedidosTelefono;
    }

    public void setClientePedidosTelefono(String clientePedidosTelefono) {
        this.clientePedidosTelefono = clientePedidosTelefono;
    }

    @Basic
    @Column(name = "cliente_pedidos_correo")
    public String getClientePedidosCorreo() {
        return clientePedidosCorreo;
    }

    public void setClientePedidosCorreo(String clientePedidosCorreo) {
        this.clientePedidosCorreo = clientePedidosCorreo;
    }

    @Basic
    @Column(name = "cliente_pedidos_localidad_id")
    public Integer getClientePedidosLocalidadId() {
        return clientePedidosLocalidadId;
    }

    public void setClientePedidosLocalidadId(Integer clientePedidosLocalidadId) {
        this.clientePedidosLocalidadId = clientePedidosLocalidadId;
    }

    @Basic
    @Column(name = "cliente_pedidos_lat")
    public String getClientePedidosLat() {
        return clientePedidosLat;
    }

    public void setClientePedidosLat(String clientePedidosLat) {
        this.clientePedidosLat = clientePedidosLat;
    }

    @Basic
    @Column(name = "cliente_pedidos_lng")
    public String getClientePedidosLng() {
        return clientePedidosLng;
    }

    public void setClientePedidosLng(String clientePedidosLng) {
        this.clientePedidosLng = clientePedidosLng;
    }

    @Basic
    @Column(name = "cliente_pedidos_direccion")
    public String getClientePedidosDireccion() {
        return clientePedidosDireccion;
    }

    public void setClientePedidosDireccion(String clientePedidosDireccion) {
        this.clientePedidosDireccion = clientePedidosDireccion;
    }

    @Basic
    @Column(name = "cliente_pedidos_foto_id")
    public Integer getClientePedidosFotoId() {
        return clientePedidosFotoId;
    }

    public void setClientePedidosFotoId(Integer clientePedidosFotoId) {
        this.clientePedidosFotoId = clientePedidosFotoId;
    }

    @Basic
    @Column(name = "cliente_pedidos_fecha_creacion")
    public Timestamp getClientePedidosFechaCreacion() {
        return clientePedidosFechaCreacion;
    }

    public void setClientePedidosFechaCreacion(Timestamp clientePedidosFechaCreacion) {
        this.clientePedidosFechaCreacion = clientePedidosFechaCreacion;
    }

    @Basic
    @Column(name = "cliente_pedidos_usuario_crea")
    public String getClientePedidosUsuarioCrea() {
        return clientePedidosUsuarioCrea;
    }

    public void setClientePedidosUsuarioCrea(String clientePedidosUsuarioCrea) {
        this.clientePedidosUsuarioCrea = clientePedidosUsuarioCrea;
    }

    @Basic
    @Column(name = "cliente_pedidos_estado")
    public int getClientePedidosEstado() {
        return clientePedidosEstado;
    }

    public void setClientePedidosEstado(int clientePedidosEstado) {
        this.clientePedidosEstado = clientePedidosEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientePedidos that = (ClientePedidos) o;
        return clientePedidosId == that.clientePedidosId &&
                clientePedidosEstado == that.clientePedidosEstado &&
                Objects.equals(clientePedidosRuc, that.clientePedidosRuc) &&
                Objects.equals(clientePedidosRazonSocial, that.clientePedidosRazonSocial) &&
                Objects.equals(clientePedidosNombre, that.clientePedidosNombre) &&
                Objects.equals(clientePedidosTelefono, that.clientePedidosTelefono) &&
                Objects.equals(clientePedidosCorreo, that.clientePedidosCorreo) &&
                Objects.equals(clientePedidosLocalidadId, that.clientePedidosLocalidadId) &&
                Objects.equals(clientePedidosLat, that.clientePedidosLat) &&
                Objects.equals(clientePedidosLng, that.clientePedidosLng) &&
                Objects.equals(clientePedidosDireccion, that.clientePedidosDireccion) &&
                Objects.equals(clientePedidosFotoId, that.clientePedidosFotoId) &&
                Objects.equals(clientePedidosFechaCreacion, that.clientePedidosFechaCreacion) &&
                Objects.equals(clientePedidosUsuarioCrea, that.clientePedidosUsuarioCrea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientePedidosId, clientePedidosRuc, clientePedidosRazonSocial, clientePedidosNombre, clientePedidosTelefono, clientePedidosCorreo, clientePedidosLocalidadId, clientePedidosLat, clientePedidosLng, clientePedidosDireccion, clientePedidosFotoId, clientePedidosFechaCreacion, clientePedidosUsuarioCrea, clientePedidosEstado);
    }

    @ManyToOne
    @JoinColumn(name = "cliente_pedidos_localidad_id", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadByClientePedidosLocalidadId() {
        return localidadByClientePedidosLocalidadId;
    }

    public void setLocalidadByClientePedidosLocalidadId(Localidad localidadByClientePedidosLocalidadId) {
        this.localidadByClientePedidosLocalidadId = localidadByClientePedidosLocalidadId;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_pedidos_foto_id", referencedColumnName = "file_id", insertable = false, updatable = false)
    public File getFileByClientePedidosFotoId() {
        return fileByClientePedidosFotoId;
    }

    public void setFileByClientePedidosFotoId(File fileByClientePedidosFotoId) {
        this.fileByClientePedidosFotoId = fileByClientePedidosFotoId;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_pedidos_usuario_crea", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false, nullable = false)
    public Usuario getUsuarioByClientePedidosUsuarioCrea() {
        return usuarioByClientePedidosUsuarioCrea;
    }

    public void setUsuarioByClientePedidosUsuarioCrea(Usuario usuarioByClientePedidosUsuarioCrea) {
        this.usuarioByClientePedidosUsuarioCrea = usuarioByClientePedidosUsuarioCrea;
    }

    @OneToMany(mappedBy = "clientePedidosByVisitaClientePedidos")
    public Collection<Visita> getVisitasByClientePedidosId() {
        return visitasByClientePedidosId;
    }

    public void setVisitasByClientePedidosId(Collection<Visita> visitasByClientePedidosId) {
        this.visitasByClientePedidosId = visitasByClientePedidosId;
    }

    @Basic
    @Column(name = "cliente_pedidos_vendedor_asignado")
    public String getClientePedidosVendedorAsignado() {
        return clientePedidosVendedorAsignado;
    }

    public void setClientePedidosVendedorAsignado(String clientePedidosVendedorAsignado) {
        this.clientePedidosVendedorAsignado = clientePedidosVendedorAsignado;
    }

    @Basic
    @Column(name = "cliente_pedidos_dia_semana_visita")
    public Integer getClientePedidosDiaSemanaVisita() {
        return clientePedidosDiaSemanaVisita;
    }

    public void setClientePedidosDiaSemanaVisita(Integer clientePedidosDiaSemanaVisita) {
        this.clientePedidosDiaSemanaVisita = clientePedidosDiaSemanaVisita;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_pedidos_vendedor_asignado", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByClientePedidosVendedorAsignado() {
        return usuarioByClientePedidosVendedorAsignado;
    }

    public void setUsuarioByClientePedidosVendedorAsignado(Usuario usuarioByClientePedidosVendedorAsignado) {
        this.usuarioByClientePedidosVendedorAsignado = usuarioByClientePedidosVendedorAsignado;
    }
}
