package ec.net.redcode.tas.on.rest.bean;

import autorizacion.ws.sri.gob.ec.Autorizacion;
import autorizacion.ws.sri.gob.ec.AutorizacionComprobantesOffline;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import ec.gob.sri.comprobantes.modelo.rentencion.Impuesto;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Pay;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.facturacion.InfoPago;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PagoBean extends TasOnResponse {

    private static final Logger logger = LoggerFactory.getLogger(PagoBean.class);

    @Setter private PagoService pagoService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private FacturaService facturaService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private OfertaService ofertaService;
    @Setter private NotaCreditoService notaCreditoService;
    private CatalogoItem catalogoItem;
    private AutorizacionComprobantesOffline service;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat format = new DecimalFormat("##.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public void init(){
        logger.info("Init Pago Bean");
        List<CatalogoItem> catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AutorizacionComprobantesOffline.class);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress(catalogoItems.stream().filter(c -> "ENDPOINT AUTH".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        service = (AutorizacionComprobantesOffline) factory.create();
    }

    /**
     * Permite crear el pago o retencion
     */
    public void createPago(Pago pago){
        Date date = new Date();
        pago.setPagoFechaRegistro(new Timestamp(date.getTime()));
        pagoService.create(pago);
        calculatePay(pago.getPagoFacturaId());
    }

    /**
     * Obtiene el detalle del pago
     */
    public Pay getPayDetail(int pagoId){
        Pago pago = pagoService.read(pagoId);
        Pay pay = externalToPay.apply(pago);
        return pay;
    }

    /**
     * Permite obtener los pagos por factura
     */
    public List<Pay> readPagos(String facturaId){
        List<Pago> pagos = pagoService.getPayByFactura(facturaId);
        List<Pay> pays = pagos.stream().map(externalToPay).filter(Objects::nonNull).collect(Collectors.toList());
        return pays;
    }

    public Map<String, Object> getPagoNCInfo(String facturaId) throws TasOnException {
        Map<String, Object> data = new ConcurrentHashMap<>();
        NotaCredito notaCredito = notaCreditoService.readByNotaCreditoFacturaId(facturaId);

        if(notaCredito == null)
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, "No existe una nota de crédito asociada a la factura");
        if(!notaCredito.getNotaCreditoEstado().equals("AUTORIZADO"))
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, "La nota de crédito no tiene estado autorizado");

        data.put("estado", notaCredito.getNotaCreditoEstado());
        data.put("valor", notaCredito.getNotaCreditoValor());
        data.put("numeroAutorizacion", notaCredito.getNotaCreditoClaveAcceso());
        data.put("fechaAutorizacion", notaCredito.getNotaCreditoFechaAutorizacion());
        data.put("facturaId", facturaId);
        return data;
    }

    /**
     * Permite obtener en linea el comproante de retencion desde el SRI
     */
    public Map<String, Object> getComprobanteRetencion(String claveAcceso, String facturaId) throws TasOnException {
        Map<String, Object> data = new ConcurrentHashMap<>();
        RespuestaComprobante comprobante = obtenerComprobante(claveAcceso);
        for (Autorizacion autorizacion: comprobante.getAutorizaciones().getAutorizacion()){
            validarEstadoComprobante(autorizacion);
            String comprobanteXML = autorizacion.getComprobante();
            ComprobanteRetencion comprobanteRetencion = obtenerComprobateRetencionDeFactura(comprobanteXML, facturaId);
            data.put("estado", autorizacion.getEstado());
            data.put("numeroAutorizacion", autorizacion.getNumeroAutorizacion());
            data.put("fechaAutorizacion", autorizacion.getFechaAutorizacion());
            data.put("comprobante", comprobanteRetencion);
            data.put("facturaId", facturaId);
            data.put("comprobanteXML", comprobanteXML);
        }
        return data;
    }

    private RespuestaComprobante obtenerComprobante(String claveAcceso) throws TasOnException{
        RespuestaComprobante response =  service.autorizacionComprobante(claveAcceso);
        if ("0".equals(response.getNumeroComprobantes())) {
            logger.error("No existe un comprobante con la clave de acceso: " + claveAcceso);
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, "No existe comprobante");
        }
        return response;
    }

    private void validarEstadoComprobante(Autorizacion autorizacion) throws TasOnException{
        if ("RECHAZADA".equals(autorizacion.getEstado())){
            AtomicReference<String> error = new AtomicReference<>();
            autorizacion.getMensajes().getMensaje().forEach(m -> error.set(m.getIdentificador().concat(" ").concat(m.getMensaje())));
            logger.error("Comprobante en estado rechazado. " + error.get());
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, error.get());
        }
    }

    private ComprobanteRetencion obtenerComprobateRetencionDeFactura(String comprobanteXML, String facturaId) throws TasOnException{
        ComprobanteRetencion comprobanteRetencion;
        try {
            StringReader sr = new StringReader(comprobanteXML);
            JAXBContext jaxbContext = JAXBContext.newInstance(ComprobanteRetencion.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            comprobanteRetencion = (ComprobanteRetencion) unmarshaller.unmarshal(sr);
            for (Impuesto impuesto: comprobanteRetencion.getImpuestos().getImpuesto()){
                if (!facturaId.replace("-", "").equals(impuesto.getNumDocSustento().replace("-", ""))){
                    logger.error("La retencion no corresponde a la factura");
                    throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, "La retencion no corresponde a la factura");
                }
            }
        } catch (Exception e) {
            logger.error("Error en comprobante", e);
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), "Error en comprobante", e.getMessage());
        }
        return comprobanteRetencion;
    }

    /**
     * Permite saber si ya se cumplio el pago total para que cambie de estado a pagadas
     */
    private void calculatePay(String facturaId){
        Factura factura = facturaService.read(facturaId);
        List<Pago> pagos = pagoService.getPayByFactura(facturaId);
        double totalPagos = 0.0;
        for (Pago pago: pagos){
            totalPagos = Double.sum(totalPagos, pago.getPagoValor());
        }
        if (Double.compare(Double.parseDouble(format.format(factura.getFacturaTotal())), Double.parseDouble(format.format(totalPagos))) == 0){
            List<FacturaDetalle> facturaDetalles = facturaDetalleService.getFacturaDetalleByFactura(factura.getFacturaId());
            factura.setFacturaEstado(Constant.INVOICE_FINISH);
            facturaService.update(factura);
            for (FacturaDetalle facturaDetalle: facturaDetalles){
                Oferta oferta = ofertaService.read(facturaDetalle.getFacturaDetalleOfertaId());
                oferta.setOfertaEstado(Constant.OFERTA_READY_TO_PAY);
                ofertaService.update(oferta);
                SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
                solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_PAYED);
                solicitudEnvioService.update(solicitudEnvio);
            }
        }
    }

    /**
     * Pemite crear el pago de la retencion
     */
    protected void createPago(InfoPago infoPago) throws ParseException {
        Pago pago = new Pago();
        pago.setPagoFacturaId(infoPago.getFacturaId());
        String numeroDocumento = infoPago.getComprobanteRetencion().getInfoTributaria().getEstab().concat("-").concat(infoPago.getComprobanteRetencion().getInfoTributaria().getPtoEmi()
                .concat("-").concat(infoPago.getComprobanteRetencion().getInfoTributaria().getSecuencial()));
        pago.setPagoNumeroDocumento(numeroDocumento);
        pago.setPagoRetencionXml(infoPago.getComprobanteXML());
        Date fechaDocumento = sdf.parse(infoPago.getComprobanteRetencion().getInfoCompRetencion().getFechaEmision());
        pago.setPagoFechaDocumento(new Timestamp(fechaDocumento.getTime()));
        double total = 0;
        for (Impuesto impuesto: infoPago.getComprobanteRetencion().getImpuestos().getImpuesto()){
            total += impuesto.getValorRetenido().doubleValue();
        }
        pago.setPagoValor(total);
        pago.setPagoTipoId(Constant.COMPROBANTE_RETENCION);
        createPago(pago);
    }

    /**
     * Permite castear de la entidad Pago al Pojo Pay
     */
    private Function<Pago, Pay> externalToPay = pago -> {
        Pay pay = new Pay();
        pay.setFacturaId(pago.getPagoFacturaId());
        pay.setPagoId(pago.getPagoId());
        pay.setPagoTipoId(pago.getPagoTipoId());
        catalogoItem = catalogoItemService.read(pago.getPagoTipoId());
        pay.setPagoTipo(catalogoItem.getCatalogoItemDescripcion());
        pay.setPagoBancoId(pago.getPagoBancoId());
        catalogoItem = catalogoItemService.read(pago.getPagoBancoId());
        pay.setPagoBanco(catalogoItem.getCatalogoItemDescripcion());
        pay.setPagoNumeroDocumento(pago.getPagoNumeroDocumento());
        pay.setPagoValor(pago.getPagoValor());
        pay.setPagoFechaDocumento(pago.getPagoFechaDocumento());
        pay.setPagoFechaRegistro(pago.getPagoFechaRegistro());
        if (pago.getPagoTipoId() == Constant.COMPROBANTE_RETENCION){
            StringReader sr = new StringReader(pago.getPagoRetencionXml());
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ComprobanteRetencion.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ComprobanteRetencion comprobanteRetencion = (ComprobanteRetencion) unmarshaller.unmarshal(sr);
                pay.setComprobanteRetencion(comprobanteRetencion);
            } catch (JAXBException e) {
                logger.error("Error getting compreobante retencion", e);
            }
        }
        return pay;
    };

}
