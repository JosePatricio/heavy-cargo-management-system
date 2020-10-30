package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.Catalogo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CatalogoDAOTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(CatalogoItemDAOTest.class);

    @Test
    @Ignore
    public void testCrudCatalogo(){
        CatalogoDAO catalogoDAO = (CatalogoDAO) getBean("catalogoDAO");
        Catalogo catalogo = new Catalogo();
        catalogo.setCatalogoId(2);
        catalogo.setCatalogoDescripcion("Nuevo Catalogo");
        catalogo.setCatalogoEstado(1);
        logger.info("Creating catalogo with document " + catalogo.getCatalogoId());
        catalogoDAO.create(catalogo);
        Catalogo catalogoRead = catalogoDAO.read(1);
        logger.info("Reading catalogo with document " + catalogoRead.getCatalogoDescripcion());
        catalogo.setCatalogoDescripcion("Testing upate");
        logger.info("Updating catalogo with document " + catalogoRead.getCatalogoDescripcion());
        catalogoDAO.update(catalogo);
        logger.info("Deleting catalogo with document " + catalogoRead.getCatalogoDescripcion());
        catalogoDAO.delete(catalogo);
    }

    @Test
    public void testGetAllCatalogo(){
        CatalogoDAO catalogoDAO = (CatalogoDAO) getBean("catalogoDAO");
        List<Catalogo> catalogos = catalogoDAO.getAllCatalogo();
        Assert.assertNotNull(catalogos);
    }

}
