package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "catalogo")
@NamedQueries({
        @NamedQuery(name = "Catalogo.CatalogoAll", query = "select c from Catalogo c")
})
public class Catalogo {
    private int catalogoId;
    private String catalogoDescripcion;
    private Integer catalogoEstado;
    @JsonIgnore
    private List<CatalogoItem> catalogoItemsByCatalogoId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "catalogo_id", nullable = false)
    public int getCatalogoId() {
        return catalogoId;
    }

    public void setCatalogoId(int catalogoId) {
        this.catalogoId = catalogoId;
    }

    @Basic
    @Column(name = "catalogo_descripcion", nullable = true, length = 20)
    public String getCatalogoDescripcion() {
        return catalogoDescripcion;
    }

    public void setCatalogoDescripcion(String catalogoDescripcion) {
        this.catalogoDescripcion = catalogoDescripcion;
    }

    @Basic
    @Column(name = "catalogo_estado", nullable = true)
    public Integer getCatalogoEstado() {
        return catalogoEstado;
    }

    public void setCatalogoEstado(Integer catalogoEstado) {
        this.catalogoEstado = catalogoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catalogo catalogo = (Catalogo) o;
        return catalogoId == catalogo.catalogoId &&
                Objects.equals(catalogoDescripcion, catalogo.catalogoDescripcion) &&
                Objects.equals(catalogoEstado, catalogo.catalogoEstado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(catalogoId, catalogoDescripcion, catalogoEstado);
    }

    @OneToMany(mappedBy = "catalogoByCatalogoItemIdCatalogo")
    public List<CatalogoItem> getCatalogoItemsByCatalogoId() {
        return catalogoItemsByCatalogoId;
    }

    public void setCatalogoItemsByCatalogoId(List<CatalogoItem> catalogoItemsByCatalogoId) {
        this.catalogoItemsByCatalogoId = catalogoItemsByCatalogoId;
    }

}
