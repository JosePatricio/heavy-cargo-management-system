package ec.net.redcode.tas.on.notifications.processor;

import ec.net.redcode.tas.on.common.CipherHash;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j
public class NotificationProcesor implements Processor {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Setter private UsuarioService usuarioService;
    @Setter private ClienteService clienteService;
    @Setter private FacturaService facturaService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private EbillingService ebillingService;
    @Setter private OfertaService ofertaService;
    @Setter private ConductorService conductorService;
    @Setter private VehiculoService vehiculoService;
    @Setter private CalificacionTransportistaService calificacionTransportistaService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> data;
        String template;

        if(exchange.getIn().getBody(Map.class) == null){
            data = new HashMap<>();
            template = (String) exchange.getIn().getHeader("template");
            data.put("template", template);
        }else{
            data = exchange.getIn().getBody(Map.class);
            template = data.get("template");
        }

        if(TasOnUtil.isStringNullOrEmpty(template)){
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
            return;
        }

        if (Constant.EMAIL_FACTURA.equals(template) || Constant.EMAIL_RETENCION.equals(template)) {
            if ("facturaProveedor".equals(data.get("tipo"))){
                Cliente cliente = clienteService.readCliente(data.get("ruc"));
                data.put("email", cliente.getClienteCorreo());
                data.put("empresa", cliente.getClienteRazonSocial());
            } else {
                String ruc = data.get("ruc");
                List<Usuario> usuarioList = usuarioService.getUsuarioByRuc(ruc);
                for (Usuario u : usuarioList) {
                    if (u.getUsuarioPrincipal()) {
                        data.put("email", u.getUsuarioMail());
                    }
                }
                Cliente cliente = clienteService.readCliente(ruc);
                data.put("empresa", cliente.getClienteRazonSocial());
            }
            data.put("copy", catalogoItemService.read(Constant.CATALOGO_MAIL_CONTADOR).getCatalogoItemValor());
        } else if (Constant.EMAIL_CANCEL.equals(template)) {
            System.out.println("no se que hay que hacer");
        } else if (Constant.EMAIL_CANCEL_JOB.equals(template)) {
            System.out.println("no se que hay que hacer");
        } else if (Constant.EMAIL_PRONTO_PAGO.equals(template)){
            String nroFactura = data.get("nroFactura");
            Factura factura = facturaService.read(nroFactura);
            Cliente cliente = clienteService.readCliente(factura.getFacturaEmpresa());
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.DATE, 1);
            data.put("email", catalogoItemService.read(261).getCatalogoItemValor());
            data.put("fechaPago", simpleDateFormat.format(calendar.getTime()));
            data.put("valor", String.valueOf(new BigDecimal(factura.getFacturaTotal()).setScale(2, BigDecimal.ROUND_UP)));
            data.put("transportista", cliente.getClienteRazonSocial());
            data.put("cuenta", cliente.getClienteCuenta());
            data.put("tipoCuenta", catalogoItemService.read(cliente.getClienteTipoCuenta()).getCatalogoItemDescripcion());
            List<FacturaDetalle> facturaDetalles = facturaDetalleService.getFacturaDetalleByFactura(nroFactura);
            StringBuilder stringBuilder = new StringBuilder();
            SolicitudEnvio solicitudEnvio;
            for (FacturaDetalle facturaDetalle: facturaDetalles){
                solicitudEnvio = solicitudEnvioService.getSolicitudEnvioByOferta(facturaDetalle.getFacturaDetalleOfertaId());
                stringBuilder.append(solicitudEnvio.getSolicitudEnvioId());
                stringBuilder.append(", ");
            }
            data.put("solicitudes", stringBuilder.toString());
            data.put("subject","Nueva Factura de Pronto Recibida $".concat(String.valueOf(new BigDecimal(factura.getFacturaTotal()).setScale(2, BigDecimal.ROUND_UP))));
        } else if (Constant.EMAIL_EBILLING.equals(template)){
            Integer ebillingId = (Integer) exchange.getIn().getHeader("ebilling");
            Ebilling ebilling = ebillingService.read(ebillingId);
            String correos = data.get("correos");
            data.put("email", correos);
            data.put("pdf", ebilling.getEbillingRide());
            data.put("xml", ebilling.getEbillingXml());
            data.put("nombreArchivo", ebilling.getEbillingClaveAcceso());

        } else if(Constant.EMAIL_PAGO_OFERTA.equals(template)){
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read((String) exchange.getIn().getHeader("solicitudPagadaId"));
            Oferta oferta = ofertaService.read(solicitudEnvio.getSolicitudEnvioOfertaId());
            Usuario usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
            List<Usuario> usuarios = usuarioService.getUsuariosByEmpresaTipoUsuario(usuario.getUsuarioRuc(), Constant.USER_DRIVER_ADMIN, Constant.USER_ACTIVE);
            Usuario usuarioOferta = usuarios.get(0);
            Cliente cliente = clienteService.readCliente(usuarioOferta.getUsuarioRuc());
            data.put("email", usuarioOferta.getUsuarioMail());
            data.put("valor", String.format("%.2f", oferta.getOfertaValor()));
            data.put("cliente", cliente.getClienteRazonSocial());
            data.put("cantidadCajas", solicitudEnvio.getSolicitudEnvioNumeroPiesas().toString());
            data.put("origen", solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion());
            data.put("destino", solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion());
            data.put("solicitudId", solicitudEnvio.getSolicitudEnvioId());
        } else if(Constant.EMAIL_NOTA_CREDITO.equals(template)){
            String ruc = data.get("ruc");
            List<Usuario> usuarioList = usuarioService.getUsuarioByRuc(ruc);
            usuarioList.stream().filter(Usuario::getUsuarioPrincipal).forEach(u -> data.put("email", u.getUsuarioMail()));
            Cliente cliente = clienteService.readCliente(ruc);
            data.put("empresa", cliente.getClienteRazonSocial());
            data.put("copy", catalogoItemService.read(Constant.CATALOGO_MAIL_CONTADOR).getCatalogoItemValor());
        } else if(Constant.EMAIL_ASIGNAR_CONDUCTOR_VEHICULO.equals(template)) {
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read((String) exchange.getIn().getHeader("idSolicitud"));
            Integer idConductor = (Integer) exchange.getIn().getHeader("idConductor");
            Integer idVehiculo = (Integer) exchange.getIn().getHeader("idVehiculo");
            Conductor conductor = conductorService.read(idConductor);
            Vehiculo vehiculo = vehiculoService.read(idVehiculo);
            Cliente cliente = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
            Cliente transportista = conductor.getConductorByConductorUsuario().getClienteByUsuarioRuc();
            //Envia correo al usuario principal
            String mailCliente = cliente.getUsuariosByclienteRuc().stream()
                    .filter(Usuario::getUsuarioPrincipal)
                    .map(Usuario::getUsuarioMail)
                    .findFirst().orElse("technology@tas-on.com");
            String mailTransportista = transportista.getUsuariosByclienteRuc().stream()
                    .filter(Usuario::getUsuarioPrincipal)
                    .map(Usuario::getUsuarioMail)
                    .findFirst().orElse("technology@tas-on.com");
            data.put("email", mailCliente.concat(", ").concat(mailTransportista));
            String nombresConductor = (conductor.getConductorNombre().concat(" ").concat(conductor.getConductorApellido())).toUpperCase();
            data.put("nombresConductor", nombresConductor);
            data.put("cedulaConductor", conductor.getConductorCedula());
            data.put("telefonoConductor", conductor.getConductorTelefono());
            data.put("tipoLicenciaConductor", conductor.getConductorByConductorTipoLicencia().getCatalogoItemDescripcion());
            String capacidad = vehiculo.getVehiculoCapacidad() + " " + vehiculo.getCatalogoItemByVehiculoTipoCapacidad().getCatalogoItemDescripcion();
            data.put("modeloVehiculo", vehiculo.getVehiculoModelo());
            data.put("anioVehiculo", String.valueOf(vehiculo.getVehiculoAnio()));
            data.put("placaVehiculo", vehiculo.getVehiculoPlaca());
            data.put("capacidadVehiculo", capacidad);
            Map<String, Number> calificacionConductor = calificacionTransportistaService.getResultadoCalificacionesTransportista(conductor.getConductorId());
            int fletesRealizados = TasOnUtil.getIntFromObject(calificacionConductor.get("fletes"));
            data.put("fletesConductor", fletesRealizados > 0 ? fletesRealizados+" fletes realizados" : null) ;
            Double calificacion = TasOnUtil.roundDouble(TasOnUtil.getDoubleFromObject(calificacionConductor.get("calificacion")), 2);
            data.put("calificacionConductor", fletesRealizados > 0 ? "Puntaje: "+calificacion+"/5" : null);
            data.put("solicitudId", solicitudEnvio.getSolicitudEnvioId());
            data.put("solicitudNumDoc", solicitudEnvio.getSolicitudEnvioNumeroDocCliente() != null && !solicitudEnvio.getSolicitudEnvioNumeroDocCliente().isEmpty()? "Docs: ".concat(solicitudEnvio.getSolicitudEnvioNumeroDocCliente()) : null);
            data.put("origen", solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion());
            data.put("destino", solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
            data.put("fechaRecoleccion", dateTimeFormatter.format(solicitudEnvio.getSolicitudEnvioFechaRecoleccion().toLocalDateTime()));
            data.put("fechaEntrega", dateTimeFormatter.format(solicitudEnvio.getSolicitudEnvioFechaEntrega().toLocalDateTime()));

            List<String> certificaciones = new ArrayList<>();
            if(vehiculo.getVehiculoArcsaAlimentos()) certificaciones.add("ARCSA alimentos");
            if(vehiculo.getVehiculoArcsaCosmeticos()) certificaciones.add("ARCSA cosméticos");
            if(vehiculo.getVehiculoArcsaMedicamentos()) certificaciones.add("ARCSA medicamentos");
            if(vehiculo.getVehiculoBasc()) certificaciones.add("BASC");
            if(vehiculo.getVehiculoPaseInternacional()) certificaciones.add("Pase internacional");
            String certicados = "";
            for(String certificacion : certificaciones){
                if(TasOnUtil.isStringNullOrEmpty(certicados)) certicados += certificacion;
                else certicados += ", ".concat(certificacion);
            }

            data.put("certificacionARCSA", certicados);
            data.put("cliente", cliente.getClienteRazonSocial());
            data.put("transportista", transportista.getClienteRazonSocial());
            data.put("producto", catalogoItemService.read(cliente.getClienteTipoProducto()).getCatalogoItemDescripcion());
            data.put("imagenConductor", "https://www.tas-on.com/assets/images/conductor.png");
            data.put("imagenVehiculo", "https://www.tas-on.com/assets/images/vehiculo.png");
        } else {
            String idDocumento = data.get("idDocumento");
            Usuario usuario = usuarioService.readUsuario(idDocumento);
            if (usuario != null) {
                data.put("usuario", usuario.getUsuarioNombreUsuario());
                if (Constant.EMAIL_NUEVO.equals(template) || Constant.EMAIL_RESTABLECER.equals(template) ||
                        Constant.EMAIL_DESBLOQUEO.equals(template)) {
                    String password = getSaltString();
                    data.put("password", password);
                    CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
                    usuario.setUsuarioEstado(Constant.USER_CREATED);
                    usuario.setUsuarioContrasenia(cipherHash.hashString(password));
                    usuarioService.updateUsuario(usuario);
                }
                data.put("email", usuario.getUsuarioMail());
                data.put("nombres", usuario.getUsuarioNombres());
                data.put("apellidos", usuario.getUsuarioApellidos());
            }
        }
        exchange.getIn().setBody(data);
    }

    private String getSaltString() {
        String SALTCHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#$_.";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8 ) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
