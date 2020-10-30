package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;

import ec.net.redcode.tas.on.persistence.entities.Localidad;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class LocalidadDAOTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(LocalidadDAOTest.class);


    @Test
    public void testCrudUsuarios(){
        LocalidadDAO localidadDAO= (LocalidadDAO) getBean("localidadDAO");
        Localidad localidad = new Localidad();
        localidad.setLocalidadLocalidadPadre(null);
        localidad.setLocalidadId(775);
        localidad.setLocalidadDescripcion("Calceta");

        localidadDAO.create(localidad);
        logger.info("Creating localidad with idLocalidad " + localidad.getLocalidadId());
        Localidad localidadPadre = new Localidad();
        localidadPadre.setLocalidadId(593);
        localidadPadre.setLocalidadDescripcion("Manabi");
        localidadPadre.setLocalidadLocalidadPadre(null);
        localidadDAO.create(localidadPadre);
        localidad.setLocalidadLocalidadPadre(593);

        logger.info("Updating user with idlocalidad Padre origen :" + localidad.getLocalidadLocalidadPadre());
        localidadDAO.update(localidad);
        logger.info("Updating user with idlocalidad Padre nuevo  :" + localidad.getLocalidadLocalidadPadre());


        Localidad localidadRead = localidadDAO.read(775);
        logger.info("Reading user with document " + localidadRead.getLocalidadId());
        logger.info("Deleting user with document " + localidadRead.getLocalidadId());
        localidadDAO.delete(localidad);

    }

    @Test
    public void testGetLocalidadByLocalidadPadreId(){
        LocalidadDAO localidadDAO= (LocalidadDAO) getBean("localidadDAO");
        List<Localidad> localidad= localidadDAO.getLocalidadByLocalidadIdPadre(1,1);
        logger.info("Registro leido " + localidad.size());
        Assert.assertNotNull(localidad);
    }
    @Test
    public void testGetLocalidadByLocalidadId(){
        LocalidadDAO localidadDAO= (LocalidadDAO) getBean("localidadDAO");
        Localidad localidad = new Localidad();
        localidad= localidadDAO.getLocalidadByLocalidadId(1,1);
        logger.info("Registro leido " + localidad.getLocalidadId());
        Assert.assertNotNull(localidad);
    }


}
