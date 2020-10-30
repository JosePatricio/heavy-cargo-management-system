package ec.net.redcode.tas.on.persistence.entities;

import ec.net.redcode.tas.on.persistence.component.JsonDateDeserializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "factura_proveedor")
@NamedQueries({
        @NamedQuery(name = "FacturaProveedorByFacturaProveedorEstado", query = "select f from FacturaProveedor f where f.facturaProveedorEstado = :estado"),
        @NamedQuery(name = "FacturaProveedorByfacturaProveedorNumero", query = "select f from FacturaProveedor f where f.facturaProveedorNumero = :numero"),
        @NamedQuery(name = "FacturaProveedorByfacturaProveedorNumeroAndFacturaProveedorRucCliente", query = "select f from FacturaProveedor f where f.facturaProveedorNumero = :numero " +
                "and f.facturaProveedorRucCliente = :ruc")
})
public class FacturaProveedor implements Serializable {

    private int facturaProveedorId;
    private String facturaProveedorUsuario;
    private String facturaProveedorRucCliente;
    private String facturaProveedorNumero;
    private String facturaProveedorNumeroAutorizacion;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp facturaProveedorFechaAutorizacion;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp facturaProveedorFechaEmision;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp facturaProveedorFechaRecepcion;
    private Double facturaProveedorSubtotal;
    private Double facturaProveedorIva;
    private Double facturaProveedorRetencion;
    private Double facturaProveedorTotal;
    private Double facturaProveedorSubTotal12;
    private Double facturaProveedorSubTotal0;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp facturaProveedorFechaPago;
    private Integer facturaProveedorFormaPago;
    private Integer facturaProveedorCompra;
    private Integer facturaProveedorEstado;
    private String facturaProveedorTransferencia;
    @JsonIgnore
    private Usuario usuarioByFacturaProveedorUsuario;
    @JsonIgnore
    private CatalogoItem catalogoItemByFacturaProveedorFormaPago;
    @JsonIgnore
    private List<FacturaRetencion> facturaRetencionsByFacturaProveedorId;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "factura_proveedor_id", nullable = false)
    public int getFacturaProveedorId() {
        return facturaProveedorId;
    }

    public void setFacturaProveedorId(int facturaProveedorId) {
        this.facturaProveedorId = facturaProveedorId;
    }

    @Basic
    @Column(name = "factura_proveedor_usuario", nullable = true)
    public String getFacturaProveedorUsuario() {
        return facturaProveedorUsuario;
    }

    public void setFacturaProveedorUsuario(String facturaProveedorUsuario) {
        this.facturaProveedorUsuario = facturaProveedorUsuario;
    }

    @Basic
    @Column(name = "factura_proveedor_ruc_cliente", nullable = true)
    public String getFacturaProveedorRucCliente() {
        return facturaProveedorRucCliente;
    }

    public void setFacturaProveedorRucCliente(String facturaProveedorRucCliente) {
        this.facturaProveedorRucCliente = facturaProveedorRucCliente;
    }

    @Basic
    @Column(name = "factura_proveedor_numero", nullable = true)
    public String getFacturaProveedorNumero() {
        return facturaProveedorNumero;
    }

    public void setFacturaProveedorNumero(String facturaProveedorNumero) {
        this.facturaProveedorNumero = facturaProveedorNumero;
    }

    @Basic
    @Column(name = "factura_proveedor_numero_autorizacion", nullable = true)
    public String getFacturaProveedorNumeroAutorizacion() {
        return facturaProveedorNumeroAutorizacion;
    }

    public void setFacturaProveedorNumeroAutorizacion(String facturaProveedorNumeroAutorizacion) {
        this.facturaProveedorNumeroAutorizacion = facturaProveedorNumeroAutorizacion;
    }

    @Basic
    @Column(name = "factura_proveedor_fecha_autorizacion", nullable = true)
    public Timestamp getFacturaProveedorFechaAutorizacion() {
        return facturaProveedorFechaAutorizacion;
    }

    public void setFacturaProveedorFechaAutorizacion(Timestamp facturaProveedorFechaAutorizacion) {
        this.facturaProveedorFechaAutorizacion = facturaProveedorFechaAutorizacion;
    }

    @Basic
    @Column(name = "factura_proveedor_fecha_emision", nullable = true)
    public Timestamp getFacturaProveedorFechaEmision() {
        return facturaProveedorFechaEmision;
    }

    public void setFacturaProveedorFechaEmision(Timestamp facturaProveedorFechaEmision) {
        this.facturaProveedorFechaEmision = facturaProveedorFechaEmision;
    }

    @Basic
    @Column(name = "factura_proveedor_fecha_recepcion", nullable = true)
    public Timestamp getFacturaProveedorFechaRecepcion() {
        return facturaProveedorFechaRecepcion;
    }

    public void setFacturaProveedorFechaRecepcion(Timestamp facturaProveedorFechaRecepcion) {
        this.facturaProveedorFechaRecepcion = facturaProveedorFechaRecepcion;
    }

    @Basic
    @Column(name = "factura_proveedor_subtotal", nullable = true)
    public Double getFacturaProveedorSubtotal() {
        return facturaProveedorSubtotal;
    }

    public void setFacturaProveedorSubtotal(Double facturaProveedorSubtotal) {
        this.facturaProveedorSubtotal = facturaProveedorSubtotal;
    }

    @Basic
    @Column(name = "factura_proveedor_iva", nullable = true)
    public Double getFacturaProveedorIva() {
        return facturaProveedorIva;
    }

    public void setFacturaProveedorIva(Double facturaProveedorIva) {
        this.facturaProveedorIva = facturaProveedorIva;
    }

    @Basic
    @Column(name = "factura_proveedor_retencion", nullable = true)
    public Double getFacturaProveedorRetencion() {
        return facturaProveedorRetencion;
    }

    public void setFacturaProveedorRetencion(Double facturaProveedorRetencion) {
        this.facturaProveedorRetencion = facturaProveedorRetencion;
    }

    @Basic
    @Column(name = "factura_proveedor_total", nullable = true)
    public Double getFacturaProveedorTotal() {
        return facturaProveedorTotal;
    }

    public void setFacturaProveedorTotal(Double facturaProveedorTotal) {
        this.facturaProveedorTotal = facturaProveedorTotal;
    }

    @Basic
    @Column(name = "factura_proveedor_subtotal12", nullable = true)
    public Double getFacturaProveedorSubTotal12() {
        return facturaProveedorSubTotal12;
    }

    public void setFacturaProveedorSubTotal12(Double facturaProveedorSubTotal12) {
        this.facturaProveedorSubTotal12 = facturaProveedorSubTotal12;
    }

    @Basic
    @Column(name = "factura_proveedor_subtotal0", nullable = true)
    public Double getFacturaProveedorSubTotal0() {
        return facturaProveedorSubTotal0;
    }

    public void setFacturaProveedorSubTotal0(Double facturaProveedorSubTotal0) {
        this.facturaProveedorSubTotal0 = facturaProveedorSubTotal0;
    }

    @Basic
    @Column(name = "factura_proveedor_fecha_pago", nullable = true)
    public Timestamp getFacturaProveedorFechaPago() {
        return facturaProveedorFechaPago;
    }

    public void setFacturaProveedorFechaPago(Timestamp facturaProveedorFechaPago) {
        this.facturaProveedorFechaPago = facturaProveedorFechaPago;
    }

    @Basic
    @Column(name = "factura_proveedor_forma_pago", nullable = true)
    public Integer getFacturaProveedorFormaPago() {
        return facturaProveedorFormaPago;
    }

    public void setFacturaProveedorFormaPago(Integer facturaProveedorFormaPago) {
        this.facturaProveedorFormaPago = facturaProveedorFormaPago;
    }

    @Basic
    @Column(name = "factura_proveedor_compra", nullable = true)
    public Integer getFacturaProveedorCompra() {
        return facturaProveedorCompra;
    }

    public void setFacturaProveedorCompra(Integer facturaProveedorCompra) {
        this.facturaProveedorCompra = facturaProveedorCompra;
    }

    @Basic
    @Column(name = "factura_proveedor_estado", nullable = true)
    public Integer getFacturaProveedorEstado() {
        return facturaProveedorEstado;
    }

    public void setFacturaProveedorEstado(Integer facturaProveedorEstado) {
        this.facturaProveedorEstado = facturaProveedorEstado;
    }

    @Basic
    @Column(name = "factura_proveedor_transferencia", nullable = true)
    public String getFacturaProveedorTransferencia() {
        return facturaProveedorTransferencia;
    }

    public void setFacturaProveedorTransferencia(String facturaProveedorTransferencia) {
        this.facturaProveedorTransferencia = facturaProveedorTransferencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacturaProveedor that = (FacturaProveedor) o;
        return facturaProveedorId == that.facturaProveedorId &&
                facturaProveedorFormaPago == that.facturaProveedorFormaPago &&
                facturaProveedorCompra == that.facturaProveedorCompra &&
                facturaProveedorEstado == that.facturaProveedorEstado &&
                Objects.equals(facturaProveedorUsuario, that.facturaProveedorUsuario) &&
                Objects.equals(facturaProveedorRucCliente, that.facturaProveedorRucCliente) &&
                Objects.equals(facturaProveedorNumero, that.facturaProveedorNumero) &&
                Objects.equals(facturaProveedorNumeroAutorizacion, that.facturaProveedorNumeroAutorizacion) &&
                Objects.equals(facturaProveedorFechaAutorizacion, that.facturaProveedorFechaAutorizacion) &&
                Objects.equals(facturaProveedorFechaEmision, that.facturaProveedorFechaEmision) &&
                Objects.equals(facturaProveedorFechaRecepcion, that.facturaProveedorFechaRecepcion) &&
                Objects.equals(facturaProveedorSubtotal, that.facturaProveedorSubtotal) &&
                Objects.equals(facturaProveedorIva, that.facturaProveedorIva) &&
                Objects.equals(facturaProveedorRetencion, that.facturaProveedorRetencion) &&
                Objects.equals(facturaProveedorTotal, that.facturaProveedorTotal) &&
                Objects.equals(facturaProveedorFechaPago, that.facturaProveedorFechaPago) &&
                Objects.equals(facturaProveedorTransferencia, that.facturaProveedorTransferencia) &&
                Objects.equals(usuarioByFacturaProveedorUsuario, that.usuarioByFacturaProveedorUsuario) &&
                Objects.equals(catalogoItemByFacturaProveedorFormaPago, that.catalogoItemByFacturaProveedorFormaPago) &&
                Objects.equals(facturaRetencionsByFacturaProveedorId, that.facturaRetencionsByFacturaProveedorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facturaProveedorId, facturaProveedorUsuario, facturaProveedorRucCliente, facturaProveedorNumero, facturaProveedorNumeroAutorizacion, facturaProveedorFechaAutorizacion, facturaProveedorFechaEmision, facturaProveedorFechaRecepcion, facturaProveedorSubtotal, facturaProveedorIva, facturaProveedorRetencion, facturaProveedorTotal, facturaProveedorFechaPago, facturaProveedorFormaPago, facturaProveedorCompra, facturaProveedorEstado, facturaProveedorTransferencia, usuarioByFacturaProveedorUsuario, catalogoItemByFacturaProveedorFormaPago, facturaRetencionsByFacturaProveedorId);
    }

    @ManyToOne
    @JoinColumn(name = "factura_proveedor_usuario", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false )
    public Usuario getUsuarioByFacturaProveedorUsuario() {
        return usuarioByFacturaProveedorUsuario;
    }

    public void setUsuarioByFacturaProveedorUsuario(Usuario usuarioByFacturaProveedorUsuario) {
        this.usuarioByFacturaProveedorUsuario = usuarioByFacturaProveedorUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "factura_proveedor_forma_pago", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByFacturaProveedorFormaPago() {
        return catalogoItemByFacturaProveedorFormaPago;
    }

    public void setCatalogoItemByFacturaProveedorFormaPago(CatalogoItem catalogoItemByFacturaProveedorFormaPago) {
        this.catalogoItemByFacturaProveedorFormaPago = catalogoItemByFacturaProveedorFormaPago;
    }

    @OneToMany(mappedBy = "facturaProveedorByFacturaId")
    public List<FacturaRetencion> getFacturaRetencionsByFacturaProveedorId() {
        return facturaRetencionsByFacturaProveedorId;
    }

    public void setFacturaRetencionsByFacturaProveedorId(List<FacturaRetencion> facturaRetencionsByFacturaProveedorId) {
        this.facturaRetencionsByFacturaProveedorId = facturaRetencionsByFacturaProveedorId;
    }
}
