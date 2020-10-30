package ec.net.redcode.tas.on.rest.route;

import ec.net.redcode.tas.on.common.MethodConstant;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.Map;

public class TasOnFCMRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:ec.net.redcode.tas.on.fcm")
                .delay(1000)
                .choice()
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.SOLICITUD_ENVIO_CREATE))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "solicitudNueva");
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.SOLICITUD_ACCEPT_OFERTA))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "solicitudAceptada");
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.SOLICITUD_ENVIO_UPDATE))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "solicitudMofificada");
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.SOLICITUD_ENVIO_CANCEL))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "solicitudCancelada");
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.OFERTA_OFFER))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "solicitudOferta");
                            exchange.getIn().setBody(data);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.OFERTA_OFFER_UDPATE))
                        .process(exchange -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("solicitud", String.valueOf(exchange.getIn().getHeader("solicitud")));
                            data.put("tipoNotificacion", "ofertaActualizada");
                            exchange.getIn().setBody(data);
                        })
                    .to("activemq:queue:ec.net.redcode.tason.FcmJMS")
                .end();

        from("activemq:queue:ec.net.redcode.tason.FcmJMS")
                .log("Data to send notification push ${body}")
                .errorHandler(deadLetterChannel("activemq:queue:ActiveMQ.DLQ")
                        .maximumRedeliveries(3).retryAttemptedLogLevel(LoggingLevel.ERROR).logStackTrace(true)
                        .redeliveryDelay(250).backOffMultiplier(2))
                .process(exchange -> {
                    Map<String, String> data = (Map<String, String>) exchange.getIn().getBody();
                    exchange.getIn().setBody(data.get("solicitud"));
                    exchange.getIn().setHeader("tipoNotificacion", data.get("tipoNotificacion"));
                })
                .choice()
                    .when(header("tipoNotificacion").isEqualTo("solicitudNueva"))
                        .to("fcmSolicitudNuevaProcessor")
                    .when(header("tipoNotificacion").isEqualTo("solicitudAceptada"))
                        .to("fcmSolicitudAceptadaProcessor")
                    .when(header("tipoNotificacion").isEqualTo("solicitudMofificada"))
                        .to("fcmSolicitudModificadaProcessor")
                    .when(header("tipoNotificacion").isEqualTo("solicitudCancelada"))
                        .to("fcmSolicitudCanceladaProcessor")
                    .when(header("tipoNotificacion").isEqualTo("solicitudOferta"))
                        .to("fcmSolicitudOfertaProcessor")
                    .when(header("tipoNotificacion").isEqualTo("ofertaActualizada"))
                        .to("fcmOfertaCambioProcessor")
                        .choice()
                            .when(header("solicitudPagadaId").isNotNull())
                                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS")
                    .when(header("tipoNotificacion").isEqualTo("solicitudCanceladaJob"))
                        .to("fcmSolicitudCanceladaJobProcessor")
                .end();


    }

}
