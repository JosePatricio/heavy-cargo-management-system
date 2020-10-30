package ec.net.redcode.tas.on.persistence.entities;

import ec.net.redcode.tas.on.persistence.component.JsonDateDeserializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuarios.UsuariosByNombreUsuario", query = "select u from Usuario u where u.usuarioNombreUsuario = :nombreUsuario"),
        @NamedQuery(name = "Usuarios.UsuariosByNombreUsuarioAndContrasenia", query = "select u from Usuario u where u.usuarioNombreUsuario = :nombreUsuario and u.usuarioContrasenia = :contrasenia"),
        //datos de conductores o clientes filtrados por tipo de persona
        @NamedQuery(name = "Usuarios.UsuariosByTipoUsuario", query = "select u from Usuario u where u.usuarioTipoUsuario = :tipoUsuario and u.usuarioEstado = :estado"),
        @NamedQuery(name = "Usuarios.UsuariosByEmpresaTipoUsuario", query = "select u from Usuario u where u.usuarioRuc = :usuarioRuc and  u.usuarioTipoUsuario = :tipoUsuario and u.usuarioEstado = :estado"),
        @NamedQuery(name = "Usuario.UsuarioByEmail", query = "select u from Usuario u where u.usuarioMail = :email"),
        @NamedQuery(name = "Usuario.UsuarioByRuc", query = "select u from Usuario  u where u.usuarioRuc = :ruc"),
        @NamedQuery(name = "Usuario.UsuarioAdmin", query = "select count(u) from Usuario u where u.usuarioRuc = :ruc and u.usuarioPrincipal = true "),
        @NamedQuery(name = "Usuario.UsuarioByUsuarioAndEmail", query = "select u from Usuario u where u.usuarioIdDocumento = :identificador and u.usuarioMail = :mail"),
        @NamedQuery(name = "Usuario.UsuarioByEmpresaAndEstado", query = "select u from Usuario u where u.usuarioRuc = :idEmpresa and u.usuarioEstado = :estado")
})
public class Usuario {
    private String usuarioIdDocumento;
    private Integer usuarioTipoDocumento;
    private String usuarioNombres;
    private String usuarioApellidos;
    private String usuarioCelular;
    private String usuarioConvecional;
    private String usuarioRuc;
    private String usuarioDireccion;
    private Integer usuarioLocalidad;
    private String usuarioNombreUsuario;
    private String usuarioContrasenia;
    private Integer usuarioEstado;
    private String usuarioMail;
    private Integer usuarioTipoUsuario;
    private Boolean usuarioPrincipal;
    private String usuarioFcmToken;
    private List<Vehiculo> vehiculosByUsuarioIdDocumento;
    private List<Conductor> conductorsByUsuarioIdDocumento;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp usuarioUltimaSesion;
    @JsonIgnore private List<Contrato> contratoesByUsuarioIdDocumento;
    @JsonIgnore private List<Contrato> contratoesByUsuarioIdDocumento_0;
    @JsonIgnore private List<Factura> facturasByUsuarioIdDocumento;
    @JsonIgnore private List<Oferta> ofertasByUsuarioIdDocumento;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByUsuarioIdDocumento;
    @JsonIgnore private Localidad localidadByUsuarioLocalidad;
    @JsonIgnore private CatalogoItem catalogoItemByUsuarioEstado;
    @JsonIgnore private CatalogoItem catalogoItemByUsuarioTipoUsuario;
    @JsonIgnore private List<FacturaProveedor> facturasProveedorByUsuarioIdDocumento;
    @JsonIgnore private Cliente clienteByUsuarioRuc;
    @JsonIgnore private Collection<CategoriaProducto> categoriaProductosByUsuarioIdDocumento;
    @JsonIgnore private Collection<ClientePedidos> clientePedidosByUsuarioIdDocumento;
    @JsonIgnore private Collection<Pedido> pedidosByUsuarioIdDocumento;
    @JsonIgnore private Collection<Producto> productosByUsuarioIdDocumento;
    @JsonIgnore private Collection<Visita> visitasByUsuarioIdDocumento;
    @JsonIgnore private Collection<Visita> visitasByUsuarioIdDocumento_0;
    @JsonIgnore private Collection<ClientePedidos> clientePedidosByUsuarioIdDocumento_0;

    @Id
    @Column(name = "usuario_id_documento", nullable = false, length = 13)
    public String getUsuarioIdDocumento() {
        return usuarioIdDocumento;
    }

    public void setUsuarioIdDocumento(String usuarioIdDocumento) {
        this.usuarioIdDocumento = usuarioIdDocumento;
    }

    @Basic
    @Column(name = "usuario_tipo_documento", nullable = true)
    public Integer getUsuarioTipoDocumento() {
        return usuarioTipoDocumento;
    }

    public void setUsuarioTipoDocumento(Integer usuarioTipoDocumento) {
        this.usuarioTipoDocumento = usuarioTipoDocumento;
    }

    @Basic
    @Column(name = "usuario_nombres", nullable = true, length = 50)
    public String getUsuarioNombres() {
        return usuarioNombres;
    }

    public void setUsuarioNombres(String usuarioNombres) {
        this.usuarioNombres = usuarioNombres;
    }

    @Basic
    @Column(name = "usuario_apellidos", nullable = true, length = 50)
    public String getUsuarioApellidos() {
        return usuarioApellidos;
    }

    public void setUsuarioApellidos(String usuarioApellidos) {
        this.usuarioApellidos = usuarioApellidos;
    }

    @Basic
    @Column(name = "usuario_celular", nullable = true, length = 10)
    public String getUsuarioCelular() {
        return usuarioCelular;
    }

    public void setUsuarioCelular(String usuarioCelular) {
        this.usuarioCelular = usuarioCelular;
    }

    @Basic
    @Column(name = "usuario_convecional", nullable = true, length = 10)
    public String getUsuarioConvecional() {
        return usuarioConvecional;
    }

    public void setUsuarioConvecional(String usuarioConvecional) {
        this.usuarioConvecional = usuarioConvecional;
    }

    @Basic
    @Column(name = "usuario_ruc", nullable = true, length = 13)
    public String getUsuarioRuc() {
        return usuarioRuc;
    }

    public void setUsuarioRuc(String usuarioRuc) {
        this.usuarioRuc = usuarioRuc;
    }

    @Basic
    @Column(name = "usuario_direccion", nullable = true, length = 100)
    public String getUsuarioDireccion() {
        return usuarioDireccion;
    }

    public void setUsuarioDireccion(String usuarioDireccion) {
        this.usuarioDireccion = usuarioDireccion;
    }

    @Basic
    @Column(name = "usuario_localidad", nullable = true)
    public Integer getUsuarioLocalidad() {
        return usuarioLocalidad;
    }

    public void setUsuarioLocalidad(Integer usuarioLocalidad) {
        this.usuarioLocalidad = usuarioLocalidad;
    }

    @Basic
    @Column(name = "usuario_nombre_usuario", nullable = true, length = 10)
    public String getUsuarioNombreUsuario() {
        return usuarioNombreUsuario;
    }

    public void setUsuarioNombreUsuario(String usuarioNombreUsuario) {
        this.usuarioNombreUsuario = usuarioNombreUsuario;
    }

    @Basic
    @Column(name = "usuario_contrasenia", nullable = true, length = 300)
    public String getUsuarioContrasenia() {
        return usuarioContrasenia;
    }

    public void setUsuarioContrasenia(String usuarioContrasenia) {
        this.usuarioContrasenia = usuarioContrasenia;
    }

    @Basic
    @Column(name = "usuario_estado", nullable = true)
    public Integer getUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(Integer usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    @Basic
    @Column(name = "usuario_mail", nullable = true, length = 100)
    public String getUsuarioMail() {
        return usuarioMail;
    }

    public void setUsuarioMail(String usuarioMail) {
        this.usuarioMail = usuarioMail;
    }

    @Basic
    @Column(name = "usuario_tipo_usuario", nullable = true)
    public Integer getUsuarioTipoUsuario() {
        return usuarioTipoUsuario;
    }

    public void setUsuarioTipoUsuario(Integer usuarioTipoUsuario) {
        this.usuarioTipoUsuario = usuarioTipoUsuario;
    }

    @Basic
    @Column(name = "usuario_principal")
    public Boolean getUsuarioPrincipal() {
        return usuarioPrincipal;
    }

    public void setUsuarioPrincipal(Boolean usuarioPrincipal) {
        this.usuarioPrincipal = usuarioPrincipal;
    }

    @Basic
    @Column(name = "usuario_fcm_token", nullable = true)
    public String getUsuarioFcmToken() {
        return usuarioFcmToken;
    }

    public void setUsuarioFcmToken(String usuarioFcmToken) {
        this.usuarioFcmToken = usuarioFcmToken;
    }

    @Basic
    @Column(name = "usuario_ultima_sesion", nullable = true)
    public Timestamp getUsuarioUltimaSesion() {
        return usuarioUltimaSesion;
    }

    public void setUsuarioUltimaSesion(Timestamp usuarioUltimaSesion) {
        this.usuarioUltimaSesion = usuarioUltimaSesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(usuarioIdDocumento, usuario.usuarioIdDocumento) &&
                Objects.equals(usuarioTipoDocumento, usuario.usuarioTipoDocumento) &&
                Objects.equals(usuarioNombres, usuario.usuarioNombres) &&
                Objects.equals(usuarioApellidos, usuario.usuarioApellidos) &&
                Objects.equals(usuarioCelular, usuario.usuarioCelular) &&
                Objects.equals(usuarioConvecional, usuario.usuarioConvecional) &&
                Objects.equals(usuarioRuc, usuario.usuarioRuc) &&
                Objects.equals(usuarioDireccion, usuario.usuarioDireccion) &&
                Objects.equals(usuarioLocalidad, usuario.usuarioLocalidad) &&
                Objects.equals(usuarioNombreUsuario, usuario.usuarioNombreUsuario) &&
                Objects.equals(usuarioContrasenia, usuario.usuarioContrasenia) &&
                Objects.equals(usuarioEstado, usuario.usuarioEstado) &&
                Objects.equals(usuarioMail, usuario.usuarioMail) &&
                Objects.equals(usuarioTipoUsuario, usuario.usuarioTipoUsuario) &&
                Objects.equals(usuarioPrincipal, usuario.usuarioPrincipal) &&
                Objects.equals(usuarioFcmToken, usuario.usuarioFcmToken) &&
                Objects.equals(usuarioUltimaSesion, usuario.usuarioUltimaSesion) &&
                Objects.equals(contratoesByUsuarioIdDocumento, usuario.contratoesByUsuarioIdDocumento) &&
                Objects.equals(contratoesByUsuarioIdDocumento_0, usuario.contratoesByUsuarioIdDocumento_0) &&
                Objects.equals(facturasByUsuarioIdDocumento, usuario.facturasByUsuarioIdDocumento) &&
                Objects.equals(ofertasByUsuarioIdDocumento, usuario.ofertasByUsuarioIdDocumento) &&
                Objects.equals(vehiculosByUsuarioIdDocumento, usuario.vehiculosByUsuarioIdDocumento) &&
                Objects.equals(conductorsByUsuarioIdDocumento, usuario.conductorsByUsuarioIdDocumento) &&
                Objects.equals(solicitudEnviosByUsuarioIdDocumento, usuario.solicitudEnviosByUsuarioIdDocumento) &&
                Objects.equals(localidadByUsuarioLocalidad, usuario.localidadByUsuarioLocalidad) &&
                Objects.equals(catalogoItemByUsuarioEstado, usuario.catalogoItemByUsuarioEstado) &&
                Objects.equals(catalogoItemByUsuarioTipoUsuario, usuario.catalogoItemByUsuarioTipoUsuario) &&
                Objects.equals(facturasProveedorByUsuarioIdDocumento, usuario.facturasProveedorByUsuarioIdDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioIdDocumento, usuarioTipoDocumento, usuarioNombres, usuarioApellidos, usuarioCelular, usuarioConvecional,
                usuarioRuc, usuarioDireccion, usuarioLocalidad, usuarioNombreUsuario, usuarioContrasenia, usuarioEstado, usuarioMail,
                usuarioTipoUsuario, usuarioPrincipal, usuarioFcmToken, usuarioUltimaSesion, contratoesByUsuarioIdDocumento,
                contratoesByUsuarioIdDocumento_0, facturasByUsuarioIdDocumento, ofertasByUsuarioIdDocumento, vehiculosByUsuarioIdDocumento,
                conductorsByUsuarioIdDocumento, solicitudEnviosByUsuarioIdDocumento, localidadByUsuarioLocalidad, catalogoItemByUsuarioEstado,
                catalogoItemByUsuarioTipoUsuario, facturasProveedorByUsuarioIdDocumento);
    }

    @OneToMany(mappedBy = "usuarioByContratoDocumentoCliente")
    public List<Contrato> getContratoesByUsuarioIdDocumento() {
        return contratoesByUsuarioIdDocumento;
    }

    public void setContratoesByUsuarioIdDocumento(List<Contrato> contratoesByUsuarioIdDocumento) {
        this.contratoesByUsuarioIdDocumento = contratoesByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByContratoDocumentoConductor")
    public List<Contrato> getContratoesByUsuarioIdDocumento_0() {
        return contratoesByUsuarioIdDocumento_0;
    }

    public void setContratoesByUsuarioIdDocumento_0(List<Contrato> contratoesByUsuarioIdDocumento_0) {
        this.contratoesByUsuarioIdDocumento_0 = contratoesByUsuarioIdDocumento_0;
    }

    @OneToMany(mappedBy = "usuarioByFacturaUsuarioId")
    public List<Factura> getFacturasByUsuarioIdDocumento() {
        return facturasByUsuarioIdDocumento;
    }

    public void setFacturasByUsuarioIdDocumento(List<Factura> facturasByUsuarioIdDocumento) {
        this.facturasByUsuarioIdDocumento = facturasByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByOfertaIdConductor")
    public List<Oferta> getOfertasByUsuarioIdDocumento() {
        return ofertasByUsuarioIdDocumento;
    }

    public void setOfertasByUsuarioIdDocumento(List<Oferta> ofertasByUsuarioIdDocumento) {
        this.ofertasByUsuarioIdDocumento = ofertasByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByVehiculoUsuario")
    public List<Vehiculo> getVehiculosByUsuarioIdDocumento() {
        return vehiculosByUsuarioIdDocumento;
    }

    public void setVehiculosByUsuarioIdDocumento(List<Vehiculo> vehiculosByUsuarioIdDocumento) {
        this.vehiculosByUsuarioIdDocumento = vehiculosByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "conductorByConductorUsuario")
    public List<Conductor> getConductorsByUsuarioIdDocumento() {
        return conductorsByUsuarioIdDocumento;
    }

    public void setConductorsByUsuarioIdDocumento(List<Conductor> conductorsByUsuarioIdDocumento) {
        this.conductorsByUsuarioIdDocumento = conductorsByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioBySolicitudEnvioUsuarioId")
    public List<SolicitudEnvio> getSolicitudEnviosByUsuarioIdDocumento() {
        return solicitudEnviosByUsuarioIdDocumento;
    }

    public void setSolicitudEnviosByUsuarioIdDocumento(List<SolicitudEnvio> solicitudEnviosByUsuarioIdDocumento) {
        this.solicitudEnviosByUsuarioIdDocumento = solicitudEnviosByUsuarioIdDocumento;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_localidad", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadByUsuarioLocalidad() {
        return localidadByUsuarioLocalidad;
    }

    public void setLocalidadByUsuarioLocalidad(Localidad localidadByUsuarioLocalidad) {
        this.localidadByUsuarioLocalidad = localidadByUsuarioLocalidad;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_ruc", referencedColumnName = "cliente_ruc", insertable = false, updatable = false)
    public Cliente getClienteByUsuarioRuc() {
        return clienteByUsuarioRuc;
    }

    public void setClienteByUsuarioRuc(Cliente clienteByUsuarioRuc) {
        this.clienteByUsuarioRuc = clienteByUsuarioRuc;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByUsuarioEstado() {
        return catalogoItemByUsuarioEstado;
    }

    public void setCatalogoItemByUsuarioEstado(CatalogoItem catalogoByUsuarioEstado) {
        this.catalogoItemByUsuarioEstado = catalogoItemByUsuarioEstado;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_tipo_usuario", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByUsuarioTipoUsuario() {
        return catalogoItemByUsuarioTipoUsuario;
    }

    public void setCatalogoItemByUsuarioTipoUsuario(CatalogoItem catalogoByUsuarioTipoUsuario) {
        this.catalogoItemByUsuarioTipoUsuario = getCatalogoItemByUsuarioTipoUsuario();
    }

    @OneToMany(mappedBy = "usuarioByFacturaProveedorUsuario")
    public List<FacturaProveedor> getFacturasProveedorByUsuarioIdDocumento() {
        return facturasProveedorByUsuarioIdDocumento;
    }

    public void setFacturasProveedorByUsuarioIdDocumento(List<FacturaProveedor> facturasProveedorByUsuarioIdDocumento) {
        this.facturasProveedorByUsuarioIdDocumento = facturasProveedorByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByCategoriaProductoUsuarioCrea")
    public Collection<CategoriaProducto> getCategoriaProductosByUsuarioIdDocumento() {
        return categoriaProductosByUsuarioIdDocumento;
    }

    public void setCategoriaProductosByUsuarioIdDocumento(Collection<CategoriaProducto> categoriaProductosByUsuarioIdDocumento) {
        this.categoriaProductosByUsuarioIdDocumento = categoriaProductosByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByClientePedidosUsuarioCrea")
    public Collection<ClientePedidos> getClientePedidosByUsuarioIdDocumento() {
        return clientePedidosByUsuarioIdDocumento;
    }

    public void setClientePedidosByUsuarioIdDocumento(Collection<ClientePedidos> clientePedidosByUsuarioIdDocumento) {
        this.clientePedidosByUsuarioIdDocumento = clientePedidosByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByPedidoUsuarioCrea")
    public Collection<Pedido> getPedidosByUsuarioIdDocumento() {
        return pedidosByUsuarioIdDocumento;
    }

    public void setPedidosByUsuarioIdDocumento(Collection<Pedido> pedidosByUsuarioIdDocumento) {
        this.pedidosByUsuarioIdDocumento = pedidosByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByProductoUsuarioCrea")
    public Collection<Producto> getProductosByUsuarioIdDocumento() {
        return productosByUsuarioIdDocumento;
    }

    public void setProductosByUsuarioIdDocumento(Collection<Producto> productosByUsuarioIdDocumento) {
        this.productosByUsuarioIdDocumento = productosByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByVisitaVendedorUsuario")
    public Collection<Visita> getVisitasByUsuarioIdDocumento() {
        return visitasByUsuarioIdDocumento;
    }

    public void setVisitasByUsuarioIdDocumento(Collection<Visita> visitasByUsuarioIdDocumento) {
        this.visitasByUsuarioIdDocumento = visitasByUsuarioIdDocumento;
    }

    @OneToMany(mappedBy = "usuarioByVisitaUsuarioCrea")
    public Collection<Visita> getVisitasByUsuarioIdDocumento_0() {
        return visitasByUsuarioIdDocumento_0;
    }

    public void setVisitasByUsuarioIdDocumento_0(Collection<Visita> visitasByUsuarioIdDocumento_0) {
        this.visitasByUsuarioIdDocumento_0 = visitasByUsuarioIdDocumento_0;
    }

    @OneToMany(mappedBy = "usuarioByClientePedidosVendedorAsignado")
    public Collection<ClientePedidos> getClientePedidosByUsuarioIdDocumento_0() {
        return clientePedidosByUsuarioIdDocumento_0;
    }

    public void setClientePedidosByUsuarioIdDocumento_0(Collection<ClientePedidos> clientePedidosByUsuarioIdDocumento_0) {
        this.clientePedidosByUsuarioIdDocumento_0 = clientePedidosByUsuarioIdDocumento_0;
    }
}
