package ec.net.redcode.tas.on.security;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.exception.TasOnErrorMessage;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.oauth2.services.AbstractAccessTokenValidator;
import org.apache.cxf.rs.security.oauth2.utils.AuthorizationUtils;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class TasOnPublicSecurityFilter extends AbstractAccessTokenValidator implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TasOnSecurityFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        supportedSchemes.add(OAuthConstants.BASIC_SCHEME);
        validateRequest();
    }

    protected void validateRequest() {
        try {
            String[] authParts = getAuthorizationParts();
            if (authParts.length < 2) {
                throw ExceptionUtils.toForbiddenException(null, null);
            }
            String authSchemeDataEncode = authParts[1];
            String authSchemeData = new String(Base64Utility.decode(authSchemeDataEncode));
            String[] data = authSchemeData.split(":");
            if (!Constant.PUBLIC_USER.equals(data[0]) && !Constant.PUBLIC_PASSWORD.equals(data[1])){
                logger.error("User is not allowed to use this resource");
                throw ExceptionUtils.toForbiddenException(null, null);
            }

        } catch (Exception e){
            logger.error("Error validating public authentication", e);
            TasOnErrorMessage message = new TasOnErrorMessage(Response.Status.FORBIDDEN.getStatusCode(), "Error validating public authentication", e.getLocalizedMessage());
            Response r = Response.status(Response.Status.FORBIDDEN.getStatusCode()).entity(message).type(MediaType.APPLICATION_JSON).build();
            throw ExceptionUtils.toForbiddenException(e, r);
        }
    }

    protected String[] getAuthorizationParts() {
        return AuthorizationUtils.getAuthorizationParts(getMessageContext(), supportedSchemes);
    }

}
