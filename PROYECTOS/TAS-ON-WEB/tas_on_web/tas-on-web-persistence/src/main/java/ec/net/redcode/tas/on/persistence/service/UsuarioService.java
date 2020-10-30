package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Usuario;

import java.util.List;

public interface UsuarioService {

    void createUsuario(Usuario usuarios);

    Usuario readUsuario(String idDocumento);

    void updateUsuario(Usuario usuarios);

    void deleteUsuario(Usuario usuarios);

    Usuario getUsuariosByUserName(String username);

    Usuario getUsuarioByUserNameAndPassword(String userName, String password);

    List<Usuario> getUsuariosByTipoUsuario(int tipoUsuario, int estado);

    List<Usuario> getUsuariosByEmpresaTipoUsuario(String rucEmpresa, int tipoUsuario, int estado);

    Usuario getUsuarioByEmail(String email);

    List<Usuario> getUsuarioByRuc(String ruc);

    long hasAdmin(String ruc);

    Usuario getUsuarioByIdentificadorAndEmail(String identificador, String email);

    List<Usuario> getUsuarioByEmpresaAndEstado(String idEmpresa, int estado);

}
