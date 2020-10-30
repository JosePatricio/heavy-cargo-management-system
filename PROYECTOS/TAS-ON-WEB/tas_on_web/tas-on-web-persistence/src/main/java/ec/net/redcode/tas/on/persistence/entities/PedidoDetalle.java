package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pedido_detalle")
public class PedidoDetalle {
    private int pedidoDetalleId;
    private int pedidoDetallePedidoId;
    private int pedidoDetalleProductoId;
    private int pedidoDetalleProductoCantidad;
    private double pedidoDetalleTotalSinImp;
    private double pedidoDetalleTotalConImp;
    private double pedidoDetallePrecioConImp;
    private double pedidoDetallePrecioSinImp;
    @JsonIgnore private Pedido pedidoByPedidoDetallePedidoId;
    @JsonIgnore private Producto productoByPedidoDetalleProductoId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "pedido_detalle_id")
    public int getPedidoDetalleId() {
        return pedidoDetalleId;
    }

    public void setPedidoDetalleId(int pedidoDetalleId) {
        this.pedidoDetalleId = pedidoDetalleId;
    }

    @Basic
    @Column(name = "pedido_detalle_pedido_id")
    public int getPedidoDetallePedidoId() {
        return pedidoDetallePedidoId;
    }

    public void setPedidoDetallePedidoId(int pedidoDetallePedidoId) {
        this.pedidoDetallePedidoId = pedidoDetallePedidoId;
    }

    @Basic
    @Column(name = "pedido_detalle_producto_id")
    public int getPedidoDetalleProductoId() {
        return pedidoDetalleProductoId;
    }

    public void setPedidoDetalleProductoId(int pedidoDetalleProductoId) {
        this.pedidoDetalleProductoId = pedidoDetalleProductoId;
    }

    @Basic
    @Column(name = "pedido_detalle_producto_cantidad")
    public int getPedidoDetalleProductoCantidad() {
        return pedidoDetalleProductoCantidad;
    }

    public void setPedidoDetalleProductoCantidad(int pedidoDetalleProductoCantidad) {
        this.pedidoDetalleProductoCantidad = pedidoDetalleProductoCantidad;
    }

    @Basic
    @Column(name = "pedido_detalle_total_sin_imp")
    public double getPedidoDetalleTotalSinImp() {
        return pedidoDetalleTotalSinImp;
    }

    public void setPedidoDetalleTotalSinImp(double pedidoDetalleTotalSinImp) {
        this.pedidoDetalleTotalSinImp = pedidoDetalleTotalSinImp;
    }

    @Basic
    @Column(name = "pedido_detalle_total_con_imp")
    public double getPedidoDetalleTotalConImp() {
        return pedidoDetalleTotalConImp;
    }

    public void setPedidoDetalleTotalConImp(double pedidoDetalleTotalConImp) {
        this.pedidoDetalleTotalConImp = pedidoDetalleTotalConImp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoDetalle that = (PedidoDetalle) o;
        return pedidoDetalleId == that.pedidoDetalleId &&
                pedidoDetallePedidoId == that.pedidoDetallePedidoId &&
                pedidoDetalleProductoId == that.pedidoDetalleProductoId &&
                pedidoDetalleProductoCantidad == that.pedidoDetalleProductoCantidad &&
                Double.compare(that.pedidoDetalleTotalSinImp, pedidoDetalleTotalSinImp) == 0 &&
                Double.compare(that.pedidoDetalleTotalConImp, pedidoDetalleTotalConImp) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoDetalleId, pedidoDetallePedidoId, pedidoDetalleProductoId, pedidoDetalleProductoCantidad, pedidoDetalleTotalSinImp, pedidoDetalleTotalConImp);
    }

    @ManyToOne
    @JoinColumn(name = "pedido_detalle_pedido_id", referencedColumnName = "pedido_id", insertable = false, updatable = false, nullable = false)
    public Pedido getPedidoByPedidoDetallePedidoId() {
        return pedidoByPedidoDetallePedidoId;
    }

    public void setPedidoByPedidoDetallePedidoId(Pedido pedidoByPedidoDetallePedidoId) {
        this.pedidoByPedidoDetallePedidoId = pedidoByPedidoDetallePedidoId;
    }

    @ManyToOne
    @JoinColumn(name = "pedido_detalle_producto_id", referencedColumnName = "producto_id", insertable = false, updatable = false, nullable = false)
    public Producto getProductoByPedidoDetalleProductoId() {
        return productoByPedidoDetalleProductoId;
    }

    public void setProductoByPedidoDetalleProductoId(Producto productoByPedidoDetalleProductoId) {
        this.productoByPedidoDetalleProductoId = productoByPedidoDetalleProductoId;
    }

    @Basic
    @Column(name = "pedido_detalle_precio_con_imp")
    public double getPedidoDetallePrecioConImp() {
        return pedidoDetallePrecioConImp;
    }

    public void setPedidoDetallePrecioConImp(double pedidoDetallePrecioConImp) {
        this.pedidoDetallePrecioConImp = pedidoDetallePrecioConImp;
    }

    @Basic
    @Column(name = "pedido_detalle_precio_sin_imp")
    public double getPedidoDetallePrecioSinImp() {
        return pedidoDetallePrecioSinImp;
    }

    public void setPedidoDetallePrecioSinImp(double pedidoDetallePrecioSinImp) {
        this.pedidoDetallePrecioSinImp = pedidoDetallePrecioSinImp;
    }
}
