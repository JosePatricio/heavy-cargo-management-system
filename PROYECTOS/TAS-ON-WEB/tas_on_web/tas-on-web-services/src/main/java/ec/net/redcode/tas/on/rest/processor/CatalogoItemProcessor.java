package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.rest.bean.CatalogoItemBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoItemProcessor extends CatalogoItemBean implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName) {
            case MethodConstant.CATALOGO_ITEM_CREATE:
                CatalogoItem catalogoItemCreate = exchange.getIn().getBody(CatalogoItem.class);
                createCatalogo(catalogoItemCreate);
                Map<String, String> responseCreate = new HashMap<>();
                responseCreate.put("response", "Catalogo Item creado correctamente");
                exchange.getIn().setBody(response(responseCreate));
                break;
            case MethodConstant.CATALOGO_ITEM_READ:
                int idCatalogoItem = exchange.getIn().getBody(Integer.class);
                CatalogoItem catalogoItem = readCatalogo(idCatalogoItem);
                exchange.getIn().setBody(response(catalogoItem));
                break;
            case MethodConstant.CATALOGO_ITEM_UPDATE:
                CatalogoItem catalogoItemUpdate = exchange.getIn().getBody(CatalogoItem.class);
                updateCatalogo(catalogoItemUpdate);
                Map<String, String> responseUpdate = new HashMap<>();
                responseUpdate.put("response", "Catalogo Item actualizado correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.CATALOGO_ITEM_DELETE:
                CatalogoItem catalogoItemDelete = exchange.getIn().getBody(CatalogoItem.class);
                deleteCatalogo(catalogoItemDelete);
                Map<String, String> responseDelete = new HashMap<>();
                responseDelete.put("response", "Catalogo Item borrado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.CATALOGO_ITEM_ALL_BY_CATALOGO:
                int idCatalogo = exchange.getIn().getBody(Integer.class);
                List<CatalogoItem> catalogoItems = getCatalogoItemByCatalogo(idCatalogo);
                exchange.getIn().setBody(response(catalogoItems));
                break;
        }
    }
}
