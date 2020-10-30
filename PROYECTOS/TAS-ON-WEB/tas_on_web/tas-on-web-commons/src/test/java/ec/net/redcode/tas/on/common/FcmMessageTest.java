package ec.net.redcode.tas.on.common;

import com.google.gson.Gson;
import ec.net.redcode.tas.on.common.dto.FcmMessage;
import ec.net.redcode.tas.on.common.dto.Notification;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FcmMessageTest {

    @Test
    public void testFcmMessage(){
        FcmMessage message = new FcmMessage();
        List<String> tokens = new ArrayList<>();
        tokens.add("1");
        tokens.add("2");
        tokens.add("3");
        tokens.add("4");
        tokens.add("5");
        tokens.add("6");
        tokens.add("6");
        message.setRegistration_ids(tokens);
        Notification notification = new Notification();
        notification.setTitle("Solicitudes");
        notification.setBody("Nueva Solicitud");
        message.setNotification(notification);
        Gson gson = new Gson();
        System.out.println(gson.toJson(message));
    }

}
