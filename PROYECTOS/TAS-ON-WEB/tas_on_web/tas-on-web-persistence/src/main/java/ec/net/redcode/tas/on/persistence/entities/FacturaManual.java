package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "factura_manual")
public class FacturaManual {
    private int facturaManualId;
    private String facturaManualUsuarioId;
    private String facturaManualClaveAcceso;
    private String facturaManualSecuencial;
    private Timestamp facturaManualFechaEmision;
    private Timestamp facturaManualFechaAutorizacion;
    private String facturaManualEstado;
    private Double facturaManualTotal;
    private String facturaManualAdquiriente;
    private String facturaManualXml;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "factura_manual_id")
    public int getFacturaManualId() {
        return facturaManualId;
    }

    public void setFacturaManualId(int facturaManualId) {
        this.facturaManualId = facturaManualId;
    }

    @Column(name = "factura_manual_clave_acceso")
    public String getFacturaManualClaveAcceso() {
        return facturaManualClaveAcceso;
    }

    public void setFacturaManualClaveAcceso(String facturaManualClaveAcceso) {
        this.facturaManualClaveAcceso = facturaManualClaveAcceso;
    }

    @Column(name = "factura_manual_fecha_emision")
    public Timestamp getFacturaManualFechaEmision() {
        return facturaManualFechaEmision;
    }

    public void setFacturaManualFechaEmision(Timestamp facturaManualFechaEmision) {
        this.facturaManualFechaEmision = facturaManualFechaEmision;
    }

    @Column(name = "factura_manual_fecha_autorizacion")
    public Timestamp getFacturaManualFechaAutorizacion() {
        return facturaManualFechaAutorizacion;
    }

    public void setFacturaManualFechaAutorizacion(Timestamp facturaManualFechaAutorizacion) {
        this.facturaManualFechaAutorizacion = facturaManualFechaAutorizacion;
    }

    @Column(name = "factura_manual_estado")
    public String getFacturaManualEstado() {
        return facturaManualEstado;
    }

    public void setFacturaManualEstado(String facturaManualEstado) {
        this.facturaManualEstado = facturaManualEstado;
    }

    @Column(name = "factura_manual_total")
    public Double getFacturaManualTotal() {
        return facturaManualTotal;
    }

    public void setFacturaManualTotal(Double facturaManualTotal) {
        this.facturaManualTotal = facturaManualTotal;
    }

    @Column(name = "factura_manual_xml")
    public String getFacturaManualXml() {
        return facturaManualXml;
    }

    public void setFacturaManualXml(String facturaManualXml) {
        this.facturaManualXml = facturaManualXml;
    }

    @Column(name = "factura_manual_usuario_id")
    public String getFacturaManualUsuarioId() {
        return facturaManualUsuarioId;
    }

    public void setFacturaManualUsuarioId(String facturaManualUsuarioId) {
        this.facturaManualUsuarioId = facturaManualUsuarioId;
    }

    @Column(name = "factura_manual_adquiriente")
    public String getFacturaManualAdquiriente() {
        return facturaManualAdquiriente;
    }

    public void setFacturaManualAdquiriente(String facturaManualAdquiriente) {
        this.facturaManualAdquiriente = facturaManualAdquiriente;
    }

    @Column(name="factura_manual_secuencial")
    public String getFacturaManualSecuencial() {
        return facturaManualSecuencial;
    }

    public void setFacturaManualSecuencial(String facturaManualSecuencial) {
        this.facturaManualSecuencial = facturaManualSecuencial;
    }
}
