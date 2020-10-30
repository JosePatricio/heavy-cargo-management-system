package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.notacredito.NotaCredito;
import ec.gob.sri.comprobantes.modelo.reportes.TotalesComprobante;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import ec.redcode.tas.on.facturacion.reporte.DetallesAdicionalesReporte;
import ec.redcode.tas.on.facturacion.reporte.FacturaReporte;
import ec.redcode.tas.on.facturacion.reporte.ReportUtilTasOn;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class RideNCWriterProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(RideNCWriterProcessor.class);
    @Setter private SolicitudEnvioService solicitudEnvioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        ReportUtilTasOn report = new ReportUtilTasOn();
        String numeroAutorizacion = (String) exchange.getIn().getHeader("numeroAutorizacion");
        String path = TasOnUtil.getComprobantesNotasCreditoPath() + File.separator + numeroAutorizacion + ".pdf";
        logger.info("Starting writing RIDE NC in {}", path);
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(NotaCredito.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        NotaCredito notaCredito = (NotaCredito) unmarshaller.unmarshal(sr);

        FacturaReporte notaCreditoReporte = new FacturaReporte(notaCredito, solicitudEnvioService);
        List<DetallesAdicionalesReporte> detallesAdicionalesReportes = new ArrayList<>();

        for (NotaCredito.Detalles.Detalle detalle: notaCredito.getDetalles().getDetalle()){
            DetallesAdicionalesReporte detalles = new DetallesAdicionalesReporte();
            detalles.setCantidad(String.valueOf(detalle.getCantidad()));
            detalles.setCodigoPrincipal(detalle.getCodigoInterno());
            detalles.setCodigoAuxiliar(detalle.getCodigoAdicional());
            detalles.setDescripcion(detalle.getDescripcion());
            detalles.setPrecioUnitario(detalle.getPrecioUnitario());
            detalles.setPrecioTotalSinImpuesto(String.valueOf(detalle.getPrecioTotalSinImpuesto()));
            SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(detalle.getCodigoInterno());
            detalles.setDetalle1(solicitudEnvio.getSolicitudEnvioNumeroPiesas().toString());
            detalles.setNumeroDocCliente(solicitudEnvio.getSolicitudEnvioNumeroDocCliente());
            detalles.setTotalesComprobante(getTotalesComprobante(notaCredito));
            detallesAdicionalesReportes.add(detalles);
        }

        notaCreditoReporte.setDetallesAdiciones(detallesAdicionalesReportes);
        XMLGregorianCalendar fechaAutorizacion = (XMLGregorianCalendar) exchange.getIn().getHeader("fechaAutorizacion");
        report.generateNCRide(path, notaCreditoReporte, detallesAdicionalesReportes,
                obtenerParametrosInfoTriobutaria(notaCreditoReporte.getNotaCredito().getInfoTributaria(),
                        notaCreditoReporte.getNotaCredito().getInfoTributaria().getClaveAcceso(),
                        fechaAutorizacion.toString()));
        logger.info("Finish writing RIDE");
        llenarExchange(exchange);
    }

    private void llenarExchange(Exchange exchange){
        exchange.getIn().setHeader("ruta", "notas_credito");
        exchange.getIn().setHeader("template", Constant.EMAIL_NOTA_CREDITO);
    }

    private Map<String, Object> obtenerParametrosInfoTriobutaria(InfoTributaria infoTributaria, String numAut, String fechaAut) {
        Map<String, Object> param = new HashMap<>();
        param.put("RUC", infoTributaria.getRuc());
        param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
        param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
        param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
        param.put("ACT_COMERCIAL", SRIEnum.ACTIVIDAD_COMERCIAL.getValue());
        param.put("LOGO",ReportUtilTasOn.getTasOnLogo());
        param.put("SUBREPORT_DIR", TasOnUtil.getJbossReportesPath());
        param.put("SUBREPORT_PAGOS", TasOnUtil.getJbossReportesPath());
        param.put("SUBREPORT_TOTALES", TasOnUtil.getJbossReportesPath());
        param.put("TIPO_EMISION", ReportUtilTasOn.obtenerTipoEmision(infoTributaria));
        param.put("NUM_AUT", numAut);
        param.put("FECHA_AUT", fechaAut);
        param.put("MARCA_AGUA", ReportUtilTasOn.obtenerMarcaAgua(infoTributaria.getAmbiente()));
        param.put("NUM_FACT", infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi() + "-" + infoTributaria.getSecuencial());
        param.put("AMBIENTE", ReportUtilTasOn.obtenerAmbiente(infoTributaria));
        return param;
    }

    private List<TotalesComprobante> getTotalesComprobante(NotaCredito notaCredito){
        List<TotalesComprobante> totalesComprobante = new ArrayList<>();
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL 12%", BigDecimal.ZERO, false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL 0%", BigDecimal.ZERO, false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL NO OBJETO DE IVA", notaCredito.getInfoNotaCredito().getTotalSinImpuestos(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL EXENTO DE IVA", BigDecimal.ZERO, false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL SIN IMPUESTOS", notaCredito.getInfoNotaCredito().getTotalSinImpuestos(), false));
        totalesComprobante.add(new TotalesComprobante("IVA 0%", BigDecimal.ZERO, false));
        totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", notaCredito.getInfoNotaCredito().getTotalSinImpuestos(), false));
        return totalesComprobante;
    }

}
