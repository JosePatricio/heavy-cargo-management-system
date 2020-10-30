package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Offer;
import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.OfertaBean;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OfertaProcessor extends OfertaBean implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        String username = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(username);

        int estado;
        String solicitud;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        List<Offers> offersList;
        switch (operationName) {
            case MethodConstant.OFERTA_OFFER:
                Offer offer = exchange.getIn().getBody(Offer.class);
                Map<String, String> responseOffer = new ConcurrentHashMap<>();
                try{
                    offerValue(offer, usuario);
                    responseOffer.put("response", Constant.RESPONSE_OK);
                    responseOffer.put("responseMessage", "Oferta ingresada correctamente");
                    exchange.getIn().setHeader("solicitud", offer.getIdSolicitud());
                }catch (TasOnException e){
                    responseOffer.put("response", e.getMessage());
                    responseOffer.put("responseMessage", e.getDeveloperMessage());
                    //exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE); TODO REVISAR
                }
                exchange.getIn().setBody(response(responseOffer));
                break;
            case MethodConstant.OFERTA_ALL_OFERTA_BY_USER_AND_ESTADO:
                estado = exchange.getIn().getBody(Integer.class);
                List<Offers> offers = getOfertasByEstadoAndUser(estado, usuario);
                exchange.getIn().setBody(response(offers));
                break;
            case MethodConstant.OFERTA_GET_OFFER:
                int ofertaId = exchange.getIn().getBody(Integer.class);
                Solicitud solicitudData = getOferta(ofertaId);
                exchange.getIn().setBody(response(solicitudData));
                break;
            case MethodConstant.OFERTA_ALL_OFERTA_ESTADO:
                int estadoOferta = exchange.getIn().getBody(Integer.class);
                offersList = getOfertasByEstado(estadoOferta, usuario);
                exchange.getIn().setBody(response(offersList));
                break;
            case MethodConstant.OFERTAS_BY:
                int i = 0;
                estado = TasOnUtil.getIntFromObject(messageList.get(i++));
                int tipoFactura = TasOnUtil.getIntFromObject(messageList.get(i++));
                String razonSocial = TasOnUtil.getStringFromObject(messageList.get(i++));
                solicitud = TasOnUtil.getStringFromObject(messageList.get(i++));
                String facturaNro = TasOnUtil.getStringFromObject(messageList.get(i++));
                String fechaDesde = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaDesdeLD = fechaDesde != null ? formatter.parse(fechaDesde): null;
                String fechaHasta = TasOnUtil.getStringFromObject(messageList.get(i++));
                Date fechaHastaLD = fechaHasta != null ? formatter.parse(fechaHasta) : null;
                offersList = getOfertasBy(estado, tipoFactura, razonSocial, solicitud, facturaNro,
                        fechaDesdeLD, fechaHastaLD);
                exchange.getIn().setBody(response(offersList));
                break;
            case MethodConstant.OFERTA_OFFER_UDPATE:
                Offer offerUpdate = exchange.getIn().getBody(Offer.class);
                Map<String, String> responseUpdateOffer = new ConcurrentHashMap<>();
                try{
                    updateOffer(offerUpdate, responseUpdateOffer, usuario, exchange);
                    responseUpdateOffer.put("response", Constant.RESPONSE_OK);
                    responseUpdateOffer.putIfAbsent("responseMessage", "Oferta actualizada correctamente");
                    exchange.getIn().setHeader("solicitud", offerUpdate.getIdSolicitud());
                }catch (TasOnException e){
                    responseUpdateOffer.put("response", e.getMessage());
                    responseUpdateOffer.put("responseMessage", e.getDeveloperMessage());
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }
                exchange.getIn().setBody(response(responseUpdateOffer));
                break;
            case MethodConstant.OFERTA_OFERTAS_BY_SOLICITUD:
                solicitud = messageList.get(0).toString();
                int state = Integer.parseInt(messageList.get(1).toString());
                List<Offers> offerss = getOfertasBySolicitud(solicitud, state, usuario);
                exchange.getIn().setBody(response(offerss));
                break;
            case MethodConstant.OFERTA_GET_DOCUMENT:
                int idOfertas = Integer.parseInt(messageList.get(0).toString());
                int tipoFoto = Integer.parseInt(messageList.get(1).toString());
                List<String> document = getDocumentOferta(idOfertas, tipoFoto);
                exchange.getIn().setBody(response(document));
                break;
            case MethodConstant.OFERTA_GENERATE_CASH_FILE:
                List<Offer> offersCashM = (List<Offer>) messageList.get(0);
                updateOfertaList(offersCashM, usuario);
                exchange.getIn().setBody(response(generateCashFile(offersCashM, usuario)));
                break;
            case MethodConstant.OFERTA_DOWNLOAD_CASH_FILE:
                List<Offer> ofertas = (List<Offer>) messageList.get(0);
                exchange.getIn().setBody(response(generateCashFile(ofertas, usuario)));
                break;
            case MethodConstant.OFERTA_GET_FOTO:
                int fotoId = Integer.parseInt(messageList.get(0).toString());
                exchange.getIn().setBody(response(getFoto(fotoId, usuario)));
                break;
        }
    }

}
