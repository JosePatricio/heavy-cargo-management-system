package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "localidad")
@NamedQueries({

        @NamedQuery(name = "Localidad.LocalidadBylocalidadId", query = "select l from Localidad l" +
                " where l.localidadId = :localidadId  and l.localidadEstado =:localidadEstado"),
        @NamedQuery(name = "Localidad.LocalidadByLocalidadPadre", query = "select l from Localidad l" +
                " where l.localidadLocalidadPadre = :localidadLocalidadPadre and l.localidadEstado =:localidadEstado order by l.localidadDescripcion"),
        @NamedQuery(name = "Localidad.AllCiudades", query = "select l.localidadId, l.localidadDescripcion, " +
                "(select p.localidadDescripcion from Localidad p where l.localidadLocalidadPadre = p.localidadId ) " +
                "from Localidad l where l.localidadEstado = 1 and l.localidadLocalidadPadre not in (0, 1)")
})
public class Localidad {
    private int localidadId;
    private String localidadDescripcion;
    private int localidadCode;
    private Integer localidadLocalidadPadre;
    private Integer localidadEstado;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByLocalidadId;
    @JsonIgnore private List<SolicitudEnvio> solicitudEnviosByLocalidadId_0;
    @JsonIgnore private List<Usuario> usuariosByLocalidadId;
    @JsonIgnore private List<Cliente> clientesByLocalidadId;
    @JsonIgnore private CatalogoItem localidadbyCatalogoItemIdEstado;
    @JsonIgnore private Collection<ClientePedidos> clientePedidosByLocalidadId;

    @Id
    @Column(name = "localidad_id", nullable = false)
    public int getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(int localidadId) {
        this.localidadId = localidadId;
    }

    @Basic
    @Column(name = "localidad_descripcion", nullable = true, length = 100)
    public String getLocalidadDescripcion() {
        return localidadDescripcion;
    }

    public void setLocalidadDescripcion(String localidadDescripcion) {
        this.localidadDescripcion = localidadDescripcion;
    }

    @Basic
    @Column(name = "localidad_code", nullable = false)
    public int getLocalidadCode() {
        return localidadCode;
    }

    public void setLocalidadCode(int localidadCode) {
        this.localidadCode = localidadCode;
    }

    @Basic
    @Column(name = "localidad_localidad_padre", nullable = true)
    public Integer getLocalidadLocalidadPadre() {
        return localidadLocalidadPadre;
    }

    public void setLocalidadLocalidadPadre(Integer localidadLocalidadPadre) {
        this.localidadLocalidadPadre = localidadLocalidadPadre;
    }

    @Basic
    @Column(name = "localidad_estado", nullable = true)
    public Integer getLocalidadEstado() {
        return localidadEstado;
    }

    public void setLocalidadEstado(Integer localidadEstado) {
        this.localidadEstado = localidadEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localidad localidad = (Localidad) o;
        return localidadId == localidad.localidadId &&
                Objects.equals(localidadDescripcion, localidad.localidadDescripcion) &&
                Objects.equals(localidadLocalidadPadre, localidad.localidadLocalidadPadre) &&
                Objects.equals(localidadEstado, localidad.localidadEstado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(localidadId, localidadDescripcion, localidadLocalidadPadre, localidadEstado);
    }

    @OneToMany(mappedBy = "localidadBySolicitudEnvioLocalidadOrigen")
    public List<SolicitudEnvio> getSolicitudEnviosByLocalidadId() {
        return solicitudEnviosByLocalidadId;
    }

    public void setSolicitudEnviosByLocalidadId(List<SolicitudEnvio> solicitudEnviosByLocalidadId) {
        this.solicitudEnviosByLocalidadId = solicitudEnviosByLocalidadId;
    }

    @OneToMany(mappedBy = "localidadBySolicitudEnvioLocalidadDestino")
    public List<SolicitudEnvio> getSolicitudEnviosByLocalidadId_0() {
        return solicitudEnviosByLocalidadId_0;
    }

    public void setSolicitudEnviosByLocalidadId_0(List<SolicitudEnvio> solicitudEnviosByLocalidadId_0) {
        this.solicitudEnviosByLocalidadId_0 = solicitudEnviosByLocalidadId_0;
    }

    @OneToMany(mappedBy = "localidadByUsuarioLocalidad")
    public List<Usuario> getUsuariosByLocalidadId() {
        return usuariosByLocalidadId;
    }

    public void setUsuariosByLocalidadId(List<Usuario> usuariosByLocalidadId) {
        this.usuariosByLocalidadId = usuariosByLocalidadId;
    }

    @OneToMany(mappedBy = "localidadByClienteLocalidad")
    public List<Cliente> getClientesByLocalidadId() {
        return clientesByLocalidadId;
    }

    public void setClientesByLocalidadId(List<Cliente> clientesByLocalidadId) {
        this.clientesByLocalidadId = clientesByLocalidadId;
    }

    @ManyToOne
    @JoinColumn(name = "localidad_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getLocalidadbyCatalogoItemIdEstado() {
        return localidadbyCatalogoItemIdEstado;
    }

    public void setLocalidadbyCatalogoItemIdEstado(CatalogoItem localidadbyCatalogoItemIdEstado) {
        this.localidadbyCatalogoItemIdEstado = localidadbyCatalogoItemIdEstado;
    }

    @OneToMany(mappedBy = "localidadByClientePedidosLocalidadId")
    public Collection<ClientePedidos> getClientePedidosByLocalidadId() {
        return clientePedidosByLocalidadId;
    }

    public void setClientePedidosByLocalidadId(Collection<ClientePedidos> clientePedidosByLocalidadId) {
        this.clientePedidosByLocalidadId = clientePedidosByLocalidadId;
    }
}
