package ec.net.redcode.tas.on.rest.route;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.HashMap;
import java.util.Map;

public class TasOnEmailRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:ec.net.redcode.tas.on.notificacion")
                .process(exchange -> {
                            Map<String, String> notifications = new HashMap<>();
                            notifications.put("idDocumento", (String) exchange.getIn().getHeader("idDocumento"));
                            exchange.getIn().setBody(notifications);
                        }
                )
                .choice()
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.PUBLIC_CREATE_EMPRESA_CLIENTE))
                        .process(exchange -> {
                            Map<String, String> notifications = (Map<String, String>) exchange.getIn().getBody();
                            notifications.put("template", Constant.EMAIL_NUEVO);
                            exchange.getIn().setBody(notifications);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.NotificationJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.USUARIO_RESTABLECER))
                        .process(exchange -> {
                            Map<String, String> notifications = (Map<String, String>) exchange.getIn().getBody();
                            notifications.put("template", Constant.EMAIL_RESTABLECER);
                            exchange.getIn().setBody(notifications);
                        })
                        .to("activemq:queue:ec.net.redcode.tason.NotificationJMS")
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo(MethodConstant.USUARIO_CREATE))
                        .process(exchange -> {
                            Map<String, String> notifications = (Map<String, String>) exchange.getIn().getBody();
                            notifications.put("template", Constant.EMAIL_NUEVO);
                            exchange.getIn().setBody(notifications);
                        })
                        .choice()
                            .when(header("email").isEqualTo(Boolean.TRUE))
                                .to("activemq:queue:ec.net.redcode.tason.NotificationJMS");



    }

}
