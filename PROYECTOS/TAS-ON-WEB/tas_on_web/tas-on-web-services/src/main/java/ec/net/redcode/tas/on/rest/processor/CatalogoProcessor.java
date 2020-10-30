package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.persistence.entities.Catalogo;
import ec.net.redcode.tas.on.rest.bean.CatalogoBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoProcessor extends CatalogoBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName) {
            case MethodConstant.CATALOGO_CREATE:
                Catalogo catalogoCreate = exchange.getIn().getBody(Catalogo.class);
                createCatalogo(catalogoCreate);
                Map<String, String> responseCreate = new HashMap<>();
                responseCreate.put("response", "Catalogo creado correctamente");
                exchange.getIn().setBody(response(responseCreate));
                break;
            case MethodConstant.CATALOGO_READ:
                int idCatalogo = exchange.getIn().getBody(Integer.class);
                Catalogo catalogo = readCatalogo(idCatalogo);
                exchange.getIn().setBody(response(catalogo));
                break;
            case MethodConstant.CATALOGO_UPDATE:
                Catalogo catalogoUpdate = exchange.getIn().getBody(Catalogo.class);
                updateCatalogo(catalogoUpdate);
                Map<String, String> responseUpdate = new HashMap<>();
                responseUpdate.put("response", "Catalogo actualizado correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.CATALOGO_DELETE:
                Catalogo catalogoDelete = exchange.getIn().getBody(Catalogo.class);
                deleteCatalogo(catalogoDelete);
                Map<String, String> responseDelete = new HashMap<>();
                responseDelete.put("response", "Catalogo borrado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.CATALOGO_ALL:
                List<Catalogo> catalogos = getAllCatalogo();
                exchange.getIn().setBody(response(catalogos));
                break;
        }
    }

}
