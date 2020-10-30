package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "adquiriente")
@NamedQueries({
        @NamedQuery(name = "Adquiriente.getAll", query = "select o from Adquiriente o")
})
public class Adquiriente {
    private String adquirienteIdDocumento;
    private Integer adquirienteTipoDocumento;
    private String adquirienteRazonSocial;
    private String adquirienteDireccion;
    private String adquirienteTelefono;
    private String adquirienteMail;
    private String adquirientePersonaContacto;

    @Id
    @Column(name = "adquiriente_id_documento")
    public String getAdquirienteIdDocumento() {
        return adquirienteIdDocumento;
    }

    public void setAdquirienteIdDocumento(String adquirienteIdDocumento) {
        this.adquirienteIdDocumento = adquirienteIdDocumento;
    }

    @Basic
    @Column(name = "adquiriente_tipo_documento")
    public Integer getAdquirienteTipoDocumento() {
        return adquirienteTipoDocumento;
    }

    public void setAdquirienteTipoDocumento(Integer adquirienteTipoDocumento) {
        this.adquirienteTipoDocumento = adquirienteTipoDocumento;
    }

    @Basic
    @Column(name = "adquiriente_razon_social")
    public String getAdquirienteRazonSocial() {
        return adquirienteRazonSocial;
    }

    public void setAdquirienteRazonSocial(String adquirienteRazonSocial) {
        this.adquirienteRazonSocial = adquirienteRazonSocial;
    }

    @Basic
    @Column(name = "adquiriente_direccion")
    public String getAdquirienteDireccion() {
        return adquirienteDireccion;
    }

    public void setAdquirienteDireccion(String adquirienteDireccion) {
        this.adquirienteDireccion = adquirienteDireccion;
    }

    @Basic
    @Column(name = "adquiriente_telefono")
    public String getAdquirienteTelefono() {
        return adquirienteTelefono;
    }

    public void setAdquirienteTelefono(String adquirienteTelefono) {
        this.adquirienteTelefono = adquirienteTelefono;
    }

    @Basic
    @Column(name = "adquiriente_mail")
    public String getAdquirienteMail() {
        return adquirienteMail;
    }

    public void setAdquirienteMail(String adquirienteMail) {
        this.adquirienteMail = adquirienteMail;
    }

    @Column(name="adquiriente_persona_contacto")
    public String getAdquirientePersonaContacto() {
        return adquirientePersonaContacto;
    }

    public void setAdquirientePersonaContacto(String adquirientePersonaContacto) {
        this.adquirientePersonaContacto = adquirientePersonaContacto;
    }
}
