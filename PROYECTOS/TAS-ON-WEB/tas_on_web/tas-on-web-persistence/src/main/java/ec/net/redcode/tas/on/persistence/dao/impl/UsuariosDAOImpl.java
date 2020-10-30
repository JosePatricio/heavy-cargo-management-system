package ec.net.redcode.tas.on.persistence.dao.impl;


import ec.net.redcode.tas.on.persistence.dao.UsuarioDAO;
import ec.net.redcode.tas.on.persistence.entities.Usuario;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class UsuariosDAOImpl extends GenericDAOImpl<Usuario, String> implements UsuarioDAO{

    public UsuariosDAOImpl(){
        super(Usuario.class);
    }

    public Usuario getUsuariosByUserName(String username) {
        Query query = em.createNamedQuery("Usuarios.UsuariosByNombreUsuario");
        query.setParameter("nombreUsuario", username);
        Usuario usuarios;
        try {
            usuarios = (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            usuarios = null;
        }
        return usuarios;
    }

    @Override
    public Usuario getUsuarioByUserNameAndPassword(String userName, String password) {
        Query query = em.createNamedQuery("Usuarios.UsuariosByNombreUsuarioAndContrasenia");
        query.setParameter("nombreUsuario", userName);
        query.setParameter("contrasenia", password);
        Usuario usuarios;
        try {
            usuarios = (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            usuarios = null;
        }
        return usuarios;
    }

    @Override
    public List<Usuario> getUsuariosByTipoUsuario(int tipoUsuario, int estado) {
        Query query = em.createNamedQuery("Usuarios.UsuariosByTipoUsuario");
        query.setParameter("tipoUsuario", tipoUsuario);
        query.setParameter("estado", estado);
        List<Usuario> usuarios = query.getResultList() ;
        return usuarios;
    }

    @Override
    public List<Usuario> getUsuariosByEmpresaTipoUsuario(String rucEmpresa, int tipoUsuario, int estado) {
        Query query = em.createNamedQuery("Usuarios.UsuariosByEmpresaTipoUsuario");
        query.setParameter("usuarioRuc", rucEmpresa);
        query.setParameter("tipoUsuario", tipoUsuario);
        query.setParameter("estado", estado);
        return query.getResultList() ;
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        Query query = em.createNamedQuery("Usuario.UsuarioByEmail");
        query.setParameter("email", email);
        Usuario usuario;
        try {
            usuario = (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            usuario = null;
        }
        return usuario;
    }

    @Override
    public List<Usuario> getUsuarioByRuc(String ruc) {
        Query query = em.createNamedQuery("Usuario.UsuarioByRuc");
        query.setParameter("ruc", ruc);
        List<Usuario> usuarios;
        try {
            usuarios = query.getResultList();
        }catch (NoResultException e){
            usuarios = Collections.emptyList();
        }
        return usuarios;
    }

    @Override
    public long hasAdmin(String ruc) {
        Query query = em.createNamedQuery("Usuario.UsuarioAdmin");
        query.setParameter("ruc", ruc);
        long count = (long) query.getSingleResult();
        return count;
    }

    @Override
    public Usuario getUsuarioByIdentificadorAndEmail(String identificador, String email) {
        Query query = em.createNamedQuery("Usuario.UsuarioByUsuarioAndEmail");
        query.setParameter("identificador", identificador);
        query.setParameter("mail", email);
        Usuario usuario;
        try {
            usuario = (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            usuario = null;
        }
        return usuario;
    }

    @Override
    public List<Usuario> getUsuarioByEmpresaAndEstado(String idEmpresa, int estado) {
        Query query = em.createNamedQuery("Usuario.UsuarioByEmpresaAndEstado");
        query.setParameter("idEmpresa", idEmpresa);
        query.setParameter("estado", estado);
        List<Usuario> usuarios;
        try {
            usuarios = query.getResultList();
        }catch (NoResultException e){
            usuarios = Collections.emptyList();
        }
        return usuarios;
    }
}
