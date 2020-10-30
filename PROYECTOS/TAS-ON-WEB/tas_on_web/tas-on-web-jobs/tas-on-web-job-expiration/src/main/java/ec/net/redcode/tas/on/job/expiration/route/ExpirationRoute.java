package ec.net.redcode.tas.on.job.expiration.route;

import org.apache.camel.builder.RouteBuilder;

public class ExpirationRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("quartz2://tason/NotificacionEmailTimer?cron=0+0/30+*+?+*+*+*")
                .log("Inicio de cancelacion de solicitudes")
                .to("expirationProcessor")
                .log("Fin cancelacion solicitudes");

    }

}
