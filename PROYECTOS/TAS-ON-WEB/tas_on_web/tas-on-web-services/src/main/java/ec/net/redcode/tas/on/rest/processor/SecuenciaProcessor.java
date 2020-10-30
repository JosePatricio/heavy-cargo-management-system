package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.rest.bean.SecuenciaBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecuenciaProcessor extends SecuenciaBean implements Processor {

    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        switch (operationName){
            case MethodConstant.SECUENCIA_SECUENCIA:
                //String idCliente = exchange.getIn().getBody(String.class);
                //String secuencia = getSecuenciaByCliente(idCliente);
                String secuencia = getSecuenciaTASON();
                Map<String, String> response = new HashMap<>();
                response.put("codigo", secuencia);
                response.put("fechaCaducidad", TasOnUtil.getDatePlusDays(new Date(), 2));
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.SECUENCIA_DIAS_HABILES:
                String date = exchange.getIn().getBody(String.class);
                int dias = TasOnUtil.getWorkDay2(new Date(), dt.parse(date));
                Map<String, String> responseDias = new ConcurrentHashMap<>();
                responseDias.put("dias", String.valueOf(dias));
                exchange.getIn().setBody(response(responseDias));
                break;
        }
    }

}
