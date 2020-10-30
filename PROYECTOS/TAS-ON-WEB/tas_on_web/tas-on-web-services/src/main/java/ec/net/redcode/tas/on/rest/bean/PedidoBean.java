package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.*;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public class PedidoBean extends TasOnResponse {

    @Setter protected UsuarioService usuarioService;
    @Setter protected ClientePedidosService clientePedidosService;
    @Setter protected VisitaService visitaService;
    @Setter protected ProductoService productoService;
    @Setter protected PedidoService pedidoService;
    @Setter protected PedidoDetalleService pedidoDetalleService;
    @Setter protected CategoriaProductoService categoriaProductoService;
    @Setter protected PedidoDocumentosCreditoService pedidoDocumentosCreditoService;
    @Setter protected FileService fileService;

    protected List<ClientePedidosDTO> consultarClientesBy(String razonSocial, String ruc, String vendedorId, Integer diaVisita, Usuario usuarioConsulta){
        List<ClientePedidos> clientesPedidos = clientePedidosService.getClientesBy(razonSocial, ruc, vendedorId, diaVisita, usuarioConsulta.getUsuarioRuc());
        return clientesPedidos.stream().map(clientePedidosToDto).collect(Collectors.toList());
    }

    private Function<ClientePedidos, ClientePedidosDTO> clientePedidosToDto = clientePedidos -> {
        ClientePedidosDTO clientePedidosDTO = new ClientePedidosDTO();
        clientePedidosDTO.setClienteId(clientePedidos.getClientePedidosId());
        clientePedidosDTO.setClienteRazonSocial(clientePedidos.getClientePedidosRazonSocial());
        clientePedidosDTO.setClienteDireccion(clientePedidos.getClientePedidosDireccion());
        clientePedidosDTO.setClienteNombre(clientePedidos.getClientePedidosNombre());
        clientePedidosDTO.setClienteTelefono(clientePedidos.getClientePedidosTelefono());
        clientePedidosDTO.setClienteLat(clientePedidos.getClientePedidosLat());
        clientePedidosDTO.setClienteLng(clientePedidos.getClientePedidosLng());
        clientePedidosDTO.setClienteCorreo(clientePedidos.getClientePedidosCorreo());
        clientePedidosDTO.setClienteRuc(clientePedidos.getClientePedidosRuc());
        clientePedidosDTO.setDiaSemanaVisita(clientePedidos.getClientePedidosDiaSemanaVisita());
        VendedorClienteDTO vendedorClienteDTO = new VendedorClienteDTO();
        Usuario vendedorAsignado = clientePedidos.getUsuarioByClientePedidosVendedorAsignado();
        if(vendedorAsignado != null){
            vendedorClienteDTO.setVendedorNombres(vendedorAsignado.getUsuarioNombres() + " " + vendedorAsignado.getUsuarioApellidos());
            vendedorClienteDTO.setVendedorId(vendedorAsignado.getUsuarioIdDocumento());
        }
        clientePedidosDTO.setClienteVendedorAsignado(vendedorClienteDTO);
        return clientePedidosDTO;
    };

    protected List<VendedorClienteDTO> consultarVendedores(Usuario usuarioConsulta){
        List<Usuario> vendedores = usuarioService.getUsuariosByEmpresaTipoUsuario(usuarioConsulta.getUsuarioRuc(), Constant.USER_VENDEDOR_CLIENTE, Constant.USER_ACTIVE);
        return vendedores.stream().map(usuarioToVendedorDto).collect(Collectors.toList());
    }

    private Function<Usuario, VendedorClienteDTO> usuarioToVendedorDto = usuario -> {
        VendedorClienteDTO vendedorClienteDTO = new VendedorClienteDTO();
        vendedorClienteDTO.setVendedorId(usuario.getUsuarioIdDocumento());
        vendedorClienteDTO.setVendedorNombres(usuario.getUsuarioNombres()+" "+usuario.getUsuarioApellidos());
        return vendedorClienteDTO;
    };

    protected void guardarVisita(Visita visita, Usuario usuarioGuarda){
        visita.setVisitaEstado(Constant.ESTADO_VISITA_PENDIENTE);
        visita.setVisitaFechaCreacion(new Timestamp(new Date().getTime()));
        visita.setVisitaUsuarioCrea(usuarioGuarda.getUsuarioIdDocumento());
        visitaService.create(visita);
    }

    protected void agendarmeVisitas(AgendarVisitaDTO agendarVisitaDTO, Usuario usuario){
        agendarVisitaDTO.getClientesId().forEach( clienteId -> {
            Visita visita = new Visita();
            visita.setVisitaEstado(Constant.ESTADO_VISITA_PENDIENTE);
            visita.setVisitaFechaCreacion(new Timestamp(new Date().getTime()));
            visita.setVisitaUsuarioCrea(usuario.getUsuarioIdDocumento());
            visita.setVisitaVendedorUsuario(usuario.getUsuarioIdDocumento());
            visita.setVisitaClientePedidos(clienteId);
            visita.setVisitaFecha(agendarVisitaDTO.getFechaVisita());
            visitaService.create(visita);
        });
    }

    protected List<VisitaDTO> getVisitasBy(Usuario usuario, Date fechaVisita){
        List<Visita> visitas = visitaService.getVisitasBy(usuario.getUsuarioIdDocumento(), fechaVisita);
        return visitas.stream().map(visitaToDto).sorted(Comparator.comparingInt(VisitaDTO::getVisitaEstadoId)).collect(Collectors.toList());
    }

    protected List<VisitaDTO> getVisitasAdminBy(Usuario usuario, Date fechaVisita){
        List<Visita> visitas = visitaService.getVisitasEmpresaBy(usuario.getUsuarioRuc(), fechaVisita);
        return visitas.stream().map(visitaToDto).sorted(Comparator.comparingInt(VisitaDTO::getVisitaEstadoId)).collect(Collectors.toList());
    }

    private Function<Visita, VisitaDTO> visitaToDto = visita -> {
        VisitaDTO visitaDTO = new VisitaDTO();
        visitaDTO.setVisitaId(visita.getVisitaId());
        visitaDTO.setVisitaFecha(visita.getVisitaFecha());
        visitaDTO.setVisitaEstado(visita.getCatalogoItemByVisitaEstado().getCatalogoItemDescripcion());
        visitaDTO.setCliente(clientePedidosToDto.apply(visita.getClientePedidosByVisitaClientePedidos()));
        visitaDTO.setVisitaEstadoId(visita.getVisitaEstado());
        visitaDTO.setVendedor(usuarioToVendedorDto.apply(visita.getUsuarioByVisitaVendedorUsuario()));
        return visitaDTO;
    };

    protected void guardarNuevoCliente(ClientePedidosDTO clientePedidosDTO, Usuario usuarioCrea){
        ClientePedidos clientePedidos = new ClientePedidos();
        clientePedidos.setClientePedidosRuc(clientePedidosDTO.getClienteRuc());
        clientePedidos.setClientePedidosCorreo(clientePedidosDTO.getClienteCorreo());
        clientePedidos.setClientePedidosDireccion(clientePedidosDTO.getClienteDireccion());
        clientePedidos.setClientePedidosLat(clientePedidosDTO.getClienteLat());
        clientePedidos.setClientePedidosLng(clientePedidosDTO.getClienteLng());
        clientePedidos.setClientePedidosNombre(clientePedidosDTO.getClienteNombre());
        clientePedidos.setClientePedidosRazonSocial(clientePedidosDTO.getClienteRazonSocial());
        clientePedidos.setClientePedidosTelefono(clientePedidosDTO.getClienteTelefono());
        clientePedidos.setClientePedidosDiaSemanaVisita(clientePedidosDTO.getDiaSemanaVisita());
        clientePedidos.setClientePedidosVendedorAsignado(clientePedidosDTO.getClienteVendedorAsignado() != null ? clientePedidosDTO.getClienteVendedorAsignado().getVendedorId() : null);
        clientePedidos.setClientePedidosFechaCreacion(new Timestamp(new Date().getTime()));
        clientePedidos.setClientePedidosUsuarioCrea(usuarioCrea.getUsuarioIdDocumento());
        clientePedidos.setClientePedidosEstado(1);
        clientePedidosService.create(clientePedidos);
    }

    protected void actualizarCliente(ClientePedidosDTO clientePedidosDTO, Usuario usuario){
        ClientePedidos clientePedidos = clientePedidosService.read(clientePedidosDTO.getClienteId()); //TODO leer por id y empresa
        clientePedidos.setClientePedidosRuc(clientePedidosDTO.getClienteRuc());
        clientePedidos.setClientePedidosCorreo(clientePedidosDTO.getClienteCorreo());
        clientePedidos.setClientePedidosDireccion(clientePedidosDTO.getClienteDireccion());
        clientePedidos.setClientePedidosLat(clientePedidosDTO.getClienteLat());
        clientePedidos.setClientePedidosLng(clientePedidosDTO.getClienteLng());
        clientePedidos.setClientePedidosNombre(clientePedidosDTO.getClienteNombre());
        clientePedidos.setClientePedidosRazonSocial(clientePedidosDTO.getClienteRazonSocial());
        clientePedidos.setClientePedidosTelefono(clientePedidosDTO.getClienteTelefono());
        clientePedidos.setClientePedidosDiaSemanaVisita(clientePedidosDTO.getDiaSemanaVisita());
        clientePedidos.setClientePedidosVendedorAsignado(clientePedidosDTO.getClienteVendedorAsignado() != null ? clientePedidosDTO.getClienteVendedorAsignado().getVendedorId() : null);
        clientePedidosService.update(clientePedidos);
    }

    protected List<CategoriaDTO> getCategorias(Usuario usuarioConsulta){
        List<CategoriaProducto> categoriaProductoList = categoriaProductoService.getCategoriasByCliente(usuarioConsulta.getUsuarioRuc());
        return categoriaProductoList.stream().map(categoriaProductoToDto).collect(Collectors.toList());
    }

    private Function<CategoriaProducto, CategoriaDTO> categoriaProductoToDto = categoriaProducto -> {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setCategoriaId(categoriaProducto.getCategoriaProductoId());
        categoriaDTO.setCategoriaNombre(categoriaProducto.getCategoriaProductoNombre());
        return categoriaDTO;
    };

    protected List<ProductoDTO> getProductos(Usuario usuarioConsulta){
        List<Producto> productos = productoService.getProductos(usuarioConsulta.getUsuarioRuc());
        return productos.stream().map(productoToDto).collect(Collectors.toList());
    }

    private Function<Producto, ProductoDTO> productoToDto = producto -> {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setProductoId(producto.getProductoId());
        productoDTO.setProductoCodigo(producto.getProductoCodigo());
        productoDTO.setProductoCategoriaId(producto.getProductoCategoria());
        productoDTO.setProductoCategoria(producto.getCategoriaProductoByProductoCategoria() != null ? producto.getCategoriaProductoByProductoCategoria().getCategoriaProductoNombre() : null);
        productoDTO.setProductoNombre(producto.getProductoNombre());
        productoDTO.setProductoDescripcion(producto.getProductoDescripcion());
        productoDTO.setProductoVolumen(producto.getProductoVolumen());
        productoDTO.setProductoUnidadVolumen(producto.getCatalogoItemByProductoUnidadVolumen() != null ? producto.getCatalogoItemByProductoUnidadVolumen().getCatalogoItemDescripcion() : null);
        productoDTO.setProductoUnidadVolumenId(producto.getProductoUnidadVolumen());
        productoDTO.setProductoUnidadesPorCaja(producto.getProductoUnidadesPorCaja());
        productoDTO.setProductoPrecioSinImp(producto.getProductoPrecioSinImp());
        productoDTO.setProductoPrecioConImp(producto.getProductoPrecioConImp());
        productoDTO.setProductoPrecioPvpSinImp(producto.getProductoPrecioPvpSinImp());
        productoDTO.setProductoPrecioPvpConImp(producto.getProductoPrecioPvpConImp());
        return productoDTO;
    };

    protected void guardarProducto(ProductoDTO productoDTO, Usuario usuario){
        Producto producto = new Producto();
        producto.setProductoCodigo(productoDTO.getProductoCodigo());
        producto.setProductoCategoria(productoDTO.getProductoCategoriaId());
        producto.setProductoNombre(productoDTO.getProductoNombre());
        producto.setProductoDescripcion(productoDTO.getProductoDescripcion());
        producto.setProductoVolumen(productoDTO.getProductoVolumen());
        producto.setProductoUnidadVolumen(productoDTO.getProductoUnidadVolumenId());
        producto.setProductoUnidadesPorCaja(productoDTO.getProductoUnidadesPorCaja());
        producto.setProductoPrecioSinImp(productoDTO.getProductoPrecioSinImp());
        producto.setProductoPrecioConImp(productoDTO.getProductoPrecioConImp());
        producto.setProductoPrecioPvpSinImp(productoDTO.getProductoPrecioPvpSinImp());
        producto.setProductoPrecioPvpConImp(productoDTO.getProductoPrecioPvpConImp());
        producto.setProductoFechaCreacion(new Timestamp(new Date().getTime()));
        producto.setProductoUsuarioCrea(usuario.getUsuarioIdDocumento());
        producto.setProductoEstado(1);
        productoService.create(producto);
    }

    protected void actualizarProducto(ProductoDTO productoDTO, Usuario usuario){
        Producto producto = productoService.read(productoDTO.getProductoId());
        if(producto == null) return;
        producto.setProductoCodigo(productoDTO.getProductoCodigo());
        producto.setProductoCategoria(productoDTO.getProductoCategoriaId());
        producto.setProductoNombre(productoDTO.getProductoNombre());
        producto.setProductoDescripcion(productoDTO.getProductoDescripcion());
        producto.setProductoVolumen(productoDTO.getProductoVolumen());
        producto.setProductoUnidadVolumen(productoDTO.getProductoUnidadVolumenId());
        producto.setProductoUnidadesPorCaja(productoDTO.getProductoUnidadesPorCaja());
        producto.setProductoPrecioSinImp(productoDTO.getProductoPrecioSinImp());
        producto.setProductoPrecioConImp(productoDTO.getProductoPrecioConImp());
        producto.setProductoPrecioPvpSinImp(productoDTO.getProductoPrecioPvpSinImp());
        producto.setProductoPrecioPvpConImp(productoDTO.getProductoPrecioPvpConImp());
        productoService.update(producto);
    }

    protected void guardarPedido(PedidoDTO pedidoDTO, Usuario usuario){
        Pedido pedido = new Pedido();
        pedido.setPedidoFechaEntregaDesde(pedidoDTO.getFechaEntregaDesde());
        pedido.setPedidoFechaEntregaHasta(pedidoDTO.getFechaEntregaHasta());
        pedido.setPedidoPersonaRecibe(pedidoDTO.getPersonaRecibe());
        pedido.setPedidoSolicitaCredito(pedidoDTO.getSolicitaCredito() != null && pedidoDTO.getSolicitaCredito() ? 1 : 0);
        pedido.setPedidoTotal(pedidoDTO.getTotal());
        pedido.setPedidoLat(pedidoDTO.getLat());
        pedido.setPedidoLng(pedidoDTO.getLng());
        pedido.setPedidoVisitaId(pedidoDTO.getVisitaId());
        pedido.setPedidoFechaCreacion(new Timestamp(new Date().getTime()));
        pedido.setPedidoUsuarioCrea(usuario.getUsuarioIdDocumento());
        pedidoService.create(pedido);
        pedidoDTO.getDetalle().forEach(producto ->{
            PedidoDetalle pedidoDetalle = new PedidoDetalle();
            pedidoDetalle.setPedidoDetallePedidoId(pedido.getPedidoId());
            pedidoDetalle.setPedidoDetalleProductoId(producto.getProductoId());
            pedidoDetalle.setPedidoDetalleProductoCantidad(producto.getProductoCantidad());
            pedidoDetalle.setPedidoDetallePrecioConImp(producto.getProductoPrecioConImp());
            pedidoDetalle.setPedidoDetallePrecioSinImp(producto.getProductoPrecioSinImp());
            pedidoDetalle.setPedidoDetalleTotalSinImp(producto.getPrecioTotalSinImp());
            pedidoDetalle.setPedidoDetalleTotalConImp(producto.getPrecioTotalConImp());
            pedidoDetalleService.create(pedidoDetalle);
        });
        if(pedidoDTO.getSolicitaCredito()){
            pedidoDTO.getDocumentosCredito().forEach(foto -> {
                File fileCredito = new File();
                fileCredito.setFileContent(foto.getFile());
                fileCredito.setFileName("Pedido " + pedido.getPedidoId());
                fileCredito.setFileSize(foto.getFileSize());
                fileCredito.setFileType(foto.getFileType());
                fileCredito.setFileUploadDate(new Timestamp(new Date().getTime()));
                fileService.create(fileCredito);
                PedidoDocumentosCredito pedidoDocumentosCredito = new PedidoDocumentosCredito();
                pedidoDocumentosCredito.setPedidoDocumentosCreditoPedidoId(pedido.getPedidoId());
                pedidoDocumentosCredito.setPedidoDocumentosFileId(fileCredito.getFileId());
                pedidoDocumentosCreditoService.create(pedidoDocumentosCredito);
            });
        }
        Visita visita = visitaService.read(pedidoDTO.getVisitaId());
        visita.setVisitaEstado(Constant.ESTADO_VISITA_PEDIDO_TOMADO);
        visitaService.update(visita);
    }

    protected PedidoDTO getPedidoByVisitaId(Integer visitaId, Usuario usuarioConsulta){
        Pedido pedido = pedidoService.getByVisitaId(visitaId, usuarioConsulta.getUsuarioRuc());
        return pedidoToDto.apply(pedido);
    }

    protected List<PedidoDTO> getAllPedidosBy(Date fechaPedido, Date fechaEntrega, Usuario usuarioConsulta){
        List<Pedido> pedidos = pedidoService.getAllPedidosBy(fechaPedido, fechaEntrega, usuarioConsulta.getUsuarioRuc());
        return pedidos.stream().map(pedidoToDto).collect(Collectors.toList());
    }

    private Function<Pedido, PedidoDTO> pedidoToDto = pedido -> {
        if(pedido == null) return null;
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setPedidoId(pedido.getPedidoId());
        pedidoDTO.setFechaEntregaDesde(pedido.getPedidoFechaEntregaDesde());
        pedidoDTO.setFechaEntregaHasta(pedido.getPedidoFechaEntregaHasta());
        pedidoDTO.setClienteRazonSocial(pedido.getVisitaByPedidoVisitaId().getClientePedidosByVisitaClientePedidos().getClientePedidosRazonSocial());
        pedidoDTO.setPersonaRecibe(pedido.getPedidoPersonaRecibe());
        pedidoDTO.setSolicitaCredito(pedido.getPedidoSolicitaCredito() != null && pedido.getPedidoSolicitaCredito() == 1);
        pedidoDTO.setTotal(pedido.getPedidoTotal());
        pedidoDTO.setLat(pedido.getPedidoLat());
        pedidoDTO.setLng(pedido.getPedidoLng());
        pedidoDTO.setVisitaId(pedido.getPedidoVisitaId());
        pedidoDTO.setFechaCreacion(pedido.getPedidoFechaCreacion());
        pedidoDTO.setUsuarioCrea(pedido.getUsuarioByPedidoUsuarioCrea().getUsuarioNombres() + " " + pedido.getUsuarioByPedidoUsuarioCrea().getUsuarioApellidos());
        pedidoDTO.setDetalle(pedido.getPedidoDetallesByPedidoId().stream().map(pedidoDetalleToDto).collect(Collectors.toList()));
        return pedidoDTO;
    };

    private static Function<PedidoDetalle, PedidoDetalleDTO> pedidoDetalleToDto = pedidoDetalle -> {
        if(pedidoDetalle == null) return null;
        PedidoDetalleDTO pedidoDetalleDTO = new PedidoDetalleDTO();
        pedidoDetalleDTO.setProductoId(pedidoDetalle.getPedidoDetalleProductoId());
        pedidoDetalleDTO.setProductoCodigo(pedidoDetalle.getProductoByPedidoDetalleProductoId().getProductoCodigo());
        pedidoDetalleDTO.setProductoNombre(pedidoDetalle.getProductoByPedidoDetalleProductoId().getProductoNombre());
        pedidoDetalleDTO.setProductoCantidad(pedidoDetalle.getPedidoDetalleProductoCantidad());
        pedidoDetalleDTO.setProductoPrecioSinImp(pedidoDetalle.getPedidoDetallePrecioSinImp());
        pedidoDetalleDTO.setProductoPrecioConImp(pedidoDetalle.getPedidoDetallePrecioConImp());
        pedidoDetalleDTO.setPrecioTotalConImp(pedidoDetalle.getPedidoDetalleTotalConImp());
        pedidoDetalleDTO.setPrecioTotalSinImp(pedidoDetalle.getPedidoDetalleTotalSinImp());
        return pedidoDetalleDTO;
    };

    protected List<String> getDocumentosCredito(Integer pedidoId, Usuario usuarioSolicita){
        Pedido pedido = pedidoService.read(pedidoId, usuarioSolicita.getUsuarioRuc());
        if(pedido == null || pedido.getPedidoDocumentosCreditosByPedidoId() == null) return new ArrayList<>();
        return pedido.getPedidoDocumentosCreditosByPedidoId().stream()
                .map(PedidoDocumentosCredito::getFileByPedidoDocumentosFileId)
                .map(File::getFileContent)
                .collect(Collectors.toList());
    }

}
