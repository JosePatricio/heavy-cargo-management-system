package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "factura_retencion")
public class FacturaRetencion implements Serializable {

    private String facturaRetencionId;
    private String facturaRetencionFacturaId;
    private String facturaRetencionEstado;
    private Timestamp facturaRetencionFechaAutorizacion;
    private String facturaRetencionXml;
    private int facturaRetencionFacturaProveedorId;
    @JsonIgnore
    private Factura facturasByFacturaId;
    @JsonIgnore
    private FacturaProveedor facturaProveedorByFacturaId;

    @Id
    @Column(name = "factura_retencion_id", nullable = false)
    public String getFacturaRetencionId() {
        return facturaRetencionId;
    }

    public void setFacturaRetencionId(String facturaRetencionId) {
        this.facturaRetencionId = facturaRetencionId;
    }

    @Basic
    @Column(name = "factura_retencion_factura_id", nullable = true)
    public String getFacturaRetencionFacturaId() {
        return facturaRetencionFacturaId;
    }

    public void setFacturaRetencionFacturaId(String facturaRetencionFacturaId) {
        this.facturaRetencionFacturaId = facturaRetencionFacturaId;
    }

    @Basic
    @Column(name = "factura_retencion_estado", nullable = true)
    public String getFacturaRetencionEstado() {
        return facturaRetencionEstado;
    }

    public void setFacturaRetencionEstado(String facturaRetencionEstado) {
        this.facturaRetencionEstado = facturaRetencionEstado;
    }

    @Basic
    @Column(name = "factura_retencion_fecha_autorizacion", nullable = true)
    public Timestamp getFacturaRetencionFechaAutorizacion() {
        return facturaRetencionFechaAutorizacion;
    }

    public void setFacturaRetencionFechaAutorizacion(Timestamp facturaRetencionFechaAutorizacion) {
        this.facturaRetencionFechaAutorizacion = facturaRetencionFechaAutorizacion;
    }

    @Basic
    @Column(name = "factura_retencion_xml", nullable = true)
    public String getFacturaRetencionXml() {
        return facturaRetencionXml;
    }

    public void setFacturaRetencionXml(String facturaRetencionXml) {
        this.facturaRetencionXml = facturaRetencionXml;
    }

    @Basic
    @Column(name = "factura_retencion_factura_proveedor_id", nullable = true)
    public int getFacturaRetencionFacturaProveedorId() {
        return facturaRetencionFacturaProveedorId;
    }

    public void setFacturaRetencionFacturaProveedorId(int facturaRetencionFacturaProveedorId) {
        this.facturaRetencionFacturaProveedorId = facturaRetencionFacturaProveedorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacturaRetencion that = (FacturaRetencion) o;
        return Objects.equals(facturaRetencionId, that.facturaRetencionId) &&
                Objects.equals(facturaRetencionFacturaId, that.facturaRetencionFacturaId) &&
                Objects.equals(facturaRetencionEstado, that.facturaRetencionEstado) &&
                Objects.equals(facturaRetencionFechaAutorizacion, that.facturaRetencionFechaAutorizacion) &&
                Objects.equals(facturaRetencionXml, that.facturaRetencionXml) &&
                Objects.equals(facturasByFacturaId, that.facturasByFacturaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facturaRetencionId, facturaRetencionFacturaId, facturaRetencionEstado, facturaRetencionFechaAutorizacion, facturaRetencionXml, facturasByFacturaId);
    }

    @ManyToOne
    @JoinColumn(name = "factura_retencion_factura_id", referencedColumnName = "factura_id", insertable = false, updatable = false)
    public Factura getFacturasByFacturaId() {
        return facturasByFacturaId;
    }

    public void setFacturasByFacturaId(Factura facturasByFacturaId) {
        this.facturasByFacturaId = facturasByFacturaId;
    }

    @ManyToOne
    @JoinColumn(name = "factura_retencion_factura_proveedor_id", referencedColumnName = "factura_proveedor_id", insertable = false, updatable = false)
    public FacturaProveedor getFacturaProveedorByFacturaId() {
        return facturaProveedorByFacturaId;
    }

    public void setFacturaProveedorByFacturaId(FacturaProveedor facturaProveedorByFacturaId) {
        this.facturaProveedorByFacturaId = facturaProveedorByFacturaId;
    }

}
