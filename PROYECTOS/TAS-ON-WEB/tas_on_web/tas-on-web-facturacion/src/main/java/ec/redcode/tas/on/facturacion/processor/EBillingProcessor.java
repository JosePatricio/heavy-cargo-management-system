package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.gob.sri.comprobantes.modelo.factura.Impuesto;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.CatalogoItemDTO;
import ec.net.redcode.tas.on.common.dto.DetalleFactura;
import ec.net.redcode.tas.on.common.dto.EBillingRequest;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EBillingProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(EBillingProcessor.class);

    private Date now;
    private String ambiente;
    private String establecimiento;
    private String emision;
    private String puntoEmision;
    private String documento;
    private String secuencia;
    private String signature;

    @Setter private UsuarioService usuarioService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private LocalidadService localidadService;
    @Setter private UsuarioEbillingService usuarioEbillingService;
    @Setter private EbillingService ebillingService;
    @Setter private EbillingDetalleService ebillingDetalleService;
    @Setter private ClienteService clienteService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
    private SimpleDateFormat dateFormatFechaEmision = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void process(Exchange exchange) {
        now = new Date();
        EBillingRequest eBillingRequest = exchange.getIn().getBody(EBillingRequest.class);

        String idUsuario = exchange.getIn().getHeader("user").toString();
        Usuario usuarioTasOn  = usuarioService.getUsuariosByUserName(idUsuario);
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = createFactura(eBillingRequest, usuarioTasOn);
        int ebillingId = guardarEbilling(factura, eBillingRequest);
        exchange.getIn().setBody(factura);
        exchange.getIn().setHeader("ebilling", ebillingId);
        exchange.getIn().setHeader("claveAcceso", factura.getInfoTributaria().getClaveAcceso());
        exchange.getIn().setHeader("tipo", "factura");
        exchange.getIn().setHeader("signature", signature);
        exchange.getIn().setHeader("claveFirma", eBillingRequest.getClaveFirma());
        exchange.getIn().setHeader("adquiriente", eBillingRequest.getAdquiriente().getAdquirienteRazonSocial());
    }

    private ec.gob.sri.comprobantes.modelo.factura.Factura createFactura(EBillingRequest eBillingRequest, Usuario usuarioTasOn){
        UsuarioEbilling usuarioEbilling = usuarioEbillingService.read(usuarioTasOn.getUsuarioRuc());
        Cliente cliente = clienteService.readCliente(usuarioTasOn.getUsuarioRuc());
        eBillingRequest.setUsuarioEbillingID(usuarioEbilling.getUsuarioEbillingId());
        signature = usuarioEbilling.getUsuarioEbillingFirma();
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = new ec.gob.sri.comprobantes.modelo.factura.Factura();
        List<CatalogoItem> catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_FACTURACION);
        setValoresFacturacion(catalogoItems, usuarioEbilling);
        actualizarValorSecuencia(usuarioEbilling);
        InfoTributaria infoTributaria = new InfoTributaria();
        Factura.InfoFactura infoFactura = new Factura.InfoFactura();

        llenarInfoTributaria(infoTributaria, cliente, usuarioEbilling);
        factura.setInfoTributaria(infoTributaria);
        factura.setId(Constant.INVOICE_ID);
        factura.setVersion(catalogoItems.stream().filter(c -> "VERSION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());

        Map<String, Object> totales = getDetalle(eBillingRequest);
        List<Factura.Detalles.Detalle> detallesList = (List<Factura.Detalles.Detalle>) totales.get("detalleList");
        Factura.Detalles detalles = new Factura.Detalles();
        detalles.getDetalle().addAll(detallesList);
        factura.setDetalles(detalles);

        llenarInfoFactura(infoFactura, eBillingRequest, new BigDecimal((double)totales.get("total")),
                new BigDecimal((double)totales.get("totalDescuento")));
        factura.setInfoFactura(infoFactura);
        return factura;
    }

    private Map<String, Object> getDetalle(EBillingRequest eBillingRequest){
        List<Factura.Detalles.Detalle> detalleList = new ArrayList<>();
        Factura.Detalles.Detalle detalle;
        double totalSinImpuesto = 0.0;
        double totalDescuento = 0.0;
        Impuesto impuesto;
        Factura.Detalles.Detalle.Impuestos impuestos;
        List<Impuesto> impuestoList;
        for(DetalleFactura detallleFactura : eBillingRequest.getDetalles()) {
            detalle = new Factura.Detalles.Detalle();
            detalle.setCantidad(new BigDecimal(1));
            detalle.setPrecioUnitario(new BigDecimal(detallleFactura.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP));
            detalle.setDescuento(new BigDecimal(detallleFactura.getDescuento()).setScale(2, RoundingMode.HALF_UP));
            double precio = detallleFactura.getPrecioUnitario() - detallleFactura.getDescuento();
            detalle.setPrecioTotalSinImpuesto(new BigDecimal(precio).setScale(2, RoundingMode.HALF_UP));
            totalSinImpuesto += precio;
            totalDescuento += detallleFactura.getDescuento()!= null ? detallleFactura.getDescuento() : 0;
            Localidad localidadOrigen = new Localidad(), localidadDestino = new Localidad();
            if(detallleFactura.getCiudadOrigen() != null && detallleFactura.getCiudadOrigen().getLocalidadId() != 0){
                localidadOrigen = localidadService.readLocalidad(detallleFactura.getCiudadOrigen().getLocalidadId());
            }
            if(detallleFactura.getCiudadDestino() != null && detallleFactura.getCiudadDestino().getLocalidadId() != 0){
                localidadDestino = localidadService.readLocalidad(detallleFactura.getCiudadDestino().getLocalidadId());
            }
            detalle.setDescripcion(generateDescripcionFactura(localidadOrigen.getLocalidadDescripcion(), localidadDestino.getLocalidadDescripcion(), detallleFactura));
            detalle.setCodigoPrincipal(generateCodigoPrincipal(localidadOrigen.getLocalidadDescripcion(), localidadDestino.getLocalidadDescripcion()));
            detalle.setCodigoAuxiliar(generateCodigoAuxiliar(localidadOrigen.getLocalidadId(), localidadDestino.getLocalidadId()));
            Factura.Detalles.Detalle.DetallesAdicionales detallesAdicionales = new Factura.Detalles.Detalle.DetallesAdicionales();
            Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional detAdicional = new Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional();
            detAdicional.setNombre("Detalle adicional");
            detAdicional.setValor(detallleFactura.getDetallesAdicionales() != null && !detallleFactura.getDetallesAdicionales().isEmpty() ? detallleFactura.getDetallesAdicionales() : "NA");
            List<Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional> detAdicionals = new ArrayList<>();
            detAdicionals.add(detAdicional);
            detallesAdicionales.getDetAdicional().addAll(detAdicionals);
            detalle.setDetallesAdicionales(detallesAdicionales);

            impuesto = new Impuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje("6");
            impuesto.setTarifa(new BigDecimal(0));
            impuesto.setValor(new BigDecimal(0));
            impuesto.setBaseImponible(new BigDecimal(precio).setScale(2, RoundingMode.HALF_UP));
            impuestos = new Factura.Detalles.Detalle.Impuestos();
            impuestoList = new ArrayList<>();
            impuestoList.add(impuesto);
            impuestos.getImpuesto().addAll(impuestoList);
            detalle.setImpuestos(impuestos);
            detalleList.add(detalle);
        }
        Map<String, Object> response = new ConcurrentHashMap<>();
        response.put("total", totalSinImpuesto);
        response.put("totalDescuento", totalDescuento);
        response.put("detalleList", detalleList);
        return response;
    }

    private void llenarInfoTributaria(InfoTributaria infoTributaria, Cliente cliente, UsuarioEbilling usuarioEbilling){
        infoTributaria.setAmbiente(ambiente);
        infoTributaria.setTipoEmision(emision);
        infoTributaria.setRazonSocial(cliente.getClienteRazonSocial());
        infoTributaria.setNombreComercial(cliente.getClienteRazonSocial());
        infoTributaria.setRuc(usuarioEbilling.getUsuarioEbillingId());
        infoTributaria.setCodDoc(documento);
        infoTributaria.setEstab(establecimiento);
        infoTributaria.setPtoEmi(puntoEmision);
        infoTributaria.setDirMatriz(cliente.getClienteDireccion());
        infoTributaria.setSecuencial(secuencia);
        String claveAcceso = generateClaveAcceso(now, infoTributaria.getRuc(), secuencia);
        infoTributaria.setClaveAcceso(claveAcceso);
    }

    private void llenarInfoFactura(Factura.InfoFactura infoFactura, EBillingRequest eBillingRequest, BigDecimal total, BigDecimal totalDescuento){
        infoFactura.setFechaEmision(dateFormatFechaEmision.format(now));
        infoFactura.setRazonSocialComprador(eBillingRequest.getAdquiriente().getAdquirienteRazonSocial());
        infoFactura.setIdentificacionComprador(eBillingRequest.getAdquiriente().getAdquirienteIdDocumento());
        infoFactura.setDireccionComprador(eBillingRequest.getAdquiriente().getAdquirienteDireccion());
        //infoFactura.setDirEstablecimiento(); DIRECCION DE QUIEN CREA LA FACTURA
        infoFactura.setTipoIdentificacionComprador(infoFactura.getIdentificacionComprador().length() == 13 ? "04" : "05");
        infoFactura.setTotalSinImpuestos(total.setScale(2, RoundingMode.HALF_UP));
        infoFactura.setTotalDescuento(totalDescuento.setScale(2, RoundingMode.HALF_UP));
        if(eBillingRequest.getGuiaRemision() != null && !eBillingRequest.getGuiaRemision().isEmpty())
            infoFactura.setGuiaRemision(eBillingRequest.getGuiaRemision());

        Factura.InfoFactura.TotalConImpuestos totalConImpuestos = new Factura.InfoFactura.TotalConImpuestos();
        List<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto> totalConImpuestosList = new ArrayList<>();
        Factura.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
        totalImpuesto.setCodigo("2");
        totalImpuesto.setCodigoPorcentaje("6");
        totalImpuesto.setBaseImponible(total.setScale(2, RoundingMode.HALF_UP));
        totalImpuesto.setValor(new BigDecimal(0.0));
        totalConImpuestosList.add(totalImpuesto);
        totalConImpuestos.getTotalImpuesto().addAll(totalConImpuestosList);
        infoFactura.setTotalConImpuestos(totalConImpuestos);

        infoFactura.setPropina(new BigDecimal(0.0));
        infoFactura.setImporteTotal(total.setScale(2, RoundingMode.HALF_UP));
        infoFactura.setMoneda("DOLAR");

        Factura.InfoFactura.Pago pagos = new Factura.InfoFactura.Pago();
        List<Factura.InfoFactura.Pago.DetallePago> pagoList = new ArrayList<>();
        Factura.InfoFactura.Pago.DetallePago pago = new Factura.InfoFactura.Pago.DetallePago();

        CatalogoItem formaPago = eBillingRequest.getFormaPago()!=null?catalogoItemService.read(eBillingRequest.getFormaPago()):null;
        pago.setFormaPago(formaPago!=null ? formaPago.getCatalogoItemValor() : "01");
        pago.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        pago.setPlazo(String.valueOf(eBillingRequest.getDiasPlazo()));
        pago.setUnidadTiempo("dias");
        pagoList.add(pago);
        pagos.getPagos().addAll(pagoList);

        infoFactura.setPagos(pagos);
    }

    private String generateClaveAcceso(Date now, String ruc, String secuencial){
        String fecha = dateFormat.format(now);
        StringBuilder clave = new StringBuilder(fecha);
        clave.append(documento);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(establecimiento);
        clave.append(puntoEmision);
        clave.append(secuencial);
        clave.append(Constant.INVOICE_CODIGO_NUMERO); //TODO REPRESENTA AL CODIGO NUMERICO CONSULTAR SI PUEDE SER CUALQUIER COSA
        clave.append(emision);
        int digitoVerificador = generateDigitoModulo11(clave.toString());
        clave.append(digitoVerificador);
        return clave.toString();
    }

    private String generateCodigoPrincipal(String origen, String destino){
        if(origen == null) origen = "";
        if(destino == null) destino = "";
        if(origen.isEmpty() && destino.isEmpty()) return "";
        origen = TasOnUtil.removeSpecialCharacters(origen);
        destino = TasOnUtil.removeSpecialCharacters(destino);
        if(origen.length()>11) origen = origen.substring(11);
        if(destino.length()>11) destino = destino.substring(11);
        return origen.concat("-").concat(destino);
    }

    private String generateCodigoAuxiliar(int origenId, int destinoId){
        return String.valueOf(origenId).concat("-").concat(String.valueOf(destinoId));
    }

    private String generateDescripcionFactura(String origen, String destino, DetalleFactura detallleFactura){
        if(origen == null) origen = "";
        if(destino == null) destino = "";
        String detalle = "Transporte de mercaderia";
        String transportado = "";
        CatalogoItemDTO unidadTransportado = detallleFactura.getUnidadPiezas();
        if(!origen.isEmpty() && !destino.isEmpty()){
            detalle = "Tranporte de mercaderia desde ".concat(origen).concat(" hasta ").concat(destino);
        }
        if(!origen.isEmpty() && destino.isEmpty()){
            detalle = "Tranporte de mercaderia desde ".concat(origen);
        }
        if(origen.isEmpty() && !destino.isEmpty()){
            detalle = "Tranporte de mercaderia hasta ".concat(origen);
        }
        if(detallleFactura.getNumeroPiezas() != null && unidadTransportado == null){
            transportado = "(".concat(detallleFactura.getNumeroPiezas().toString().concat(" piezas")).concat(")");
        }
        if(detallleFactura.getNumeroPiezas() != null && unidadTransportado != null &&
                unidadTransportado.getCatalogoItemDescripcion() != null && !unidadTransportado.getCatalogoItemDescripcion().isEmpty()){
            transportado = "(".concat(detallleFactura.getNumeroPiezas().toString().concat(" ").
                    concat(unidadTransportado.getCatalogoItemDescripcion())).concat(")");
        }
        return detalle.concat(" ").concat(transportado);
    }

    private int generateDigitoModulo11(String cadena) {
        int baseMultiplicador = 7;
        int[] aux = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt("" + cadena.charAt(i));
            aux[i] *= multiplicador;
            multiplicador++;
            if (multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }
        if ((total == 0) || (total == 1)) {
            verificador = 0;
        } else {
            verificador = 11 - total % 11 == 11 ? 0 : 11 - total % 11;
        }
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }

    private void setValoresFacturacion(List<CatalogoItem> catalogoItems, UsuarioEbilling usuarioEbilling){
        ambiente = catalogoItems.stream().filter(c -> "AMBIENTE".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        establecimiento = usuarioEbilling.getUsuarioEbillingEstablecimiento();
        puntoEmision = usuarioEbilling.getUsuarioEbillingPuntoEmision();
        emision = catalogoItems.stream().filter(c -> "EMISION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        documento = catalogoItems.stream().filter(c -> "DOCUMENTO".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
    }

    private void actualizarValorSecuencia(UsuarioEbilling usuarioEbilling){
        int numeroSecuencia = usuarioEbilling.getUsuarioEbillingSecuencia();
        secuencia = TasOnUtil.getDigitsToSecuence(numeroSecuencia) + numeroSecuencia;
        usuarioEbilling.setUsuarioEbillingSecuencia(++numeroSecuencia);
        usuarioEbillingService.update(usuarioEbilling);
    }

    private int guardarEbilling(ec.gob.sri.comprobantes.modelo.factura.Factura factura, EBillingRequest eBillingRequest){
        Ebilling ebilling = new Ebilling();
        ebilling.setEbillingNumero(establecimiento+"-"+puntoEmision+"-"+secuencia);
        ebilling.setEbillingUsuarioEbilling(eBillingRequest.getUsuarioEbillingID());
        ebilling.setEbillingUsuarioId(eBillingRequest.getIdUsuario());
        ebilling.setEbillingFechaEmision(new Timestamp(now.getTime()));
        ebilling.setEbillingClaveAcceso(factura.getInfoTributaria().getClaveAcceso());
        ebilling.setEbillingEstado("ENVIADA");
        ebilling.setEbillingGuiaRemision(eBillingRequest.getGuiaRemision());
        ebilling.setEbillingAdquiriente(eBillingRequest.getAdquiriente().getAdquirienteIdDocumento());

        double subtotal = 0.0;
        double descuento = 0.0;
        List<EbillingDetalle> ebillingDetalleList =  new ArrayList<>();
        for(DetalleFactura detalle : eBillingRequest.getDetalles()){
            if(detalle.getDescuento() == null) detalle.setDescuento(0.0);
            EbillingDetalle ebillingDetalle = new EbillingDetalle();
            ebillingDetalle.setEbillingDetallePiezas(detalle.getNumeroPiezas());
            ebillingDetalle.setEbillingDetalleUnidadPiezas(detalle.getUnidadPiezas() != null ? detalle.getUnidadPiezas().getCatalogoItemId() : null);
            if(detalle.getCiudadOrigen()!=null) ebillingDetalle.setEbillingDetalleOrigen(detalle.getCiudadOrigen().getLocalidadId());
            if(detalle.getCiudadDestino()!=null) ebillingDetalle.setEbillingDetalleDestino(detalle.getCiudadDestino().getLocalidadId());
            ebillingDetalle.setEbillingDetalleDetallesAdicionales(detalle.getDetallesAdicionales());
            ebillingDetalle.setEbillingDetallePrecio(detalle.getPrecioUnitario());
            ebillingDetalle.setEbillingDetalleDescuento(detalle.getDescuento());
            ebillingDetalleList.add(ebillingDetalle);
            subtotal += detalle.getPrecioUnitario();
            descuento += detalle.getDescuento();
        }

        ebilling.setEbillingSubtotal(subtotal);
        ebilling.setEbillingDescuento(descuento);
        ebilling.setEbillingTotal(subtotal-descuento);
        ebilling.setEbillingTipoPago(eBillingRequest.getFormaPago());

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Factura.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(factura, stringWriter);
            ebilling.setEbillingXml(stringWriter.toString());
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        ebillingService.create(ebilling);

        ebillingDetalleList.forEach(e -> {
            e.setEbillingDetalleEbillingId(ebilling.getEbillingId());
            ebillingDetalleService.create(e);
        });
        return ebilling.getEbillingId();
    }

}
