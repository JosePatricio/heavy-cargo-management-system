package ec.redcode.tas.on.android.dto;

import java.util.List;

/**
 * Created by Walter on 21/3/18.
 */

public class UsuarioDTO {
    private String usuarioIdDocumento;
    private Integer usuarioTipoDocumento;
    private String usuarioNombres;
    private String usuarioApellidos;
    private String usuarioCelular;
    private String usuarioConvecional;
    private String usuarioRuc;
    private String usuarioDireccion;
    private Integer usuarioLocalidad;
    private String usuarioNombreUsuario;
    private String usuarioContrasenia;
    private Integer usuarioEstado;
    private String usuarioMail;
    private Integer usuarioTipoUsuario;
    private Boolean usuarioPrincipal;
    private List<VehiculoDTO> vehiculosByUsuarioIdDocumento;
    private List<ConductorDTO> conductorsByUsuarioIdDocumento;

    public String getUsuarioIdDocumento() {
        return usuarioIdDocumento;
    }

    public void setUsuarioIdDocumento(String usuarioIdDocumento) {
        this.usuarioIdDocumento = usuarioIdDocumento;
    }

    public Integer getUsuarioTipoDocumento() {
        return usuarioTipoDocumento;
    }

    public void setUsuarioTipoDocumento(Integer usuarioTipoDocumento) {
        this.usuarioTipoDocumento = usuarioTipoDocumento;
    }

    public String getUsuarioNombres() {
        return usuarioNombres;
    }

    public void setUsuarioNombres(String usuarioNombres) {
        this.usuarioNombres = usuarioNombres;
    }

    public String getUsuarioApellidos() {
        return usuarioApellidos;
    }

    public void setUsuarioApellidos(String usuarioApellidos) {
        this.usuarioApellidos = usuarioApellidos;
    }

    public String getUsuarioCelular() {
        return usuarioCelular;
    }

    public void setUsuarioCelular(String usuarioCelular) {
        this.usuarioCelular = usuarioCelular;
    }

    public String getUsuarioConvecional() {
        return usuarioConvecional;
    }

    public void setUsuarioConvecional(String usuarioConvecional) {
        this.usuarioConvecional = usuarioConvecional;
    }

    public String getUsuarioRuc() {
        return usuarioRuc;
    }

    public void setUsuarioRuc(String usuarioRuc) {
        this.usuarioRuc = usuarioRuc;
    }

    public String getUsuarioDireccion() {
        return usuarioDireccion;
    }

    public void setUsuarioDireccion(String usuarioDireccion) {
        this.usuarioDireccion = usuarioDireccion;
    }

    public Integer getUsuarioLocalidad() {
        return usuarioLocalidad;
    }

    public void setUsuarioLocalidad(Integer usuarioLocalidad) {
        this.usuarioLocalidad = usuarioLocalidad;
    }

    public String getUsuarioNombreUsuario() {
        return usuarioNombreUsuario;
    }

    public void setUsuarioNombreUsuario(String usuarioNombreUsuario) {
        this.usuarioNombreUsuario = usuarioNombreUsuario;
    }

    public String getUsuarioContrasenia() {
        return usuarioContrasenia;
    }

    public void setUsuarioContrasenia(String usuarioContrasenia) {
        this.usuarioContrasenia = usuarioContrasenia;
    }

    public Integer getUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(Integer usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public String getUsuarioMail() {
        return usuarioMail;
    }

    public void setUsuarioMail(String usuarioMail) {
        this.usuarioMail = usuarioMail;
    }

    public Integer getUsuarioTipoUsuario() {
        return usuarioTipoUsuario;
    }

    public void setUsuarioTipoUsuario(Integer usuarioTipoUsuario) {
        this.usuarioTipoUsuario = usuarioTipoUsuario;
    }

    public Boolean getUsuarioPrincipal() {
        return usuarioPrincipal;
    }

    public void setUsuarioPrincipal(Boolean usuarioPrincipal) {
        this.usuarioPrincipal = usuarioPrincipal;
    }

    public List<VehiculoDTO> getVehiculosByUsuarioIdDocumento() {
        return vehiculosByUsuarioIdDocumento;
    }

    public void setVehiculosByUsuarioIdDocumento(List<VehiculoDTO> vehiculosByUsuarioIdDocumento) {
        this.vehiculosByUsuarioIdDocumento = vehiculosByUsuarioIdDocumento;
    }

    public List<ConductorDTO> getConductorsByUsuarioIdDocumento() {
        return conductorsByUsuarioIdDocumento;
    }

    public void setConductorsByUsuarioIdDocumento(List<ConductorDTO> conductorsByUsuarioIdDocumento) {
        this.conductorsByUsuarioIdDocumento = conductorsByUsuarioIdDocumento;
    }
}
