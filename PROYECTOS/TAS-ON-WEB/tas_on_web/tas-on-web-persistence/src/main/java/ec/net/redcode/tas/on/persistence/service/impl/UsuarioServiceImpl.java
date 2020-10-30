package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.UsuarioDAO;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import lombok.Setter;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    @Setter private UsuarioDAO usuariosDAO;

    @Override
    public void createUsuario(Usuario usuarios) {
        usuariosDAO.create(usuarios);
    }

    @Override
    public Usuario readUsuario(String idDocumento) {
        return usuariosDAO.read(idDocumento);
    }

    @Override
    public void updateUsuario(Usuario usuarios) {
        usuariosDAO.update(usuarios);
    }

    @Override
    public void deleteUsuario(Usuario usuarios) {
        usuariosDAO.delete(usuarios);
    }

    @Override
    public Usuario getUsuariosByUserName(String username) {
        return usuariosDAO.getUsuariosByUserName(username);
    }

    @Override
    public Usuario getUsuarioByUserNameAndPassword(String userName, String password) {
        return usuariosDAO.getUsuarioByUserNameAndPassword(userName, password);
    }

    @Override
    public List<Usuario> getUsuariosByTipoUsuario(int tipoUsuario, int estado) {
        return usuariosDAO.getUsuariosByTipoUsuario(tipoUsuario, estado);
    }

    @Override
    public List<Usuario> getUsuariosByEmpresaTipoUsuario(String rucEmpresa, int tipoUsuario, int estado) {
        return usuariosDAO.getUsuariosByEmpresaTipoUsuario(rucEmpresa, tipoUsuario, estado);
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        return usuariosDAO.getUsuarioByEmail(email);
    }

    @Override
    public List<Usuario> getUsuarioByRuc(String ruc) {
        return usuariosDAO.getUsuarioByRuc(ruc);
    }

    @Override
    public long hasAdmin(String ruc) {
        return usuariosDAO.hasAdmin(ruc);
    }

    @Override
    public Usuario getUsuarioByIdentificadorAndEmail(String identificador, String email) {
        return usuariosDAO.getUsuarioByIdentificadorAndEmail(identificador, email);
    }

    @Override
    public List<Usuario> getUsuarioByEmpresaAndEstado(String idEmpresa, int estado) {
        return usuariosDAO.getUsuarioByEmpresaAndEstado(idEmpresa, estado);
    }

}
