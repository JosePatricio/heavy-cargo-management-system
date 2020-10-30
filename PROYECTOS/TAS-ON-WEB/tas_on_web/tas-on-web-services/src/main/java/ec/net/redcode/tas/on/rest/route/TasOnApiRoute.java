package ec.net.redcode.tas.on.rest.route;

import org.apache.camel.builder.RouteBuilder;

public class TasOnApiRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxfrs:bean:rsServerAPI")
                .transacted()
                .to("securityProcessor")
                .to("requestProcessor")
                .to("serviceApiProcessor");
    }

}
