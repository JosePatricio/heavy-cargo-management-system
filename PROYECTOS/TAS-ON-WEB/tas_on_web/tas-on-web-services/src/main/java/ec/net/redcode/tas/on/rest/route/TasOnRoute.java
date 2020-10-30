package ec.net.redcode.tas.on.rest.route;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Invoice;
import ec.net.redcode.tas.on.common.dto.OfertaNotificacion;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import javax.xml.bind.JAXBContext;
import java.util.HashMap;
import java.util.Map;

public class TasOnRoute extends RouteBuilder {

    @Setter
    private CatalogoItemService catalogoItemService;
    private String policy;

    public void initCron(){
        policy = catalogoItemService.read(Constant.CATALOGO_POLICY_CREACION_SOLICITUDES).getCatalogoItemValor();
    }
    @Override
    public void configure() throws Exception {

        JaxbDataFormat dataFormat = new JaxbDataFormat(JAXBContext.newInstance(Invoice.class));
        dataFormat.setPrettyPrint(Boolean.TRUE);
        dataFormat.setIgnoreJAXBElement(false);

        /**************** PREDICADOS ********************/
        Predicate headerEbillingGenerate = header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.EBILLING_GENERATE);
        Predicate headerEbillingSend = header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.EBILLING_SEND);
        Predicate headerEbillingSendMail = header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.EBILLING_SEND_MAIL);

        Predicate headerPropertyContenido = header("contenido").isNotNull();
        Predicate headerPropertyXML = header("xml").isNotNull();
        Predicate headerPropertyEbilling = header("ebilling").isNotNull();

        Predicate generateEbilling = PredicateBuilder.and(headerEbillingGenerate, headerPropertyContenido);
        Predicate sendEbilling = PredicateBuilder.and(headerEbillingSend, headerPropertyXML);
        Predicate sendEbillingMail = PredicateBuilder.and(headerEbillingSendMail, headerPropertyEbilling);
        /************** /PREDICADOS ********************/

        from("cxfrs:bean:rsServerUsuario")
                //.transacted()
                .to("requestProcessor")
                .to("usuarioProcessor")
                .wireTap("seda:ec.net.redcode.tas.on.notificacion");

        from("cxfrs:bean:rsServerLogin")
                .to("requestProcessor")
                .to("usuarioProcessor");

        from("cxfrs:bean:rsServerSecurity")
                .to("requestProcessor")
                .to("securityProcessor")
                .to("securityRestProcessor");

        from("cxfrs:bean:rsServerCatalogo")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("catalogoProcessor");

        from("cxfrs:bean:rsServerCatalogoItem")
                .transacted()
                //.to("securityProcessor")
                .to("requestProcessor")
                .to("catalogoItemProcessor");

        from("cxfrs:bean:rsServerLocalidad")
                .transacted()
                //.to("securityProcessor")
                .to("requestProcessor")
                .to("localidadProcessor");

        from("cxfrs:bean:rsServerSolicitudEnvio")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("solicitudEnvioProcessor")
                .wireTap("seda:ec.net.redcode.tas.on.cancel")
                .wireTap("seda:ec.net.redcode.tas.on.fcm");

        from("cxfrs:bean:rsServerEnvio")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("envioProcessor");

        from("cxfrs:bean:rsServerPedido")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("pedidoProcessor");

        from("quartz2://tason/CreateSolicitudRandom?cron="+policy)
                .log("Inicio creacion solicitud random")
                .process(exchange -> {
                    exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, MethodConstant.SOLICITUD_ENVIO_RANDOM);
                    exchange.getIn().setHeader("user", "tason");
                })
                .to("solicitudEnvioProcessor")
                .process(exchange -> {
                    log.info("Envio solicitud random " + exchange.getIn().getBody());
                    if((Boolean) exchange.getIn().getBody())
                        exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, MethodConstant.SOLICITUD_ENVIO_CREATE);
                })
                .log("Sending to FCM Notificaciones")
                .wireTap("seda:ec.net.redcode.tas.on.fcm");

        from("cxfrs:bean:rsServerSecuencia")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("secuenciaProcessor");

        from("cxfrs:bean:rsServerOferta")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("ofertaProcessor")
                .wireTap("seda:ec.net.redcode.tas.on.fcm")
                .wireTap("activemq:queue:ec.net.redcode.tason.NotificationJMS");

        from("cxfrs:bean:rsServerClient")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("clientProcessor");

        from("cxfrs:bean:rsServerVehiculo")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("vehiculoProcessor");

        from("cxfrs:bean:rsServerConductor")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("conductorProcessor");

        from("cxfrs:bean:rsServerFactura")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("facturaProcessor")
                .wireTap("seda:com.tas.on.factura.manual")
                .wireTap("seda:ec.net.redcode.tas.on.retenciones");

        from("cxfrs:bean:rsServerEBilling")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("eBillingProcessor")
                .choice()
                    .when(generateEbilling)
                        .wireTap("activemq:queue:ec.net.redcode.tason.Ebilling")
                        .endChoice()
                    .when(sendEbilling)
                        .wireTap("activemq:queue:ec.net.redcode.tason.SignAndSendEbilling")
                        .endChoice()
                    .when(sendEbillingMail)
                        .wireTap("activemq:queue:ec.net.redcode.tason.NotificationJMS")
                        .process(exchange -> {
                            Map<String, Object> data = new HashMap<>();
                            data.put("response", Constant.RESPONSE_OK);
                            data.put("responseMessage", "Mensaje enviado");
                            exchange.getIn().setBody(data);
                        })
                        .endChoice()
                .end();

        from("cxfrs:bean:rsServerCalificacionTransportista")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("calificacionTransportistaProcessor");

        from("cxfrs:bean:rsServerRetencion")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("retencionProcessor")
                .to("activemq:queue:ec.net.redcode.tason.RetencionJMS");

        from("cxfrs:bean:rsServerPago")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("pagoProcessor");

        from("cxfrs:bean:rsServerFacturaProveedor")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("facturaProveedorProcessor")
                .wireTap("seda:ec.net.redcode.tas.on.retenciones");

        from("seda:com.tas.on.factura.manual")
            .choice()
                .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.FACTURA_MANUAL_CREATE))
                    .wireTap("activemq:queue:ec.net.redcode.tason.FacturacionManual");


        from("seda:ec.net.redcode.tas.on.retenciones")
                .choice()

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.FACTURA_UPDATE))
                        .process(exchange -> {
                            Map<String, Object> data = new HashMap<>();
                            data.put("prefactura", exchange.getIn().getHeader("prefactura"));
                            data.put("tipo", exchange.getIn().getHeader("tipo"));
                            //String prefactura = (String) exchange.getIn().getHeader("prefactura");
                            exchange.getIn().setBody(data);
                        })
                        .to("seda:ec.net.redcode.tas.on.prontopago")
                        .to("activemq:queue:ec.net.redcode.tason.RetencionJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.FACTURA_PROVEEDOR_CREATE))
                        .process(exchange -> {
                            int compraLocal = (int) exchange.getIn().getHeader("compraLocal");
                            if (compraLocal == 174)
                                exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                            Map<String, Object> data = new HashMap<>();
                            data.put("prefactura", exchange.getIn().getHeader("prefactura"));
                            data.put("tipo", exchange.getIn().getHeader("tipo"));
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.RetencionJMS");


        from("seda:ec.net.redcode.tas.on.cancel")
                .choice()
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.SOLICITUD_ENVIO_CANCEL))
                        .to("solicitudEnvioCancelProcessor")
                        .split(body())
                            .process(exchange -> {
                                OfertaNotificacion ofertaNotificacion = exchange.getIn().getBody(OfertaNotificacion.class);
                                exchange.getIn().setHeader("datos", ofertaNotificacion);
                                Map<String, String> data = new HashMap<>();
                                data.put("template", Constant.EMAIL_CANCEL);
                                data.put("transporte", ofertaNotificacion.getEmpresa());
                                data.put("origen", ofertaNotificacion.getOrigen());
                                data.put("destino", ofertaNotificacion.getDestino());
                                data.put("tipo", ofertaNotificacion.getTipo());
                                data.put("solicitud", ofertaNotificacion.getSolicitud());
                                data.put("comentario", ofertaNotificacion.getComentario());
                                data.put("email", ofertaNotificacion.getCorreo());
                                exchange.getIn().setBody(data);
                            })
                            .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");

        from("seda:ec.net.redcode.tas.on.prontopago")
                .choice()
                    .when(header("tipoPago").isEqualTo(161))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("template", Constant.EMAIL_PRONTO_PAGO);
                            data.put("nroFactura", String.valueOf(exchange.getIn().getHeader("prefactura")));
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.NotificationJMS").endChoice()
                    .otherwise()
                        .log("La Prefactura es pago normal")
                .end();


    }

}
