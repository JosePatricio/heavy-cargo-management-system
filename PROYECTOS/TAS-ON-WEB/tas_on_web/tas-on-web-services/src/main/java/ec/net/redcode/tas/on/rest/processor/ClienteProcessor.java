package ec.net.redcode.tas.on.rest.processor;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.MethodConstant;
import ec.net.redcode.tas.on.common.dto.Company;
import ec.net.redcode.tas.on.common.dto.InfoBancariaDTO;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.rest.bean.ClienteBean;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClienteProcessor extends ClienteBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String username = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        switch (operationName){
            case MethodConstant.CLIENT_READ:
                String ruc = exchange.getIn().getBody(String.class);
                Cliente clienteRead = readCliente(ruc);
                exchange.getIn().setBody(response(clienteRead));
                break;
            case MethodConstant.CLIENT_CREATE:
                Cliente cliente = exchange.getIn().getBody(Cliente.class);
                createCliente(cliente);
                Map<String, String> responseCreate = new ConcurrentHashMap<>();
                responseCreate.put("response", Constant.RESPONSE_OK);
                responseCreate.put("responseMessage", "Empresa creada correctamente");
                exchange.getIn().setBody(response(responseCreate));
                break;
            case MethodConstant.CLIENT_UPDATE:
                Cliente clienteUpdate = exchange.getIn().getBody(Cliente.class);
                updateCliente(clienteUpdate);
                Map<String, String> responseUpdate = new ConcurrentHashMap<>();
                responseUpdate.put("response", Constant.RESPONSE_OK);
                responseUpdate.put("responseMessage", "Empresa actualizada correctamente");
                exchange.getIn().setBody(response(responseUpdate));
                break;
            case MethodConstant.CLIENT_DELETE:
                Cliente clienteDelete = exchange.getIn().getBody(Cliente.class);
                deleteCliente(clienteDelete);
                Map<String, String> responseDelete = new ConcurrentHashMap<>();
                responseDelete.put("response", Constant.RESPONSE_OK);
                responseDelete.put("responseMessage", "Empresa eliminado correctamente");
                exchange.getIn().setBody(response(responseDelete));
                break;
            case MethodConstant.CLIENT_READ_BY_TOKEN:
                Cliente clienteToken = readClientByUsername(username);
                exchange.getIn().setBody(response(clienteToken));
                break;
            case MethodConstant.CLIENT_GET_ALL:
                List<Company> clientes = getAllCliente();
                exchange.getIn().setBody(response(clientes));
                break;
            case MethodConstant.CLIENT_GET_CLIENTE_BY_TIPO:
                int tipoCliente = exchange.getIn().getBody(Integer.class);
                List<Company> clientesByTipo = getAllClienteByTipo(tipoCliente);
                exchange.getIn().setBody(response(clientesByTipo));
                break;
            case MethodConstant.CLIENT_UPDATE_INFO_BANCARIA:
                Map<String, String> response = new ConcurrentHashMap<>();
                try{
                    InfoBancariaDTO informacionBancaria = exchange.getIn().getBody(InfoBancariaDTO.class);
                    actualizarInformacionBancaria(informacionBancaria, usuario);
                    response.put("response", Constant.RESPONSE_OK);
                    response.put("responseMessage", "Información actualizada correctamente");
                }catch (TasOnException e){
                    response.put("response", Constant.RESPONSE_ERROR);
                    response.put("responseMessage", e.getDeveloperMessage());
                    exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
                }
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.CLIENT_GET_INFO_BANCARIA:
                exchange.getIn().setBody(response(getInfoBancaria(usuario)));
                break;
        }
    }

}
