package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "categoria_producto")
public class CategoriaProducto {
    private int categoriaProductoId;
    private String categoriaProductoNombre;
    private Timestamp categoriaProductoFechaCreacion;
    private String categoriaProductoUsuarioCrea;
    private int categoriaProductoEstado;
    @JsonIgnore private Usuario usuarioByCategoriaProductoUsuarioCrea;
    @JsonIgnore private Collection<Producto> productosByCategoriaProductoId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "categoria_producto_id")
    public int getCategoriaProductoId() {
        return categoriaProductoId;
    }

    public void setCategoriaProductoId(int categoriaProductoId) {
        this.categoriaProductoId = categoriaProductoId;
    }

    @Basic
    @Column(name = "categoria_producto_nombre")
    public String getCategoriaProductoNombre() {
        return categoriaProductoNombre;
    }

    public void setCategoriaProductoNombre(String categoriaProductoNombre) {
        this.categoriaProductoNombre = categoriaProductoNombre;
    }

    @Basic
    @Column(name = "categoria_producto_fecha_creacion")
    public Timestamp getCategoriaProductoFechaCreacion() {
        return categoriaProductoFechaCreacion;
    }

    public void setCategoriaProductoFechaCreacion(Timestamp categoriaProductoFechaCreacion) {
        this.categoriaProductoFechaCreacion = categoriaProductoFechaCreacion;
    }

    @Basic
    @Column(name = "categoria_producto_usuario_crea")
    public String getCategoriaProductoUsuarioCrea() {
        return categoriaProductoUsuarioCrea;
    }

    public void setCategoriaProductoUsuarioCrea(String categoriaProductoUsuarioCrea) {
        this.categoriaProductoUsuarioCrea = categoriaProductoUsuarioCrea;
    }

    @Basic
    @Column(name = "categoria_producto_estado")
    public int getCategoriaProductoEstado() {
        return categoriaProductoEstado;
    }

    public void setCategoriaProductoEstado(int categoriaProductoEstado) {
        this.categoriaProductoEstado = categoriaProductoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaProducto that = (CategoriaProducto) o;
        return categoriaProductoId == that.categoriaProductoId &&
                categoriaProductoEstado == that.categoriaProductoEstado &&
                Objects.equals(categoriaProductoNombre, that.categoriaProductoNombre) &&
                Objects.equals(categoriaProductoFechaCreacion, that.categoriaProductoFechaCreacion) &&
                Objects.equals(categoriaProductoUsuarioCrea, that.categoriaProductoUsuarioCrea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriaProductoId, categoriaProductoNombre, categoriaProductoFechaCreacion, categoriaProductoUsuarioCrea, categoriaProductoEstado);
    }

    @ManyToOne
    @JoinColumn(name = "categoria_producto_usuario_crea", referencedColumnName = "usuario_id_documento", nullable = false, insertable = false, updatable = false)
    public Usuario getUsuarioByCategoriaProductoUsuarioCrea() {
        return usuarioByCategoriaProductoUsuarioCrea;
    }

    public void setUsuarioByCategoriaProductoUsuarioCrea(Usuario usuarioByCategoriaProductoUsuarioCrea) {
        this.usuarioByCategoriaProductoUsuarioCrea = usuarioByCategoriaProductoUsuarioCrea;
    }

    @OneToMany(mappedBy = "categoriaProductoByProductoCategoria")
    public Collection<Producto> getProductosByCategoriaProductoId() {
        return productosByCategoriaProductoId;
    }

    public void setProductosByCategoriaProductoId(Collection<Producto> productosByCategoriaProductoId) {
        this.productosByCategoriaProductoId = productosByCategoriaProductoId;
    }
}
