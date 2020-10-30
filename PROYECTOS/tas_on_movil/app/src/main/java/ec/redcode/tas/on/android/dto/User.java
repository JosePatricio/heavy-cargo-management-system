package ec.redcode.tas.on.android.dto;

/**
 * Created by User on 21/12/2017.
 */

public class User {

    //String apellidos;
    String email;
    String nombres;
    String token;
    Integer estadoUsuario;
    Integer tipoUsuario;
    String tipoUsuarioDesc;

    /*public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(Integer estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoUsuarioDesc() {
        return tipoUsuarioDesc;
    }

    public void setTipoUsuarioDesc(String tipoUsuarioDesc) {
        this.tipoUsuarioDesc = tipoUsuarioDesc;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nombres='" + nombres + '\'' +
                ", token='" + token + '\'' +
                ", estadoUsuario=" + estadoUsuario +
                ", tipoUsuario=" + tipoUsuario +
                ", tipoUsuarioDesc='" + tipoUsuarioDesc + '\'' +
                '}';
    }
}
