package ec.net.redcode.tas.on.rest.processor.fcm;

import com.google.gson.Gson;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.FcmMessage;
import lombok.extern.log4j.Log4j;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;

@Log4j
public class FCMCommon {

    //TODO eliminar este metodo cuando se elimine el soporte a la app movil v1
    protected Response callService(FcmMessage message){
        callServiceV2(message);
        WebClient client = WebClient.create(Constant.FCM_ENDPOINT);
        client.header("Authorization", "key=" + Constant.FCM_KEY);
        client.type("application/json");
        client.accept("application/json");
        Gson gson = new Gson();
        String request = gson.toJson(message);
        log.info("Enviando notificacion app movil V1");
        return client.post(request);
    }

    private Response callServiceV2(FcmMessage message){
        WebClient client = WebClient.create(Constant.FCM_ENDPOINT);
        client.header("Authorization", "key=" + Constant.FCM_KEY_V2);
        client.type("application/json");
        client.accept("application/json");
        Gson gson = new Gson();
        String request = gson.toJson(message);
        log.info("Enviando notificacion app movil V2");
        return client.post(request);
    }

}
