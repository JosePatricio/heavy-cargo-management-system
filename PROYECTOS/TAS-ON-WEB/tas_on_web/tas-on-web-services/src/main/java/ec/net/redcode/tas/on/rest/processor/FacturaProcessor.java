package ec.net.redcode.tas.on.rest.processor;

import com.google.gson.Gson;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Invoice;
import ec.net.redcode.tas.on.common.dto.InvoiceDetail;
import ec.net.redcode.tas.on.common.dto.Invoices;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Factura;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.FacturaBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FacturaProcessor extends FacturaBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String username = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Map<String, String> response = new ConcurrentHashMap<>();
        int estado, i;
        switch (operationName){
            case MethodConstant.FACTURA_CREATE:
                Factura factura = exchange.getIn().getBody(Factura.class);
                createFactura(factura);
                Map<String, String> responseCreate = new ConcurrentHashMap<>();
                responseCreate.put("response", Constant.RESPONSE_OK);
                responseCreate.put("responseMessage", "Factura creada correctamente");
                exchange.getIn().setBody(response(responseCreate));
                break;
            case MethodConstant.FACTURA_CODE:
                String code = generateCode(username);
                response.put("code", code);
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.FACTURA_PUEDE_CREAR_PREFACTURA:
                try{
                    puedeCrearPrefactura(usuario);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "");
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                }
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.FACTURA_CREATE_PREINVOICE:
                try{
                    puedeCrearPrefactura(usuario);
                    Invoice invoice = exchange.getIn().getBody(Invoice.class);
                    createPreInvoice(invoice, usuario);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "PreFactura creada correctamente");
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                }
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.FACTURA_LIST:
                estado = exchange.getIn().getBody(Integer.class);
                List<Invoices> invoices = getInvoicesByUsername(username, estado);
                exchange.getIn().setBody(response(invoices));
                break;
            case MethodConstant.FACTURA_DETAIl:
                String idFactura = exchange.getIn().getBody(String.class);
                InvoiceDetail invoiceDetail = getInvoiceDetail(idFactura);
                exchange.getIn().setBody(response(invoiceDetail));
                break;
            case MethodConstant.FACTURA_LIST_ALL:
                int estados = exchange.getIn().getBody(Integer.class);
                List<Invoices> invoicesList = getInvoiceByEstado(estados);
                exchange.getIn().setBody(response(invoicesList));
                break;
            case MethodConstant.FACTURA_UPDATE:
                Invoice invoiceUpdate = exchange.getIn().getBody(Invoice.class);
                exchange.getIn().setHeader("prefactura", invoiceUpdate.getNumberInvoice());
                exchange.getIn().setHeader("tipo", "factura");
                updateInvoice(invoiceUpdate, username);
                Map<String, String> responseUpdate = new ConcurrentHashMap<>();
                exchange.getIn().setHeader("tipoPago", invoiceUpdate.getInvoiceTypePay());
                responseUpdate.put("response", Constant.RESPONSE_OK);
                responseUpdate.put("responseMessage", "Factura Actualizada correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.FACTURA_LIST_ALL_BY:
                i = 0;
                estado = TasOnUtil.getIntFromObject(messageList.get(i++));
                String cliente = TasOnUtil.getStringFromObject(messageList.get(i++));
                String facturaNro = TasOnUtil.getStringFromObject(messageList.get(i++));
                double valorDesde = TasOnUtil.getDoubleFromObject(messageList.get(i++));
                double valorHasta = TasOnUtil.getDoubleFromObject(messageList.get(i++));
                String fechaCobroDesdeStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaCobroDesde = fechaCobroDesdeStr != null ? formatter.parse(fechaCobroDesdeStr): null;
                String fechaCobroHastaStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaCobroHasta = fechaCobroHastaStr != null ? formatter.parse(fechaCobroHastaStr) : null;
                exchange.getIn().setBody(response(getInvoicesAllBy(estado, cliente, facturaNro, valorDesde, valorHasta,
                        fechaCobroDesde, fechaCobroHasta)));
                break;
            case MethodConstant.FACTURA_MANUAL_CREATE:
                ec.gob.sri.comprobantes.modelo.factura.Factura facturaManual;
                HashMap<String, String> data = new HashMap<>();
                try{
                    facturaManual = exchange.getIn().getBody(ec.gob.sri.comprobantes.modelo.factura.Factura.class);
                }catch (Exception e){
                    facturaManual = null;
                }
                if(datosValidosFactura(facturaManual)){
                    ingresarValoresDefectoFactura(facturaManual);
                    crearActualizarAdquiriente(facturaManual.getInfoFactura());
                    exchange.getIn().setHeader("facturaManual", new Gson().toJson(facturaManual));
                    exchange.getIn().setHeader("usuarioFacturaManual", getUsuario(username).getUsuarioIdDocumento());
                    data.put("response", Constant.RESPONSE_OK);
                    data.put("responseMessage", "Operación exitosa");
                }else{
                    data.put("response", Constant.RESPONSE_ERROR);
                    data.put("responseMessage", "Existe un error con los datos enviados, revise e intente de nuevo");
                }

                exchange.getIn().setBody(data);
                break;
            case MethodConstant.FACTURA_MANUAL_ALL_BY:
                i = 0;
                String secuencial = TasOnUtil.getStringFromObject(messageList.get(i++));
                String razonSocial = TasOnUtil.getStringFromObject(messageList.get(i++));
                String ruc = TasOnUtil.getStringFromObject(messageList.get(i++));
                String fechaEmisionDesdeStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaEmisionDesde = fechaEmisionDesdeStr != null ? formatter.parse(fechaEmisionDesdeStr): null;
                String fechaEmisionHastaStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaEmisionHasta = fechaEmisionHastaStr != null ? formatter.parse(fechaEmisionHastaStr) : null;
                exchange.getIn().setBody(response(getFacturasManualesBy(secuencial, razonSocial, ruc, fechaEmisionDesde, fechaEmisionHasta)));
                break;
            case MethodConstant.FACTURA_MANUAL_DOWNLOAD_XML:
                String claveAcceso = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(response(getFacturaManualXML(claveAcceso)));
                break;
            case MethodConstant.FACTURA_RETENCION_GET_BY:
                i = 0;
                String razonSocialCliente = TasOnUtil.getStringFromObject(messageList.get(i++));
                String numeroFacturaCliente = TasOnUtil.getStringFromObject(messageList.get(i++));
                String prefactura = TasOnUtil.getStringFromObject(messageList.get(i++));
                String fechaAutorizacionDesdeStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaAutorizacionDesde = fechaAutorizacionDesdeStr != null ? formatter.parse(fechaAutorizacionDesdeStr): null;
                String fechaAutorizacionHastaStr = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaAutorizacionHasta = fechaAutorizacionHastaStr != null ? formatter.parse(fechaAutorizacionHastaStr) : null;
                exchange.getIn().setBody(response(getRetencionesBy(razonSocialCliente, numeroFacturaCliente, prefactura,
                        fechaAutorizacionDesde, fechaAutorizacionHasta)));
                break;
            case MethodConstant.FACTURA_RETENCION_DOWNLOAD_XML:
                String claveAccesoRetencion = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(response(getRetencionXML(claveAccesoRetencion)));
                break;
        }
    }

    private void ingresarValoresDefectoFactura(ec.gob.sri.comprobantes.modelo.factura.Factura factura){
        factura.setId(Constant.INVOICE_ID);
        factura.setVersion(Constant.INVOICE_VERSION);
        factura.getInfoFactura().setMoneda(Constant.INVOICE_MONEDA);
        factura.getInfoFactura().setPropina(BigDecimal.ZERO);
        factura.getInfoFactura().setTotalDescuento(BigDecimal.ZERO);
    }

    private void ingresarValoresDefectoDetalle(ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle detalle){
        detalle.setDescuento(BigDecimal.ZERO);
        detalle.setCantidad(BigDecimal.ONE);
        detalle.setPrecioUnitario(detalle.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP));
    }

    private boolean datosValidosFactura(ec.gob.sri.comprobantes.modelo.factura.Factura factura){
        if(factura == null || factura.getInfoFactura() == null || factura.getDetalles() == null) return false;

        ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura infoFactura = factura.getInfoFactura();
        if(TasOnUtil.isStringNullOrEmpty(infoFactura.getTipoIdentificacionComprador(),
                infoFactura.getRazonSocialComprador(), infoFactura.getIdentificacionComprador(),
                infoFactura.getDirEstablecimiento())) return false;

        ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles detalles = factura.getDetalles();
        if(detalles.getDetalle() == null || detalles.getDetalle().size() == 0) return false;
        for(ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle detalle : detalles.getDetalle()){
            if(TasOnUtil.isStringNullOrEmpty(detalle.getCodigoPrincipal(), detalle.getDescripcion())
            || detalle.getPrecioUnitario() == null) return false;
            ingresarValoresDefectoDetalle(detalle);
        }
        return true;
    }


}
