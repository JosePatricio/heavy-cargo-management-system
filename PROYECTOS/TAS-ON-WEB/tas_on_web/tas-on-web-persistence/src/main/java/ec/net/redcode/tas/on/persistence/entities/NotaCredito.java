package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nota_credito")
public class NotaCredito {
    private int notaCreditoId;
    private String notaCreditoClaveAcceso;
    private String notaCreditoEstado;
    private Timestamp notaCreditoFechaAutorizacion;
    private String notaCreditoXml;
    private String notaCreditoFacturaId;
    private Double notaCreditoValor;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "nota_credito_id")
    public int getNotaCreditoId() {
        return notaCreditoId;
    }

    public void setNotaCreditoId(int notaCreditoId) {
        this.notaCreditoId = notaCreditoId;
    }

    @Column(name = "nota_credito_clave_acceso")
    public String getNotaCreditoClaveAcceso() {
        return notaCreditoClaveAcceso;
    }

    public void setNotaCreditoClaveAcceso(String notaCreditoClaveAcceso) {
        this.notaCreditoClaveAcceso = notaCreditoClaveAcceso;
    }

    @Column(name = "nota_credito_estado")
    public String getNotaCreditoEstado() {
        return notaCreditoEstado;
    }

    public void setNotaCreditoEstado(String notaCreditoEstado) {
        this.notaCreditoEstado = notaCreditoEstado;
    }

    @Column(name = "nota_credito_fecha_autorizacion")
    public Timestamp getNotaCreditoFechaAutorizacion() {
        return notaCreditoFechaAutorizacion;
    }

    public void setNotaCreditoFechaAutorizacion(Timestamp notaCreditoFechaAutorizacion) {
        this.notaCreditoFechaAutorizacion = notaCreditoFechaAutorizacion;
    }

    @Column(name = "nota_credito_xml")
    public String getNotaCreditoXml() {
        return notaCreditoXml;
    }

    public void setNotaCreditoXml(String notaCreditoXml) {
        this.notaCreditoXml = notaCreditoXml;
    }

    @Column(name = "nota_credito_factura_id")
    public String getNotaCreditoFacturaId() {
        return notaCreditoFacturaId;
    }

    public void setNotaCreditoFacturaId(String notaCreditoFacturaId) {
        this.notaCreditoFacturaId = notaCreditoFacturaId;
    }

    @Column(name = "nota_credito_valor")
    public Double getNotaCreditoValor() {
        return notaCreditoValor;
    }

    public void setNotaCreditoValor(Double notaCreditoValor) {
        this.notaCreditoValor = notaCreditoValor;
    }
}
