package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "factura")
@NamedQueries({
        @NamedQuery(name = "Factura.FacturaByEstado", query = "select f from Factura f where f.facturaEstado = :estado"),
        @NamedQuery(name = "Factura.FacturaByUsuarioAndEstado", query = "select f from Factura f where f.facturaUsuarioId = :usuario and f.facturaEstado = :estado")
})
public class Factura {

    private String facturaId;
    private String facturaNro;
    private String facturaUsuarioId;
    private Timestamp facturaFechaEmisionPrefactura;
    private Timestamp facturaFechaEmision;
    private Timestamp facturaFechaAutorizacion;
    private String facturaNroAutorizacion;
    private Integer facturaTipoFactura;
    private String facturaPersonaEntrega;
    private Integer facturaEstado;
    private String facturaGuiaRemision;
    private Double facturaSubtotal;
    private Double facturaTotal;
    private Double facturaComision;
    private String facturaUsuarioRecibe;
    private Timestamp facturaFechaRecepcion;
    private String facturaEmpresa;
    private int facturaTipoPago;
    private double facturaDescuento;
    private String facturaXml;
    private double facturaRetencion;
    @JsonIgnore
    private Collection<FacturaDetalle> facturaDetallesByFacturaid;
    @JsonIgnore
    private Usuario usuarioByFacturaUsuarioId;
    @JsonIgnore
    private CatalogoItem catalogoItemByFacturaEstado;
    @JsonIgnore
    private List<Pago> PagosByFacturaId;
    @JsonIgnore
    private List<FacturaRetencion> facturaRetencionsByFacturaId;

    @Id
    @Column(name = "factura_id", nullable = false)
    public String getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(String facturaId) {
        this.facturaId = facturaId;
    }

    @Basic
    @Column(name = "factura_nro", nullable = true, length = 20)
    public String getFacturaNro() {
        return facturaNro;
    }

    public void setFacturaNro(String facturaNro) {
        this.facturaNro = facturaNro;
    }

    @Basic
    @Column(name = "factura_usuario_id", nullable = true, length = 13)
    public String getFacturaUsuarioId() {
        return facturaUsuarioId;
    }

    public void setFacturaUsuarioId(String facturaUsuarioId) {
        this.facturaUsuarioId = facturaUsuarioId;
    }

    @Basic
    @Column(name = "factura_fecha_emision_prefactura", nullable = true)
    public Timestamp getFacturaFechaEmisionPrefactura() {
        return facturaFechaEmisionPrefactura;
    }

    public void setFacturaFechaEmisionPrefactura(Timestamp facturaFechaEmisionPrefactura) {
        this.facturaFechaEmisionPrefactura = facturaFechaEmisionPrefactura;
    }

    @Basic
    @Column(name = "factura_fecha_emision", nullable = true)
    public Timestamp getFacturaFechaEmision() {
        return facturaFechaEmision;
    }

    public void setFacturaFechaEmision(Timestamp facturaFechaEmision) {
        this.facturaFechaEmision = facturaFechaEmision;
    }

    @Basic
    @Column(name = "factura_fecha_autorizacion", nullable = true)
    public Timestamp getFacturaFechaAutorizacion() {
        return facturaFechaAutorizacion;
    }

    public void setFacturaFechaAutorizacion(Timestamp facturaFechaAutorizacion) {
        this.facturaFechaAutorizacion = facturaFechaAutorizacion;
    }

    @Basic
    @Column(name = "factura_nro_autorizacion", nullable = true)
    public String getFacturaNroAutorizacion() {
        return facturaNroAutorizacion;
    }

    public void setFacturaNroAutorizacion(String facturaNroAutorizacion) {
        this.facturaNroAutorizacion = facturaNroAutorizacion;
    }

    @Basic
    @Column(name = "factura_tipo_factura", nullable = true)
    public Integer getFacturaTipoFactura() {
        return facturaTipoFactura;
    }

    public void setFacturaTipoFactura(Integer facturaTipoFactura) {
        this.facturaTipoFactura = facturaTipoFactura;
    }

    @Basic
    @Column(name = "factura_persona_entrega", nullable = true, length = 100)
    public String getFacturaPersonaEntrega() {
        return facturaPersonaEntrega;
    }

    public void setFacturaPersonaEntrega(String facturaPersonaEntrega) {
        this.facturaPersonaEntrega = facturaPersonaEntrega;
    }

    @Basic
    @Column(name = "factura_estado", nullable = true)
    public Integer getFacturaEstado() {
        return facturaEstado;
    }

    public void setFacturaEstado(Integer facturaEstado) {
        this.facturaEstado = facturaEstado;
    }

    @Basic
    @Column(name = "factura_guia_remision", nullable = true, length = 20)
    public String getFacturaGuiaRemision() {
        return facturaGuiaRemision;
    }

    public void setFacturaGuiaRemision(String facturaGuiaRemision) {
        this.facturaGuiaRemision = facturaGuiaRemision;
    }

    @Basic
    @Column(name = "factura_subtotal", nullable = true)
    public Double getFacturaSubtotal() {
        return facturaSubtotal;
    }

    public void setFacturaSubtotal(Double facturaSubtotal) {
        this.facturaSubtotal = facturaSubtotal;
    }

    @Basic
    @Column(name = "factura_total", nullable = true)
    public Double getFacturaTotal() {
        return facturaTotal;
    }

    public void setFacturaTotal(Double facturaTotal) {
        this.facturaTotal = facturaTotal;
    }

    @Basic
    @Column(name = "factura_comision", nullable = true)
    public Double getFacturaComision() {
        return facturaComision;
    }

    public void setFacturaComision(Double facturaComision) {
        this.facturaComision = facturaComision;
    }

    @Basic
    @Column(name = "factura_usuario_recibe", nullable = true)
    public String getFacturaUsuarioRecibe() {
        return facturaUsuarioRecibe;
    }

    public void setFacturaUsuarioRecibe(String facturaUsuarioRecibe) {
        this.facturaUsuarioRecibe = facturaUsuarioRecibe;
    }

    @Basic
    @Column(name = "factura_fecha_recepcion", nullable = true)
    public Timestamp getFacturaFechaRecepcion() {
        return facturaFechaRecepcion;
    }

    public void setFacturaFechaRecepcion(Timestamp facturaFechaRecepcion) {
        this.facturaFechaRecepcion = facturaFechaRecepcion;
    }

    @Basic
    @Column(name = "factura_empresa", nullable = false)
    public String getFacturaEmpresa() {
        return facturaEmpresa;
    }

    public void setFacturaEmpresa(String facturaEmpresa) {
        this.facturaEmpresa = facturaEmpresa;
    }

    @Basic
    @Column(name = "factura_tipo_pago", nullable = false)
    public int getFacturaTipoPago() {
        return facturaTipoPago;
    }

    public void setFacturaTipoPago(int facturaTipoPago) {
        this.facturaTipoPago = facturaTipoPago;
    }

    @Basic
    @Column(name = "factura_descuento", nullable = false)
    public double getFacturaDescuento() {
        return facturaDescuento;
    }

    public void setFacturaDescuento(double facturaDescuento) {
        this.facturaDescuento = facturaDescuento;
    }

    @Basic
    @Column(name = "factura_xml", nullable = false)
    public String getFacturaXml() {
        return facturaXml;
    }

    public void setFacturaXml(String facturaXml) {
        this.facturaXml = facturaXml;
    }

    @Basic
    @Column(name = "factura_retencion", nullable = false)
    public double getFacturaRetencion() {
        return facturaRetencion;
    }

    public void setFacturaRetencion(double facturaRetencion) {
        this.facturaRetencion = facturaRetencion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(facturaId, factura.facturaId) &&
                Objects.equals(facturaNro, factura.facturaNro) &&
                Objects.equals(facturaUsuarioId, factura.facturaUsuarioId) &&
                Objects.equals(facturaFechaEmision, factura.facturaFechaEmision) &&
                Objects.equals(facturaTipoFactura, factura.facturaTipoFactura) &&
                Objects.equals(facturaEstado, factura.facturaEstado) &&
                Objects.equals(facturaGuiaRemision, factura.facturaGuiaRemision) &&
                Objects.equals(facturaSubtotal, factura.facturaSubtotal) &&
                Objects.equals(facturaTotal, factura.facturaTotal) &&
                Objects.equals(facturaComision, factura.facturaComision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facturaId, facturaNro, facturaUsuarioId, facturaFechaEmision, facturaTipoFactura, facturaEstado, facturaGuiaRemision, facturaSubtotal, facturaTotal, facturaComision);
    }

    @OneToMany(mappedBy = "facturasByFacturaId")
    public Collection<FacturaDetalle> getFacturaDetallesByFacturaid() {
        return facturaDetallesByFacturaid;
    }

    public void setFacturaDetallesByFacturaid(Collection<FacturaDetalle> facturaDetallesByFacturaid) {
        this.facturaDetallesByFacturaid = facturaDetallesByFacturaid;
    }

    @ManyToOne
    @JoinColumn(name = "factura_usuario_id", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false )
    public Usuario getUsuarioByFacturaUsuarioId() {
        return usuarioByFacturaUsuarioId;
    }

    public void setUsuarioByFacturaUsuarioId(Usuario usuarioByFacturaUsuarioId) {
        this.usuarioByFacturaUsuarioId = usuarioByFacturaUsuarioId;
    }

    @ManyToOne
    @JoinColumn(name = "factura_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByFacturaEstado() {
        return catalogoItemByFacturaEstado;
    }

    public void setCatalogoItemByFacturaEstado(CatalogoItem catalogoItemByFacturaEstado) {
        this.catalogoItemByFacturaEstado = catalogoItemByFacturaEstado;
    }

    @OneToMany(mappedBy = "facturasByPagoFacturaId")
    public List<Pago> getPagosByFacturaId() {
        return PagosByFacturaId;
    }

    public void setPagosByFacturaId(List<Pago> pagosByFacturaId) {
        PagosByFacturaId = pagosByFacturaId;
    }

    @OneToMany(mappedBy = "facturasByFacturaId")
    public List<FacturaRetencion> getFacturaRetencionsByFacturaId() {
        return facturaRetencionsByFacturaId;
    }

    public void setFacturaRetencionsByFacturaId(List<FacturaRetencion> facturaRetencionsByFacturaId) {
        this.facturaRetencionsByFacturaId = facturaRetencionsByFacturaId;
    }
}
