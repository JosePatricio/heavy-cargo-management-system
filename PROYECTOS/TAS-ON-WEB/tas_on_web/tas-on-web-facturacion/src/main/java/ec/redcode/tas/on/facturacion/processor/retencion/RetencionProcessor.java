package ec.redcode.tas.on.facturacion.processor.retencion;

import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion.InfoCompRetencion;
import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion.Impuestos;
import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.gob.sri.comprobantes.modelo.rentencion.Impuesto;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.Factura;
import ec.net.redcode.tas.on.persistence.entities.FacturaProveedor;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
import ec.net.redcode.tas.on.persistence.service.FacturaProveedorService;
import ec.net.redcode.tas.on.persistence.service.FacturaService;
import ec.redcode.tas.on.facturacion.BaseComprobantes;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RetencionProcessor extends BaseComprobantes implements Processor {

    private CatalogoItemService catalogoItemService;
    private FacturaService facturaService;
    private ClienteService clienteService;
    private FacturaProveedorService facturaProveedorService;

    private String ambiente;
    private String establecimiento;
    private String emision;
    private String puntoEstalecimiento;
    private List<CatalogoItem> catalogoItems;

    private static final String RETENCION_SECUENCIA = "RETE";
    private static final String RETENCION_CODIGO = "07";

    public void init(){
        catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        ambiente = catalogoItems.stream().filter(c -> "AMBIENTE".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        establecimiento = catalogoItems.stream().filter(c -> "ESTABLECIMIENTO".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        emision = catalogoItems.stream().filter(c -> "EMISION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        puntoEstalecimiento = catalogoItems.stream().filter(c -> "PUNTO ESTABLECIMIENTO".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        String tipo = (String) data.get("tipo");
        exchange.getIn().setHeader("prefactura", data.get("prefactura"));
        exchange.getIn().setHeader("tipo", tipo);
        Date now = new Date();
        Cliente cliente = clienteService.readCliente(Constant.RUC_TASON);
        ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();
        comprobanteRetencion.setId(Constant.INVOICE_ID);
        comprobanteRetencion.setVersion(catalogoItems.stream().filter(c -> "VERSION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        String secuencial = getSecuencial(RETENCION_SECUENCIA);
        String claveAcceso = generateClaveAcceso(now, Constant.RUC_TASON, secuencial);
        InfoTributaria infoTributaria = new InfoTributaria();
        infoTributaria.setAmbiente(ambiente);
        infoTributaria.setTipoEmision(emision);
        infoTributaria.setSecuencial(secuencial);
        infoTributaria.setClaveAcceso(claveAcceso);
        infoTributaria.setCodDoc(RETENCION_CODIGO);
        infoTributaria.setRazonSocial(cliente.getClienteRazonSocial());
        infoTributaria.setNombreComercial(cliente.getClienteRazonSocial());
        infoTributaria.setRuc(cliente.getClienteRuc());
        //infoTributaria.setCodDoc(documento);
        infoTributaria.setEstab(establecimiento);
        infoTributaria.setPtoEmi(puntoEstalecimiento);
        infoTributaria.setDirMatriz(cliente.getClienteDireccion());
        comprobanteRetencion.setInfoTributaria(infoTributaria);
        if ("factura".equals(tipo)) {
            String numeroPrefactura = (String) data.get("prefactura");
            createComprobanteRetencionFactura(comprobanteRetencion, numeroPrefactura, now);
        } else {
            int numeroPrefactura;
            try{
                numeroPrefactura = (int) data.get("prefactura");
            }catch (Exception e){
                numeroPrefactura = Integer.parseInt((String) data.get("prefactura"));
            }
            createComprobanteRetencionFacturaProveedor(comprobanteRetencion, numeroPrefactura, now);
        }
        exchange.getIn().setBody(comprobanteRetencion);
        exchange.getIn().setHeader("claveAcceso", claveAcceso);
    }

    private void createComprobanteRetencionFactura(ComprobanteRetencion comprobanteRetencion, String numeroPrefactura, Date now){
        Factura factura = facturaService.read(numeroPrefactura);
        InfoCompRetencion infoCompRetencion = new InfoCompRetencion();
        Cliente cliente = clienteService.readCliente(factura.getFacturaEmpresa());
        infoCompRetencion.setFechaEmision(dateFormatFechaEmision.format(now));
        //infoCompRetencion.setDirEstablecimiento(); DIRECCION DE QUIEN EMITE LA RETENCION
        //TODO: deberia estar en una constante la identificacion del sujeto retenido
        infoCompRetencion.setTipoIdentificacionSujetoRetenido("04");
        infoCompRetencion.setRazonSocialSujetoRetenido(cliente.getClienteRazonSocial());
        infoCompRetencion.setIdentificacionSujetoRetenido(cliente.getClienteRuc());
        infoCompRetencion.setPeriodoFiscal(dateFormatPeriodoFiscal.format(now));
        infoCompRetencion.setObligadoContabilidad("SI");
        comprobanteRetencion.setInfoCompRetencion(infoCompRetencion);
        Impuestos impuestos = new Impuestos();
        List<Impuesto> impuestoList = new ArrayList<>();
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo("1");
        impuesto.setCodigoRetencion("310");
        impuesto.setBaseImponible(new BigDecimal(factura.getFacturaTotal()).setScale(2, RoundingMode.HALF_UP));
        impuesto.setPorcentajeRetener(new BigDecimal(1));
        impuesto.setFechaEmisionDocSustento(dateFormatFechaEmision.format(factura.getFacturaFechaAutorizacion()));
        double valor = (factura.getFacturaTotal() * cliente.getClienteRetencion()) / 100;
        impuesto.setValorRetenido(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
        impuesto.setCodDocSustento("01");
        impuesto.setNumDocSustento(factura.getFacturaNro().replace("-", ""));
        impuestoList.add(impuesto);
        impuestos.getImpuesto().addAll(impuestoList);
        comprobanteRetencion.setImpuestos(impuestos);
    }

    private void createComprobanteRetencionFacturaProveedor(ComprobanteRetencion comprobanteRetencion, int numeroPrefactura, Date now){
        FacturaProveedor factura = facturaProveedorService.read(numeroPrefactura);
        InfoCompRetencion infoCompRetencion = new InfoCompRetencion();
        Cliente cliente = clienteService.readCliente(factura.getFacturaProveedorRucCliente());
        infoCompRetencion.setFechaEmision(dateFormatFechaEmision.format(now));
        //infoCompRetencion.setDirEstablecimiento(); DIRECCION DE QUIEN EMITE LA RETENCION
        //TODO: deberia estar en una constante la identificacion del sujeto retenido
        infoCompRetencion.setTipoIdentificacionSujetoRetenido("04");
        infoCompRetencion.setRazonSocialSujetoRetenido(cliente.getClienteRazonSocial());
        infoCompRetencion.setIdentificacionSujetoRetenido(cliente.getClienteRuc());
        infoCompRetencion.setPeriodoFiscal(dateFormatPeriodoFiscal.format(now));
        infoCompRetencion.setObligadoContabilidad("SI");
        comprobanteRetencion.setInfoCompRetencion(infoCompRetencion);
        Impuestos impuestos = new Impuestos();
        List<Impuesto> impuestoList = new ArrayList<>();
        //renta
        CatalogoItem catalogoItem = catalogoItemService.read(cliente.getClienteRetencion());
        String[] renta = catalogoItem.getCatalogoItemValor().trim().split("\\|");
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo("1");
        impuesto.setCodigoRetencion(renta[1]);
        impuesto.setBaseImponible(new BigDecimal(factura.getFacturaProveedorSubtotal()).setScale(2, RoundingMode.HALF_UP));
        impuesto.setPorcentajeRetener(new BigDecimal(renta[0]));
        impuesto.setFechaEmisionDocSustento(dateFormatFechaEmision.format(factura.getFacturaProveedorFechaAutorizacion()));
        double valor = (factura.getFacturaProveedorSubtotal() * Integer.valueOf(renta[0])) / 100;
        impuesto.setValorRetenido(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
        impuesto.setCodDocSustento("01");
        impuesto.setNumDocSustento(factura.getFacturaProveedorNumero().replace("-", ""));
        impuestoList.add(impuesto);
        factura.setFacturaProveedorRetencion(valor);
        facturaProveedorService.update(factura);
        //IVA
        catalogoItem = catalogoItemService.read(cliente.getClienteIva());
        String iva[] = catalogoItem.getCatalogoItemValor().trim().split("\\|");
        impuesto = new Impuesto();
        impuesto.setCodigo("2");
        impuesto.setCodigoRetencion(iva[1]);
        impuesto.setBaseImponible(new BigDecimal(factura.getFacturaProveedorIva()).setScale(2, RoundingMode.DOWN));
        impuesto.setPorcentajeRetener(new BigDecimal(iva[0]));
        impuesto.setFechaEmisionDocSustento(dateFormatFechaEmision.format(factura.getFacturaProveedorFechaAutorizacion()));
        valor = (factura.getFacturaProveedorIva() * Integer.valueOf(iva[0])) / 100;
        impuesto.setValorRetenido(new BigDecimal(valor).setScale(2, RoundingMode.DOWN));
        impuesto.setCodDocSustento("01");
        impuesto.setNumDocSustento(factura.getFacturaProveedorNumero().replace("-", ""));
        impuestoList.add(impuesto);
        impuestos.getImpuesto().addAll(impuestoList);
        comprobanteRetencion.setImpuestos(impuestos);
    }

    /**
     * Permite generar la clave de acceso del SRI
     *
     * @param now
     * @param ruc
     * @param secuencial
     * @return
     */
    private String generateClaveAcceso(Date now, String ruc, String secuencial){
        String fecha = dateFormat.format(now);
        StringBuilder clave = new StringBuilder(fecha);
        clave.append("07");
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(establecimiento);
        clave.append(puntoEstalecimiento);
        clave.append(secuencial);
        clave.append(Constant.INVOICE_CODIGO_NUMERO);
        clave.append(emision);
        int digitoVerificador = generateDigitoModulo11(clave.toString());
        clave.append(String.valueOf(digitoVerificador));
        return clave.toString();
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }

    public void setFacturaService(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setFacturaProveedorService(FacturaProveedorService facturaProveedorService) {
        this.facturaProveedorService = facturaProveedorService;
    }
}
