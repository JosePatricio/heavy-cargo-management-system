package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Request;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.sql.Timestamp;
import java.util.Date;

public abstract class RequestBean extends TasOnResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestBean.class);

    @Setter protected RequestService requestService;

    protected void saveRequest(Exchange exchange){
        try{
            Request request = new Request();
            request.setRequestRemoteIp(getRemoteIp(exchange));
            request.setRequestMethod(getRequestMethod(exchange));
            request.setRequestOperationName(getRequestOperationName(exchange));
            request.setRequestPath(getRequestPath(exchange));
            request.setRequestUri(getRequestUri(exchange));
            request.setRequestUser(getRequestUser(exchange));
            request.setRequestUserAgent(getRequestUserAgent(exchange));
            request.setRequestDate(new Timestamp(new Date().getTime()));
            requestService.create(request);
        }catch (Exception e){
            logger.error("Error al registrar request " + e.getMessage());
        }
    }

    private String getRemoteIp(Exchange exchange){
        try{
            String ipAddress = exchange.getIn().getHeader("X-Real-IP", String.class);
            if(ipAddress != null) return ipAddress;
            else throw new Exception();
        }catch (Exception e){
            org.apache.cxf.message.Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, org.apache.cxf.message.Message.class);
            ServletRequest request = (ServletRequest) cxfMessage.get("HTTP.REQUEST");
            return request.getRemoteAddr();
        }
    }

    private String getRequestMethod(Exchange exchange){
        return getHeader(exchange, "CamelHttpMethod");
    }

    private String getRequestOperationName(Exchange exchange){
        String response =  exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        return TasOnUtil.castString(response, 99);
    }

    private String getRequestPath(Exchange exchange){
        String response = getHeader(exchange, "CamelHttpPath");
        return TasOnUtil.castString(response, 99);
    }

    private String getRequestUri(Exchange exchange) {
        String response = getHeader(exchange, "CamelHttpUri");
        return TasOnUtil.castString(response, 299);
    }

    private String getRequestUser(Exchange exchange){
        return getHeader(exchange, "user");
    }

    private String getRequestUserAgent(Exchange exchange){
        String response = getHeader(exchange, "User-Agent");
        return TasOnUtil.castString(response, 299);
    }

    private String getHeader(Exchange exchange, String headerName){
        Object methodObject = exchange.getIn().getHeaders().get(headerName);
        return TasOnUtil.getStringFromObject(methodObject);
    }


}