package ec.net.redcode.tas.on.rest.route;

import org.apache.camel.builder.RouteBuilder;

public class TasOnPublicRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxfrs:bean:rsServerPublic")
                //.transacted()
                .to("requestProcessor")
                .to("publicServiceProcessor")
                .wireTap("seda:ec.net.redcode.tas.on.notificacion");

        from("cxfrs:bean:rsServerFcm")
                .to("requestProcessor")
                .to("fcmDispositivoProcessor");
    }

}
