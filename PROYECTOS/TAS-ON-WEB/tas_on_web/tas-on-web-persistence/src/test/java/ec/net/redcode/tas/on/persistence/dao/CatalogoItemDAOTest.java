package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CatalogoItemDAOTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(CatalogoItemDAOTest.class);

    @Test
    @Ignore
    public void testCrudCatalogoItem(){
        CatalogoItemDAO catalogoItemDAO = (CatalogoItemDAO) getBean("catalogoItemDAO");
        CatalogoItem catalogoItem = new CatalogoItem();
        catalogoItem.setCatalogoItemId(2);
        catalogoItem.setCatalogoItemIdCatalogo(1);
        catalogoItem.setCatalogoItemDescripcion("Testing Calatolo");
        catalogoItem.setCatalogoItemEstado(1);
        logger.info("Creating catalogo item with document " + catalogoItem.getCatalogoItemId());
        catalogoItemDAO.create(catalogoItem);
        CatalogoItem catalogoItemRead = catalogoItemDAO.read(1);
        logger.info("Reading catalogo item with document " + catalogoItemRead.getCatalogoItemDescripcion());
        catalogoItem.setCatalogoItemDescripcion("Testing upate");
        logger.info("Updating catalogo item with document " + catalogoItemRead.getCatalogoItemDescripcion());
        catalogoItemDAO.update(catalogoItem);
        logger.info("Deleting catalogo item with document " + catalogoItemRead.getCatalogoItemDescripcion());
        catalogoItemDAO.delete(catalogoItem);
    }

    @Test
    public void testGetCatalogoItemByCatalogo(){
        CatalogoItemDAO catalogoItemDAO = (CatalogoItemDAO) getBean("catalogoItemDAO");
        List<CatalogoItem> catalogoItems = catalogoItemDAO.getCatalogoItemByCatalogo(1);
        Assert.assertNotNull(catalogoItems);
    }

}
