package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Usuario;

import java.util.List;

public interface UsuarioDAO extends GenericDAO<Usuario, String> {

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
