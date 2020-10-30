package ec.redcode.tas.on.sri;

import autorizacion.ws.sri.gob.ec.*;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class AutorizacionComprobantesOfflineTest {

    private String claveAcceso = "310120150717913148670012001001000000091123456780";

    @Test
    public void testAutorizacionComprobantesOffline(){
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AutorizacionComprobantesOffline.class);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline");
        AutorizacionComprobantesOffline service = (AutorizacionComprobantesOffline) factory.create();
        RespuestaComprobante response =  service.autorizacionComprobante(claveAcceso);
        if ("0".equals(response.getNumeroComprobantes())) {
            System.out.println("No tiene retenciones");
        }
        for (Autorizacion autorizacion: response.getAutorizaciones().getAutorizacion()){
            if ("RECHAZADA".equals(autorizacion.getEstado())){
                AtomicReference<String> error = new AtomicReference<>();
                autorizacion.getMensajes().getMensaje().forEach(m -> error.set(m.getIdentificador().concat(" ").concat(m.getMensaje())));
                System.out.println(error.get());
            }
        }
        Assert.assertNotNull(response);
    }

}
