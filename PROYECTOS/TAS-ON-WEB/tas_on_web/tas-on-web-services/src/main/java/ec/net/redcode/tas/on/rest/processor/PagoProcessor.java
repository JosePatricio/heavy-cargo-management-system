package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Pay;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.facturacion.InfoPago;
import ec.net.redcode.tas.on.persistence.entities.Pago;
import ec.net.redcode.tas.on.rest.bean.PagoBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagoProcessor extends PagoBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        try {
            switch (operationName) {
                case MethodConstant.PAGO_CREATE:
                    Pago pago = exchange.getIn().getBody(Pago.class);
                    createPago(pago);
                    setResponse(exchange);
                    break;
                case MethodConstant.PAGO_CREATE_RETENCION:
                    InfoPago infoPago = exchange.getIn().getBody(InfoPago.class);
                    createPago(infoPago);
                    setResponse(exchange);
                    break;
                case MethodConstant.PAGO_PAGOS:
                    String facturaId = messageList.get(0).toString();
                    List<Pay> pays = readPagos(facturaId);
                    exchange.getIn().setBody(response(pays));
                    break;
                case MethodConstant.PAGO_DETAIL:
                    int pagoId = Integer.parseInt(messageList.get(0).toString());
                    Pay pay = getPayDetail(pagoId);
                    exchange.getIn().setBody(response(pay));
                    break;
                case MethodConstant.PAGO_RETENCION:
                    String claveAcceso = messageList.get(0).toString();
                    String idFactura = messageList.get(1).toString();
                    Map<String, Object> response = getComprobanteRetencion(claveAcceso, idFactura);
                    exchange.getIn().setBody(response(response));
                    break;
                case MethodConstant.PAGO_NOTA_CREDITO_INFO:
                    idFactura = messageList.get(0).toString();
                    response = getPagoNCInfo(idFactura);
                    exchange.getIn().setBody(response(response));
                    break;
            }
        } catch (TasOnException e) {
            webFault(e, exchange, Boolean.TRUE);
        }
    }

    private void setResponse(Exchange exchange){
        Map<String, String> responseCreate = new HashMap<>();
        responseCreate.put("response", Constant.RESPONSE_OK);
        responseCreate.put("responseMessage", "Pago creado correctamente");
        exchange.getIn().setBody(response(responseCreate));
    }

}
