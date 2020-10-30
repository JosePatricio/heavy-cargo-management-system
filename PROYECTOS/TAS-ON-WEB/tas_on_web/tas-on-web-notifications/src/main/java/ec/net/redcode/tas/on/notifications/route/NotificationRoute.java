package ec.net.redcode.tas.on.notifications.route;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import lombok.Setter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.velocity.VelocityConstants;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRoute extends RouteBuilder {

    @Setter private CatalogoItemService catalogoItemService;
    private String host;
    private String port;
    private String protocol;
    private String username;
    private String password;
    private String pathPlantillas;
    private String pathFacturas;
    private String pathRetenciones;
    private String pathNotasCredito;
    private boolean starttls;
    private boolean debug;

    public void initNotificationRoute(){
        List<CatalogoItem> catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_EMAIL);
        host = catalogoItem.stream().filter(c -> Constant.EMAIL_HOST.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        port = catalogoItem.stream().filter(c -> Constant.EMAIL_PORT.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        protocol = catalogoItem.stream().filter(c -> Constant.EMAIL_PROTOCOL.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        username = catalogoItem.stream().filter(c -> Constant.EMAIL_USERNAME.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        password = catalogoItem.stream().filter(c -> Constant.EMAIL_PASSWORD.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        password = TasOnUtil.decrypt(password);
        debug = Boolean.valueOf(catalogoItem.stream().filter(c -> Constant.EMAIL_DEBUG.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        starttls = Boolean.valueOf(catalogoItem.stream().filter(c -> Constant.EMAIL_STARTTLS.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        pathPlantillas = TasOnUtil.getJbossPlantillasPath();
        pathFacturas = TasOnUtil.getComprobantesFacturasPath() + File.separator;
        pathRetenciones = TasOnUtil.getComprobantesRetencionesPath() + File.separator;
        pathNotasCredito = TasOnUtil.getComprobantesNotasCreditoPath() + File.separator;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:queue:ec.net.redcode.tason.NotificationJMS")
                .log("Notificacion Body ${body}")
                .to("notificationProcessor")
                .process(exchange -> {
                    Map<String, String> data = exchange.getIn().getBody(Map.class);
                    Map<String, Object> headers = new HashMap<>();
                    headers.put("to", data.get("email"));
                    headers.put("from", "TAS-ON < " + username + " > ");
                    headers.put("subject", Constant.EMAIL_SUBJECT);
                    String template = data.get("template");
                    switch (template){
                        case Constant.EMAIL_NUEVO:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_NUEVO);
                            break;
                        case Constant.EMAIL_RESTABLECER:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_RESTABLECER);
                            break;
                        case Constant.EMAIL_BLOQUEO:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_BLOQUEO);
                            break;
                        case Constant.EMAIL_DESBLOQUEO:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_DESBLOQUEO);
                            break;
                        case Constant.EMAIL_FACTURA:
                            String autorizacion = data.get("autorizacion");
                            headers.put("subject", "TAS-ON S.A. ha emitido un nuevo comprobante electr\u00f3nico: Factura");
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_FACTURA);
                            exchange.getIn().addAttachment(autorizacion.concat(".xml"), new DataHandler(new FileDataSource(pathFacturas.concat(autorizacion).concat(".xml"))));
                            exchange.getIn().addAttachment(autorizacion.concat(".pdf"), new DataHandler(new FileDataSource(pathFacturas.concat(autorizacion).concat(".pdf"))));
                            exchange.getIn().addAttachment("detalle_".concat(autorizacion).concat(".pdf"), new DataHandler(new FileDataSource(pathFacturas.concat("detalle_").concat(autorizacion).concat(".pdf"))));
                            break;
                        case Constant.EMAIL_RETENCION:
                            String auth = data.get("autorizacion");
                            headers.put("subject", "TAS-ON S.A. ha emitido un nuevo comprobante electr\u00f3nico: Retenci\u00f3n");
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_RETENCION);
                            headers.put("bcc", data.get("copy"));
                            exchange.getIn().addAttachment(auth.concat(".xml"), new DataHandler(new FileDataSource(pathRetenciones.concat(auth).concat(".xml"))));
                            exchange.getIn().addAttachment(auth.concat(".pdf"), new DataHandler(new FileDataSource(pathRetenciones.concat(auth).concat(".pdf"))));
                            break;
                        case Constant.EMAIL_CANCEL:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_CANCELAR);
                            break;
                        case Constant.EMAIL_CANCEL_JOB:
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_CANCELAR_JOB);
                            break;
                        case Constant.EMAIL_PRONTO_PAGO:
                            headers.put("subject", data.get("subject"));
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_PRONTO_PAGO);
                            break;
                        case Constant.EMAIL_EBILLING:
                            headers.put("subject", "TAS-ON S.A. ha emitido un nuevo comprobante electr\u00f3nico: Factura");
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_EBILLING);
                            String pdf = data.get("pdf");
                            String xml = data.get("xml");
                            String nombreArchivo = data.get("nombreArchivo");
                            exchange.getIn().addAttachment(nombreArchivo.concat(".xml"), new DataHandler(xml.getBytes(),"application/xml"));
                            exchange.getIn().addAttachment(nombreArchivo.concat(".pdf"), new DataHandler(Base64.getDecoder().decode(pdf),"application/pdf"));
                            break;
                        case Constant.EMAIL_PAGO_OFERTA:
                            headers.put("subject", "TAS-ON S.A. pago solicitud ".concat((String) exchange.getIn().getHeader("solicitudPagadaId")));
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_PAGO_OFERTA);
                            break;
                        case Constant.EMAIL_NOTA_CREDITO:
                            autorizacion = data.get("autorizacion");
                            headers.put("subject", "TAS-ON S.A. ha emitido un nuevo comprobante electr\u00f3nico: Nota de cr\u00E9dito");
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_NOTA_CREDITO);
                            exchange.getIn().addAttachment(autorizacion.concat(".xml"), new DataHandler(new FileDataSource(pathNotasCredito.concat(autorizacion).concat(".xml"))));
                            exchange.getIn().addAttachment(autorizacion.concat(".pdf"), new DataHandler(new FileDataSource(pathNotasCredito.concat(autorizacion).concat(".pdf"))));
                            break;
                        case Constant.EMAIL_ASIGNAR_CONDUCTOR_VEHICULO:
                            headers.put("subject", "TAS-ON S.A. Se ha seleccionado conductor y veh\u00EDculo para la solicitud ".concat(data.get("solicitudId")));
                            headers.put(VelocityConstants.VELOCITY_RESOURCE_URI, "file:/" + pathPlantillas + File.separator + Constant.EMAIL_PLANTILLA_ASIGNAR_CONDUCTOR_VEHICULO);
                            break;
                    }
                    exchange.getIn().setHeaders(headers);
                    exchange.getIn().setBody(data);
                })
                .to("velocity:template-in-header?encoding=UTF-8")
                .to(protocol + "://" + host + ":" + port + "?username=" + username + "&password=" + password + "&debugMode=" + debug + "&mail.smtp.starttls.enable=" + starttls + "&contentType=text/html");

    }

}
