package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.persistence.entities.FcmDispositivo;
import ec.net.redcode.tas.on.rest.bean.FcmDispositivoBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class FcmDispositivoProcessor extends FcmDispositivoBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        FcmDispositivo fcmDispositivo = exchange.getIn().getBody(FcmDispositivo.class);
        create(fcmDispositivo);
        Map<String, String> response = new HashMap<>();
        response.put("response", "OK");
        response.put("responseMessage", "Token insertado correctamente");
        exchange.getIn().setBody(response(response));
    }

}
