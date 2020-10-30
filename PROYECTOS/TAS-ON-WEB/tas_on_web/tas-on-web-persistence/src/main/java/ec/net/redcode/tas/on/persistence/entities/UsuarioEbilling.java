package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "usuario_ebilling")
@NamedQueries({
        @NamedQuery(name = "UsuarioEbilling.getByUserID", query = "select o from UsuarioEbilling o where o.usuarioEbillingIdUsuario = :idUsuario")
})
public class UsuarioEbilling {
    private String usuarioEbillingId;
    private String usuarioEbillingIdUsuario;
    private Timestamp usuarioEbillingFechaFirma;
    private String usuarioEbillingFirma;
    private String usuarioEbillingLogo;
    private String usuarioEbillingEstablecimiento;
    private String usuarioEbillingPuntoEmision;
    private Integer usuarioEbillingSecuencia;
    private Integer usuarioEbillingEstado;
    private String usuarioEBillingActividadComercial;

    @Id
    @Column(name = "usuario_ebilling_id")
    public String getUsuarioEbillingId() {
        return usuarioEbillingId;
    }

    public void setUsuarioEbillingId(String usuarioEbillingId) {
        this.usuarioEbillingId = usuarioEbillingId;
    }

    @Basic
    @Column(name = "usuario_ebilling_id_usuario")
    public String getUsuarioEbillingIdUsuario() {
        return usuarioEbillingIdUsuario;
    }

    public void setUsuarioEbillingIdUsuario(String usuarioEbillingIdUsuario) {
        this.usuarioEbillingIdUsuario = usuarioEbillingIdUsuario;
    }

    @Basic
    @Column(name = "usuario_ebilling_fecha_firma")
    public Timestamp getUsuarioEbillingFechaFirma() {
        return usuarioEbillingFechaFirma;
    }

    public void setUsuarioEbillingFechaFirma(Timestamp usuarioEbillingFechaFirma) {
        this.usuarioEbillingFechaFirma = usuarioEbillingFechaFirma;
    }

    @Basic
    @Column(name = "usuario_ebilling_firma")
    public String getUsuarioEbillingFirma() {
        return usuarioEbillingFirma;
    }

    public void setUsuarioEbillingFirma(String usuarioEbillingRutaFirma) {
        this.usuarioEbillingFirma = usuarioEbillingRutaFirma;
    }

    @Basic
    @Column(name = "usuario_ebilling_logo")
    public String getUsuarioEbillingLogo() {
        return usuarioEbillingLogo;
    }

    public void setUsuarioEbillingLogo(String usuarioEbillingLogo) {
        this.usuarioEbillingLogo = usuarioEbillingLogo;
    }

    @Column(name = "usuario_ebilling_establecimiento")
    public String getUsuarioEbillingEstablecimiento() {
        return usuarioEbillingEstablecimiento;
    }

    public void setUsuarioEbillingEstablecimiento(String usuarioEbillingEstablecimiento) {
        this.usuarioEbillingEstablecimiento = usuarioEbillingEstablecimiento;
    }

    @Column(name = "usuario_ebilling_punto_emision")
    public String getUsuarioEbillingPuntoEmision() {
        return usuarioEbillingPuntoEmision;
    }

    public void setUsuarioEbillingPuntoEmision(String usuarioEbillingPuntoEmision) {
        this.usuarioEbillingPuntoEmision = usuarioEbillingPuntoEmision;
    }

    @Basic
    @Column(name = "usuario_ebilling_secuencia")
    public Integer getUsuarioEbillingSecuencia() {
        return usuarioEbillingSecuencia;
    }

    public void setUsuarioEbillingSecuencia(Integer usuarioEbillingSecuencia) {
        this.usuarioEbillingSecuencia = usuarioEbillingSecuencia;
    }

    @Basic
    @Column(name = "usuario_ebilling_estado")
    public Integer getUsuarioEbillingEstado() {
        return usuarioEbillingEstado;
    }

    public void setUsuarioEbillingEstado(Integer usuarioEbillingEstado) {
        this.usuarioEbillingEstado = usuarioEbillingEstado;
    }

    @Column(name="usuario_ebilling_actividad_comercial")
    public String getUsuarioEBillingActividadComercial() {
        return usuarioEBillingActividadComercial;
    }

    public void setUsuarioEBillingActividadComercial(String usuarioEBillingActividadComercial) {
        this.usuarioEBillingActividadComercial = usuarioEBillingActividadComercial;
    }
}
