package ec.net.redcode.tas.on.common.util;

import ec.net.redcode.tas.on.common.Constant;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class TasOnUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *  Metodo de obtiene una fecha de acuerdo a los dias de validez, hay que tomar en cuentas que no cuenta sabado y domingos
     *
     * @param sFechaIni
     * @param iDias
     * @return
     */
    public static String getDatePlusDays(Date sFechaIni, int iDias) {
        //int iCountSabDom;
        Calendar cldInicio = Calendar.getInstance();
        cldInicio.setFirstDayOfWeek(cldInicio.MONDAY);
        cldInicio.setTime(sFechaIni);

        //int iDiaSemana = cldInicio.get(cldInicio.DAY_OF_WEEK);
        //int iDInicio = cldInicio.get(cldInicio.DAY_OF_YEAR);

        Calendar cldFin = Calendar.getInstance();
        cldFin.setFirstDayOfWeek(cldFin.MONDAY);
        cldFin.setTime(sFechaIni);

        cldFin.add(cldFin.DATE, iDias);

        //int iDiaSemanaFin = cldFin.get(cldFin.DAY_OF_WEEK);
        //int iDFin = cldFin.get(cldFin.DAY_OF_YEAR);

        //iCountSabDom = getSatSunDays(iDInicio, iDFin, iDiaSemana,
                //iDiaSemanaFin);
        //cldFin.add(cldFin.DATE,iCountSabDom);
        Date d = cldFin.getTime();
        return simpleDateFormat.format(d);
    }

    /**
     * Recuperar el numero de dias habiles entre dos fechas dadas.
     *
     * @param sFechaIni - Date
     * @param sFechaFin - Date
     * @return cantidad de días hábiles.
     */
    public static int getWorkDay2(Date sFechaIni, Date sFechaFin) {
        int iDiasAsueto = 0;
        int iDiasHabiles;
        int iCountSabDom = 0;

        Calendar cldInicio = Calendar.getInstance();
        Calendar _calIni = Calendar.getInstance();
        _calIni.setTime(sFechaIni);

        //JuCa: 12/05/2004 --> tomamos el mes
        int viMesInicio = sFechaIni.getMonth();

        cldInicio.setFirstDayOfWeek(cldInicio.MONDAY);
        cldInicio.setTime(sFechaIni);

        int iDInicio = cldInicio.get(cldInicio.DAY_OF_YEAR);

        Calendar cldFin = Calendar.getInstance();

        Calendar _calFin = Calendar.getInstance();
        _calFin.setTime(sFechaFin);

        cldFin.setFirstDayOfWeek(cldFin.MONDAY);
        cldFin.setTime(sFechaFin);

        //JuCa: 12/05/2004 --> tomamos el mes
        int viMesFin = sFechaFin.getMonth();

        int iDFin = cldFin.get(cldFin.DAY_OF_YEAR);

        if ((cldInicio == null) || (cldFin == null))
            return 0;

        //Recupera los dias naturales
        int iNumeroDias = 0;
        if (viMesInicio == viMesFin) {
            iNumeroDias = iDFin - iDInicio; //JuCa: 11/05/2004 ---> Agregaba un
            // día extra y eso no es válido para
            // el mismo mes!!!
        } else {
            iNumeroDias = (iDFin - iDInicio);// + 1; //JuCa: 26/08/2004 --> Ese
            // 1 estaba de más :D
        }

        if (iDFin == iDInicio)
            iNumeroDias = 0;
        //Recupera los dias festivos
        // iDiasAsueto = getHollyDaysSinSatSun(iDInicio, iDFin);
        //Date sFechaIniAux = sFechaIni;
        //iCountSabDom = getSatSunDays2(sFechaIniAux, sFechaFin);
        iDiasHabiles = (iNumeroDias - iCountSabDom) - iDiasAsueto;
        if (iDiasHabiles < 0)
            iDiasHabiles = 0;

        return iDiasHabiles;
    }

    /**
     * Método que recupera los sabados y domingos que se encuentren dentro de un
     * rango establecido.
     *
     * @param dateIni - Fecha inicial en formato "dd/mm/aaaa"
     * @param dateFin - Fecha final en formato "dd/mm/aaaa".
     * @return Cantidad de sábados y domingos.
     */
    private static int getSatSunDays2(Date dateIni, Date dateFin) {
        int iCountSatSun = 0;
        Calendar calIni = Calendar.getInstance();
        // Inicializando el calendario a la fecha de inicio.
        calIni.setTime(dateIni);
        // Incrementamos día por día hasta la fecha final.
        // Cuando encontramos que es día sábado o domingo incrementamos la
        // variable "iCountSatSun".
        while (dateIni.compareTo(dateFin) <= 0) {
            // Validación de sábado o domingo.
            if (calIni.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || calIni.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                iCountSatSun++;
            }
            // Esta instrucción incrementa la fecha por un día y no importa
            // si está al fin de mes o al fin de año, siempre lo hace bien.
            calIni.add(Calendar.DATE, 1);
            dateIni = calIni.getTime();
        }

        return iCountSatSun;
    }

    private static int getSatSunDays(int iDInicio, int iDFin, int iDiaSemana, int iDiaSemanaFin) {
        boolean bDiasAntes = false;
        int iNumeroDias = iDFin - iDInicio;
        int iTmp = nextMonday(iDiaSemana);
        int iCountSabDom = 0;
        iTmp = iTmp + iDInicio;
        while (iTmp + 7 <= finalRange(iDFin, iDiaSemanaFin)) {
            iTmp = iTmp + 7;
            iCountSabDom = iCountSabDom + 2;
            bDiasAntes = false;
        }
        if (iDiaSemana == 1) {
            iCountSabDom += 1;//domingo
            bDiasAntes = true;
        } else if (iDiaSemana == 7) {
            iCountSabDom += 2; //sabado
            bDiasAntes = true;
        }

        if (iNumeroDias > 5 && !bDiasAntes)
            iCountSabDom += 2; //cuando dia final lunes
        return iCountSabDom;
    }

    private static int nextMonday(int iDia) {
        if (iDia == 1)
            return 1; //domingo
        else
            return 9 - iDia;
    }

    private static int finalRange(int iDias, int iDiaSemana) {
        if (iDiaSemana == 1)
            return iDias + 1; //domingo
        else if (iDiaSemana == 7)
            return iDias + 2; //sabado
        else
            return iDias;
    }

    public static void main(String[] args) throws ParseException {
        Date now = new Date();
        System.out.println(now);
        String date = getDatePlusDays(now, 1);
        System.out.println(date);
        String date_s = " 2018-02-18 00:00:00.0";
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss");
        Date dateAfter = dt.parse(date_s);
        int days = getWorkDay2(now, dateAfter);
        System.out.println("days " + days);
    }

    public static String decrypt(String textoEncriptado){
        String base64EncryptedString = "";
        try {
            byte[] message = Base64.getDecoder().decode(textoEncriptado.getBytes(StandardCharsets.UTF_8));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(Constant.KEY.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = decipher.doFinal(message);
            base64EncryptedString = new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    /**
     * Calculates the last digits for the card number received as parameter
     *
     * @param card {@link String} number
     * @return {@link String} the check digit
     */
    public static String calculateCheckDigit(String card) {
        if (card == null)
            return null;
        String digit;
        /* convert to array of int for simplicity */
        int[] digits = new int[card.length()];
        for (int i = 0; i < card.length(); i++) {
            digits[i] = Character.getNumericValue(card.charAt(i));
        }

        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2)	{
            digits[i] += digits[i];

            /* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }
        /* multiply by 9 step */
        sum = sum * 9;

        /* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }

    public static String divideAndConquer(int number) {
        String digits = "";
        if (number < 100000){
            // 5 digits or less
            if (number < 100){
                // 1 or 2
                if (number < 10)
                    digits = "000000";
                else
                    digits = "00000";
            }else{
                // 3 to 5 digits
                if (number < 1000)
                    digits = "0000";
                else{
                    // 4 or 5 digits
                    if (number < 10000)
                        digits = "000";
                    else
                        digits = "00";
                }
            }
        } else {
            // 6 digits or more
            if (number < 10000000) {
                // 6 or 7 digits
                if (number < 1000000)
                    digits = "0";
            }
        }
        return digits;
    }

    public static String getDigitsToSecuence(int number) {
        if (number < 100000) {
            // 5 or less
            if (number < 100) {
                // 1 or 2
                if (number < 10)
                    return "00000000";
                else
                    return "0000000";
            }
            else {
                // 3 or 4 or 5
                if (number < 1000)
                    return "000000";
                else {
                    // 4 or 5
                    if (number < 10000)
                        return "00000";
                    else
                        return "0000";
                }
            }
        }
        else {
            // 6 or more
            if (number < 10000000) {
                // 6 or 7
                if (number < 1000000)
                    return "000";
                else
                    return "00";
            }
            else {
                // 8 to 10
                if (number < 100000000)
                    return "0";
                else {
                    // 9 or 10
                    if (number < 1000000000)
                        return "";
                    else
                        return "";
                }
            }
        }
    }

    public static String castString(String string, int maxLength){
        if(string == null) return null;
        return string.substring(0, Math.min(maxLength, string.length()));
    }

    public static Date getDateFromTimeStamp(Timestamp timeStamp){
        return timeStamp != null ? new Date(timeStamp.getTime()) : null;
    }

    public static Timestamp getTimeStampFromObject(Object object){
        if(object instanceof Timestamp) return (Timestamp) object;
        return null;
    }

    public static String getStringFromDate(Date date, SimpleDateFormat dateFormat){
        return date!=null && dateFormat!=null? dateFormat.format(date) : null;
    }

    public static Double roundDouble(Double value, int places) {
        if(places < 0) throw new IllegalArgumentException();
        if(value == null) return null;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double getDoubleFromObject(Object object){
        double result = 0;
        if(object instanceof Number){
            result = ((Number) object).doubleValue();
        }
        return result;
    }

    public static Double getDOUBLEFromObject(Object object){
        Double result = null;
        if(object instanceof Number){
            result = ((Number) object).doubleValue();
        }
        return result;
    }

    public static int getIntFromObject(Object object){
        int result = 0;
        if(object instanceof Number){
            result = ((Number) object).intValue();
        }
        return result;
    }

    public static Integer getIntegerFromObject(Object object){
        if(object == null) return null;
        return getIntFromObject(object);
    }

    public static long getLongFromObject(Object object){
        long result = 0;
        if(object instanceof Number){
            result = ((Number) object).longValue();
        }
        return result;
    }

    public static String getStringFromObject(Object object){
        String result = null;
        if(object instanceof String){
            result = (String) object;
        }
        if(object instanceof Number){
            result = String.valueOf(object);
        }
        return result;
    }


    public static String removeSpecialCharacters(String string){
        if(string == null) return "";
        string = string.toUpperCase();
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        string = string.replaceAll("[^A-Z0-9 ]+"," ");
        string = string.replaceAll(" +"," ");
        return string;
    }

    public static String replaceSpaces(String string, String character){
        if(string == null) return "";
        if(character == null) return string.replaceAll(" +","");
        string = string.replaceAll(" +"," ");
        return string.replaceAll(" ",character);
    }

    public static boolean isStringNullOrEmpty(String... strings){
        for(String string : strings){
            if(string == null || string.trim().isEmpty()) return true;
        }
        return false;
    }

    public static int getExpiredDays(Timestamp fechaPago){
        int dias = 0;
        Date dateNow = new Date();
        if (fechaPago != null && fechaPago.before(dateNow)) {
            long daysInTime = dateNow.getTime() - fechaPago.getTime();
            dias = (int) (daysInTime / (1000 * 60 * 60 * 24));
        }
        return dias;
    }

    public static double getMaxValorSubastaValorObjetivo(double valorObjetivo, int comision){
        return TasOnUtil.roundDouble(valorObjetivo*(1-comision/100.00), 2);
    }

    public static double getComisionExtraSubastaValorObjetivo(double valorObjetivo, int comisionCliente, double valorOferta){
        double maximoValorOfertar = getMaxValorSubastaValorObjetivo(valorObjetivo, comisionCliente);
        double porcentajeOferta = (valorOferta*100.00/maximoValorOfertar);
        double ahorro = maximoValorOfertar-valorOferta;
        double comisionExtra = 0;
        if(porcentajeOferta<=100 && porcentajeOferta>=99){
            comisionExtra = ahorro*0.5;
        } else if(porcentajeOferta<99 && porcentajeOferta>=90){
            comisionExtra = ahorro*0.55;
        } else if(porcentajeOferta<90 && porcentajeOferta>=80){
            comisionExtra = ahorro*0.60;
        } else if(porcentajeOferta<80 && porcentajeOferta>=70){
            comisionExtra = ahorro*0.64;
        } else if(porcentajeOferta<70 && porcentajeOferta>=60){
            comisionExtra = ahorro*0.68;
        } else if(porcentajeOferta<60 && porcentajeOferta>=50){
            comisionExtra = ahorro*0.71;
        } else if(porcentajeOferta<50 && porcentajeOferta>=40){
            comisionExtra = ahorro*0.74;
        } else if(porcentajeOferta<40 && porcentajeOferta>=30){
            comisionExtra = ahorro*0.76;
        } else if(porcentajeOferta<30 && porcentajeOferta>=20){
            comisionExtra = ahorro*0.78;
        } else if(porcentajeOferta<20 && porcentajeOferta>=10){
            comisionExtra = ahorro*0.79;
        } else if(porcentajeOferta<10){
            comisionExtra = ahorro*0.80;
        }
        return comisionExtra;
    }

    public static double transformarKgATon(Double kilogramos){
        if(kilogramos == null) return 0;
        return kilogramos/1000.0;
    }

    public static double transformarLbATon(Double libras){
        if(libras == null) return 0;
        return libras/2204.623;
    }

    public static String eliminarEspacios(String str){
        if (str == null) return null;
        return str.trim().replaceAll("\\s", " ");
    }

    public static boolean esUsuarioConductor(int tipoUsuario){
        return Constant.USER_DRIVER == tipoUsuario || Constant.USER_DRIVER_ADMIN == tipoUsuario;
    }

    public static boolean esUsuarioContador(int tipoUsuario){
        return Constant.USER_ACCOUNTANT == tipoUsuario;
    }

    public static boolean esUsuarioCliente(int tipoUsuario){
        return Constant.USER_CLIENT_ADMIN == tipoUsuario
                || Constant.USER_CLIENT == tipoUsuario
                || Constant.USER_CLIENT_REDUCIDO == tipoUsuario;
    }

    public static boolean esUsuarioAdmin(int tipoUsuario){
        return Constant.USER_ADMIN == tipoUsuario;
    }

    /***************************************RUTAS**********************************************/

    public static String getJbossHome(){
        String jbossHome = System.getenv("jboss.home");
        if (jbossHome == null)
            jbossHome = System.getenv("JBOSS_HOME");
        return jbossHome;
    }

    public static String getJbossResourcesPath(){
        return TasOnUtil.getJbossHome() + File.separator +
                "standalone" + File.separator +
                "configuration" + File.separator +
                "tas-on" + File.separator +
                "resources" + File.separator;
    }

    public static String getJbossReportesPath(){
        return TasOnUtil.getJbossResourcesPath() + File.separator +
                "reportes" + File.separator;
    }

    public static String getJbossPlantillasPath(){
        return TasOnUtil.getJbossResourcesPath() + File.separator +
                "plantillas" + File.separator;
    }

    public static String getJbossImagesPath(){
        return TasOnUtil.getJbossResourcesPath() + File.separator +
                "images" + File.separator;
    }

    public static String getComprobantesPath(){
        return System.getProperty("user.home") + File.separator + "comprobantes" + File.separator;
    }

    public static String getComprobantesInboxPath(){
        return getComprobantesPath() + "inbox";
    }

    public static String getComprobantesOutboxPath(){
        return getComprobantesPath() + "outbbox";
    }

    public static String getComprobantesCashManagementPath(){
        return getComprobantesPath() + "archivosCashManagement" ;
    }

    public static String getComprobantesFacturasPath(){
        return getComprobantesPath() + "facturas" ;
    }

    public static String getComprobantesRetencionesPath(){
        return getComprobantesPath() + "retenciones" ;
    }

    public static String getComprobantesNotasCreditoPath(){
        return getComprobantesPath() + "notas_credito" ;
    }


}