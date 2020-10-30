package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle;
import ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura.TotalConImpuestos;
import ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura.TotalConImpuestos.TotalImpuesto;
import ec.gob.sri.comprobantes.modelo.factura.Impuesto;
import ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura.Pago;
import ec.gob.sri.comprobantes.modelo.factura.Factura.InfoFactura.Pago.DetallePago;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle.Impuestos;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle.DetallesAdicionales;
import ec.gob.sri.comprobantes.modelo.factura.Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional;

public class FacturasProcessor extends BaseFacturaProcessor implements Processor {

    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private OfertaService ofertaService;

    private SimpleDateFormat dateFormatFechaEmision = new SimpleDateFormat(SRIEnum.FORMATO_FECHA.getValue());

    private static final Logger logger = LoggerFactory.getLogger(FacturasProcessor.class);

    @Override
    public void process(Exchange exchange) {
        logger.info("Starting facturacion");
        List<Cliente> clientes = clienteService.getCienteByTipo(Constant.EMPRESA_CLIENTE);
        List<ec.gob.sri.comprobantes.modelo.factura.Factura> facturas = new ArrayList<>();
        ec.gob.sri.comprobantes.modelo.factura.Factura factura;
        Integer maximoDiaFacturacion;
        for (Cliente cliente: clientes){
            int periocidad = cliente.getClienteDiasPeriodicidad();
            maximoDiaFacturacion = cliente.getClienteMaxDiaFacturacion();
            factura = null;
            switch (periocidad){
                case Constant.DIARIA:
                    if(maximoDiaFacturacion == null || LocalDate.now().getDayOfMonth() <= maximoDiaFacturacion){
                        factura = createFactura(cliente);
                    }
                    break;
                case Constant.SEMANAL:
                    int diaFacturacionSemana = cliente.getClienteDiaFacturacion() != null ?
                            cliente.getClienteDiaFacturacion() : 7;
                    if(diaFacturacionSemana<1 || diaFacturacionSemana>7) diaFacturacionSemana = 7;
                    DayOfWeek diaFacturacionSemanaEnum = DayOfWeek.of(diaFacturacionSemana);

                    if (LocalDate.now().getDayOfWeek() == diaFacturacionSemanaEnum){
                        if(maximoDiaFacturacion == null || LocalDate.now().getDayOfMonth() <= maximoDiaFacturacion)
                            factura = createFactura(cliente);
                    }
                    break;
                case Constant.QUINCENAL:
                    if (LocalDate.now().getDayOfMonth() == 15 || LocalDate.now().getDayOfMonth() == LocalDate.now().lengthOfMonth()){
                        factura = createFactura(cliente);
                    }
                    break;
                case Constant.MENSUAL:
                    int diaFacturacionMes = cliente.getClienteDiaFacturacion() != null ?
                            cliente.getClienteDiaFacturacion() : LocalDate.now().lengthOfMonth();
                    if(diaFacturacionMes > LocalDate.now().lengthOfMonth()) diaFacturacionMes = LocalDate.now().lengthOfMonth();

                    if (LocalDate.now().getDayOfMonth() == diaFacturacionMes){
                        factura = createFactura(cliente);
                    }
                    break;
            }

            if (factura != null) facturas.add(factura);

        }
        logger.info("Finish creating {} facturas", facturas.size());
        exchange.getIn().setBody(facturas);
    }

    private ec.gob.sri.comprobantes.modelo.factura.Factura createFactura(Cliente cliente){
        List<SolicitudEnvio> solicitudEnvios = solicitudEnvioService.getSolicitudEnvioPorFacturarse(cliente.getClienteRuc());
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = null;
        Date now = new Date();
        Double minValorFacturacion = cliente.getClienteMinFacturacion();
        if (solicitudEnvios.size() != 0) {
            Map<String, Object> totales = getDetalle(solicitudEnvios, cliente);
            if (minValorFacturacion == null || (Double) totales.get("total") >= minValorFacturacion) {
                BigDecimal total = new BigDecimal((Double) totales.get("total"));
                total = total.setScale(2, RoundingMode.HALF_UP);
                factura = new ec.gob.sri.comprobantes.modelo.factura.Factura();
                factura.setId(Constant.INVOICE_ID);
                factura.setVersion(catalogoItems.stream().filter(c -> "VERSION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
                factura.setInfoTributaria(llenarInfoTributaria(now));
                factura.setDetalles(llenarDetalles(totales));
                factura.setInfoFactura(llenarInfoFactura(now, total, cliente));
            } else {
                for (SolicitudEnvio solicitudEnvio: solicitudEnvios) {
                    actualizarFechaFacturacionSolicitudEnvio(solicitudEnvio);
                }
            }
        }
        return factura;
    }

    private InfoTributaria llenarInfoTributaria(Date now){
        InfoTributaria infoTributaria = getInfoTributaria(SRIEnum.COMPROBANTE_FACTURA.getValue());
        String secuencia = getSecuencial(SRIEnum.COMPROBANTE_FACTURA);
        infoTributaria.setSecuencial(secuencia);
        String claveAcceso = generateClaveAcceso(now, infoTributaria.getRuc(), secuencia, SRIEnum.COMPROBANTE_FACTURA.getValue());
        infoTributaria.setClaveAcceso(claveAcceso);
        return infoTributaria;
    }

    private TotalConImpuestos llenarTotalConImpuestos(BigDecimal total){
        TotalConImpuestos totalConImpuestos = new TotalConImpuestos();
        List<TotalImpuesto> totalConImpuestosList = new ArrayList<>();

        TotalImpuesto totalImpuesto = new TotalImpuesto();
        totalImpuesto.setCodigo(SRIEnum.IMPUESTO_IVA.getValue());
        totalImpuesto.setCodigoPorcentaje(SRIEnum.PORCENTAJE_IVA_NO_OBJETO_DE_IMPUESTO.getValue());
        totalImpuesto.setBaseImponible(total);
        totalImpuesto.setValor(BigDecimal.ZERO);

        totalConImpuestosList.add(totalImpuesto);
        totalConImpuestos.getTotalImpuesto().addAll(totalConImpuestosList);
        return totalConImpuestos;
    }

    private InfoFactura llenarInfoFactura(Date now, BigDecimal total, Cliente cliente){
        InfoFactura infoFactura = new InfoFactura();
        infoFactura.setFechaEmision(dateFormatFechaEmision.format(now));
        infoFactura.setRazonSocialComprador(cliente.getClienteRazonSocial());
        infoFactura.setIdentificacionComprador(cliente.getClienteRuc());
        infoFactura.setDireccionComprador(cliente.getClienteDireccion());
        //infoFactura.setDirEstablecimiento(); DIRECCION DE QUIEN CREA LA FACTURA
        infoFactura.setTipoIdentificacionComprador(SRIEnum.IDENTIFICACION_RUC.getValue());
        infoFactura.setTotalSinImpuestos(total);
        infoFactura.setTotalDescuento(BigDecimal.ZERO);
        infoFactura.setTotalConImpuestos(llenarTotalConImpuestos(total));
        infoFactura.setPropina(BigDecimal.ZERO);
        infoFactura.setImporteTotal(total);
        infoFactura.setMoneda(SRIEnum.MONEDA.getValue());
        infoFactura.setPagos(llenarPagos(total, cliente.getClienteDiasCredito()));
        infoFactura.setObligadoContabilidad("SI");
        return infoFactura;
    }

    private Pago llenarPagos(BigDecimal total, int diasCredito){
        Pago pagos = new Pago();
        List<DetallePago> pagoList = new ArrayList<>();
        DetallePago pago = new DetallePago();
        pago.setTotal(total);
        pago.setFormaPago(SRIEnum.FORMA_PAGO_SIN_UTILIZACION_SISTEMA_FINANCIERO.getValue());
        pago.setPlazo(String.valueOf(diasCredito));
        pago.setUnidadTiempo(SRIEnum.UNIDAD_TIEMPO_DIAS.getValue());
        pagoList.add(pago);
        pagos.getPagos().addAll(pagoList);
        return pagos;
    }

    private Detalles llenarDetalles(Map<String, Object> totales){
        List<Detalle> detallesList = (List<Detalle>) totales.get("detalleList");
        Detalles detalles = new Detalles();
        detalles.getDetalle().addAll(detallesList);
        return detalles;
    }

    private Map<String, Object> getDetalle(List<SolicitudEnvio> solicitudEnvios, Cliente cliente){
        Oferta oferta;
        List<Detalle> detalleList = new ArrayList<>();
        double totalSinImpuesto = 0.0;
        double totalComision = 0.0;
        Double maxValorFacturacion = cliente.getClienteMaxFacturacion();

        boolean debeEmitirNotaCredito = BaseFacturaProcessor.debeEmitirNotaCredito(cliente);
        for (SolicitudEnvio solicitudEnvio: solicitudEnvios){
            oferta = ofertaService.read(solicitudEnvio.getSolicitudEnvioOfertaId());
            if(debeEmitirNotaCredito){
                totalSinImpuesto += solicitudEnvio.getSolicitudEnvioValorObjetivo();
            } else {
                totalSinImpuesto += oferta.getOfertaValor();
                totalComision += oferta.getOfertaComision();
            }
            if (maxValorFacturacion == null || (totalSinImpuesto + totalComision) <= maxValorFacturacion) {
                Detalle detalle = new Detalle();
                detalle.setCantidad(BigDecimal.ONE);
                double precioFlete;
                if(debeEmitirNotaCredito){
                    precioFlete = solicitudEnvio.getSolicitudEnvioValorObjetivo();
                } else {
                    precioFlete = oferta.getOfertaValor() + oferta.getOfertaComision();
                }
                BigDecimal precio = new BigDecimal(precioFlete).setScale(2, RoundingMode.HALF_UP);
                detalle.setPrecioUnitario(precio);
                detalle.setDescuento(BigDecimal.ZERO);
                detalle.setCodigoPrincipal(solicitudEnvio.getSolicitudEnvioId());
                detalle.setCodigoAuxiliar(String.valueOf(oferta.getOfertaId()));
                detalle.setPrecioTotalSinImpuesto(precio);
                String origen = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion();
                String destino = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion();
                detalle.setDescripcion("Tranporte de mercaderia pesada desde ".concat(origen).concat(" hasta ").concat(destino));

                DetallesAdicionales detallesAdicionales = new DetallesAdicionales();
                DetAdicional detAdicional = new DetAdicional();
                detAdicional.setNombre(String.valueOf(solicitudEnvio.getSolicitudEnvioNumeroPiesas()));
                detAdicional.setValor(String.valueOf(solicitudEnvio.getSolicitudEnvioNumeroPiesas()));
                List<DetAdicional> detAdicionals = new ArrayList<>();
                detAdicionals.add(detAdicional);
                detallesAdicionales.getDetAdicional().addAll(detAdicionals);
                detalle.setDetallesAdicionales(detallesAdicionales);
                detalle.setImpuestos(llenarImpuestos(precio));
                detalleList.add(detalle);
            } else {
                actualizarFechaFacturacionSolicitudEnvio(solicitudEnvio);
                if(debeEmitirNotaCredito){
                    totalSinImpuesto -= solicitudEnvio.getSolicitudEnvioValorObjetivo();
                } else {
                    totalSinImpuesto -= oferta.getOfertaValor();
                    totalComision -= oferta.getOfertaComision();
                }
            }
        }
        Map<String, Object> response = new ConcurrentHashMap<>();
        response.put("total", totalSinImpuesto + totalComision );
        response.put("detalleList", detalleList);
        return response;
    }

    private Impuestos llenarImpuestos(BigDecimal precio){
        Impuestos impuestos = new Impuestos();
        List<Impuesto> impuestoList = new ArrayList<>();
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo(SRIEnum.IMPUESTO_IVA.getValue());
        impuesto.setCodigoPorcentaje(SRIEnum.PORCENTAJE_IVA_NO_OBJETO_DE_IMPUESTO.getValue());
        impuesto.setTarifa(BigDecimal.ZERO);
        impuesto.setValor(BigDecimal.ZERO);
        impuesto.setBaseImponible(precio);
        impuestoList.add(impuesto);
        impuestos.getImpuesto().addAll(impuestoList);
        return impuestos;
    }

    private void actualizarFechaFacturacionSolicitudEnvio(SolicitudEnvio solicitudEnvio){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(solicitudEnvio.getSolicitudEnvioFechaFacturacion().getTime());
        calendar.add(Calendar.DATE, 1);
        solicitudEnvio.setSolicitudEnvioFechaFacturacion(new Timestamp(calendar.getTimeInMillis()));
        solicitudEnvioService.update(solicitudEnvio);
    }

}
