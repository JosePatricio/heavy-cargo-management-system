package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioServiceTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(UsuarioServiceTest.class);

    @Test
    public void testGetUsuarioByUserNameAndPassword(){
        UsuarioService usuarioService = (UsuarioService) getBean("usuarioService");
        Usuario usuario = usuarioService.getUsuarioByUserNameAndPassword("mauchilan", "password");
        Assert.assertNotNull(usuario);
        //logger.info(String.valueOf("Estado ----> " + usuario.);
    }

}
