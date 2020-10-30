package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ebilling")
public class Ebilling {
    private Integer ebillingId;
    private String ebillingNumero;
    private String ebillingUsuarioEbilling;
    private String ebillingUsuarioId;
    private Timestamp ebillingFechaEmision;
    private Timestamp ebillingFechaAutorizacion;
    private String ebillingClaveAcceso;
    private String ebillingEstado;
    private String ebillingGuiaRemision;
    private Double ebillingSubtotal;
    private Double ebillingDescuento;
    private Double ebillingTotal;
    private String ebillingAdquiriente;
    private String ebillingXml;
    private Integer ebillingTipoPago;
    private String ebillingRide;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ebilling_id")
    public Integer getEbillingId() {
        return ebillingId;
    }

    public void setEbillingId(Integer ebillingId) {
        this.ebillingId = ebillingId;
    }

    @Column(name="ebilling_numero")
    public String getEbillingNumero() {
        return ebillingNumero;
    }

    public void setEbillingNumero(String ebillingNumero) {
        this.ebillingNumero = ebillingNumero;
    }

    @Column(name="ebilling_usuario_ebilling")
    public String getEbillingUsuarioEbilling() {
        return ebillingUsuarioEbilling;
    }

    public void setEbillingUsuarioEbilling(String ebillingUsuarioEbilling) {
        this.ebillingUsuarioEbilling = ebillingUsuarioEbilling;
    }

    @Basic
    @Column(name = "ebilling_fecha_emision")
    public Timestamp getEbillingFechaEmision() {
        return ebillingFechaEmision;
    }

    public void setEbillingFechaEmision(Timestamp ebillingFechaEmision) {
        this.ebillingFechaEmision = ebillingFechaEmision;
    }

    @Basic
    @Column(name = "ebilling_fecha_autorizacion")
    public Timestamp getEbillingFechaAutorizacion() {
        return ebillingFechaAutorizacion;
    }

    public void setEbillingFechaAutorizacion(Timestamp ebillingFechaAutorizacion) {
        this.ebillingFechaAutorizacion = ebillingFechaAutorizacion;
    }

    @Basic
    @Column(name = "ebilling_clave_acceso")
    public String getEbillingClaveAcceso() {
        return ebillingClaveAcceso;
    }

    public void setEbillingClaveAcceso(String ebillingClaveAcceso) {
        this.ebillingClaveAcceso = ebillingClaveAcceso;
    }

    @Basic
    @Column(name = "ebilling_estado")
    public String getEbillingEstado() {
        return ebillingEstado;
    }

    public void setEbillingEstado(String ebillingEstado) {
        this.ebillingEstado = ebillingEstado;
    }

    @Basic
    @Column(name = "ebilling_guia_remision")
    public String getEbillingGuiaRemision() {
        return ebillingGuiaRemision;
    }

    public void setEbillingGuiaRemision(String ebillingGuiaRemision) {
        this.ebillingGuiaRemision = ebillingGuiaRemision;
    }

    @Basic
    @Column(name = "ebilling_subtotal")
    public Double getEbillingSubtotal() {
        return ebillingSubtotal;
    }

    public void setEbillingSubtotal(Double ebillingSubtotal) {
        this.ebillingSubtotal = ebillingSubtotal;
    }

    @Basic
    @Column(name = "ebilling_descuento")
    public Double getEbillingDescuento() {
        return ebillingDescuento;
    }

    public void setEbillingDescuento(Double ebillingDescuento) {
        this.ebillingDescuento = ebillingDescuento;
    }

    @Basic
    @Column(name = "ebilling_total")
    public Double getEbillingTotal() {
        return ebillingTotal;
    }

    public void setEbillingTotal(Double ebillingTotal) {
        this.ebillingTotal = ebillingTotal;
    }

    @Basic
    @Column(name = "ebilling_adquiriente")
    public String getEbillingAdquiriente() {
        return ebillingAdquiriente;
    }

    public void setEbillingAdquiriente(String ebillingAdquiriente) {
        this.ebillingAdquiriente = ebillingAdquiriente;
    }

    @Basic
    @Column(name = "ebilling_xml")
    public String getEbillingXml() {
        return ebillingXml;
    }

    public void setEbillingXml(String ebillingXml) {
        this.ebillingXml = ebillingXml;
    }

    @Column(name = "ebilling_tipo_pago")
    public Integer getEbillingTipoPago() {
        return ebillingTipoPago;
    }

    public void setEbillingTipoPago(Integer ebillingTipoPago) {
        this.ebillingTipoPago = ebillingTipoPago;
    }

    @Column(name="ebilling_ride")
    public String getEbillingRide() {
        return ebillingRide;
    }

    public void setEbillingRide(String ebillingRide) {
        this.ebillingRide = ebillingRide;
    }

    @Column(name="ebilling_usuario_id")
    public String getEbillingUsuarioId() {
        return ebillingUsuarioId;
    }

    public void setEbillingUsuarioId(String ebillingUsuarioId) {
        this.ebillingUsuarioId = ebillingUsuarioId;
    }
}
