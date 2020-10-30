package ec.redcode.tas.on.facturacion.processor;

import ec.gob.sri.comprobantes.modelo.InfoTributaria;
import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.Secuencia;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import ec.net.redcode.tas.on.persistence.service.ClienteService;
import ec.net.redcode.tas.on.persistence.service.FacturaManualService;
import ec.net.redcode.tas.on.persistence.service.SecuenciaService;
import ec.redcode.tas.on.facturacion.processor.enums.SRIEnum;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseFacturaProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BaseFacturaProcessor.class);

    @Setter protected ClienteService clienteService;
    @Setter private CatalogoItemService catalogoItemService;
    @Setter private SecuenciaService secuenciaService;
    @Setter protected FacturaManualService facturaManualService;

    protected List<CatalogoItem> catalogoItems;

    private String ambiente;
    private String establecimiento;
    private String emision;
    private String puntoEstalecimiento;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
    protected SimpleDateFormat dateFormatFechaEmision = new SimpleDateFormat("dd/MM/yyyy");

    public void initProcessor(){
        logger.info("Creating Processor Facturas");
        catalogoItems = catalogoItemService.getCatalogoItemByCatalogo(22);
        ambiente = catalogoItems.stream().filter(c -> "AMBIENTE".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        establecimiento = catalogoItems.stream().filter(c -> "ESTABLECIMIENTO".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        emision = catalogoItems.stream().filter(c -> "EMISION".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        puntoEstalecimiento = catalogoItems.stream().filter(c -> "PUNTO ESTABLECIMIENTO".equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
    }

    protected InfoTributaria getInfoTributaria(String tipoDocumento){
        Cliente TASON = clienteService.readCliente(Constant.RUC_TASON);
        InfoTributaria infoTributaria = new InfoTributaria();
        infoTributaria.setAmbiente(ambiente);
        infoTributaria.setTipoEmision(emision);
        infoTributaria.setRazonSocial(TASON.getClienteRazonSocial());
        infoTributaria.setNombreComercial("TAS-ON");
        infoTributaria.setRuc(TASON.getClienteRuc());
        infoTributaria.setCodDoc(tipoDocumento);
        infoTributaria.setEstab(establecimiento);
        infoTributaria.setPtoEmi(puntoEstalecimiento);
        infoTributaria.setDirMatriz(TASON.getClienteDireccion());
        return infoTributaria;
    }

    protected String getSecuencial(SRIEnum tipoDocumento){
        Secuencia secuencia;
        if(SRIEnum.COMPROBANTE_FACTURA == tipoDocumento){
            secuencia = secuenciaService.read(1);
        } else if(SRIEnum.COMPROBANTE_NOTA_CREDITO == tipoDocumento){
            secuencia = secuenciaService.getSecuenciaByNemonico("NOTAC");
        }else{
            throw new IllegalArgumentException("Error al obtener el secuencial");
        }

        int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
        if (nextValue > 999999999){
            //TODO: crear el algoritmo para cuando la factura pase del numero 999999999
        }
        secuencia.setSecuencia(nextValue);
        secuenciaService.update(secuencia);
        return TasOnUtil.getDigitsToSecuence(nextValue) + nextValue;
    }

    protected String generateClaveAcceso(Date now, String ruc, String secuencial, String tipoDocumento){
        String fecha = dateFormat.format(now);
        StringBuilder clave = new StringBuilder(fecha);
        clave.append(tipoDocumento);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(establecimiento);
        clave.append(puntoEstalecimiento);
        clave.append(secuencial);
        clave.append(Constant.INVOICE_CODIGO_NUMERO);
        clave.append(emision);
        int digitoVerificador = generateDigitoModulo11(clave.toString());
        clave.append(digitoVerificador);
        return clave.toString();
    }

    /**
     * Permite crear el digito verificador modulo 11 para la clave de acceso
     */
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

    public static boolean debeEmitirNotaCredito(Cliente cliente){
        return cliente != null &&
                cliente.getClienteNotaCredito() != null &&
                cliente.getClienteNotaCredito() == 1;
    }

}
