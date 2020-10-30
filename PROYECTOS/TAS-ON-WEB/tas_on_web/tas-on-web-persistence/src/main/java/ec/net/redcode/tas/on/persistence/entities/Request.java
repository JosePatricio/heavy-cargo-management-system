package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "request")
public class Request {
    private int requestId;
    private String requestRemoteIp;
    private String requestMethod;
    private String requestOperationName;
    private String requestPath;
    private String requestUri;
    private String requestUser;
    private String requestUserAgent;
    private Timestamp requestDate;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "request_id")
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Column(name = "request_remote_ip")
    public String getRequestRemoteIp() {
        return requestRemoteIp;
    }

    public void setRequestRemoteIp(String requestRemoteIp) {
        this.requestRemoteIp = requestRemoteIp;
    }

    @Column(name = "request_method")
    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Column(name = "request_operation_name")
    public String getRequestOperationName() {
        return requestOperationName;
    }

    public void setRequestOperationName(String requestOperationName) {
        this.requestOperationName = requestOperationName;
    }

    @Column(name = "request_path")
    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    @Column(name = "request_uri")
    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    @Column(name = "request_user")
    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    @Column(name = "request_user_agent")
    public String getRequestUserAgent() {
        return requestUserAgent;
    }

    public void setRequestUserAgent(String requestUserAgent) {
        this.requestUserAgent = requestUserAgent;
    }

    @Column(name = "request_date")
    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }


}
