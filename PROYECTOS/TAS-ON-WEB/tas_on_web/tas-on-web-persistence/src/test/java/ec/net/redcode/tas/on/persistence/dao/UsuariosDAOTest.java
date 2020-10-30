package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuariosDAOTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(UsuariosDAOTest.class);

    @Test
    public void testCrudUsuarios(){
        UsuarioDAO usuariosDAO = (UsuarioDAO) getBean("usuariosDAO");
        Usuario usuarios = new Usuario();

        usuarios.setUsuarioIdDocumento("1308930807");
        usuarios.setUsuarioTipoDocumento(1);
        usuarios.setUsuarioNombres("Mauricio Nicolas");
        usuarios.setUsuarioApellidos("Chilan Macias");
        usuarios.setUsuarioMail("mauricio.chilan@redcode.net.ec");
        //usuarios.setLocalidad(1);
        logger.info("Creating user with document " + usuarios.getUsuarioIdDocumento());
        usuariosDAO.create(usuarios);
        usuarios.setUsuarioCelular("0981950430");
        usuarios.setUsuarioDireccion("Pto Lopez");
        logger.info("Updating user with document " + usuarios.getUsuarioIdDocumento());
        usuariosDAO.update(usuarios);
        Usuario usuariosRead = usuariosDAO.read("1308930807");
        logger.info("Reading user with document " + usuariosRead.getUsuarioIdDocumento());
        logger.info("Deleting user with document " + usuarios.getUsuarioIdDocumento());
        usuariosDAO.delete(usuarios);
    }

    @Test
    public void testGetUsuariosByUserName(){
        UsuarioDAO usuariosDAO = (UsuarioDAO) getBean("usuariosDAO");
        Usuario usuarios = usuariosDAO.getUsuariosByUserName("mauchilan");
        Assert.assertNotNull(usuarios);
    }

}
