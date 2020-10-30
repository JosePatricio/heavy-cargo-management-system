package ec.net.redcode.tas.on.security;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

public class TasOnSecurityProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OAuthContext oauthContext = getOAthContext(exchange.getIn());
        if (oauthContext != null) {
            String idCliente = oauthContext.getClientId();
            Object object = exchange.getIn().getBody();
            exchange.getIn().setBody(object);
            exchange.getIn().setHeader("user", idCliente);
        }
    }

    private OAuthContext getOAthContext(Message message){
        org.apache.cxf.message.Message cxfMessage = (org.apache.cxf.message.Message)message.getHeader(CxfConstants.CAMEL_CXF_MESSAGE);
        if (cxfMessage == null) return null;
        return cxfMessage.getContent(OAuthContext.class);
    }

}
