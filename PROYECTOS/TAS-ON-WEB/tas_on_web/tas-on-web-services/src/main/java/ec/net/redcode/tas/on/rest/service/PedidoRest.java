package ec.net.redcode.tas.on.rest.service;

import ec.net.redcode.tas.on.common.dto.*;
import ec.net.redcode.tas.on.persistence.entities.Visita;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("/pedido/")
@Consumes({"application/json"})
@Produces({"application/json;charset=UTF-8"})
public class PedidoRest {

    @GET
    @Path("/vendedores")
    public List<VendedorClienteDTO> consultarVendedoresCliente(){
        return null;
    }

    @GET
    @Path("/clientes-by")
    public List<ClientePedidosDTO> consultarClientesPedidosBy(@QueryParam("razonSocial") String razonSocial,
                                                              @QueryParam("ruc") String ruc,
                                                              @QueryParam("vendedor") String vendedor,
                                                              @QueryParam("diaVisita") Integer diaVisita){
        return null;
    }

    @POST
    @Path("/cliente/guardar")
    public Map<String, String> guardarNuevoCliente(ClientePedidosDTO clientePedidosDTO){
        return null;
    }

    @POST
    @Path("/cliente/actualizar")
    public Map<String, String> actualizarClientePedidos(ClientePedidosDTO clientePedidosDTO){
        return null;
    }

    @GET
    @Path("/categorias")
    public List<CategoriaDTO> consultarCategorias(){
        return null;
    }

    @GET
    @Path("/productos")
    public List<ProductoDTO> consultarProductos(){
        return null;
    }

    @POST
    @Path("/producto/guardar")
    public Map<String, String> guardarProducto(ProductoDTO productoDTO){
        return null;
    }

    @POST
    @Path("/producto/actualizar")
    public Map<String, String> actualizarProducto(ProductoDTO productoDTO){
        return null;
    }

    @POST
    @Path("/visita/nueva")
    public Map<String, String> registrarNuevaVisita(Visita visita){
        return null;
    }

    @POST
    @Path("/visita/agendarme")
    public Map<String, String> agendarmeVisitas(AgendarVisitaDTO agendarVisitaDTO){
        return null;
    }

    @GET
    @Path("/visita/consultar-by")
    public List<VisitaDTO> consultarMisVisitasBy(@QueryParam("fecha") String fecha){
        return null;
    }

    @GET
    @Path("/visita/all/consultar-by")
    public List<VisitaDTO> consultarVisitasBy(@QueryParam("fecha") String fecha){
        return null;
    }

    @POST
    @Path("/guardar")
    public Map<String, String> guardarPedido(PedidoDTO pedido){
        return null;
    }

    @GET
    @Path("/by-visita/{visitaId}")
    public PedidoDTO getPedidoByVisita(@PathParam("visitaId") Integer visitaId){
        return null;
    }

    @GET
    @Path("/all/consultar-by")
    public List<PedidoDTO> consultarAllPedidosBy(@QueryParam("fechaPedido") String fechaPedido,
                                                 @QueryParam("fechaEntrega") String fechaEntrega){
        return null;
    }

    @GET
    @Path("/credito/documentos/{pedidoId}")
    public List<String> getDocumentosCredito(@PathParam("pedidoId") int pedidoId){
        return null;
    }

}
