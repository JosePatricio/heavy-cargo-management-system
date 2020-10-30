package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ebilling_detalle")
public class EbillingDetalle {
    private int ebillingDetalleId;
    private int ebillingDetalleEbillingId;
    private Integer ebillingDetallePiezas;
    private Integer ebillingDetalleUnidadPiezas;
    private String ebillingDetalleDetallesAdicionales;
    private double ebillingDetallePrecio;
    private Double ebillingDetalleDescuento;
    private Integer ebillingDetalleOrigen;
    private Integer ebillingDetalleDestino;

    @Id
    @Column(name = "ebilling_detalle_id")
    public int getEbillingDetalleId() {
        return ebillingDetalleId;
    }

    public void setEbillingDetalleId(int ebillingDetalleId) {
        this.ebillingDetalleId = ebillingDetalleId;
    }

    @Column(name="ebilling_detalle_ebilling_id")
    public int getEbillingDetalleEbillingId() {
        return ebillingDetalleEbillingId;
    }

    public void setEbillingDetalleEbillingId(int ebillingDetalleEbillingId) {
        this.ebillingDetalleEbillingId = ebillingDetalleEbillingId;
    }

    @Basic
    @Column(name = "ebilling_detalle_piezas")
    public Integer getEbillingDetallePiezas() {
        return ebillingDetallePiezas;
    }

    public void setEbillingDetallePiezas(Integer ebillingDetallePiezas) {
        this.ebillingDetallePiezas = ebillingDetallePiezas;
    }

    @Basic
    @Column(name = "ebilling_detalle_detalles_adicionales")
    public String getEbillingDetalleDetallesAdicionales() {
        return ebillingDetalleDetallesAdicionales;
    }

    public void setEbillingDetalleDetallesAdicionales(String ebillingDetalleDetallesAdicionales) {
        this.ebillingDetalleDetallesAdicionales = ebillingDetalleDetallesAdicionales;
    }

    @Basic
    @Column(name = "ebilling_detalle_precio")
    public double getEbillingDetallePrecio() {
        return ebillingDetallePrecio;
    }

    public void setEbillingDetallePrecio(double ebillingDetallePrecio) {
        this.ebillingDetallePrecio = ebillingDetallePrecio;
    }

    @Basic
    @Column(name = "ebilling_detalle_descuento")
    public Double getEbillingDetalleDescuento() {
        return ebillingDetalleDescuento;
    }

    public void setEbillingDetalleDescuento(Double ebillingDetalleDescuento) {
        this.ebillingDetalleDescuento = ebillingDetalleDescuento;
    }

    @Column(name = "ebilling_detalle_origen")
    public Integer getEbillingDetalleOrigen() {
        return ebillingDetalleOrigen;
    }

    public void setEbillingDetalleOrigen(Integer ebillingDetalleOrigen) {
        this.ebillingDetalleOrigen = ebillingDetalleOrigen;
    }

    @Column(name = "ebilling_detalle_destino")
    public Integer getEbillingDetalleDestino() {
        return ebillingDetalleDestino;
    }

    public void setEbillingDetalleDestino(Integer ebillingDetalleDestino) {
        this.ebillingDetalleDestino = ebillingDetalleDestino;
    }

    @Column(name = "ebilling_detalle_unidad_piezas")
    public Integer getEbillingDetalleUnidadPiezas() {
        return ebillingDetalleUnidadPiezas;
    }

    public void setEbillingDetalleUnidadPiezas(Integer ebillingDetalleUnidadPiezas) {
        this.ebillingDetalleUnidadPiezas = ebillingDetalleUnidadPiezas;
    }
}
