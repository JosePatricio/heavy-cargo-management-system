package ec.redcode.tas.on.facturacion;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Secuencia;
import ec.net.redcode.tas.on.persistence.service.SecuenciaService;

import java.text.SimpleDateFormat;

public class BaseComprobantes {

    private SecuenciaService secuenciaService;

    protected SimpleDateFormat dateFormatFechaEmision = new SimpleDateFormat("dd/MM/yyyy");
    protected SimpleDateFormat dateFormatPeriodoFiscal = new SimpleDateFormat("MM/yyyy");
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

    /**
     * Metodo que permite obtener el secuencial de los comprobantes
     *
     * @param nemonico
     * @return
     */
    protected String getSecuencial(String nemonico){
        Secuencia secuencia = secuenciaService.getSecuenciaByNemonico(nemonico);
        int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
        if (nextValue > 999999999){
            //TODO: crear el algoritmo para cuando la factura pase del numero 999999999
        }
        secuencia.setSecuencia(nextValue);
        secuenciaService.update(secuencia);
        return TasOnUtil.getDigitsToSecuence(nextValue) + String.valueOf(nextValue);
    }

    /**
     * Permite crear el digito verificador modulo 11 para la clave de acceso
     * @param cadena
     * @return
     */
    protected int generateDigitoModulo11(String cadena) {
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

    public void setSecuenciaService(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }
}
