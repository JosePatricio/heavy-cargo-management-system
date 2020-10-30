package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "factura_detalle")
@NamedQueries({
        @NamedQuery(name = "FacturaDetalle.FacturaDetalleByFactura", query = "select f from FacturaDetalle f where f.facturaDetalleFacturaId = :idFactura"),
        @NamedQuery(name = "FacturaDetalle.FacturaDetalleByOferta", query = "select fd from FacturaDetalle fd, Factura f where fd.facturaDetalleOfertaId = :idOferta and f.facturaTipoFactura = :tipo " +
                "and f.facturaId = fd.facturaDetalleFacturaId")
})
public class FacturaDetalle {

    private int facturaDetalleId;
    private String facturaDetalleFacturaId;
    private int facturaDetalleOfertaId;
    @JsonIgnore private Factura facturasByFacturaId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "factura_detalle_id", nullable = false)
    public int getFacturaDetalleId() {
        return facturaDetalleId;
    }

    public void setFacturaDetalleId(int facturaDetalleId) {
        this.facturaDetalleId = facturaDetalleId;
    }

    @Basic
    @Column(name = "factura_detalle_factura_id", nullable = true)
    public String getFacturaDetalleFacturaId() {
        return facturaDetalleFacturaId;
    }

    public void setFacturaDetalleFacturaId(String facturaDetalleFacturaId) {
        this.facturaDetalleFacturaId = facturaDetalleFacturaId;
    }

    @Basic
    @Column(name = "factura_detalle_oferta_id", nullable = true)
    public int getFacturaDetalleOfertaId() {
        return facturaDetalleOfertaId;
    }

    public void setFacturaDetalleOfertaId(int facturaDetalleOfertaId) {
        this.facturaDetalleOfertaId = facturaDetalleOfertaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacturaDetalle that = (FacturaDetalle) o;
        return facturaDetalleId == that.facturaDetalleId &&
                Objects.equals(facturaDetalleFacturaId, that.facturaDetalleFacturaId) &&
                Objects.equals(facturaDetalleOfertaId, that.facturaDetalleOfertaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facturaDetalleId, facturaDetalleFacturaId, facturaDetalleOfertaId);
    }

    @ManyToOne
    @JoinColumn(name = "factura_detalle_factura_id", referencedColumnName = "factura_id", insertable = false, updatable = false)
    public Factura getFacturasByFacturaId() {
        return facturasByFacturaId;
    }

    public void setFacturasByFacturaId(Factura facturasByFacturaId) {
        this.facturasByFacturaId = facturasByFacturaId;
    }
}
