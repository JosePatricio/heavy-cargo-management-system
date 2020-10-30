package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.rest.bean.RequestBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RequestProcessor extends RequestBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        saveRequest(exchange);
    }

}
