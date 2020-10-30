package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "conductor")
@NamedQueries({
        @NamedQuery(name = "Conductor.ConductorByConductorUsuario", query = "select c from Conductor c where c.conductorUsuario = :usuario"),
        @NamedQuery(name = "Conductor.ConductorByConductorUsuarioAndEstado", query = "select c from Conductor c where c.conductorUsuario = :usuario and c.conductorEstado = :estado")
})
public class Conductor {

    private int conductorId;
    private String conductorUsuario;
    private String conductorCedula;
    private String conductorNombre;
    private String conductorApellido;
    private int conductorTipoLicencia;
    private String conductorTelefono;
    private String conductorEmail;
    private int conductorEstado;
    @JsonIgnore private String conductorLicencia;
    @JsonIgnore private Usuario conductorByConductorUsuario;
    @JsonIgnore private CatalogoItem conductorByConductorTipoLicencia;
    @JsonIgnore private List<Envio> enviosByConductor;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "conductor_id", nullable = false)
    public int getConductorId() {
        return conductorId;
    }

    public void setConductorId(int conductorId) {
        this.conductorId = conductorId;
    }

    @Basic
    @Column(name = "conductor_usuario", nullable = true)
    public String getConductorUsuario() {
        return conductorUsuario;
    }

    public void setConductorUsuario(String conductorUsuario) {
        this.conductorUsuario = conductorUsuario;
    }

    @Basic
    @Column(name = "conductor_cedula", nullable = true)
    public String getConductorCedula() {
        return conductorCedula;
    }

    public void setConductorCedula(String conductorCedula) {
        this.conductorCedula = conductorCedula;
    }

    @Basic
    @Column(name = "conductor_nombre", nullable = true)
    public String getConductorNombre() {
        return conductorNombre;
    }

    public void setConductorNombre(String conductorNombre) {
        this.conductorNombre = conductorNombre;
    }

    @Basic
    @Column(name = "conductor_apellido", nullable = true)
    public String getConductorApellido() {
        return conductorApellido;
    }

    public void setConductorApellido(String conductorApellido) {
        this.conductorApellido = conductorApellido;
    }

    @Basic
    @Column(name = "conductor_tipo_licencia", nullable = true)
    public int getConductorTipoLicencia() {
        return conductorTipoLicencia;
    }

    public void setConductorTipoLicencia(int conductorTipoLicencia) {
        this.conductorTipoLicencia = conductorTipoLicencia;
    }

    @Basic
    @Column(name = "conductor_telefono", nullable = true)
    public String getConductorTelefono() {
        return conductorTelefono;
    }

    public void setConductorTelefono(String conductorTelefono) {
        this.conductorTelefono = conductorTelefono;
    }

    @Basic
    @Column(name = "conductor_email", nullable = true)
    public String getConductorEmail() {
        return conductorEmail;
    }

    public void setConductorEmail(String conductorEmail) {
        this.conductorEmail = conductorEmail;
    }

    @Basic
    @Column(name = "conductor_licencia", nullable = true)
    public String getConductorLicencia() {
        return conductorLicencia;
    }

    public void setConductorLicencia(String conductorLicencia) {
        this.conductorLicencia = conductorLicencia;
    }

    @Column(name = "conductor_estado")
    public int getConductorEstado() {
        return conductorEstado;
    }

    public void setConductorEstado(int conductorEstado) {
        this.conductorEstado = conductorEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conductor conductor = (Conductor) o;
        return conductorId == conductor.conductorId &&
                conductorTipoLicencia == conductor.conductorTipoLicencia &&
                Objects.equals(conductorUsuario, conductor.conductorUsuario) &&
                Objects.equals(conductorCedula, conductor.conductorCedula) &&
                Objects.equals(conductorNombre, conductor.conductorNombre) &&
                Objects.equals(conductorApellido, conductor.conductorApellido) &&
                Objects.equals(conductorTelefono, conductor.conductorTelefono) &&
                Objects.equals(conductorEmail, conductor.conductorEmail) &&
                Objects.equals(conductorLicencia, conductor.conductorLicencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conductorId, conductorUsuario, conductorCedula, conductorNombre, conductorApellido, conductorTipoLicencia, conductorTelefono, conductorEmail, conductorLicencia);
    }

    @ManyToOne
    @JoinColumn(name = "conductor_usuario", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getConductorByConductorUsuario() {
        return conductorByConductorUsuario;
    }

    public void setConductorByConductorUsuario(Usuario conductorByConductorUsuario) {
        this.conductorByConductorUsuario = conductorByConductorUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "conductor_tipo_licencia", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getConductorByConductorTipoLicencia() {
        return conductorByConductorTipoLicencia;
    }

    public void setConductorByConductorTipoLicencia(CatalogoItem conductorByConductorTipoLicencia) {
        this.conductorByConductorTipoLicencia = conductorByConductorTipoLicencia;
    }

    @OneToMany(mappedBy = "conductorByEnvio")
    public List<Envio> getEnviosByConductor() {
        return enviosByConductor;
    }

    public void setEnviosByConductor(List<Envio> enviosByConductor) {
        this.enviosByConductor = enviosByConductor;
    }
}
