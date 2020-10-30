package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.InvoiceProvider;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;
import ec.net.redcode.tas.on.rest.bean.FacturaProveedorBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FacturaProveedorProcessor extends FacturaProveedorBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        String username = exchange.getIn().getHeader("user").toString();
        Map<String, String> response = new ConcurrentHashMap<>();
        switch (operationName) {
            case MethodConstant.FACTURA_PROVEEDOR_CREATE:
                FacturaProveedor facturaProveedor = exchange.getIn().getBody(FacturaProveedor.class);
                try{
                    create(facturaProveedor, username);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Factura Proveedor creado correctamente");
                    exchange.getIn().setHeader("prefactura", facturaProveedor.getFacturaProveedorId());
                    exchange.getIn().setHeader("tipo", "facturaProveedor");
                    exchange.getIn().setHeader("compraLocal", facturaProveedor.getFacturaProveedorCompra());
                    exchange.getIn().setBody(response(response));
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                    exchange.getIn().setBody(response(response));
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }

                break;
            case MethodConstant.FACTURA_PROVEEDOR_READ:
                int idFacturaProveedor = exchange.getIn().getBody(Integer.class);
                FacturaProveedor facturaProveedor1 = read(idFacturaProveedor);
                exchange.getIn().setBody(response(facturaProveedor1));
                break;
            case MethodConstant.FACTURA_PROVEEDOR_UPDATE:
                List<InvoiceProvider> invoiceProvider = (List<InvoiceProvider>) messageList.get(0);
                update(invoiceProvider);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Factura Proveedor actualizado correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.FACTURA_PROVEEDOR_BY_NUMERO_RUC:
                String numeroFactura = (String) messageList.get(0);
                String ruc = (String) messageList.get(1);
                FacturaProveedor facturaProveedorBy = getByNumeroFacturaAndRuc(numeroFactura, ruc);
                exchange.getIn().setBody(response(facturaProveedorBy));
                break;
            case MethodConstant.FACTURA_PROVEEDOR_BY_ESTADO:
                int estado = exchange.getIn().getBody(Integer.class);
                List<InvoiceProvider> facturaProveedors = getByEstado(estado);
                exchange.getIn().setBody(response(facturaProveedors));
                break;
        }
    }

}
