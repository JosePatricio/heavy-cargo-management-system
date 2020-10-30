package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.InvoiceProvider;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
import ec.net.redcode.tas.on.persistence.service.FacturaProveedorService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FacturaProveedorBean extends TasOnResponse {

    private FacturaProveedorService facturaProveedorService;
    private UsuarioService usuarioService;
    private ClienteService clienteService;
    private CatalogoItemService catalogoItemService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(FacturaProveedorBean.class);

    /**
     * Permite registrar una Factura de Proveedor
     *
     * @param facturaProveedor
     * @param userName
     */
    public void create(FacturaProveedor facturaProveedor, String userName) throws TasOnException{
        Date now = new Date();
        Usuario usuario = usuarioService.getUsuariosByUserName(userName);
        facturaProveedor.setFacturaProveedorUsuario(usuario.getUsuarioIdDocumento());
        facturaProveedor.setFacturaProveedorFechaRecepcion(new Timestamp(new Date().getTime()));

        FacturaProveedor facturaProveedorExiste = facturaProveedorService.getByNumeroFacturaAndRuc(facturaProveedor.getFacturaProveedorNumero(), facturaProveedor.getFacturaProveedorRucCliente());
        if(facturaProveedorExiste.getFacturaProveedorId() > 0) throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "La factura que intenta ingresar ya se encuentra registrada!");

        try {
            Date newFechaPagoString = sdf.parse(sdf.format(facturaProveedor.getFacturaProveedorFechaPago()));
            Date newNow = sdf.parse(sdf.format(now));
            if (newFechaPagoString.after(newNow) || newFechaPagoString.equals(newNow))
                facturaProveedor.setFacturaProveedorEstado(Constant.FACTURAS_PROVEEDOR_PEDIENTE_PAGO);
            else
                facturaProveedor.setFacturaProveedorEstado(Constant.FACTURAS_PROVEEDOR_PAGADAS);
            facturaProveedorService.create(facturaProveedor);
        } catch (ParseException e) {
            logger.error("Error parsing data", e);
        }

    }

    /**
     * Permite obtener la darta la Facrtura por el identificador
     *
     * @param idFacturaProveedor
     * @return
     */
    public FacturaProveedor read(int idFacturaProveedor){
        FacturaProveedor facturaProveedor = facturaProveedorService.read(idFacturaProveedor);
        return facturaProveedor;
    }

    /**
     * Permite actualizar las Facrturas
     *
     * @param invoiceProviders
     */
    protected void update(List<InvoiceProvider> invoiceProviders){
        for (InvoiceProvider invoiceProvider: invoiceProviders) {
            FacturaProveedor facturaProveedor = facturaProveedorService.getByNumeroFacturaAndRuc(invoiceProvider.getFacturaProveedorNumero(), invoiceProvider.getFacturaProveedorRucCliente());
            facturaProveedor.setFacturaProveedorEstado(invoiceProvider.getFacturaProveedorState());
            facturaProveedor.setFacturaProveedorTransferencia(invoiceProvider.getFacturaProveedorTransfer() != null ? invoiceProvider.getFacturaProveedorTransfer() : "");
            facturaProveedorService.update(facturaProveedor);
        }
    }

    /**
     * Permite obtener las Facturas por el estado
     *
     * @param estado
     * @return
     */
    public List<InvoiceProvider> getByEstado(int estado) {
        List<FacturaProveedor> facturaProveedors = facturaProveedorService.getByEstado(estado);
        List<InvoiceProvider> invoiceProviders = facturaProveedors.stream().map(externalToInvoiceProvider).filter(Objects::nonNull).collect(Collectors.toList());
        return invoiceProviders;
    }

    /**
     * Permite obtener la Factura por numero y ruc
     *
     * @param numeroFactura
     * @param ruc
     * @return
     */
    protected FacturaProveedor getByNumeroFacturaAndRuc(String numeroFactura, String ruc){
        FacturaProveedor facturaProveedor = facturaProveedorService.getByNumeroFacturaAndRuc(numeroFactura, ruc);
        return facturaProveedor;
    }

    Function<FacturaProveedor, InvoiceProvider> externalToInvoiceProvider = facturaProveedor -> {
        InvoiceProvider invoice = new InvoiceProvider();
        invoice.setFacturaProveedorNumero(facturaProveedor.getFacturaProveedorNumero());
        invoice.setFacturaProveedorRucCliente(facturaProveedor.getFacturaProveedorRucCliente());
        Cliente cliente = clienteService.readCliente(facturaProveedor.getFacturaProveedorRucCliente());
        if(cliente == null) return null;
        invoice.setFacturaProveedorRucClienteName(cliente.getClienteRazonSocial());
        invoice.setFacturaProveedorFechaEmision(facturaProveedor.getFacturaProveedorFechaEmision());
        invoice.setFacturaProveedorFormaPago(facturaProveedor.getFacturaProveedorFormaPago());
        CatalogoItem catalogoItem = catalogoItemService.read(facturaProveedor.getFacturaProveedorFormaPago());
        invoice.setFacturaProveedorFormaPagoDesc(catalogoItem.getCatalogoItemDescripcion());
        invoice.setFacturaProveedorCompra(facturaProveedor.getFacturaProveedorCompra());
        catalogoItem = catalogoItemService.read(facturaProveedor.getFacturaProveedorCompra());
        invoice.setFacturaProveedorCompraDesc(catalogoItem.getCatalogoItemDescripcion());
        invoice.setFacturaProveedorFechaPago(facturaProveedor.getFacturaProveedorFechaPago());
        invoice.setFacturaProveedorTotal(facturaProveedor.getFacturaProveedorTotal());
        return invoice;
    };

    public void setFacturaProveedorService(FacturaProveedorService facturaProveedorService) {
        this.facturaProveedorService = facturaProveedorService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }
}
