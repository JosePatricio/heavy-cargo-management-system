package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Documents;
import ec.net.redcode.tas.on.common.dto.Offer;
import ec.net.redcode.tas.on.common.dto.Offers;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.ServicesUtil;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.processor.UsuariosProcessor;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OfertaBean extends TasOnResponse {

    private static final Logger logger = LoggerFactory.getLogger(UsuariosProcessor.class);
    private static SimpleDateFormat simpleDateShortFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Setter protected UsuarioService usuarioService;
    @Setter private OfertaService ofertaService;
    @Setter private LocalidadService localidadService;
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private FacturaService facturaService;
    @Setter private ClienteService clienteService;
    @Setter private CalificacionTransportistaService calificacionTransportistaService;
    @Setter private ConductorService conductorService;
    @Setter private VehiculoService vehiculoService;
    @Setter private OfertaFileService ofertaFileService;
    @Setter private FileService fileService;

    private int diasGestion;
    private double prontoPago;
    private String mensajeEntregaFacturas;

    public void init(){
        List<CatalogoItem> catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_SECURITY);
        diasGestion = Integer.valueOf(catalogoItem.stream().filter(c -> Constant.ADMIN_DIAS_GESTION == c.getCatalogoItemId()).findAny().orElse(null).getCatalogoItemValor());
        catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(20);
        prontoPago = Double.valueOf(catalogoItem.stream().filter(c -> Constant.ADMIN_PAGOS_INMEDIATO == c.getCatalogoItemId()).findAny().orElse(null).getCatalogoItemValor());
        mensajeEntregaFacturas = catalogoItemService.read(Constant.CATALOGO_MENSAJE_ENTREGA_FACTURAS).getCatalogoItemValor();
    }

    public Oferta read(int idOferta){
        return ofertaService.read(idOferta);
    }

    protected void offerValue(Offer offer, Usuario usuario) throws TasOnException{
        try {
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(offer.getIdSolicitud());
            if (Constant.SOLICITUD_ENVIO_CREATE == solicitudEnvio.getSolicitudEnvioEstado()) {
                if (Constant.USER_DRIVER != usuario.getUsuarioTipoUsuario() &&
                        Constant.USER_DRIVER_ADMIN != usuario.getUsuarioTipoUsuario()) {
                    logger.error("Usuario {} intenta realizar accion no autorizada", usuario.getUsuarioIdDocumento());
                    throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "El usuario no esta permitido ofertar");
                }
                if(!precioOfertaValido(offer.getAmount(), solicitudEnvio)){
                    throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Su oferta debe ser más competitiva frente a la mejor oferta");
                }
                if(!ServicesUtil.puedeUsuarioOfertar(usuario, solicitudEnvio, conductorService, vehiculoService)){
                    throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No puede ofertar en esta solicitud");
                }
                if(solicitudEnvio.getOfertasBySolicitudEnvioId().stream().anyMatch(s -> s.getOfertaIdUsuario().equals(usuario.getUsuarioIdDocumento()))){
                    throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "Ya ha ofertado en esta solicitud");
                }
                Oferta oferta = new Oferta();
                oferta.setOfertaValor(TasOnUtil.roundDouble(offer.getAmount(), 2));
                oferta.setOfertaObservacion(offer.getComments());
                oferta.setOfertaIdSolicitud(offer.getIdSolicitud());
                oferta.setOfertaFecha(new Timestamp(new Date().getTime()));
                oferta.setOfertaEstado(Constant.OFERTA_CREATED);
                oferta.setOfertaIdUsuario(usuario.getUsuarioIdDocumento());
                setRetIVAComOfferValues(oferta, solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId(), usuario);
                ofertaService.create(oferta);
            } else {
                logger.error("La solicitud de envio {} fue cancelada por {}", solicitudEnvio.getSolicitudEnvioId(), solicitudEnvio.getSolicitudEnvioComentario());
                throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "La solicitud de envio " + solicitudEnvio.getSolicitudEnvioId()
                        + " fue cancelada por " + solicitudEnvio.getSolicitudEnvioComentario());
            }
        } catch (TasOnException e){
            throw e;
        } catch (Exception e){
            logger.error("Error en Ofertas", e);
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), "Error en creaci\u00f3n de la Oferta", e);
        }
    }

    private boolean precioOfertaValido(Double precioOferta, SolicitudEnvio solicitudEnvio){
        if(precioOferta==null || solicitudEnvio==null) return false;
        Cliente clienteCreadorSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
        List<Oferta> ofertaList = ofertaService.getOfertaBySolicitudAOrder(solicitudEnvio.getSolicitudEnvioId());
        DoubleSummaryStatistics estadisticasOfertas = ofertaList.stream().mapToDouble(Oferta::getOfertaValor).summaryStatistics();
        double precioAMejorar = estadisticasOfertas.getMin();
        if(estadisticasOfertas.getCount() == 0){
            if(solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.SUBASTA_INVERSA_ABIERTA){
                return true;
            } else {
                precioAMejorar = TasOnUtil.getMaxValorSubastaValorObjetivo(solicitudEnvio.getSolicitudEnvioValorObjetivo(), clienteCreadorSolicitud.getClienteComision());
            }
        }
        return precioOferta <= precioAMejorar*(1-0.02); //Debe ser menor o igual al 2% de la mejor oferta
    }

    private void setRetIVAComOfferValues(Oferta oferta, Usuario usuarioSolicitud, Usuario usuarioOferta){
        Cliente clienteOferta = usuarioOferta.getClienteByUsuarioRuc();
        Cliente clienteSolicitud = usuarioSolicitud.getClienteByUsuarioRuc();
        //La comision es con respecto al cliente que creo la solicitud
        Double comision = TasOnUtil.roundDouble((oferta.getOfertaValor() * clienteSolicitud.getClienteComision())/100 , 2);
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
        if(solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.SUBASTA_INVERSA_VALOR_OBJETIVO ||
                (clienteSolicitud.getClienteNotaCredito() != null &&  clienteSolicitud.getClienteNotaCredito() == 1)){
            Double comisionExtra = TasOnUtil.getComisionExtraSubastaValorObjetivo(solicitudEnvio.getSolicitudEnvioValorObjetivo(),
                    clienteSolicitud.getClienteComision(), oferta.getOfertaValor());
            comision = TasOnUtil.roundDouble(comision+comisionExtra,2);
        }
        if(solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.PRECIO_FIJO){
            comision = TasOnUtil.roundDouble(solicitudEnvio.getSolicitudEnvioValorObjetivo() - oferta.getOfertaValor(),2);
        }
        //La retencion y el iva es con respecto al usuario que crea la oferta
        Double retencion = TasOnUtil.roundDouble((oferta.getOfertaValor() * clienteOferta.getClienteRetencion()) / 100, 2);
        Double iva = TasOnUtil.roundDouble((oferta.getOfertaValor() * clienteOferta.getClienteIva()) / 100, 2);
        oferta.setOfertaRetencion(retencion);
        oferta.setOfertaIva(iva);
        oferta.setOfertaComision(comision);
    }

    protected void updateOffer(Offer offer, Map<String, String> response, Usuario usuario, Exchange exchange) throws TasOnException{
        try {
            Oferta oferta = ofertaService.read(offer.getIdOferta());
            if(!usuario.getClienteByUsuarioRuc().getClienteRuc().equals(oferta.getUsuarioByOfertaIdConductor().getClienteByUsuarioRuc().getClienteRuc())
            && !usuario.getUsuarioTipoUsuario().equals(Constant.USER_ACCOUNTANT) && !usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN)){
                logger.error("Usuario {} intenta realizar accion no autorizada", usuario.getUsuarioIdDocumento());
                throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                        Constant.RESPONSE_ERROR, "Error al actualizar la oferta");
            }
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
            offer.setIdSolicitud(oferta.getOfertaIdSolicitud());
            switch (oferta.getOfertaEstado()) {
                case Constant.OFERTA_ASSIGN:
                    if(offer.getIdConductor() == 0 || offer.getIdVehiculo() == 0){
                        throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                                Constant.RESPONSE_ERROR, "La oferta ya ha sido aceptada");
                    }
                    Cliente clienteCreadorSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
                    if(ServicesUtil.puedeSoloOfertarConductoresAcreditados(clienteCreadorSolicitud)){
                        if (conductorService.readConductorAcreditado(offer.getIdConductor(), clienteCreadorSolicitud.getClienteRuc()) == null)
                            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No puede seleccionar un conductor no acreditado por el cliente para esta solicitud");
                    }
                    if(ServicesUtil.puedeSoloOfertarVehiculosAcreditados(clienteCreadorSolicitud)){
                        if (vehiculoService.readVehiculoAcreditado(offer.getIdVehiculo(), clienteCreadorSolicitud.getClienteRuc()) == null)
                            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No puede seleccionar un vehículo no acreditado por el cliente para esta solicitud");
                    }
                    oferta.setOfertaIdConductor(offer.getIdConductor());
                    oferta.setOfertaIdVehiculo(offer.getIdVehiculo());
                    solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_DISPATCH);
                    solicitudEnvioService.update(solicitudEnvio);
                    oferta.setOfertaEstado(Constant.OFERTA_RECEIVE);
                    //Datos para enviar correo al cliente con el conductor y vehiculo asignado
                    exchange.getIn().setHeader("template", Constant.EMAIL_ASIGNAR_CONDUCTOR_VEHICULO);
                    exchange.getIn().setHeader("idSolicitud", solicitudEnvio.getSolicitudEnvioId());
                    exchange.getIn().setHeader("idConductor", offer.getIdConductor());
                    exchange.getIn().setHeader("idVehiculo", offer.getIdVehiculo());
                    break;
                case Constant.OFERTA_CREATED:
                    Date date = new Date();
                    if (offer.getState() == Constant.OFERTA_CANCEL) {
                        if(Duration.between(oferta.getOfertaFecha().toLocalDateTime(), LocalDateTime.now()).toMinutes() <  30 ){
                            ofertaService.delete(oferta);
                            return;
                        } else {
                            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                                    Constant.RESPONSE_ERROR, "No puede cancelar una oferta después de haber transcurrido 30 minutos desde su creación");
                        }
                    }else {
                        if(!precioOfertaValido(offer.getAmount(), solicitudEnvioService.read(oferta.getOfertaIdSolicitud()))){
                            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                                    Constant.RESPONSE_ERROR, "Su oferta debe ser más competitiva frente a la mejor oferta");
                        }
                        oferta.setOfertaValor(TasOnUtil.roundDouble(offer.getAmount(), 2));
                        setRetIVAComOfferValues(oferta, solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId(), usuarioService.readUsuario(oferta.getOfertaIdUsuario()));
                        oferta.setOfertaObservacion(offer.getComments());
                        oferta.setOfertaFecha(new Timestamp(date.getTime()));
                    }
                    break;
                case Constant.OFERTA_RECEIVE:
                    guardarFotos(offer.getFotos(), Constant.ESTADO_FOTO_POR_RECIBIR, oferta);
                    solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_DELIVERY_PROCESS);
                    solicitudEnvioService.update(solicitudEnvio);
                    oferta.setOfertaEstado(Constant.OFERTA_DELIVER);
                    oferta.setOfertaFechaRecepcion(new Timestamp(new Date().getTime()));
                    break;
                case Constant.OFERTA_DELIVER:
                    guardarFotos(offer.getFotos(), Constant.ESTADO_FOTO_POR_ENTREGAR, oferta);
                    solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_DELIVER);
                    Cliente cliente = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
                    solicitudEnvio.setSolicitudEnvioFechaFacturacion(getFechaFacturacion(cliente.getClienteDiasPeriodicidad()));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(solicitudEnvio.getSolicitudEnvioFechaFacturacion().getTime());
                    calendar.add(Calendar.DATE, cliente.getClienteDiasCredito());
                    solicitudEnvio.setSolicitudEnvioFechaPago(new Timestamp(calendar.getTimeInMillis()));
                    solicitudEnvioService.update(solicitudEnvio);
                    oferta.setOfertaEstado(Constant.OFERTA_GENERATE_BILL);
                    oferta.setOfertaFechaEntrega(new Timestamp(new Date().getTime()));
                    crearCalificacionTransportista(oferta.getOfertaId());
                    calendar.add(Calendar.DATE, diasGestion);
                    String fechaPago = simpleDateShortFormat.format(new Timestamp(calendar.getTimeInMillis()));
                    response.put("fechaPago", fechaPago);
                    response.put("responseMessage", mensajeEntregaFacturas.replace("#fechaPago", fechaPago));
                    break;
                case Constant.OFERTA_TRANSIT_BILL:
                    oferta.setOfertaNroTransferencial(offer.getNroTransferencia());
                    oferta.setOfertaFechaFinalizada(new Timestamp(new Date().getTime()));
                    oferta.setOfertaEstado(Constant.OFERTA_FINISH);
                    break;
            }
            ofertaService.update(oferta);
        } catch (TasOnException t){
            throw t;
        } catch (Exception e){
            logger.error("Error actualizando oferta");
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), "Error en la actualización de la oferta", e);
        }
    }

    private void guardarFotos(List<Documents> fotos, int tipoFoto, Oferta oferta) throws TasOnException{
        if(fotos == null || fotos.isEmpty()){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Debe enviar al menos una foto para actualizar la oferta");
        }
        for(Documents archivo : fotos){
            ec.net.redcode.tas.on.persistence.entities.File foto = new ec.net.redcode.tas.on.persistence.entities.File();
            foto.setFileContent(archivo.getFile());
            foto.setFileName(archivo.getFileName());
            foto.setFileSize(archivo.getFileSize());
            foto.setFileType(archivo.getFileType());
            foto.setFileUploadDate(new Timestamp(new Date().getTime()));
            fileService.create(foto);

            OfertaFile ofertaFile = new OfertaFile();
            ofertaFile.setOfertaFileFileId(foto.getFileId());
            ofertaFile.setOfertaFileOfertaId(oferta.getOfertaId());
            ofertaFile.setOfertaFileTipo(tipoFoto);
            ofertaFileService.create(ofertaFile);
        }
    }

    private void crearCalificacionTransportista(int ofertaId){
        CalificacionTransportista calificacionTransportista = new CalificacionTransportista();
        calificacionTransportista.setCalificacionTransportistaOfertaId(ofertaId);
        calificacionTransportista.setCalificacionTransportistaValor(5);
        calificacionTransportista.setCalificacionTransportistaComentario("");
        calificacionTransportista.setCalificacionTransportistaFechaCalificacion(new Timestamp(new Date().getTime()));
        calificacionTransportistaService.create(calificacionTransportista);
    }

    protected List<Offers> getOfertasByEstadoAndUser(int estado, Usuario usuario){
        List<Oferta> ofertas;
        if (estado == Constant.OFERTA_TO_PAY)
            ofertas = ofertaService.getOfertasPorCobrar(usuario.getUsuarioIdDocumento());
        else
            ofertas = ofertaService.getOfertasByUsuarioAndEstado(usuario.getUsuarioIdDocumento(), estado);
        return ofertas.stream().map(oferta -> externalToOffers.apply(oferta, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<Offers> getOfertasByEstado(int estado, Usuario usuario){
        List<Oferta> ofertas = ofertaService.getOfertasyEstado(estado);
        return ofertas.stream().map(oferta -> externalToOffers.apply(oferta, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<Offers> getOfertasBy(int estado, int tipoFactura, String razonSocial, String solicitud, String facturaNro,
                                     Date fechaDesde, Date fechaHasta){
        return ofertaService.getOfertasBy(estado, tipoFactura, razonSocial, solicitud, facturaNro,
                fechaDesde, fechaHasta);
    }

    protected List<Offers> getOfertasBySolicitud(String solicitud, int estado, Usuario usuario){
        List<Oferta> ofertas = ofertaService.getOfertasBySolicitudAndEstado(solicitud, estado);
        return ofertas.stream().map( oferta -> externalToOffers.apply(oferta, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<String> getDocumentOferta(int idOferta, int tipo){
        Oferta oferta = ofertaService.read(idOferta);
        return oferta.getFotos().stream()
                .filter(x -> x.getOfertaFileTipo() == tipo)
                .map(x -> x.getFoto().getFileContent())
                .collect(Collectors.toList());
    }

    protected ec.net.redcode.tas.on.persistence.entities.File getFoto(int fotoId, Usuario usuarioConsulta){
        ec.net.redcode.tas.on.persistence.entities.File foto = fileService.read(fotoId);
        Oferta oferta = foto.getOfertaFileByFileId().getOfertaByFoto();
        SolicitudEnvio solicitudEnvio = oferta.getSolicitudEnvioByOfertaIdSolicitud();
        if(TasOnUtil.esUsuarioAdmin(usuarioConsulta.getUsuarioTipoUsuario()) ||
                TasOnUtil.esUsuarioContador(usuarioConsulta.getUsuarioTipoUsuario()) ||
                usuarioConsulta.getUsuarioRuc().equals(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc()) ||
                usuarioConsulta.getUsuarioRuc().equals(oferta.getUsuarioByOfertaIdConductor().getUsuarioRuc())){
            return foto;
        }
        return new ec.net.redcode.tas.on.persistence.entities.File();
    }

    protected Solicitud getOferta(int idOferta){
        Oferta oferta = ofertaService.read(idOferta);
        return externalToSolicitud.apply(oferta);
    }

    protected void updateOfertaList(List<Offer> offers, Usuario usuario){
        if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_ACCOUNTANT) || usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN)){
            for (Offer offer: offers){
                Oferta oferta = ofertaService.read(offer.getIdOferta());
                oferta.setOfertaEstado(Constant.OFERTA_TRANSIT_BILL);
                ofertaService.update(oferta);
            }
        } else {
            logger.error("Usuario {} intenta realizar accion no autorizada", usuario.getUsuarioIdDocumento());
        }
    }

    //TODO obtener todos estos valores de una sola consulta a la base de datos
    private BiFunction<Oferta, Usuario, Offers> externalToOffers = (oferta, usuarioConsulta) -> {
        Offers offers = new Offers();
        offers.setIdOferta(oferta.getOfertaId());
        offers.setIdSolicitud(oferta.getOfertaIdSolicitud());
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
        Localidad localidadOrigen = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen();
        Localidad localidadDestino = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino();
        offers.setOrigen(localidadOrigen.getLocalidadDescripcion());
        offers.setDestino(localidadDestino.getLocalidadDescripcion());
        offers.setProvinciaOrigen(localidadService.readLocalidad(localidadOrigen.getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        offers.setProvinciaDestino(localidadService.readLocalidad(localidadDestino.getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        offers.setNumeroPiezas(solicitudEnvio.getSolicitudEnvioNumeroPiesas());
        offers.setPeso(solicitudEnvio.getSolicitudEnvioPeso());
        offers.setTipoPeso(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadPeso().getCatalogoItemDescripcion());
        Date date = new Date();
        date.setTime(oferta.getOfertaFecha().getTime());
        offers.setDate(simpleDateFormat.format(date));
        date.setTime(oferta.getOfertaFechaCancelada() != null ? oferta.getOfertaFechaCancelada().getTime() : new Date().getTime());
        offers.setDateCanceled(simpleDateFormat.format(date));
        offers.setComments(oferta.getOfertaObservacion());
        offers.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());

        if(TasOnUtil.esUsuarioCliente(usuarioConsulta.getUsuarioTipoUsuario())){
            offers.setAmount(TasOnUtil.roundDouble(oferta.getOfertaValor()+oferta.getOfertaComision(), 2));
            offers.setComision(0D);
        } else if(TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
            offers.setAmount(TasOnUtil.roundDouble(oferta.getOfertaValor(), 2));
            offers.setComision(0D);
        } else {
            offers.setAmount(TasOnUtil.roundDouble(oferta.getOfertaValor(), 2));
            offers.setComision(oferta.getOfertaComision());
        }

        offers.setValorObjetivo(solicitudEnvio.getSolicitudEnvioValorObjetivo());
        if (oferta.getOfertaEstado() == Constant.OFERTA_TO_PAY || oferta.getOfertaEstado() == Constant.OFERTA_TRANSIT_BILL
                || oferta.getOfertaEstado() == Constant.OFERTA_FINISH || oferta.getOfertaEstado() == Constant.OFERTA_READY_TO_PAY){
            FacturaDetalle facturaDetalle = facturaDetalleService.getFacturaDetalleByOfertaId(oferta.getOfertaId(), Constant.USER_DRIVER);
            offers.setDescuento(oferta.getOfertaDescuento() != null ? oferta.getOfertaDescuento() : 0.0);
            if (facturaDetalle != null) {
                Factura factura = facturaService.read(facturaDetalle.getFacturaDetalleFacturaId());
                CatalogoItem catalogoItem = catalogoItemService.read(factura.getFacturaTipoPago());
                offers.setInvoicesTypePay(factura.getFacturaTipoPago());
                offers.setTypePay(catalogoItem.getCatalogoItemDescripcion());
                offers.setInvoiceId(factura.getFacturaNro());
                FacturaDetalle facturaDetalleCliente = facturaDetalleService.getFacturaDetalleByOfertaId(oferta.getOfertaId(), Constant.USER_CLIENT);
                Factura facturaCliente = facturaDetalleCliente != null ? facturaService.read(facturaDetalleCliente.getFacturaDetalleFacturaId()) : null;
                offers.setInvoiceClientId(facturaCliente != null ? facturaCliente.getFacturaNro() : "");
                Usuario usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
                Cliente cliente = usuario.getClienteByUsuarioRuc();
                offers.setSupplier(cliente.getClienteRazonSocial());
                offers.setNumeroPrefactura(facturaDetalle.getFacturaDetalleFacturaId());
                offers.setFechaPago(facturaService.fechaPagoFacturaOferta(facturaDetalle.getFacturaDetalleFacturaId(), offers.getIdOferta()));
                //TODO: tengo que revisar si afecta los dias de gestion administrativa
                Timestamp fechaPago = solicitudEnvio.getSolicitudEnvioFechaPago();
                if (oferta.getOfertaEstado() == Constant.OFERTA_TO_PAY || oferta.getOfertaEstado() == Constant.OFERTA_READY_TO_PAY) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(fechaPago.getTime());
                    calendar.add(Calendar.DATE, diasGestion);
                    fechaPago = new Timestamp(calendar.getTimeInMillis());
                }

                if(oferta.getOfertaEstado() == Constant.OFERTA_TO_PAY  ||
                        oferta.getOfertaEstado() == Constant.OFERTA_READY_TO_PAY ||
                        oferta.getOfertaEstado() == Constant.OFERTA_TRANSIT_BILL ||
                        oferta.getOfertaEstado() == Constant.OFERTA_FINISH){
                    double impuestos = oferta.getOfertaRetencion() + oferta.getOfertaIva();
                    offers.setAmount(TasOnUtil.roundDouble(oferta.getOfertaValor() - impuestos, 2));
                }
                offers.setDatePay(fechaPago);
                offers.setExpiredDay(TasOnUtil.getExpiredDays(fechaPago));
            }
        }
        if (oferta.getOfertaEstado() == Constant.OFERTA_CREATED) {
            Usuario usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
            Cliente cliente = clienteService.readCliente(usuario.getUsuarioRuc());
            offers.setSupplier(cliente.getClienteRazonSocial());
        }
        //Si esta lista para el pago y la factura no ha sido recibida no la devuelve
        if(oferta.getOfertaEstado() == Constant.OFERTA_READY_TO_PAY){
            if(offers.getInvoiceId() == null || offers.getInvoiceId().isEmpty()) return null;
        }
        if (Constant.OFERTA_GENERATE_BILL == oferta.getOfertaEstado()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(solicitudEnvio.getSolicitudEnvioFechaPago().getTime());
            calendar.add(Calendar.DATE, diasGestion);
            Date fechaPago = new Date(calendar.getTimeInMillis());
            int dias = (int) ((fechaPago.getTime() - date.getTime()) / 86400000) + 1;
            double valor = oferta.getOfertaValor() * dias * (prontoPago /100);
            if(valor<0)valor=0;
            offers.setDescuento(valor);
        }
        return offers;
    };

    private Function<Oferta, Solicitud> externalToSolicitud = oferta -> {
        Solicitud solicitud = new Solicitud();
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(oferta.getOfertaIdSolicitud());
        solicitud.setIdSolicitud(solicitudEnvio.getSolicitudEnvioId());
        solicitud.setIdOrigen(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
        solicitud.setOrigen(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion());
        solicitud.setIdDestino(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
        solicitud.setDestino(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion());
        solicitud.setFechaEnvio(solicitudEnvio.getSolicitudEnvioFechaRecoleccion());
        solicitud.setDireccion(solicitudEnvio.getSolicitudEnvioDireccionOrigen());
        solicitud.setTipo("");
        if(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId() != null)
            solicitud.setTipo(obtenerDescripcionTipoProducto(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc()));
        solicitud.setNumeroPiezas(solicitudEnvio.getSolicitudEnvioNumeroPiesas());
        solicitud.setVolumen(solicitudEnvio.getSolicitudEnvioVolumen());
        solicitud.setTipoVolumen(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadVolumen().getCatalogoItemDescripcion());
        solicitud.setPeso(solicitudEnvio.getSolicitudEnvioPeso());
        solicitud.setTipoPeso(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadPeso().getCatalogoItemDescripcion());
        solicitud.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());
        solicitud.setDireccionEntrega(solicitudEnvio.getSolicitudEnvioDireccionDestino());
        solicitud.setNumeroEstibadores(solicitudEnvio.getSolicitudEnvioNumeroEstibadores());
        solicitud.setPersonaEntrega(solicitudEnvio.getSolicitudEnvioPersonaEntrega());
        solicitud.setPersonaRecibe(solicitudEnvio.getSolicitudEnvioPersonaRecibe());
        List<Oferta> ofertas = ofertaService.getOfertasBySolicitudAndEstado(solicitudEnvio.getSolicitudEnvioId(), Constant.OFERTA_CREATED);
        if (ofertas.size() == 0){
            solicitud.setBestOffer(null);
        } else {
            Double offerValue = ofertas.get(0).getOfertaValor();
            Double offerComision = ofertas.get(0).getOfertaComision();
            solicitud.setBestOffer(TasOnUtil.roundDouble(offerValue+offerComision, 2));
        }
        Double totalValueOffers = 0.0;
        for(Oferta o : ofertas){
            totalValueOffers += o.getOfertaValor() + o.getOfertaComision();
        }
        solicitud.setAverageOffer(ofertas.size() != 0 ? TasOnUtil.roundDouble(totalValueOffers/ofertas.size() , 2) : null);
        solicitud.setTotalOffer(ofertas.size());
        Offer offer = new Offer();
        offer.setIdSolicitud(solicitudEnvio.getSolicitudEnvioId());
        offer.setComments(oferta.getOfertaObservacion());
        offer.setAmount(TasOnUtil.roundDouble(oferta.getOfertaValor(), 2));
        offer.setIdOferta(oferta.getOfertaId());
        offer.setDate(oferta.getOfertaFecha());
        offer.setIdConductor(oferta.getOfertaIdConductor());
        offer.setIdVehiculo(oferta.getOfertaIdVehiculo());
        offer.setState(oferta.getOfertaEstado());
        offer.setComision(oferta.getOfertaComision());
        solicitud.setOffer(offer);
        return solicitud;
    };

    private String obtenerDescripcionTipoProducto(String clienteRUC){
        if(clienteRUC == null || clienteRUC.isEmpty()) return "";
        Cliente cliente = clienteService.readCliente(clienteRUC);
        if(cliente == null || cliente.getClienteTipoProducto() == null) return "";
        CatalogoItem catalogoItem = catalogoItemService.read(cliente.getClienteTipoProducto());
        if(catalogoItem == null) return "";
        return(catalogoItem.getCatalogoItemDescripcion());
    }

    protected File generateCashFile(List<Offer> offerList, Usuario usuario) throws TasOnException{
        if(!usuario.getUsuarioTipoUsuario().equals(Constant.USER_ACCOUNTANT) && !usuario.getUsuarioTipoUsuario().equals(Constant.USER_ADMIN)) {
            logger.error("Usuario {} intenta realizar accion no autorizada", usuario.getUsuarioIdDocumento());
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No se pudo generar el archivo");
        }

        Cliente tason = clienteService.readCliente(Constant.RUC_TASON);

        String path = getPathCashManagement();
        new File(path);
        try {
            String[] columns = {
                    "TIPO: PAGOS",
                    "NUMERO DE CUENTA DE EMPRESA",
                    "NUMERO SECUENCIAL",
                    "NUMERO DE COMPROBANTE DE PAGO",
                    "CONTRAPARTIDA CODIGO DE BENEFICIARIO Y/O EMPLEADO",
                    "MONEDA",
                    "VALOR A PAGAR",
                    "FORMA DE PAGO",
                    "CODIGO DE BANCO",
                    "TIPO DE CUENTA",
                    "NUMERO DE CUENTA",
                    "TIPO DE DOCUMENTO DE BENEFICIARIO Y/O EMPLEADO",
                    "NUMERO DE CEDULA DE  BENEFICIARIO Y/O EMPLEADO",
                    "NOMBRES DE  BENEFICIARIO Y/O EMPLEADO",
                    "DIRECCION  BENEFICIARIO Y/O EMPLEADO",
                    "CIUDAD BENEFICIARIO Y/O EMPLEADO",
                    "TELEFONO BENEFICIARIO Y/O EMPLEADO",
                    "LOCALIDAD DE COBRO",
                    "REFERENCIA",
                    "REFERENCIA ADICIONAL"
            };
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Pagos");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create Other rows and cells with contacts data
            int contador = 0;
            for (Offer offer : offerList) {
                Oferta oferta = ofertaService.read(offer.getIdOferta());
                FacturaDetalle facturaDetalle = facturaDetalleService.getFacturaDetalleByOfertaId(oferta.getOfertaId(), Constant.USER_DRIVER);
                Factura factura = facturaDetalle.getFacturasByFacturaId();
                Usuario usuarioOferta = oferta.getUsuarioByOfertaIdConductor();
                Cliente clienteOferta = usuarioOferta.getClienteByUsuarioRuc();
                Localidad localidadCliente = clienteOferta.getLocalidadByClienteLocalidad();
                CatalogoItem banco = clienteOferta.getCatalogoItemByClienteBanco();
                contador++;
                Row row = sheet.createRow(contador);
                row.createCell(0).setCellValue("PA");//TIPO: PAGOS
                row.createCell(1).setCellValue(tason.getClienteCuenta());//NUMERO DE CUENTA DE EMPRESA
                row.createCell(2).setCellValue(contador);//NUMERO SECUENCIAL
                row.createCell(3).setCellValue(factura != null ? factura.getFacturaNro() : "");//NUMERO DE COMPROBANTE DE PAGO
                String codigoBeneficiario = !TasOnUtil.isStringNullOrEmpty(clienteOferta.getClienteBancoIdentificacion()) ? clienteOferta.getClienteBancoIdentificacion() : clienteOferta.getClienteRuc();
                row.createCell(4).setCellValue(codigoBeneficiario);//CONTRAPARTIDA CODIGO DE BENEFICIARIO Y/O EMPLEADO
                row.createCell(5).setCellValue("USD");//MONEDA
                double ofertaValor = oferta.getOfertaValor() == null ? 0 : oferta.getOfertaValor();
                double ofertaRetencion = oferta.getOfertaRetencion() == null ? 0 : oferta.getOfertaRetencion();
                double ofertaIva = oferta.getOfertaIva() == null ? 0 : oferta.getOfertaIva();
                double ofertaImpuestos = TasOnUtil.roundDouble(ofertaRetencion+ofertaIva, 2);
                double ofertaDescuento = oferta.getOfertaDescuento() == null ? 0 : oferta.getOfertaDescuento();
                row.createCell(6).setCellValue(transformDecimalsCashMFormat(ofertaValor-ofertaDescuento-ofertaImpuestos));//VALOR A PAGAR
                row.createCell(7).setCellValue("CTA");//FORMA DE PAGO
                row.createCell(8).setCellValue(banco.getCatalogoItemValor());//CODIGO DE BANCO
                row.createCell(9).setCellValue(clienteOferta.getClienteTipoCuenta() == Constant.CLIENTE_CUENTA_AHORROS ? "AHO" :
                        clienteOferta.getClienteTipoCuenta() == Constant.CLIENTE_CUENTA_CORRIENTE ? "CTE" : "");//TIPO DE CUENTA
                row.createCell(10).setCellValue(clienteOferta.getClienteCuenta());//NUMERO DE CUENTA
                row.createCell(11).setCellValue(codigoBeneficiario.length() == 13 ? "R" : codigoBeneficiario.length() == 10 ? "C" : "");//TIPO DE DOCUMENTO DE BENEFICIARIO Y/O EMPLEADO
                row.createCell(12).setCellValue(codigoBeneficiario);//NUMERO DE CEDULA DE  BENEFICIARIO Y/O EMPLEADO
                String nombreBeneficiario = !TasOnUtil.isStringNullOrEmpty(clienteOferta.getClienteBancoNombres()) ? removeSpecialCharacters(clienteOferta.getClienteBancoNombres()) : removeSpecialCharacters(clienteOferta.getClienteRazonSocial());
                row.createCell(13).setCellValue(nombreBeneficiario.length() > 40 ? nombreBeneficiario.substring(40) : nombreBeneficiario);//NOMBRES DE  BENEFICIARIO Y/O EMPLEADO
                row.createCell(14).setCellValue(removeSpecialCharacters(clienteOferta.getClienteDireccion()));//DIRECCION  BENEFICIARIO Y/O EMPLEADO
                row.createCell(15).setCellValue(removeSpecialCharacters(localidadCliente.getLocalidadDescripcion()));//CIUDAD BENEFICIARIO Y/O EMPLEADO
                row.createCell(16).setCellValue("");//TELEFONO BENEFICIARIO Y/O EMPLEADO
                row.createCell(17).setCellValue("");//LOCALIDAD DE COBRO
                row.createCell(18).setCellValue("Pago de la oferta " + oferta.getOfertaIdSolicitud());//REFERENCIA
                row.createCell(19).setCellValue("");//REFERENCIA ADICIONAL
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            return new File(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String transformDecimalsCashMFormat(Double valor){
        if(valor == null) return "0";
        DecimalFormat formateador = new DecimalFormat("#.00");
        char decSep = formateador.getDecimalFormatSymbols().getDecimalSeparator();
        return formateador.format(valor).replace(Character.toString(decSep), "");
    }

    private String removeSpecialCharacters(String string){
        if(string == null) return "";
        string = string.toUpperCase();
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        string = string.replaceAll("[^A-Z0-9 ]+"," ");
        string = string.replaceAll(" +"," ");
        return string;
    }

    private String getPathCashManagement(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String nombreArchivo = formatter.format(localDateTime);
        return TasOnUtil.getComprobantesCashManagementPath()+ File.separator + nombreArchivo + ".xlsx";
    }

}
