package ec.net.redcode.tas.on.notifications;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.velocity.VelocityConstants;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.camel.builder.Builder.body;

public class MailTest extends CamelTestSupport {

    private String smtpHost = "smtp.office365.com";
    private String username = "info@tas-on.com";
    private String password = "TasOn01*";
    private String port = "587";
    private String templateVelocity = "file://Users/mauchilan/Downloads/apache-karaf-4.0.6/etc/creacion-usuario.vm";

    @Test
    @Ignore
    public void testMail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);
        mock.expectsNoDuplicates(body());

        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put("to", "Mauricio Chilan <mauchilan@gmail.com>");
        headers.put("from", "TASON <info@tas-on.com>");
        headers.put("subject", "Creacion nuevo usuario.");
        headers.put("reply-to", username);

        Map<String, String> pruebas = new HashMap<>();
        pruebas.put("nombre", "MAURICIO NICOLAS CHILAN MACIAS");
        pruebas.put("usuario", "mauchilan");
        pruebas.put("password", "CvC1234");


        template.sendBodyAndHeaders("direct:start",pruebas,headers);
        // just in case we run on slow boxes
        assertMockEndpointsSatisfied(3, TimeUnit.SECONDS);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(exchange -> {
                    log.info("Message headers " + exchange.getIn().getHeaders());
                    log.info("-------->>>>>>>> Template " + templateVelocity);
                })
                        .setHeader(VelocityConstants.VELOCITY_TEMPLATE).constant(templateVelocity)
                        .to("velocity:template-in-header")
                        .to("smtp://" + smtpHost + ":" + port + "?username=" + username + "&password=" + password + "&debugMode=true&mail.smtp.starttls.enable=true")
                        .to("mock:result").process(exchange -> {
                    log.debug("Message headers " + exchange.getIn().getHeaders());
                });


            }
        };
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry reg = super.createRegistry();
        addSslContextParametersToRegistry(reg);
        return reg;
    }

    protected void addSslContextParametersToRegistry(JndiRegistry registry) {
        registry.bind("sslContextParameters", MailTestHelper.createSslContextParameters());
    }

    public String getTemplateVelocity() {
        return templateVelocity;
    }

    public void setTemplateVelocity(String templateVelocity) {
        this.templateVelocity = templateVelocity;
    }

}
