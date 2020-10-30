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
import ec.net.redcode.tas.on.rest.bean.PedidoBean;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public class PedidoProcessor extends PedidoBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        String idUsuario = exchange.getIn().getHeader("user").toString();
        Usuario usuario = usuarioService.getUsuariosByUserName(idUsuario);
        MessageContentsList messageList = exchange.getIn().getBody(MessageContentsList.class);
        Map<String, String> response = new ConcurrentHashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        switch (operationName) {
            case MethodConstant.PEDIDO_CONSULTAR_CLIENTES_BY:
                String razonSocial = TasOnUtil.getStringFromObject(messageList.get(0));;
                String ruc = TasOnUtil.getStringFromObject(messageList.get(1));;
                String vendedorId = TasOnUtil.getStringFromObject(messageList.get(2));;
                Integer diaVisita = TasOnUtil.getIntegerFromObject(messageList.get(3));
                exchange.getIn().setBody(response(consultarClientesBy(razonSocial, ruc, vendedorId, diaVisita, usuario)));
                break;
            case MethodConstant.PEDIDO_CONSULTAR_VENDEDORES:
                exchange.getIn().setBody(response(consultarVendedores(usuario)));
                break;
            case MethodConstant.PEDIDO_REGISTRAR_VISITA:
                Visita visita = exchange.getIn().getBody(Visita.class);
                guardarVisita(visita, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Visita registrada correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.PEDIDO_AGENDARME_VISITAS:
                AgendarVisitaDTO agendarVisitaDTO = exchange.getIn().getBody(AgendarVisitaDTO.class);
                agendarmeVisitas(agendarVisitaDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Visitas registradas correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.PEDIDO_CONSULTAR_MIS_VISITAS_BY:
                String fechaVisitaStr = TasOnUtil.getStringFromObject(messageList.get(0));
                Date fechaVisita = fechaVisitaStr != null ? formatter.parse(fechaVisitaStr): null;
                exchange.getIn().setBody(response(getVisitasBy(usuario, fechaVisita)));
                break;
            case MethodConstant.PEDIDO_CONSULTAR_VISITAS_BY:
                fechaVisitaStr = TasOnUtil.getStringFromObject(messageList.get(0));
                fechaVisita = fechaVisitaStr != null ? formatter.parse(fechaVisitaStr): null;
                exchange.getIn().setBody(response(getVisitasAdminBy(usuario, fechaVisita)));
                break;
            case MethodConstant.PEDIDO_NUEVO_CLIENTE:
                ClientePedidosDTO clientePedidosDTO = exchange.getIn().getBody(ClientePedidosDTO.class);
                guardarNuevoCliente(clientePedidosDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Cliente creado correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.PEDIDO_ACTUALIZAR_CLIENTE:
                clientePedidosDTO = exchange.getIn().getBody(ClientePedidosDTO.class);
                actualizarCliente(clientePedidosDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Cliente actualizado correctamente");
                exchange.getIn().setBody(response(response));
                break;
            case MethodConstant.PEDIDO_GUARDAR:
                PedidoDTO pedidoDTO = exchange.getIn().getBody(PedidoDTO.class);
                guardarPedido(pedidoDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Pedido registrado correctamente");
                exchange.getIn().setBody(response(response));
            break;
            case MethodConstant.PEDIDO_CONSULTAR_POR_VISITA:
                Integer visitaId = exchange.getIn().getBody(Integer.class);
                exchange.getIn().setBody(response(getPedidoByVisitaId(visitaId, usuario)));
            break;
            case MethodConstant.PEDIDO_CONSULTAR_ALL_BY:
                String fechaPedidoStr = TasOnUtil.getStringFromObject(messageList.get(0));
                String fechaEntregaStr = TasOnUtil.getStringFromObject(messageList.get(1));
                Date fechaPedido = fechaPedidoStr != null ? formatter.parse(fechaPedidoStr): null;
                Date fechaEntrega = fechaEntregaStr != null ? formatter.parse(fechaEntregaStr): null;
                exchange.getIn().setBody(response(getAllPedidosBy(fechaPedido, fechaEntrega, usuario)));
            break;
            case MethodConstant.PEDIDO_CONSULTAR_CATEGORIAS:
                exchange.getIn().setBody(response(getCategorias(usuario)));
                break;
            case MethodConstant.PEDIDO_CONSULTAR_PRODUCTOS:
                exchange.getIn().setBody(response(getProductos(usuario)));
                break;
            case MethodConstant.PEDIDO_PRODUCTO_NUEVO:
                ProductoDTO productoDTO = exchange.getIn().getBody(ProductoDTO.class);
                guardarProducto(productoDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Producto registrado correctamente");
                exchange.getIn().setBody(response(response));
            break;
            case MethodConstant.PEDIDO_PRODUCTO_ACTUALIZAR:
                productoDTO = exchange.getIn().getBody(ProductoDTO.class);
                actualizarProducto(productoDTO, usuario);
                response.put("response", Constant.RESPONSE_OK);
                response.put("responseMessage", "Producto actualizado correctamente");
                exchange.getIn().setBody(response(response));
            break;
            case MethodConstant.PEDIDO_CREDITO_GET_DOCUMENTOS:
                Integer pedidoId = exchange.getIn().getBody(Integer.class);
                exchange.getIn().setBody(getDocumentosCredito(pedidoId, usuario));
                break;
        }
    }

}
