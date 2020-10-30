package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.persistence.entities.Catalogo;
import ec.net.redcode.tas.on.persistence.service.CatalogoService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;

import java.util.List;

public class CatalogoBean extends TasOnResponse {

    CatalogoService catalogoService;

    public void createCatalogo(Catalogo catalogo){
        catalogoService.create(catalogo);
    }

    public Catalogo readCatalogo(int idCatalogo){
        Catalogo catalogo = catalogoService.read(idCatalogo);
        return catalogo;
    }

    public void updateCatalogo(Catalogo catalogo){
        catalogoService.update(catalogo);
    }

    public void deleteCatalogo(Catalogo catalogo){
        catalogoService.delete(catalogo);
    }

    public List<Catalogo> getAllCatalogo(){
        List<Catalogo> catalogos = catalogoService.getAllCatalogos();
        return catalogos;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }
}
