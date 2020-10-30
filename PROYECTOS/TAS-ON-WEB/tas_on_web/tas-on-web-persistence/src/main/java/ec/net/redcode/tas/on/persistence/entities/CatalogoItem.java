package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "catalogo_item")
@NamedQueries({
        @NamedQuery(name = "CatalogoItem.CatalogoItemByCatalogo", query = "select c from CatalogoItem c where c.catalogoItemIdCatalogo = :idCatalogo " +
                "and c.catalogoItemEstado = 1 order by catalogoItemDescripcion")
})
public class CatalogoItem {
    private Integer catalogoItemId;
    private Integer catalogoItemIdCatalogo;
    private String catalogoItemDescripcion;
    private String catalogoItemValor;
    private Integer catalogoItemEstado;
    @JsonIgnore private Catalogo catalogoByCatalogoItemIdCatalogo;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByCatalogoItemId;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByCatalogoItemId_0;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByCatalogoId;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByCatalogoIdTipoSubasta;
    @JsonIgnore private List<Usuario> usuariosByCatalogoId_0;
    @JsonIgnore private List<Usuario> usuariosByCatalogoItemIdTipoUsuario;
    @JsonIgnore private List<Contrato> contratoByCatalofoItemId;
    @JsonIgnore private List<Localidad> localidadByCatalofoItemId;
    @JsonIgnore private List<Oferta> ofertasByCatalogoId;
    @JsonIgnore private List<Cliente> clientesByCatalogoItemIdTipoPerson;
    @JsonIgnore private List<Conductor> conductorsByCatalogoItem;
    @JsonIgnore private List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCamion;
    @JsonIgnore private List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCarga;
    @JsonIgnore private List<Cliente> clientesByCatalogoClienteDiasPeriodicidad;
    @JsonIgnore private List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCapacidad;
    @JsonIgnore private List<Cliente> clientesByCatalogoClienteTipoCliente;
    @JsonIgnore private List<Factura> facturasByCatalogoItermByFacturaEstado;
    @JsonIgnore private List<Pago> pagosByCatalogoItemByTipo;
    @JsonIgnore private List<Pago> pagosByCatalogoItemByBanco;
    @JsonIgnore private List<FacturaProveedor> facturasProveedorByCatalogoItermByFacturaProveedorFormaPago;
    @JsonIgnore private Collection<Producto> productosByCatalogoItemId;
    @JsonIgnore private Collection<Cliente> clientesByCatalogoItemId_2;
    @JsonIgnore private Collection<Cliente> clientesByCatalogoItemId_3;
    @JsonIgnore private Collection<Cliente> clientesByCatalogoItemId_4;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "catalogo_item_id", nullable = false)
    public Integer getCatalogoItemId() {
        return catalogoItemId;
    }

    public void setCatalogoItemId(Integer catalogoItemId) {
        this.catalogoItemId = catalogoItemId;
    }

    @Basic
    @Column(name = "catalogo_item_id_catalogo", nullable = true)
    public Integer getCatalogoItemIdCatalogo() {
        return catalogoItemIdCatalogo;
    }

    public void setCatalogoItemIdCatalogo(Integer catalogoItemIdCatalogo) {
        this.catalogoItemIdCatalogo = catalogoItemIdCatalogo;
    }

    @Basic
    @Column(name = "catalogo_item_descripcion", nullable = true)
    public String getCatalogoItemDescripcion() {
        return catalogoItemDescripcion;
    }

    public void setCatalogoItemDescripcion(String catalogoItemDescripcion) {
        this.catalogoItemDescripcion = catalogoItemDescripcion;
    }

    @Basic
    @Column(name = "catalogo_item_valor", nullable = true)
    public String getCatalogoItemValor() {
        return catalogoItemValor;
    }

    public void setCatalogoItemValor(String catalogoItemValor) {
        this.catalogoItemValor = catalogoItemValor;
    }

    @Basic
    @Column(name = "catalogo_item_estado", nullable = true)
    public Integer getCatalogoItemEstado() {
        return catalogoItemEstado;
    }

    public void setCatalogoItemEstado(Integer catalogoItemEstado) {
        this.catalogoItemEstado = catalogoItemEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatalogoItem that = (CatalogoItem) o;
        return Objects.equals(catalogoItemId, that.catalogoItemId) &&
                Objects.equals(catalogoItemIdCatalogo, that.catalogoItemIdCatalogo) &&
                Objects.equals(catalogoItemDescripcion, that.catalogoItemDescripcion) &&
                Objects.equals(catalogoItemEstado, that.catalogoItemEstado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(catalogoItemId, catalogoItemIdCatalogo, catalogoItemDescripcion, catalogoItemEstado);
    }

    @ManyToOne
    @JoinColumn(name = "catalogo_item_id_catalogo", referencedColumnName = "catalogo_id", insertable = false, updatable = false)
    public Catalogo getCatalogoByCatalogoItemIdCatalogo() {
        return catalogoByCatalogoItemIdCatalogo;
    }

    public void setCatalogoByCatalogoItemIdCatalogo(Catalogo catalogoByCatalogoItemIdCatalogo) {
        this.catalogoByCatalogoItemIdCatalogo = catalogoByCatalogoItemIdCatalogo;
    }

    @OneToMany(mappedBy = "catalogoItemBySolicitudEnvioUnidadPeso")
    public List<SolicitudEnvio> getSolicitudEnviosByCatalogoItemId() {
        return solicitudEnviosByCatalogoItemId;
    }

    public void setSolicitudEnviosByCatalogoItemId(List<SolicitudEnvio> solicitudEnviosByCatalogoItemId) {
        this.solicitudEnviosByCatalogoItemId = solicitudEnviosByCatalogoItemId;
    }

    @OneToMany(mappedBy = "catalogoItemBySolicitudEnvioUnidadVolumen")
    public List<SolicitudEnvio> getSolicitudEnviosByCatalogoItemId_0() {
        return solicitudEnviosByCatalogoItemId_0;
    }

    public void setSolicitudEnviosByCatalogoItemId_0(List<SolicitudEnvio> solicitudEnviosByCatalogoItemId_0) {
        this.solicitudEnviosByCatalogoItemId_0 = solicitudEnviosByCatalogoItemId_0;
    }

    @OneToMany(mappedBy = "catalogoItemByUsuarioEstado")
    public List<Usuario> getUsuariosByCatalogoId_0() {
        return usuariosByCatalogoId_0;
    }

    public void setUsuariosByCatalogoId_0(List<Usuario> usuariosByCatalogoId_0) {
        this.usuariosByCatalogoId_0 = usuariosByCatalogoId_0;
    }

    @OneToMany(mappedBy = "catalogoItemByUsuarioTipoUsuario")
    public List<Usuario> getUsuariosByCatalogoItemIdTipoUsuario() {
        return usuariosByCatalogoItemIdTipoUsuario;
    }

    public void setUsuariosByCatalogoItemIdTipoUsuario(List<Usuario> usuariosByCatalogoItemIdTipoUsuario) {
        this.usuariosByCatalogoItemIdTipoUsuario = usuariosByCatalogoItemIdTipoUsuario;
    }

    @OneToMany(mappedBy = "catalogoItemByContratoEstado")
    public List<Contrato> getContratoByCatalofoItemId() {
        return contratoByCatalofoItemId;
    }

    public void setContratoByCatalofoItemId(List<Contrato> contratoByCatalofoItemId) {
        this.contratoByCatalofoItemId = contratoByCatalofoItemId;
    }

    @OneToMany(mappedBy = "localidadbyCatalogoItemIdEstado")
    public List<Localidad> getLocalidadByCatalofoItemId() {
        return localidadByCatalofoItemId;
    }

    public void setLocalidadByCatalofoItemId(List<Localidad> localidadByCatalofoItemId) {
        this.localidadByCatalofoItemId = localidadByCatalofoItemId;
    }

    @OneToMany(mappedBy = "catalogoBySolicitudEnvioEstado")
    public List<SolicitudEnvio> getSolicitudEnviosByCatalogoId() {
        return solicitudEnviosByCatalogoId;
    }

    public void setSolicitudEnviosByCatalogoId(List<SolicitudEnvio> solicitudEnviosByCatalogoId) {
        this.solicitudEnviosByCatalogoId = solicitudEnviosByCatalogoId;
    }

    @OneToMany(mappedBy = "catalogoItemBySolicitudEnvioTipoSubasta")
    public List<SolicitudEnvio> getSolicitudEnviosByCatalogoIdTipoSubasta() {
        return solicitudEnviosByCatalogoIdTipoSubasta;
    }

    public void setSolicitudEnviosByCatalogoIdTipoSubasta(List<SolicitudEnvio> solicitudEnviosByCatalogoIdTipoSubasta) {
        this.solicitudEnviosByCatalogoIdTipoSubasta = solicitudEnviosByCatalogoIdTipoSubasta;
    }

    @OneToMany(mappedBy = "catalogoByOfertaEstado")
    public List<Oferta> getOfertasByCatalogoId() {
        return ofertasByCatalogoId;
    }

    public void setOfertasByCatalogoId(List<Oferta> ofertasByCatalogoId) {
        this.ofertasByCatalogoId = ofertasByCatalogoId;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteTipoPersona")
    public List<Cliente> getClientesByCatalogoItemIdTipoPerson() {
        return clientesByCatalogoItemIdTipoPerson;
    }

    public void setClientesByCatalogoItemIdTipoPerson(List<Cliente> clientesByCatalogoItemIdTipoPerson) {
        this.clientesByCatalogoItemIdTipoPerson = clientesByCatalogoItemIdTipoPerson;
    }

    @OneToMany(mappedBy = "conductorByConductorTipoLicencia")
    public List<Conductor> getConductorsByCatalogoItem() {
        return conductorsByCatalogoItem;
    }

    public void setConductorsByCatalogoItem(List<Conductor> conductorsByCatalogoItem) {
        this.conductorsByCatalogoItem = conductorsByCatalogoItem;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteDiasPeriodicidad")
    public List<Cliente> getClientesByCatalogoClienreDiasPeriodicidad() {
        return clientesByCatalogoClienteDiasPeriodicidad;
    }

    public void setClientesByCatalogoClienreDiasPeriodicidad(List<Cliente> clientesByCatalogoClienteDiasPeriodicidad) {
        this.clientesByCatalogoClienteDiasPeriodicidad = clientesByCatalogoClienteDiasPeriodicidad;
    }

    @OneToMany(mappedBy = "catalogoItemByVehiculoTipoCamion")
    public List<Vehiculo> getVehiculosByCatalogoItemByVehiculoTipoCamion() {
        return vehiculosByCatalogoItemByVehiculoTipoCamion;
    }

    public void setVehiculosByCatalogoItemByVehiculoTipoCamion(List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCamion) {
        this.vehiculosByCatalogoItemByVehiculoTipoCamion = vehiculosByCatalogoItemByVehiculoTipoCamion;
    }

    @OneToMany(mappedBy = "catalogoItemByVehiculoTipoCarga")
    public List<Vehiculo> getVehiculosByCatalogoItemByVehiculoTipoCarga() {
        return vehiculosByCatalogoItemByVehiculoTipoCarga;
    }

    public void setVehiculosByCatalogoItemByVehiculoTipoCarga(List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCarga) {
        this.vehiculosByCatalogoItemByVehiculoTipoCarga = vehiculosByCatalogoItemByVehiculoTipoCarga;
    }

    @OneToMany(mappedBy = "catalogoItemByVehiculoTipoCapacidad")
    public List<Vehiculo> getVehiculosByCatalogoItemByVehiculoTipoCapacidad() {
        return vehiculosByCatalogoItemByVehiculoTipoCapacidad;
    }

    public void setVehiculosByCatalogoItemByVehiculoTipoCapacidad(List<Vehiculo> vehiculosByCatalogoItemByVehiculoTipoCapacidad) {
        this.vehiculosByCatalogoItemByVehiculoTipoCapacidad = vehiculosByCatalogoItemByVehiculoTipoCapacidad;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteTipoCliente")
    public List<Cliente> getClientesByCatalogoClienteTipoCliente() {
        return clientesByCatalogoClienteTipoCliente;
    }

    public void setClientesByCatalogoClienteTipoCliente(List<Cliente> clientesByCatalogoClienteTipoCliente) {
        this.clientesByCatalogoClienteTipoCliente = clientesByCatalogoClienteTipoCliente;
    }

    @OneToMany(mappedBy = "catalogoItemByFacturaEstado")
    public List<Factura> getFacturasByCatalogoItermByFacturaEstado() {
        return facturasByCatalogoItermByFacturaEstado;
    }

    public void setFacturasByCatalogoItermByFacturaEstado(List<Factura> facturasByCatalogoItermByFacturaEstado) {
        this.facturasByCatalogoItermByFacturaEstado = facturasByCatalogoItermByFacturaEstado;
    }

    @OneToMany(mappedBy = "catalogoItemByPagoTipoId")
    public List<Pago> getPagosByCatalogoItemByTipo() {
        return pagosByCatalogoItemByTipo;
    }

    public void setPagosByCatalogoItemByTipo(List<Pago> pagosByCatalogoItemByTipo) {
        this.pagosByCatalogoItemByTipo = pagosByCatalogoItemByTipo;
    }

    @OneToMany(mappedBy = "catalogoItemByPagoBancoId")
    public List<Pago> getPagosByCatalogoItemByBanco() {
        return pagosByCatalogoItemByBanco;
    }

    public void setPagosByCatalogoItemByBanco(List<Pago> pagosByCatalogoItemByBanco) {
        this.pagosByCatalogoItemByBanco = pagosByCatalogoItemByBanco;
    }

    @OneToMany(mappedBy = "catalogoItemByFacturaProveedorFormaPago")
    public List<FacturaProveedor> getFacturasProveedorByCatalogoItermByFacturaProveedorFormaPago() {
        return facturasProveedorByCatalogoItermByFacturaProveedorFormaPago;
    }

    public void setFacturasProveedorByCatalogoItermByFacturaProveedorFormaPago(List<FacturaProveedor> facturasProveedorByCatalogoItermByFacturaProveedorFormaPago) {
        this.facturasProveedorByCatalogoItermByFacturaProveedorFormaPago = facturasProveedorByCatalogoItermByFacturaProveedorFormaPago;
    }

    @OneToMany(mappedBy = "catalogoItemByProductoUnidadVolumen")
    public Collection<Producto> getProductosByCatalogoItemId() {
        return productosByCatalogoItemId;
    }

    public void setProductosByCatalogoItemId(Collection<Producto> productosByCatalogoItemId) {
        this.productosByCatalogoItemId = productosByCatalogoItemId;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteBanco")
    public Collection<Cliente> getClientesByCatalogoItemId_2() {
        return clientesByCatalogoItemId_2;
    }

    public void setClientesByCatalogoItemId_2(Collection<Cliente> clientesByCatalogoItemId_2) {
        this.clientesByCatalogoItemId_2 = clientesByCatalogoItemId_2;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteTipoCuenta")
    public Collection<Cliente> getClientesByCatalogoItemId_3() {
        return clientesByCatalogoItemId_3;
    }

    public void setClientesByCatalogoItemId_3(Collection<Cliente> clientesByCatalogoItemId_3) {
        this.clientesByCatalogoItemId_3 = clientesByCatalogoItemId_3;
    }

    @OneToMany(mappedBy = "catalogoItemByClienteBancoTipoIdentificacion")
    public Collection<Cliente> getClientesByCatalogoItemId_4() {
        return clientesByCatalogoItemId_4;
    }

    public void setClientesByCatalogoItemId_4(Collection<Cliente> clientesByCatalogoItemId_4) {
        this.clientesByCatalogoItemId_4 = clientesByCatalogoItemId_4;
    }
}

