package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.*;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Adquiriente;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FacturaBean extends TasOnResponse {

    @Setter private FacturaService facturaService;
    @Setter private LocalidadService localidadService;
    @Setter protected UsuarioService usuarioService;
    @Setter private ClienteService clienteService;
    @Setter private SecuenciaService secuenciaService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private OfertaService ofertaService;
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private PagoService pagoService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter protected AdquirienteService adquirienteService;
    @Setter protected FacturaManualService facturaManualService;
    @Setter protected FacturaRetencionService facturaRetencionService;

    private double prontoPago;
    private int diasGestion;

    public void init(){
        List<CatalogoItem> catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(20);
        prontoPago = Double.valueOf(catalogoItem.stream().filter(c -> Constant.ADMIN_PAGOS_INMEDIATO == c.getCatalogoItemId()).findAny().orElse(null).getCatalogoItemValor());
        catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_SECURITY);
        diasGestion = Integer.valueOf(catalogoItem.stream().filter(c -> Constant.ADMIN_DIAS_GESTION == c.getCatalogoItemId()).findAny().orElse(null).getCatalogoItemValor());
    }

    /**
     * Metodo que permite crear la Entidad Factura
     */
    public void createFactura(Factura factura){
        facturaService.create(factura);
    }

    /**
     * Metodo que permite generar el codigo de prefactura
     */
    public String generateCode(String username){
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        Cliente cliente = clienteService.readCliente(usuario.getUsuarioRuc());
        Localidad localidad = localidadService.readLocalidad(cliente.getClienteLocalidadId());
        while (localidad.getLocalidadLocalidadPadre() !=0 ){
            localidad = localidadService.getLocalidadByLocalidadId(localidad.getLocalidadLocalidadPadre(), 1);
        }
        Date date = new Date();
        Secuencia secuencia = secuenciaService.read(0);
        int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
        secuencia.setSecuencia(nextValue);
        secuenciaService.update(secuencia);
        String code = String.valueOf(localidad.getLocalidadCode()) + String.valueOf(date.getYear()) + String.valueOf(TasOnUtil.divideAndConquer(nextValue)) + String.valueOf(nextValue);
        String digit = TasOnUtil.calculateCheckDigit(code);
        return code + digit;
    }

    protected void puedeCrearPrefactura(Usuario usuario) throws TasOnException {
        Cliente cliente = usuario.getClienteByUsuarioRuc();
        if(cliente.getClienteBanco() == 0
                || cliente.getClienteTipoCuenta() == 0
                || TasOnUtil.isStringNullOrEmpty(cliente.getClienteCuenta())
                || cliente.getClienteBancoTipoIdentificacion() == null
                || TasOnUtil.isStringNullOrEmpty(cliente.getClienteBancoIdentificacion())
                || TasOnUtil.isStringNullOrEmpty(cliente.getClienteBancoNombres())
        )
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Necesita actualizar su información bancaria desde administración");
    }

    /**
     * Permite grabar los datos de la prefactura
     */
    protected void createPreInvoice(Invoice invoice, Usuario usuario){
        Factura factura = new Factura();
        factura.setFacturaId(invoice.getNumberInvoice());
        factura.setFacturaUsuarioId(usuario.getUsuarioIdDocumento());
        Date date = new Date();
        factura.setFacturaFechaEmisionPrefactura(new Timestamp(date.getTime()));
        factura.setFacturaTipoFactura(Constant.USER_DRIVER);
        factura.setFacturaEstado(Constant.INVOICE_PRE);
        factura.setFacturaSubtotal(invoice.getAmount());
        factura.setFacturaTotal(invoice.getAmount());
        factura.setFacturaEmpresa(usuario.getUsuarioRuc());
        factura.setFacturaTipoPago(invoice.getInvoiceTypePay());
        factura.setFacturaDescuento(invoice.getInvoiceDiscount());
        if (Constant.ADMIN_PAGOS_INMEDIATO == invoice.getInvoiceTypePay()){
            double valor = createDescuento(invoice);
            factura.setFacturaDescuento(valor);
        }
        facturaService.create(factura);
        FacturaDetalle facturaDetalle;
        for (Integer idOferta: invoice.getOffersId()){
            facturaDetalle = new FacturaDetalle();
            facturaDetalle.setFacturaDetalleFacturaId(factura.getFacturaId());
            facturaDetalle.setFacturaDetalleOfertaId(idOferta);
            facturaDetalleService.create(facturaDetalle);
            Oferta oferta = ofertaService.read(idOferta);
            oferta.setOfertaEstado(Constant.OFERTA_DELIVERY_BILL);
            ofertaService.update(oferta);
        }
    }

    /**
     * Permite crear el descuento por el pronto pago
     */
    public double createDescuento(Invoice invoice){
        Date date = new Date();
        SolicitudEnvio solicitudEnvio;
        Oferta oferta;
        double total = 0.0;
        for (Integer idOferta: invoice.getOffersId()){
            oferta = ofertaService.read(idOferta);
            solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(solicitudEnvio.getSolicitudEnvioFechaPago().getTime());
            calendar.add(Calendar.DATE, diasGestion);
            Date fechaPago = new Date(calendar.getTimeInMillis());
            int dias = (int) ((fechaPago.getTime() - date.getTime()) / 86400000) + 1;
            double valor = oferta.getOfertaValor() * dias * (prontoPago /100);
            if(valor<0)valor=0;
            oferta.setOfertaDescuento(valor);
            ofertaService.update(oferta);
            total += valor;
        }
        return total;
    }

    /**
     * Permite obtener la lista de la factura por usuario y estado
     * @param username
     * @param estado
     * @return
     */
    protected List<Invoices> getInvoicesByUsername(String username, int estado){
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        List<Factura> facturas = facturaService.getFacturasByUsuarioAndEstado(usuario.getUsuarioIdDocumento(), estado);
        List<Invoices> invoices = facturas.stream().map(externalToInvoices).filter(Objects::nonNull).collect(Collectors.toList());
        return invoices;
    }

    protected List<Invoices> getInvoiceByEstado(int estado){
        List<Factura> facturas = facturaService.getFacturasByEstado(estado);
        List<Invoices> invoices = facturas.stream().map(externalToInvoices).filter(Objects::nonNull).collect(Collectors.toList());
        return invoices;
    }

    protected List<Invoices> getInvoicesAllBy(int estado, String cliente, String facturaNro, Double valorDesde,
                                              Double valorHasta, Date fechaCobroDesde, Date fechaCobroHasta){
        return facturaService.getFacturasBy(estado, cliente, facturaNro, valorDesde, valorHasta, fechaCobroDesde, fechaCobroHasta);
    }

    protected List<FacturaManualDTO> getFacturasManualesBy(String secuencial, String razonSocial, String ruc, Date fechaEmisionDesde, Date fechaEmisionHasta){
        return facturaService.getFacturasManualesBy(secuencial, razonSocial, ruc, fechaEmisionDesde, fechaEmisionHasta);
    }

    protected List<FacturaRetencionDTO> getRetencionesBy(String secuencial, String razonSocial, String ruc, Date fechaEmisionDesde, Date fechaEmisionHasta){
        return facturaRetencionService.getRetencionesBy(secuencial, razonSocial, ruc, fechaEmisionDesde, fechaEmisionHasta);
    }

    /**
     * Permite obtener el detalle de la factura por el id
     * @param idFactura
     * @return
     */
    protected InvoiceDetail getInvoiceDetail(String idFactura){
        Factura factura = facturaService.read(idFactura);
        InvoiceDetail invoiceDetail = externalToInvoiceDetail.apply(factura);
        return invoiceDetail;
    }

    /**
     *
     * @param invoice
     */
    protected void updateInvoice(Invoice invoice, String username){
        Factura factura = facturaService.read(invoice.getNumberInvoice());
        factura.setFacturaNro(invoice.getIdInvoice());
        factura.setFacturaNroAutorizacion(invoice.getAuthNroInvoice());
        factura.setFacturaFechaAutorizacion(invoice.getDateAuthInvoice());
        factura.setFacturaPersonaEntrega(invoice.getPersonInvoice());
        factura.setFacturaEstado(Constant.INVOICE_TO_PAY);
        Date now = new Date();
        factura.setFacturaFechaRecepcion(new Timestamp(now.getTime()));
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        factura.setFacturaUsuarioRecibe(usuario.getUsuarioIdDocumento());
        Cliente cliente = clienteService.readCliente(factura.getFacturaEmpresa());
        double valor = (factura.getFacturaSubtotal() * cliente.getClienteRetencion()) / 100;
        factura.setFacturaRetencion(valor);
        facturaService.update(factura);
        List<FacturaDetalle> facturaDetalles = facturaDetalleService.getFacturaDetalleByFactura(factura.getFacturaId());
        for (FacturaDetalle facturaDetalle: facturaDetalles){
            Oferta oferta = ofertaService.read(facturaDetalle.getFacturaDetalleOfertaId());
            oferta.setOfertaEstado(Constant.OFERTA_TO_PAY);
            ofertaService.update(oferta);
        }
        invoice.setInvoiceTypePay(factura.getFacturaTipoPago());
    }

    /**
     * Permite castear de la clase Factura a Invoices
     */
    Function<Factura, Invoices> externalToInvoices = factura -> {
        Invoices invoices = new Invoices();
        invoices.setInvoiceId(factura.getFacturaId());
        invoices.setInvoiceNumber(factura.getFacturaNro() != null ? factura.getFacturaNro() : "");
        invoices.setInvoiceAmount(factura.getFacturaSubtotal());
        invoices.setInvoiceDatePrefactura(factura.getFacturaFechaEmisionPrefactura());
        invoices.setInvoicesTypePay(factura.getFacturaTipoPago());
        invoices.setInvoiceEmisionDate(factura.getFacturaFechaEmision());
        CatalogoItem catalogoItem = catalogoItemService.read(factura.getFacturaTipoPago());
        invoices.setInvoicesTypePayDesc(catalogoItem.getCatalogoItemDescripcion());
        invoices.setInvoicesDiscount(factura.getFacturaDescuento());
        //invoices.setInvoiceDate(factura.getFacturaFechaEmision() != null ? factura.getFacturaFechaEmision() : null);
        //TODO: fecha de cobro es co
        Cliente cliente;
        if (factura.getFacturaEstado() == 83 || factura.getFacturaEstado() == 71) {
            cliente = clienteService.readCliente(factura.getFacturaEmpresa());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(factura.getFacturaFechaEmision().getTime());
            calendar.add(Calendar.DATE, cliente.getClienteDiasCredito());
            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
            invoices.setInvoiceDate(timestamp);
            Date now = new Date();
            Date fechaFacturaEmision = new Date(timestamp.getTime());
            int dias = 0;
            if (now.after(fechaFacturaEmision))
                dias = (int) ((fechaFacturaEmision.getTime() - now.getTime()) / 86400000);
            invoices.setDiasFacturaVencida(dias);
            if (factura.getFacturaEstado() == 83) {
                List<Pago> pagos = pagoService.getPayByFactura(factura.getFacturaId());
                double pagoTotal = 0.0;
                for (Pago pago : pagos) {
                    pagoTotal += pago.getPagoValor();
                }
                double saldo = factura.getFacturaTotal() - pagoTotal;
                saldo = TasOnUtil.roundDouble(saldo, 2);
                invoices.setInvoiceBalanceDue(saldo);
            }
        } else {
            Usuario usuario = usuarioService.readUsuario(factura.getFacturaUsuarioId());
            cliente = clienteService.readCliente(usuario.getUsuarioRuc());
        }
        invoices.setCompany(cliente.getClienteRazonSocial());
        return invoices;
    };

    Function<Factura, InvoiceDetail> externalToInvoiceDetail = factura -> {
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoiceId(factura.getFacturaId());
        invoiceDetail.setInvoiceNumer(factura.getFacturaNro() != null ? factura.getFacturaNro() : "");
        invoiceDetail.setInvoiceDatePreFactura(factura.getFacturaFechaEmisionPrefactura());
        invoiceDetail.setInvoiceDateFactura(factura.getFacturaFechaEmision());
        invoiceDetail.setInvoiceTypePay(factura.getFacturaTipoPago());
        invoiceDetail.setInvoiceDiscount(factura.getFacturaDescuento());
        Cliente cliente = clienteService.readCliente(Constant.RUC_TASON);
        Company company = new Company();
        company.setRuc(cliente.getClienteRuc());
        company.setRazonSocial(cliente.getClienteRazonSocial());
        company.setDireccion(cliente.getClienteDireccion());
        invoiceDetail.setTason(company);
        if (factura.getFacturaEstado() == 83 || factura.getFacturaEstado() == 71) {
            cliente = clienteService.readCliente(factura.getFacturaEmpresa());
        }
        else {
            Usuario usuario = usuarioService.readUsuario(factura.getFacturaUsuarioId());
            cliente = clienteService.readCliente(usuario.getUsuarioRuc());
        }
        company = new Company();
        company.setRuc(cliente.getClienteRuc());
        company.setRazonSocial(cliente.getClienteRazonSocial());
        company.setDireccion(cliente.getClienteDireccion());
        invoiceDetail.setClient(company);
        List<FacturaDetalle> facturaDetalles = facturaDetalleService.getFacturaDetalleByFactura(factura.getFacturaId());
        List<Offers> offersList = new ArrayList<>();
        Offers offers;
        Localidad localidad;
        for (FacturaDetalle facturaDetalle: facturaDetalles){
            Oferta oferta = ofertaService.read(facturaDetalle.getFacturaDetalleOfertaId());
            offers = new Offers();
            offers.setIdOferta(oferta.getOfertaId());
            offers.setComments(oferta.getOfertaObservacion());
            offers.setAmount(oferta.getOfertaValor());
            offers.setIdSolicitud(oferta.getOfertaIdSolicitud());
            offers.setComision(oferta.getOfertaComision());
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
            offers.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());
            localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
            offers.setOrigen(localidad.getLocalidadDescripcion());
            localidad = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
            offers.setDestino(localidad.getLocalidadDescripcion());
            offers.setNumeroDocCliente(solicitudEnvio.getSolicitudEnvioNumeroDocCliente());
            offersList.add(offers);
        }
        invoiceDetail.setOffers(offersList);
        return invoiceDetail;
    };

    Function<FacturaDetalle, Offers> externalToOffers = facturaDetalle -> {
        Offers offers = new Offers();
        Oferta oferta = ofertaService.read(facturaDetalle.getFacturaDetalleOfertaId());
        offers.setIdOferta(oferta.getOfertaId());
        offers.setComments(oferta.getOfertaObservacion());
        offers.setAmount(oferta.getOfertaValor());
        return offers;
    };

    protected Usuario getUsuario(String username){
        return usuarioService.getUsuariosByUserName(username);
    }
    
    protected void crearActualizarAdquiriente(ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura infoFactura){
        Adquiriente adquiriente = new Adquiriente();
        if(TasOnUtil.isStringNullOrEmpty(infoFactura.getTipoIdentificacionComprador(),
                infoFactura.getIdentificacionComprador(),
                infoFactura.getRazonSocialComprador(),
                infoFactura.getDirEstablecimiento())){
            return;
        }

        adquiriente.setAdquirienteTipoDocumento(infoFactura.getTipoIdentificacionComprador().equals(Constant.CODIGO_SRI_RUC)?Constant.CATALOGO_RUC:Constant.CATALOGO_CEDULA);
        adquiriente.setAdquirienteIdDocumento(infoFactura.getIdentificacionComprador());
        adquiriente.setAdquirienteRazonSocial(infoFactura.getRazonSocialComprador());
        adquiriente.setAdquirienteDireccion(infoFactura.getDirEstablecimiento());

        Adquiriente adq = adquirienteService.read(adquiriente.getAdquirienteIdDocumento());
        if(adq == null){
            adquirienteService.create(adquiriente);
            return;
        }
        if(!adq.getAdquirienteRazonSocial().equals(adquiriente.getAdquirienteRazonSocial())){
            adq.setAdquirienteRazonSocial(adquiriente.getAdquirienteRazonSocial());
        }
        if(!adq.getAdquirienteDireccion().equals(adquiriente.getAdquirienteDireccion())){
            adq.setAdquirienteDireccion(adquiriente.getAdquirienteDireccion());
        }
    }

    protected byte[] getFacturaManualXML(String claveAcceso){
        FacturaManual facturaManual = facturaManualService.read(claveAcceso);
        if(facturaManual == null) return null;
        if(!TasOnUtil.isStringNullOrEmpty(facturaManual.getFacturaManualXml())) return facturaManual.getFacturaManualXml().getBytes();
        return null;
    }

    protected byte[] getRetencionXML(String claveAcceso){
        FacturaRetencion retencion = facturaRetencionService.read(claveAcceso);
        if(retencion == null) return null;
        if(!TasOnUtil.isStringNullOrEmpty(retencion.getFacturaRetencionXml())) return retencion.getFacturaRetencionXml().getBytes();
        return null;
    }

}
