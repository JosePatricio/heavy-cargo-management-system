package ec.net.redcode.tas.on.persistence.entities;

import ec.net.redcode.tas.on.persistence.component.JsonDateDeserializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "pago")
@NamedQueries({
        @NamedQuery(name = "Pago.PagoByFactura", query = "select p from Pago p where p.pagoFacturaId = :facturaId")
})
public class Pago {

    private int pagoId;
    private String pagoFacturaId;
    private int pagoTipoId;
    private int pagoBancoId;
    private String pagoNumeroDocumento;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp pagoFechaDocumento;
    private Timestamp pagoFechaRegistro;
    private Double pagoValor;
    private String pagoRetencionXml;
    @JsonIgnore
    private Factura facturasByPagoFacturaId;
    @JsonIgnore
    private CatalogoItem catalogoItemByPagoTipoId;
    @JsonIgnore
    private CatalogoItem catalogoItemByPagoBancoId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "pago_id", nullable = false)
    public int getPagoId() {
        return pagoId;
    }

    public void setPagoId(int pagoId) {
        this.pagoId = pagoId;
    }

    @Basic
    @Column(name = "pago_factura_id", nullable = true)
    public String getPagoFacturaId() {
        return pagoFacturaId;
    }

    public void setPagoFacturaId(String pagoFacturaId) {
        this.pagoFacturaId = pagoFacturaId;
    }

    @Basic
    @Column(name = "pago_tipo_id", nullable = true)
    public int getPagoTipoId() {
        return pagoTipoId;
    }

    public void setPagoTipoId(int pagoTipoId) {
        this.pagoTipoId = pagoTipoId;
    }

    @Basic
    @Column(name = "pago_banco_id", nullable = true)
    public int getPagoBancoId() {
        return pagoBancoId;
    }

    public void setPagoBancoId(int pagoBancoId) {
        this.pagoBancoId = pagoBancoId;
    }

    @Basic
    @Column(name = "pago_numero_documento", nullable = true)
    public String getPagoNumeroDocumento() {
        return pagoNumeroDocumento;
    }

    public void setPagoNumeroDocumento(String pagoNumeroDocumento) {
        this.pagoNumeroDocumento = pagoNumeroDocumento;
    }

    @Basic
    @Column(name = "pago_fecha_documento", nullable = true)
    public Timestamp getPagoFechaDocumento() {
        return pagoFechaDocumento;
    }

    public void setPagoFechaDocumento(Timestamp pagoFechaDocumento) {
        this.pagoFechaDocumento = pagoFechaDocumento;
    }

    @Basic
    @Column(name = "pago_fecha_registro", nullable = true)
    public Timestamp getPagoFechaRegistro() {
        return pagoFechaRegistro;
    }

    public void setPagoFechaRegistro(Timestamp pagoFechaRegistro) {
        this.pagoFechaRegistro = pagoFechaRegistro;
    }

    @Basic
    @Column(name = "pago_valor", nullable = true)
    public Double getPagoValor() {
        return pagoValor;
    }

    public void setPagoValor(Double pagoValor) {
        this.pagoValor = pagoValor;
    }

    @Basic
    @Column(name = "pago_retencion_xml", nullable = true)
    public String getPagoRetencionXml() {
        return pagoRetencionXml;
    }

    public void setPagoRetencionXml(String pagoRetencionXml) {
        this.pagoRetencionXml = pagoRetencionXml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return pagoId == pago.pagoId &&
                pagoTipoId == pago.pagoTipoId &&
                pagoBancoId == pago.pagoBancoId &&
                Objects.equals(pagoFacturaId, pago.pagoFacturaId) &&
                Objects.equals(pagoNumeroDocumento, pago.pagoNumeroDocumento) &&
                Objects.equals(pagoFechaDocumento, pago.pagoFechaDocumento) &&
                Objects.equals(pagoFechaRegistro, pago.pagoFechaRegistro) &&
                Objects.equals(pagoValor, pago.pagoValor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagoId, pagoFacturaId, pagoTipoId, pagoBancoId, pagoNumeroDocumento, pagoFechaDocumento, pagoFechaRegistro, pagoValor);
    }

    @ManyToOne
    @JoinColumn(name = "pago_factura_id", referencedColumnName = "factura_id", insertable = false, updatable = false)
    public Factura getFacturasByPagoFacturaId() {
        return facturasByPagoFacturaId;
    }

    public void setFacturasByPagoFacturaId(Factura facturasByPagoFacturaId) {
        this.facturasByPagoFacturaId = facturasByPagoFacturaId;
    }

    @ManyToOne
    @JoinColumn(name = "pago_tipo_id", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByPagoTipoId() {
        return catalogoItemByPagoTipoId;
    }

    public void setCatalogoItemByPagoTipoId(CatalogoItem catalogoItemByPagoTipoId) {
        this.catalogoItemByPagoTipoId = catalogoItemByPagoTipoId;
    }

    @ManyToOne
    @JoinColumn(name = "pago_banco_id", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByPagoBancoId() {
        return catalogoItemByPagoBancoId;
    }

    public void setCatalogoItemByPagoBancoId(CatalogoItem catalogoItemByPagoBancoId) {
        this.catalogoItemByPagoBancoId = catalogoItemByPagoBancoId;
    }
}

