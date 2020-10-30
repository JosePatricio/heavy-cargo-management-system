package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "producto")
public class Producto {
    private int productoId;
    private String productoCodigo;
    private Integer productoCategoria;
    private String productoNombre;
    private String productoDescripcion;
    private Double productoVolumen;
    private Integer productoUnidadVolumen;
    private Integer productoUnidadesPorCaja;
    private double productoPrecioSinImp;
    private double productoPrecioConImp;
    private Double productoPrecioPvpSinImp;
    private Double productoPrecioPvpConImp;
    private Timestamp productoFechaCreacion;
    private String productoUsuarioCrea;
    private int productoEstado;
    @JsonIgnore private Collection<PedidoDetalle> pedidoDetallesByProductoId;
    @JsonIgnore private CategoriaProducto categoriaProductoByProductoCategoria;
    @JsonIgnore private CatalogoItem catalogoItemByProductoUnidadVolumen;
    @JsonIgnore private Usuario usuarioByProductoUsuarioCrea;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "producto_id")
    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    @Basic
    @Column(name = "producto_codigo")
    public String getProductoCodigo() {
        return productoCodigo;
    }

    public void setProductoCodigo(String productoCodigo) {
        this.productoCodigo = productoCodigo;
    }

    @Basic
    @Column(name = "producto_categoria")
    public Integer getProductoCategoria() {
        return productoCategoria;
    }

    public void setProductoCategoria(Integer productoCategoria) {
        this.productoCategoria = productoCategoria;
    }

    @Basic
    @Column(name = "producto_nombre")
    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    @Basic
    @Column(name = "producto_descripcion")
    public String getProductoDescripcion() {
        return productoDescripcion;
    }

    public void setProductoDescripcion(String productoDescripcion) {
        this.productoDescripcion = productoDescripcion;
    }

    @Basic
    @Column(name = "producto_volumen")
    public Double getProductoVolumen() {
        return productoVolumen;
    }

    public void setProductoVolumen(Double productoVolumen) {
        this.productoVolumen = productoVolumen;
    }

    @Basic
    @Column(name = "producto_unidad_volumen")
    public Integer getProductoUnidadVolumen() {
        return productoUnidadVolumen;
    }

    public void setProductoUnidadVolumen(Integer productoUnidadVolumen) {
        this.productoUnidadVolumen = productoUnidadVolumen;
    }

    @Basic
    @Column(name = "producto_unidades_por_caja")
    public Integer getProductoUnidadesPorCaja() {
        return productoUnidadesPorCaja;
    }

    public void setProductoUnidadesPorCaja(Integer productoUnidadesPorCaja) {
        this.productoUnidadesPorCaja = productoUnidadesPorCaja;
    }

    @Basic
    @Column(name = "producto_precio_sin_imp")
    public double getProductoPrecioSinImp() {
        return productoPrecioSinImp;
    }

    public void setProductoPrecioSinImp(double productoPrecioSinImp) {
        this.productoPrecioSinImp = productoPrecioSinImp;
    }

    @Basic
    @Column(name = "producto_precio_con_imp")
    public double getProductoPrecioConImp() {
        return productoPrecioConImp;
    }

    public void setProductoPrecioConImp(double productoPrecioConImp) {
        this.productoPrecioConImp = productoPrecioConImp;
    }

    @Basic
    @Column(name = "producto_precio_pvp_sin_imp")
    public Double getProductoPrecioPvpSinImp() {
        return productoPrecioPvpSinImp;
    }

    public void setProductoPrecioPvpSinImp(Double productoPrecioPvpSinImp) {
        this.productoPrecioPvpSinImp = productoPrecioPvpSinImp;
    }

    @Basic
    @Column(name = "producto_precio_pvp_con_imp")
    public Double getProductoPrecioPvpConImp() {
        return productoPrecioPvpConImp;
    }

    public void setProductoPrecioPvpConImp(Double productoPrecioPvpConImp) {
        this.productoPrecioPvpConImp = productoPrecioPvpConImp;
    }

    @Basic
    @Column(name = "producto_fecha_creacion")
    public Timestamp getProductoFechaCreacion() {
        return productoFechaCreacion;
    }

    public void setProductoFechaCreacion(Timestamp productoFechaCreacion) {
        this.productoFechaCreacion = productoFechaCreacion;
    }

    @Basic
    @Column(name = "producto_usuario_crea")
    public String getProductoUsuarioCrea() {
        return productoUsuarioCrea;
    }

    public void setProductoUsuarioCrea(String productoUsuarioCrea) {
        this.productoUsuarioCrea = productoUsuarioCrea;
    }

    @Basic
    @Column(name = "producto_estado")
    public int getProductoEstado() {
        return productoEstado;
    }

    public void setProductoEstado(int productoEstado) {
        this.productoEstado = productoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return productoId == producto.productoId &&
                Double.compare(producto.productoPrecioSinImp, productoPrecioSinImp) == 0 &&
                Double.compare(producto.productoPrecioConImp, productoPrecioConImp) == 0 &&
                productoEstado == producto.productoEstado &&
                Objects.equals(productoCodigo, producto.productoCodigo) &&
                Objects.equals(productoCategoria, producto.productoCategoria) &&
                Objects.equals(productoNombre, producto.productoNombre) &&
                Objects.equals(productoDescripcion, producto.productoDescripcion) &&
                Objects.equals(productoVolumen, producto.productoVolumen) &&
                Objects.equals(productoUnidadVolumen, producto.productoUnidadVolumen) &&
                Objects.equals(productoUnidadesPorCaja, producto.productoUnidadesPorCaja) &&
                Objects.equals(productoPrecioPvpSinImp, producto.productoPrecioPvpSinImp) &&
                Objects.equals(productoPrecioPvpConImp, producto.productoPrecioPvpConImp) &&
                Objects.equals(productoFechaCreacion, producto.productoFechaCreacion) &&
                Objects.equals(productoUsuarioCrea, producto.productoUsuarioCrea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, productoCodigo, productoCategoria, productoNombre, productoDescripcion, productoVolumen, productoUnidadVolumen, productoUnidadesPorCaja, productoPrecioSinImp, productoPrecioConImp, productoPrecioPvpSinImp, productoPrecioPvpConImp, productoFechaCreacion, productoUsuarioCrea, productoEstado);
    }

    @OneToMany(mappedBy = "productoByPedidoDetalleProductoId")
    public Collection<PedidoDetalle> getPedidoDetallesByProductoId() {
        return pedidoDetallesByProductoId;
    }

    public void setPedidoDetallesByProductoId(Collection<PedidoDetalle> pedidoDetallesByProductoId) {
        this.pedidoDetallesByProductoId = pedidoDetallesByProductoId;
    }

    @ManyToOne
    @JoinColumn(name = "producto_categoria", referencedColumnName = "categoria_producto_id", insertable = false, updatable = false)
    public CategoriaProducto getCategoriaProductoByProductoCategoria() {
        return categoriaProductoByProductoCategoria;
    }

    public void setCategoriaProductoByProductoCategoria(CategoriaProducto categoriaProductoByProductoCategoria) {
        this.categoriaProductoByProductoCategoria = categoriaProductoByProductoCategoria;
    }

    @ManyToOne
    @JoinColumn(name = "producto_unidad_volumen", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByProductoUnidadVolumen() {
        return catalogoItemByProductoUnidadVolumen;
    }

    public void setCatalogoItemByProductoUnidadVolumen(CatalogoItem catalogoItemByProductoUnidadVolumen) {
        this.catalogoItemByProductoUnidadVolumen = catalogoItemByProductoUnidadVolumen;
    }

    @ManyToOne
    @JoinColumn(name = "producto_usuario_crea", referencedColumnName = "usuario_id_documento", nullable = false, insertable = false, updatable = false)
    public Usuario getUsuarioByProductoUsuarioCrea() {
        return usuarioByProductoUsuarioCrea;
    }

    public void setUsuarioByProductoUsuarioCrea(Usuario usuarioByProductoUsuarioCrea) {
        this.usuarioByProductoUsuarioCrea = usuarioByProductoUsuarioCrea;
    }
}
