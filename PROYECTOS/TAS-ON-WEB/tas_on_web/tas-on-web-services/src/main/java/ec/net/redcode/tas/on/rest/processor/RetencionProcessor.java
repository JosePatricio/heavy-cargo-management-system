package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.AgendarVisitaDTO;
import ec.net.redcode.tas.on.common.dto.ClientePedidosDTO;
import ec.net.redcode.tas.on.common.dto.PedidoDTO;
import ec.net.redcode.tas.on.common.dto.ProductoDTO;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.entities.Visita;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import ec.net.redcode.tas.on.rest.bean.PedidoBean;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public class RetencionProcessor implements Processor {

    @Setter protected UsuarioService usuarioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String idUsuario = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(idUsuario);
        switch (operationName) {
            case MethodConstant.RETENCION_ENVIAR_SRI:
                if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN)){
                    Map<String, Object> data = new HashMap<>();
                    Map<String, String> input = exchange.getIn().getBody(Map.class);
                    data.put("prefactura", input.get("prefactura"));
                    data.put("tipo", input.get("tipo"));
                    exchange.getIn().setBody(data);
                } else{
                    log.error("Usuario no autorizado");
                }
            break;
        }
    }
}
