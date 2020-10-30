package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.factura.Factura;
import ec.net.redcode.tas.on.common.dto.DetalleReporteFactura;
import ec.net.redcode.tas.on.common.dto.ReporteFactura;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.Oferta;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReporteFacturaProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ReporteFacturaProcessor.class);

    @Setter private OfertaService ofertaService;
    @Setter private SolicitudEnvioService solicitudEnvioService;
    @Setter private UsuarioService usuarioService;
    @Setter private ClienteService clienteService;
    @Setter private LocalidadService localidadService;

    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private String pathJboss = getJbossHome() + File.separator + "standalone" + File.separator + "configuration" + File.separator + "tas-on" + File.separator;

    @Override
    public void process(Exchange exchange) throws Exception {
        String numeroAutorizacion = (String) exchange.getIn().getHeader("numeroAutorizacion");
        String path = System.getProperty("user.home") + "/comprobantes/facturas/detalle_" + numeroAutorizacion + ".pdf";
        logger.info("Starting writing Reporte in {}", path);
        String xml = exchange.getIn().getBody(String.class);
        StringReader sr = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(ec.gob.sri.comprobantes.modelo.factura.Factura.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ec.gob.sri.comprobantes.modelo.factura.Factura factura = (ec.gob.sri.comprobantes.modelo.factura.Factura) unmarshaller.unmarshal(sr);
        ReporteFactura reporteFactura = new ReporteFactura();
        String facturaId = factura.getInfoTributaria().getEstab().concat("-").concat(factura.getInfoTributaria().getPtoEmi().concat("-").concat(factura.getInfoTributaria().getSecuencial()));
        reporteFactura.setNoFactura(facturaId);
        reporteFactura.setClienteTason(factura.getInfoFactura().getRazonSocialComprador());
        DetalleReporteFactura detalleReporteFactura;
        Oferta oferta;
        SolicitudEnvio solicitudEnvio;
        Usuario usuario;
        Cliente cliente;
        List<DetalleReporteFactura> detalleReporteFacturas = new ArrayList<>();
        for (Factura.Detalles.Detalle detalle: factura.getDetalles().getDetalle()){
            detalleReporteFactura = new DetalleReporteFactura();
            detalleReporteFactura.setNoOferta(detalle.getCodigoPrincipal());
            solicitudEnvio = solicitudEnvioService.read(detalle.getCodigoPrincipal());
            oferta = ofertaService.read(Integer.valueOf(detalle.getCodigoAuxiliar()));
            String origen = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadOrigen()).getLocalidadDescripcion();
            String destino = localidadService.readLocalidad(solicitudEnvio.getSolicitudEnvioLocalidadDestino()).getLocalidadDescripcion();
            detalleReporteFactura.setOrigen(origen);
            detalleReporteFactura.setDestino(destino);
            String fechaEntrega = df.format(oferta.getOfertaFechaEntrega());
            detalleReporteFactura.setFechaEntrega(fechaEntrega);
            usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
            cliente = clienteService.readCliente(usuario.getUsuarioRuc());
            detalleReporteFactura.setTransportista(cliente.getClienteRazonSocial());
            detalleReporteFactura.setValor(oferta.getOfertaValor() + oferta.getOfertaComision());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(oferta.getOfertaFechaEntrega().getTime());
            calendar.add(Calendar.DATE, cliente.getClienteDiasCredito());
            detalleReporteFactura.setFechaRequerida(df.format(new Timestamp(calendar.getTimeInMillis())));
            detalleReporteFactura.setNumeroCajas(solicitudEnvio.getSolicitudEnvioNumeroPiesas());
            List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(detalle.getCodigoPrincipal());
            int i = 0;
            Double valOfertas = 0.0;
            for (Oferta o: ofertas){
                i++;
                if (o.getOfertaId() == Integer.valueOf(detalle.getCodigoAuxiliar())){
                    detalleReporteFactura.setPosicionOferta(i);
                }
                valOfertas = valOfertas + (o.getOfertaValor() + o.getOfertaComision());
            }
            Double valPromedio = valOfertas / i;
            Double valOfertaPromedio = (valPromedio - (oferta.getOfertaValor() + oferta.getOfertaComision())) / valPromedio;
            detalleReporteFactura.setAhorroOfertaPromedio(valOfertaPromedio);
            detalleReporteFactura.setNumeroOfertas(ofertas.size());
            Double maxOferta = ofertaService.getOfertaMax(detalle.getCodigoPrincipal());
            detalleReporteFactura.setOfertaAlta(maxOferta);
            Double ahorroOfertaMasAlta = (maxOferta - (oferta.getOfertaValor() + oferta.getOfertaComision())) / maxOferta;
            detalleReporteFactura.setAhorroOfertaAlta(ahorroOfertaMasAlta);
            detalleReporteFactura.setCostoPromedio((oferta.getOfertaValor() + oferta.getOfertaComision()) / solicitudEnvio.getSolicitudEnvioNumeroPiesas());
            detalleReporteFacturas.add(detalleReporteFactura);
        }
        reporteFactura.setDetalleReporteFacturas(detalleReporteFacturas);
        reporteFactura.setSubtotal(new BigDecimal((String) exchange.getIn().getHeader("subtotal")).setScale(2, RoundingMode.HALF_UP));
        reporteFactura.setTotal(new BigDecimal((String) exchange.getIn().getHeader("total")).setScale(2, RoundingMode.HALF_UP));
        reporteFactura.setIva(reporteFactura.getTotal().subtract(reporteFactura.getSubtotal()).setScale(2, RoundingMode.HALF_UP));
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporteFactura.getDetalleReporteFacturas());
        String pathReporte = pathJboss + "/resources/reportes/rpt_detalle_anex_fact.jasper";
        JasperPrint reporte_view = JasperFillManager.fillReport(pathReporte, obtenerDatosCabecera(reporteFactura), dataSource);
        JasperExportManager.exportReportToPdfFile(reporte_view, path);
        logger.info("Finish writing Reporte");
    }

    private Map<String, Object> obtenerDatosCabecera(ReporteFactura reporteFactura) throws FileNotFoundException {
        Map<String, Object> param = new HashMap<>();
        param.put("noFactura", reporteFactura.getNoFactura());
        param.put("clienteTason", reporteFactura.getClienteTason());
        param.put("LOGO", new FileInputStream(pathJboss.concat("resources/images/logo.jpeg")));
        param.put("subtotal", reporteFactura.getSubtotal());
        param.put("iva", reporteFactura.getIva());
        param.put("total", reporteFactura.getTotal());
        return param;
    }

    private String getJbossHome(){
        String jbossHome = System.getenv("jboss.home");
        if (jbossHome == null)
            jbossHome = System.getenv("JBOSS_HOME");
        return jbossHome;
    }

}
