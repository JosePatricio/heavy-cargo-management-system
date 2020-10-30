package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "cliente")
@NamedQueries({
        @NamedQuery(name = "Cliente.ClienteGetAllCliente", query = "select c from Cliente c order by c.clienteRazonSocial"),
        @NamedQuery(name = "Cliente.ClienteByTipo", query = "select c from Cliente c where c.clienteTipoCliente = :clienteTipoCliente order by c.clienteRazonSocial"),
})
public class Cliente {

    private String clienteRuc;
    private int clienteTipoId;
    private int clienteLocalidadId;
    private String clienteRazonSocial;
    private String clienteDireccion;
    private int clienteDiasCredito;
    private int clienteDiasPeriodicidad;
    private int clienteTipoCliente;
    private int clienteComision;
    private String clienteCuenta;
    private int clienteTipoCuenta;
    private int clienteBanco;
    private int clienteRetencion;
    private int clienteIva;
    private String clienteCorreo;
    private Integer clienteTipoProducto;
    private Integer clienteConocimientoRegistro;
    private String clienteCodigoVendedor;
    private Timestamp clienteFechaRegistro;
    private Double clienteMinFacturacion;
    private Double clienteMaxFacturacion;
    private Integer clienteDiaFacturacion;
    private Integer clienteNotaCredito;
    private Integer clienteMaxDiaFacturacion;
    private Integer clienteAcreditarCondutor;
    private Integer clienteAcreditarVehiculo;
    private Integer clienteBancoTipoIdentificacion;
    private String clienteBancoIdentificacion;
    private String clienteBancoNombres;
    private Timestamp clienteBancoUltimaActualizacion;
    private Timestamp clienteBancoSolicActualizar;
    private Integer clienteBancoIntentoActualizar;

    @JsonIgnore private Localidad localidadByClienteLocalidad;
    @JsonIgnore private CatalogoItem catalogoItemByClienteTipoPersona;
    @JsonIgnore private CatalogoItem catalogoItemByClienteDiasPeriodicidad;
    @JsonIgnore private CatalogoItem catalogoItemByClienteTipoCliente;
    @JsonIgnore private List<Usuario> usuariosByclienteRuc;
    @JsonIgnore private CatalogoItem catalogoItemByClienteBanco;
    @JsonIgnore private CatalogoItem catalogoItemByClienteTipoCuenta;
    @JsonIgnore private CatalogoItem catalogoItemByClienteBancoTipoIdentificacion;

    @Id
    @Column(name = "cliente_ruc", nullable = false)
    public String getClienteRuc() {
        return clienteRuc;
    }

    public void setClienteRuc(String clienteRuc) {
        this.clienteRuc = clienteRuc;
    }

    @Basic
    @Column(name = "cliente_tipo_id", nullable = true)
    public int getClienteTipoId() {
        return clienteTipoId;
    }

    public void setClienteTipoId(int clienteTipoId) {
        this.clienteTipoId = clienteTipoId;
    }

    @Basic
    @Column(name = "cliente_localidad_id", nullable = true)
    public int getClienteLocalidadId() {
        return clienteLocalidadId;
    }

    public void setClienteLocalidadId(int clienteLocalidadId) {
        this.clienteLocalidadId = clienteLocalidadId;
    }

    @Basic
    @Column(name = "cliente_razon_social", nullable = true)
    public String getClienteRazonSocial() {
        return clienteRazonSocial;
    }

    public void setClienteRazonSocial(String clienteRazonSocial) {
        this.clienteRazonSocial = clienteRazonSocial;
    }

    @Basic
    @Column(name = "cliente_direccion", nullable = true)
    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }

    @Basic
    @Column(name = "cliente_dias_credito", nullable = true)
    public int getClienteDiasCredito() {
        return clienteDiasCredito;
    }

    public void setClienteDiasCredito(int clienteDiasCredito) {
        this.clienteDiasCredito = clienteDiasCredito;
    }

    @Basic
    @Column(name = "cliente_dias_periodicidad", nullable = true)
    public int getClienteDiasPeriodicidad() {
        return clienteDiasPeriodicidad;
    }

    public void setClienteDiasPeriodicidad(int clienteDiasPeriodicidad) {
        this.clienteDiasPeriodicidad = clienteDiasPeriodicidad;
    }

    @Basic
    @Column(name = "cliente_tipo_cliente", nullable = true)
    public int getClienteTipoCliente() {
        return clienteTipoCliente;
    }

    public void setClienteTipoCliente(int clienteTipoCliente) {
        this.clienteTipoCliente = clienteTipoCliente;
    }

    @Basic
    @Column(name = "cliente_comision", nullable = true)
    public int getClienteComision() {
        return clienteComision;
    }

    public void setClienteComision(int clienteComision) {
        this.clienteComision = clienteComision;
    }

    @Basic
    @Column(name = "cliente_cuenta", nullable = true)
    public String getClienteCuenta() {
        return clienteCuenta;
    }

    public void setClienteCuenta(String clienteCuenta) {
        this.clienteCuenta = clienteCuenta;
    }

    @Basic
    @Column(name = "cliente_tipo_cuenta", nullable = true)
    public int getClienteTipoCuenta() {
        return clienteTipoCuenta;
    }

    public void setClienteTipoCuenta(int clienteTipoCuenta) {
        this.clienteTipoCuenta = clienteTipoCuenta;
    }

    @Basic
    @Column(name = "cliente_banco", nullable = true)
    public int getClienteBanco() {
        return clienteBanco;
    }

    public void setClienteBanco(int clienteBanco) {
        this.clienteBanco = clienteBanco;
    }

    @Basic
    @Column(name = "cliente_retencion", nullable = true)
    public int getClienteRetencion() {
        return clienteRetencion;
    }

    public void setClienteRetencion(int clienteRetencion) {
        this.clienteRetencion = clienteRetencion;
    }

    @Basic
    @Column(name = "cliente_iva", nullable = true)
    public int getClienteIva() {
        return clienteIva;
    }

    public void setClienteIva(int clienteIva) {
        this.clienteIva = clienteIva;
    }

    @Basic
    @Column(name = "cliente_correo", nullable = true)
    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public void setClienteCorreo(String clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    @Column(name = "cliente_tipo_producto")
    public Integer getClienteTipoProducto() {
        return clienteTipoProducto;
    }

    public void setClienteTipoProducto(Integer clienteTipoProducto) {
        this.clienteTipoProducto = clienteTipoProducto;
    }

    @Column(name = "cliente_conocimiento_registro")
    public Integer getClienteConocimientoRegistro() {
        return clienteConocimientoRegistro;
    }

    public void setClienteConocimientoRegistro(Integer clienteConocimientoRegistro) {
        this.clienteConocimientoRegistro = clienteConocimientoRegistro;
    }

    @Column(name = "cliente_codigo_vendedor")
    public String getClienteCodigoVendedor() {
        return clienteCodigoVendedor;
    }

    public void setClienteCodigoVendedor(String clienteCodigoVendedor) {
        this.clienteCodigoVendedor = clienteCodigoVendedor;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_localidad_id", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadByClienteLocalidad() {
        return localidadByClienteLocalidad;
    }

    public void setLocalidadByClienteLocalidad(Localidad localidadByClienteLocalidad) {
        this.localidadByClienteLocalidad = localidadByClienteLocalidad;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_tipo_id", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteTipoPersona() {
        return catalogoItemByClienteTipoPersona;
    }

    public void setCatalogoItemByClienteTipoPersona(CatalogoItem catalogoItemByClienteTipoPersona) {
        this.catalogoItemByClienteTipoPersona = catalogoItemByClienteTipoPersona;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_dias_periodicidad", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteDiasPeriodicidad() {
        return catalogoItemByClienteDiasPeriodicidad;
    }

    public void setCatalogoItemByClienteDiasPeriodicidad(CatalogoItem catalogoItemByClienteDiasPeriodicidad) {
        this.catalogoItemByClienteDiasPeriodicidad = catalogoItemByClienteDiasPeriodicidad;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_tipo_cliente", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteTipoCliente() {
        return catalogoItemByClienteTipoCliente;
    }

    public void setCatalogoItemByClienteTipoCliente(CatalogoItem catalogoItemByClienteTipoCliente) {
        this.catalogoItemByClienteTipoCliente = catalogoItemByClienteTipoCliente;
    }

    @Column(name = "cliente_fecha_registro")
    public Timestamp getClienteFechaRegistro() {
        return clienteFechaRegistro;
    }

    public void setClienteFechaRegistro(Timestamp clienteFechaRegistro) {
        this.clienteFechaRegistro = clienteFechaRegistro;
    }

    @OneToMany(mappedBy = "clienteByUsuarioRuc")
    public List<Usuario> getUsuariosByclienteRuc() {
        return usuariosByclienteRuc;
    }

    public void setUsuariosByclienteRuc(List<Usuario> usuariosByclienteRuc) {
        this.usuariosByclienteRuc = usuariosByclienteRuc;
    }

    @Column(name = "cliente_min_facturacion")
    public Double getClienteMinFacturacion() {
        return clienteMinFacturacion;
    }

    public void setClienteMinFacturacion(Double clienteMinFacturacion) {
        this.clienteMinFacturacion = clienteMinFacturacion;
    }

    @Column(name = "cliente_max_facturacion")
    public Double getClienteMaxFacturacion() {
        return clienteMaxFacturacion;
    }

    public void setClienteMaxFacturacion(Double clienteMaxFacturacion) {
        this.clienteMaxFacturacion = clienteMaxFacturacion;
    }

    @Column(name = "cliente_dia_facturacion")
    public Integer getClienteDiaFacturacion() {
        return clienteDiaFacturacion;
    }

    public void setClienteDiaFacturacion(Integer clienteDiaFacturacion) {
        this.clienteDiaFacturacion = clienteDiaFacturacion;
    }

    @Column(name = "cliente_nota_credito")
    public Integer getClienteNotaCredito() {
        return clienteNotaCredito;
    }

    public void setClienteNotaCredito(Integer clienteNotaCredito) {
        this.clienteNotaCredito = clienteNotaCredito;
    }

    @Column(name = "cliente_max_dia_facturacion")
    public Integer getClienteMaxDiaFacturacion() {
        return clienteMaxDiaFacturacion;
    }

    public void setClienteMaxDiaFacturacion(Integer clienteMaxDiaFacturacion) {
        this.clienteMaxDiaFacturacion = clienteMaxDiaFacturacion;
    }

    @Column(name = "cliente_acreditar_conductor")
    public Integer getClienteAcreditarCondutor() {
        return clienteAcreditarCondutor;
    }

    public void setClienteAcreditarCondutor(Integer clienteAcreditarCondutor) {
        this.clienteAcreditarCondutor = clienteAcreditarCondutor;
    }

    @Column(name = "cliente_acreditar_vehiculo")
    public Integer getClienteAcreditarVehiculo() {
        return clienteAcreditarVehiculo;
    }

    public void setClienteAcreditarVehiculo(Integer clienteAcreditarVehiculo) {
        this.clienteAcreditarVehiculo = clienteAcreditarVehiculo;
    }

    @Column(name = "cliente_banco_tipo_identificacion")
    public Integer getClienteBancoTipoIdentificacion() {
        return clienteBancoTipoIdentificacion;
    }

    public void setClienteBancoTipoIdentificacion(Integer clienteBancoTipoIdentificacion) {
        this.clienteBancoTipoIdentificacion = clienteBancoTipoIdentificacion;
    }

    @Column(name = "cliente_banco_identificacion")
    public String getClienteBancoIdentificacion() {
        return clienteBancoIdentificacion;
    }

    public void setClienteBancoIdentificacion(String clienteBancoIdentificacion) {
        this.clienteBancoIdentificacion = clienteBancoIdentificacion;
    }

    @Column(name = "cliente_banco_nombres")
    public String getClienteBancoNombres() {
        return clienteBancoNombres;
    }

    public void setClienteBancoNombres(String clienteBancoNombres) {
        this.clienteBancoNombres = clienteBancoNombres;
    }

    @Column(name = "cliente_banco_ultima_actualizacion")
    public Timestamp getClienteBancoUltimaActualizacion() {
        return clienteBancoUltimaActualizacion;
    }

    public void setClienteBancoUltimaActualizacion(Timestamp clienteBancoUltimaActualizacion) {
        this.clienteBancoUltimaActualizacion = clienteBancoUltimaActualizacion;
    }

    @Column(name = "cliente_banco_solic_actualizar")
    public Timestamp getClienteBancoSolicActualizar() {
        return clienteBancoSolicActualizar;
    }

    public void setClienteBancoSolicActualizar(Timestamp clienteBancoSolicActualizar) {
        this.clienteBancoSolicActualizar = clienteBancoSolicActualizar;
    }

    @Column(name = "cliente_banco_intento_actualizar")
    public Integer getClienteBancoIntentoActualizar() {
        return clienteBancoIntentoActualizar;
    }

    public void setClienteBancoIntentoActualizar(Integer clienteBancoIntentoActualizar) {
        this.clienteBancoIntentoActualizar = clienteBancoIntentoActualizar;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_banco", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteBanco() {
        return catalogoItemByClienteBanco;
    }

    public void setCatalogoItemByClienteBanco(CatalogoItem catalogoItemByClienteBanco) {
        this.catalogoItemByClienteBanco = catalogoItemByClienteBanco;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_tipo_cuenta", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteTipoCuenta() {
        return catalogoItemByClienteTipoCuenta;
    }

    public void setCatalogoItemByClienteTipoCuenta(CatalogoItem catalogoItemByClienteTipoCuenta) {
        this.catalogoItemByClienteTipoCuenta = catalogoItemByClienteTipoCuenta;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_banco_tipo_identificacion", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByClienteBancoTipoIdentificacion() {
        return catalogoItemByClienteBancoTipoIdentificacion;
    }

    public void setCatalogoItemByClienteBancoTipoIdentificacion(CatalogoItem catalogoItemByClienteBancoTipoIdentificacion) {
        this.catalogoItemByClienteBancoTipoIdentificacion = catalogoItemByClienteBancoTipoIdentificacion;
    }
}
