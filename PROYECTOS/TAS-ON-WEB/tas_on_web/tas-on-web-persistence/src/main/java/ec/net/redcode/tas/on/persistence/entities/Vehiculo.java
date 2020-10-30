package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehiculo")
@NamedQueries({
        @NamedQuery(name = "Vehiculo.VehiculoByUsuario", query = "select v from Vehiculo v where v.vehiculoUsuario = :usuario"),
        @NamedQuery(name = "Vehiculo.VehiculoByUsuarioAndEstado", query = "select v from Vehiculo v where v.vehiculoUsuario = :usuario and v.vehiculoEstado = :estado"),
        @NamedQuery(name = "Vehiculo.VehiculoByPlaca", query = "select v from Vehiculo v where v.vehiculoPlaca = :placa")
})
public class Vehiculo {

    private int vehiculoId;
    private String vehiculoUsuario;
    private String vehiculoModelo;
    private int vehiculoAnio;
    private String vehiculoPlaca;
    private int vehiculoTipoCarga;
    private int vehiculoTipoCamion;
    private double vehiculoCapacidad;
    private int vehiculoTipoCapacidad;
    private int vehiculoEstado;
    private Boolean vehiculoArcsaAlimentos;
    private Boolean vehiculoArcsaCosmeticos;
    private Boolean vehiculoArcsaMedicamentos;
    private Boolean vehiculoBasc;
    private Boolean vehiculoPaseInternacional;
    private Boolean vehiculoCertificadoArcsa;

    @JsonIgnore private String vehiculoMatricula;
    @JsonIgnore private Usuario usuarioByVehiculoUsuario;
    @JsonIgnore private CatalogoItem catalogoItemByVehiculoTipoCarga;
    @JsonIgnore private CatalogoItem catalogoItemByVehiculoTipoCamion;
    @JsonIgnore private CatalogoItem catalogoItemByVehiculoTipoCapacidad;
    @JsonIgnore private List<Envio> enviosByVehiculo;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "vehiculo_id", nullable = false)
    public int getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(int vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    @Basic
    @Column(name = "vehiculo_usuario", nullable = true, length = 9)
    public String getVehiculoUsuario() {
        return vehiculoUsuario;
    }

    public void setVehiculoUsuario(String vehiculoUsuario) {
        this.vehiculoUsuario = vehiculoUsuario;
    }

    @Basic
    @Column(name = "vehiculo_modelo", nullable = true, length = 8)
    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    @Basic
    @Column(name = "vehiculo_anio", nullable = true)
    public int getVehiculoAnio() {
        return vehiculoAnio;
    }

    public void setVehiculoAnio(int vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
    }

    @Basic
    @Column(name = "vehiculo_placa", nullable = true)
    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    @Basic
    @Column(name = "vehiculo_matricula", nullable = true)
    public String getVehiculoMatricula() {
        return vehiculoMatricula;
    }

    public void setVehiculoMatricula(String vehiculoMatricula) {
        this.vehiculoMatricula = vehiculoMatricula;
    }

    @Basic
    @Column(name = "vehiculo_arcsa_alimentos")
    public Boolean getVehiculoArcsaAlimentos() {
        if(vehiculoArcsaAlimentos==null) vehiculoArcsaAlimentos = false;
        return vehiculoArcsaAlimentos;
    }

    public void setVehiculoArcsaAlimentos(Boolean vehiculoArcsaAlimentos) {
        if(vehiculoArcsaAlimentos==null) vehiculoArcsaAlimentos = false;
        this.vehiculoArcsaAlimentos = vehiculoArcsaAlimentos;
    }

    @Basic
    @Column(name = "vehiculo_arcsa_cosmeticos")
    public Boolean getVehiculoArcsaCosmeticos() {
        if(vehiculoArcsaCosmeticos==null) vehiculoArcsaCosmeticos = false;
        return vehiculoArcsaCosmeticos;
    }

    public void setVehiculoArcsaCosmeticos(Boolean vehiculoArcsaCosmeticos) {
        if(vehiculoArcsaCosmeticos==null) vehiculoArcsaCosmeticos = false;
        this.vehiculoArcsaCosmeticos = vehiculoArcsaCosmeticos;
    }

    @Basic
    @Column(name = "vehiculo_arcsa_medicamentos")
    public Boolean getVehiculoArcsaMedicamentos() {
        if(vehiculoArcsaMedicamentos==null) vehiculoArcsaMedicamentos = false;
        return vehiculoArcsaMedicamentos;
    }

    public void setVehiculoArcsaMedicamentos(Boolean vehiculoArcsaMedicamentos) {
        if(vehiculoArcsaMedicamentos==null) vehiculoArcsaMedicamentos = false;
        this.vehiculoArcsaMedicamentos = vehiculoArcsaMedicamentos;
    }

    @Basic
    @Column(name = "vehiculo_basc")
    public Boolean getVehiculoBasc() {
        if(vehiculoBasc==null) vehiculoBasc = false;
        return vehiculoBasc;
    }

    public void setVehiculoBasc(Boolean vehiculoBasc) {
        if(vehiculoBasc==null) vehiculoBasc = false;
        this.vehiculoBasc = vehiculoBasc;
    }

    @Basic
    @Column(name = "vehiculo_pase_internacional")
    public Boolean getVehiculoPaseInternacional() {
        if(vehiculoPaseInternacional==null) vehiculoPaseInternacional = false;
        return vehiculoPaseInternacional;
    }

    public void setVehiculoPaseInternacional(Boolean vehiculoPaseInternacional) {
        if(vehiculoPaseInternacional==null) vehiculoPaseInternacional = false;
        this.vehiculoPaseInternacional = vehiculoPaseInternacional;
    }

    @Basic
    @Column(name = "vehiculo_certificado_arcsa", nullable = true)
    public Boolean getVehiculoCertificadoArcsa() {
        if(vehiculoCertificadoArcsa == null) vehiculoCertificadoArcsa = false;
        return vehiculoCertificadoArcsa;
    }

    public void setVehiculoCertificadoArcsa(Boolean vehiculoCertificadoArcsa) {
        if(vehiculoCertificadoArcsa==null) vehiculoCertificadoArcsa = false;
        this.vehiculoCertificadoArcsa = vehiculoCertificadoArcsa;
    }

    @Basic
    @Column(name = "vehiculo_capacidad", nullable = true)
    public double getVehiculoCapacidad() {
        return vehiculoCapacidad;
    }

    public void setVehiculoCapacidad(double vehiculoCapacidad) {
        this.vehiculoCapacidad = vehiculoCapacidad;
    }

    @Basic
    @Column(name = "vehiculo_tipo_carga", nullable = true)
    public int getVehiculoTipoCarga() {
        return vehiculoTipoCarga;
    }

    public void setVehiculoTipoCarga(int vehiculoTipoCarga) {
        this.vehiculoTipoCarga = vehiculoTipoCarga;
    }
    @Basic
    @Column(name = "vehiculo_tipo_camion", nullable = true)
    public int getVehiculoTipoCamion() {
        return vehiculoTipoCamion;
    }

    public void setVehiculoTipoCamion(int vehiculoTipoCamion) {
        this.vehiculoTipoCamion = vehiculoTipoCamion;
    }

    @Basic
    @Column(name = "vehiculo_tipo_capacidad", nullable = true, length = 8)
    public int getVehiculoTipoCapacidad () {
        return vehiculoTipoCapacidad;
    }

    public void setVehiculoTipoCapacidad(int vehiculoTipoCapacidad) {
        this.vehiculoTipoCapacidad = vehiculoTipoCapacidad;
    }

    @Column(name = "vehiculo_estado")
    public int getVehiculoEstado() {
        return vehiculoEstado;
    }

    public void setVehiculoEstado(int vehiculoEstado) {
        this.vehiculoEstado = vehiculoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return vehiculoId == vehiculo.vehiculoId &&
                vehiculoAnio == vehiculo.vehiculoAnio &&
                Objects.equals(vehiculoUsuario, vehiculo.vehiculoUsuario) &&
                Objects.equals(vehiculoModelo, vehiculo.vehiculoModelo) &&
                Objects.equals(vehiculoPlaca, vehiculo.vehiculoPlaca) &&
                Objects.equals(vehiculoMatricula, vehiculo.vehiculoMatricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehiculoId, vehiculoUsuario, vehiculoModelo, vehiculoAnio, vehiculoPlaca, vehiculoMatricula);
    }

    @ManyToOne
    @JoinColumn(name = "vehiculo_usuario", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByVehiculoUsuario() {
        return usuarioByVehiculoUsuario;
    }

    public void setUsuarioByVehiculoUsuario(Usuario usuarioByVehiculoUsuario) {
        this.usuarioByVehiculoUsuario = usuarioByVehiculoUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "vehiculo_tipo_carga", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByVehiculoTipoCarga() {
        return catalogoItemByVehiculoTipoCarga;
    }

    public void setCatalogoItemByVehiculoTipoCarga(CatalogoItem catalogoItemByVehiculoTipoCarga) {
        this.catalogoItemByVehiculoTipoCarga = catalogoItemByVehiculoTipoCarga;
    }

    @ManyToOne
    @JoinColumn(name = "vehiculo_tipo_camion", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByVehiculoTipoCamion() {
        return catalogoItemByVehiculoTipoCamion;
    }

    public void setCatalogoItemByVehiculoTipoCamion(CatalogoItem catalogoItemByVehiculoTipoCamion) {
        this.catalogoItemByVehiculoTipoCamion = catalogoItemByVehiculoTipoCamion;
    }

    @ManyToOne
    @JoinColumn(name = "vehiculo_tipo_capacidad", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByVehiculoTipoCapacidad() {
        return catalogoItemByVehiculoTipoCapacidad;
    }

    public void setCatalogoItemByVehiculoTipoCapacidad(CatalogoItem catalogoItemByVehiculoTipoCapacidad) {
        this.catalogoItemByVehiculoTipoCapacidad = catalogoItemByVehiculoTipoCapacidad;
    }

    @OneToMany(mappedBy = "vehiculoByEnvio")
    public List<Envio> getEnviosByVehiculo() {
        return enviosByVehiculo;
    }

    public void setEnviosByVehiculo(List<Envio> enviosByVehiculo) {
        this.enviosByVehiculo = enviosByVehiculo;
    }
}
