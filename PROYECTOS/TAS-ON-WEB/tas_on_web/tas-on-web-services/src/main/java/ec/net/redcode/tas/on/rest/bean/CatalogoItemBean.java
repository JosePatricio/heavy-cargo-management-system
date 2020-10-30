package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;

public class CatalogoItemBean extends CommonBean {

    public void createCatalogo(CatalogoItem catalogo){
        this.catalogoItemService.create(catalogo);
    }

    public void updateCatalogo(CatalogoItem catalogo){
        this.catalogoItemService.update(catalogo);
    }

    public void deleteCatalogo(CatalogoItem catalogo){
        this.catalogoItemService.delete(catalogo);
    }

}
