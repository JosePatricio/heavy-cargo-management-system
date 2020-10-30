package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.notacredito.Impuesto;
import ec.gob.sri.comprobantes.modelo.notacredito.TotalConImpuestos;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class NotaCreditoProcessor extends BaseFacturaProcessor implements Processor {

    @Setter private OfertaService ofertaService;
    @Setter private NotaCreditoService notaCreditoService;
    @Setter private FacturaDetalleService facturaDetalleService;
    @Setter private FacturaService facturaService;

    private SimpleDateFormat dateFormatFechaEmision = new SimpleDateFormat(SRIEnum.FORMATO_FECHA.getValue());

    private static final Logger logger = LoggerFactory.getLogger(NotaCreditoProcessor.class);

    @Override
    public void process(Exchange exchange) {
        logger.info("Empieza revision de notas de credito pendientes por emitir");
        List<ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito> notaCreditoList = new ArrayList<>();
        List<NotaCredito> notasCreditoPorEmitir = notaCreditoService.getNCPendientesEmitir();
        for(NotaCredito nota : notasCreditoPorEmitir){
            notaCreditoList.add(crearNotaCreadito(nota));
        }
        logger.info("Termina la creacion de {} notas de credito", notaCreditoList.size());
        exchange.getIn().setBody(notaCreditoList);
    }

    private ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito crearNotaCreadito(NotaCredito nota){
        ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito notaCredito = new ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito();
        Date now = new Date();
        Factura factura = facturaService.read(nota.getNotaCreditoFacturaId());
        List<SolicitudEnvio> solicitudEnvioList = obtenerSolicitudesEnvio(nota.getNotaCreditoFacturaId());
        double total = solicitudEnvioList.stream()
                .mapToDouble(SolicitudEnvioCommon::getSolicitudEnvioAhorroValorObjetivo)
                .sum();
        BigDecimal totalNC = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);

        notaCredito.setId(Constant.INVOICE_ID);
        notaCredito.setVersion(catalogoItems.stream().filter(c -> "VERSION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        notaCredito.setInfoTributaria(llenarInfoTributaria(now));
        notaCredito.setInfoNotaCredito(llenarInfoNotaCredito(now,
                clienteService.readCliente(factura.getFacturaEmpresa()), factura, totalNC));
        notaCredito.setDetalles(llenarDetalles(solicitudEnvioList));
        notaCredito.setInfoAdicional(llenarInfoAdicional(notaCredito));
        return notaCredito;
    }

    private List<SolicitudEnvio> obtenerSolicitudesEnvio(String facturaId){
        //1. Obtengo el detalle de la factura para revisar todas las ofertas facturadas
        List<FacturaDetalle> facturaDetalleList = facturaDetalleService.getFacturaDetalleByFactura(facturaId);
        //2. Lleno una lista con todas las ofertas
        List<Oferta> ofertaList = facturaDetalleList.stream()
                .map(facturaDetalle -> ofertaService.read(facturaDetalle.getFacturaDetalleOfertaId()))
                .collect(Collectors.toList());
        //3. Obtengo todas las solicitudes validas que se hicieron
        return ofertaList.stream()
                .map(Oferta::getSolicitudEnvioByOfertaIdSolicitud)
                .filter(solicitudEnvio -> solicitudEnvio.getSolicitudEnvioAhorroValorObjetivo() != null &&
                        solicitudEnvio.getSolicitudEnvioAhorroValorObjetivo() > 0)
                .collect(Collectors.toList());
    }

    private InfoTributaria llenarInfoTributaria(Date now){
        InfoTributaria infoTributaria = getInfoTributaria(SRIEnum.COMPROBANTE_NOTA_CREDITO.getValue());
        String secuencia = getSecuencial(SRIEnum.COMPROBANTE_NOTA_CREDITO);
        infoTributaria.setSecuencial(secuencia);
        String claveAcceso = generateClaveAcceso(now, infoTributaria.getRuc(), secuencia, SRIEnum.COMPROBANTE_NOTA_CREDITO.getValue());
        infoTributaria.setClaveAcceso(claveAcceso);
        return infoTributaria;
    }

    private InfoNotaCredito llenarInfoNotaCredito(Date now, Cliente cliente, Factura factura, BigDecimal total){
        InfoNotaCredito infoNotaCredito = new InfoNotaCredito();
        infoNotaCredito.setFechaEmision(dateFormatFechaEmision.format(now));
        //infoNotaCredito.setDirEstablecimiento(); DIRECCION DE QUIEN EMITE LA NOTA DE CREDITO
        infoNotaCredito.setTipoIdentificacionComprador(SRIEnum.IDENTIFICACION_RUC.getValue());
        infoNotaCredito.setRazonSocialComprador(cliente.getClienteRazonSocial());
        infoNotaCredito.setIdentificacionComprador(cliente.getClienteRuc());
        infoNotaCredito.setCodDocModificado(SRIEnum.COMPROBANTE_FACTURA.getValue());
        infoNotaCredito.setNumDocModificado(factura.getFacturaId());
        infoNotaCredito.setFechaEmisionDocSustento(dateFormatFechaEmision.format(factura.getFacturaFechaEmision()));
        infoNotaCredito.setTotalSinImpuestos(total);
        infoNotaCredito.setValorModificacion(total);
        infoNotaCredito.setMoneda(SRIEnum.MONEDA.getValue());
        infoNotaCredito.setTotalConImpuestos(llenarTotalConImpuestos(total));
        infoNotaCredito.setMotivo("DESCUENTO");
        infoNotaCredito.setObligadoContabilidad("SI");
        return infoNotaCredito;
    }

    private TotalConImpuestos llenarTotalConImpuestos(BigDecimal total){
        TotalConImpuestos totalConImpuestos = new TotalConImpuestos();

        TotalConImpuestos.TotalImpuesto totalImpuesto = new TotalConImpuestos.TotalImpuesto();
        totalImpuesto.setCodigo(SRIEnum.IMPUESTO_IVA.getValue());
        totalImpuesto.setCodigoPorcentaje(SRIEnum.PORCENTAJE_IVA_NO_OBJETO_DE_IMPUESTO.getValue());
        totalImpuesto.setBaseImponible(total);
        totalImpuesto.setValor(BigDecimal.ZERO);

        totalConImpuestos.getTotalImpuesto().add(totalImpuesto);
        return totalConImpuestos;
    }

    private Detalles llenarDetalles(List<SolicitudEnvio> solicitudEnvioList){
        Detalles detalles = new Detalles();
        for(SolicitudEnvio solicitudEnvio : solicitudEnvioList){
            Detalles.Detalle detalle = new Detalles.Detalle();
            detalle.setCodigoAdicional(solicitudEnvio.getSolicitudEnvioOfertaId().toString());
            detalle.setCodigoInterno(solicitudEnvio.getSolicitudEnvioId());
            String origen = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion();
            String destino = solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion();
            detalle.setDescripcion("Descuento en transporte de mercadería pesada desde ".concat(origen).concat(" hasta ").concat(destino));
            detalle.setCantidad(BigDecimal.ONE);
            BigDecimal precioAhorro = new BigDecimal(solicitudEnvio.getSolicitudEnvioAhorroValorObjetivo()).setScale(2, RoundingMode.HALF_UP);
            detalle.setPrecioUnitario(precioAhorro);
            detalle.setDescuento(BigDecimal.ZERO);
            detalle.setPrecioTotalSinImpuesto(precioAhorro);
            detalle.setImpuestos(llenarImpuestos(precioAhorro));
            detalles.getDetalle().add(detalle);
        }
        return detalles;
    }

    private Detalles.Detalle.Impuestos llenarImpuestos(BigDecimal precio){
        Detalles.Detalle.Impuestos impuestos = new Detalles.Detalle.Impuestos();
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo(SRIEnum.IMPUESTO_IVA.getValue());
        impuesto.setCodigoPorcentaje(SRIEnum.PORCENTAJE_IVA_NO_OBJETO_DE_IMPUESTO.getValue());
        impuesto.setTarifa(BigDecimal.ZERO);
        impuesto.setBaseImponible(precio);
        impuesto.setValor(BigDecimal.ZERO);
        impuestos.getImpuesto().add(impuesto);
        return impuestos;
    }

    private InfoAdicional llenarInfoAdicional(ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito notaCredito){
        InfoAdicional infoAdicional = new InfoAdicional();
        InfoAdicional.CampoAdicional campoAdicional = new InfoAdicional.CampoAdicional();
        String notaCreditoNro = notaCredito.getInfoTributaria().getEstab().concat("-").concat(notaCredito.getInfoTributaria().getPtoEmi().concat("-").concat(notaCredito.getInfoTributaria().getSecuencial()));
        campoAdicional.setNombre("Nota credito Nro");
        campoAdicional.setValue(notaCreditoNro);
        infoAdicional.getCampoAdicional().add(campoAdicional);
        return infoAdicional;
    }

}
