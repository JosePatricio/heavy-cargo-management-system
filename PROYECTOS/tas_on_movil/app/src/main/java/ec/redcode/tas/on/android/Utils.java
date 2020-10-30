package ec.redcode.tas.on.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.models.City;

/**
 * Created by Josue Ortiz on 29/12/2017.
 * Clase de utilidades
 */

public class Utils {
    public static Integer ALL = 0;
    public static Integer DRIVER = 1;
    public static Integer CUSTOMER = 2;

    public static String RGX_EMAIL = "^([a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)*";
    public static String RGX_ALPHANUMERIC = "^[a-zA-Z0-9]*";
    public static String RGX_GUIA_REMISION = "[0-9]{3}-[0-9]{3}-[0-9]{9}";

    public static String convertDateMonth(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM");
        return format.format(date);
    }

    public static String convertDateYear(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public static String convertDateTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.format(date);
    }

    public static String convertTimeHour(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MM/dd HH:mm");
        return format.format(date);
    }

    public static String addNewLineBetweenDateTime(String string){
        if(string == null) return null;
        String[] strings = string.split(" ");
        if(strings.length == 2){
            return strings[0]+System.getProperty("line.separator")+strings[1];
        }
        return string;
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void fillCitiesMockUpData(Context context) throws MalformedURLException {
        Constants.cities = new ArrayList<>();
        Constants.cities.add(new City("Quevedo", String.valueOf(12), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b2/Bandera_de_Quevedo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Babahoyo", String.valueOf(17), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_de_Babahoyo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Buena Fe", String.valueOf(35), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/4c/Bandera_de_Buena_F%C3%A9.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Ventanas", String.valueOf(36), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/77/Bandera_de_Ventanas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Vinces", String.valueOf(44), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/6c/Bandera_de_Vinces.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Valencia", String.valueOf(65), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/89/Bandera_de_Valencia_2.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Montalvo", String.valueOf(76), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/9/94/Bandera_de_Montalvo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Mocache", String.valueOf(100), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/83/Bandera_de_Mocahe.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Puebloviejo", String.valueOf(101), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b1/Bandera_de_Puebloviejo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Palenque", String.valueOf(115), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d7/Bandera_de_Palenque.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Catarama", String.valueOf(118), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/56/Bandera_de_Urdaneta.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Baba", String.valueOf(131), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f5/Bandera_de_Baba.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Quinsaloma", String.valueOf(137), false, "Los Ríos", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/c/ce/Bandera_de_Quinsaloma.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Los_R%C3%ADos.svg")));
        Constants.cities.add(new City("Cuenca", String.valueOf(3), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e3/Flag_of_Cuenca%2C_Ecuador.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Gualaceo", String.valueOf(71), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f2/Bandera_Canton_Gualaceo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Paute", String.valueOf(108), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/6d/Bandera_Canton_Paute.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Santa Isabel", String.valueOf(126), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a0/Bandera_de_Santa_Isabel.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Camilo Ponce Enríquez", String.valueOf(134), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/12/Bandera_de_Camilo_Ponce_Enr%C3%ADquez.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Chordeleg", String.valueOf(143), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/96/Bandera_Canton_Chordeleg.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Girón", String.valueOf(147), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e5/Bandera_de_Gir%C3%B3n.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Sígsig", String.valueOf(153), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/ca/Bandera_de_Sigsig.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("San Fernando", String.valueOf(189), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/81/Bandera_Canton_San_Fernando.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Nabón", String.valueOf(198), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/17/Bandera_de_Nab%C3%B3n.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Guachapala", String.valueOf(202), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e1/Bandera_del_Cant%C3%B3n_Guachapala.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Pucará", String.valueOf(211), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/5/50/Bandera_Canton_Pucara.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("San Felipe de Oña", String.valueOf(214), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b4/Bandera_Canton_O%C3%B1a.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Sevilla de Oro", String.valueOf(215), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5b/Bandera_Canton_Sevilla_de_Oro.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("El Pan", String.valueOf(221), false, "Azuay", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/d/de/Bandera_Canton_El_Pan.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/aa/Bandera_Provincia_Azuay.svg")));
        Constants.cities.add(new City("Guaranda", String.valueOf(50), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/96/Bandera_de_Guaranda.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("San Miguel", String.valueOf(112), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/40/Bandera_de_San_Miguel_de_Bol%C3%ADvar.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("Caluma", String.valueOf(117), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/88/Bandera_de_Echend%C3%ADa.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("Echeandía", String.valueOf(121), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/88/Bandera_de_Echend%C3%ADa.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("San José de Chimbo", String.valueOf(141), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5b/Bandera_del_Cant%C3%B3n_Chimbo.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("Chillanes", String.valueOf(165), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b7/Bandera_del_Cant%C3%B3n_Chillanes.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("Las Naves", String.valueOf(187), false, "Bolívar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/28/Bandera_de_Las_Naves.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_Provincia_Bol%C3%ADvar.svg")));
        Constants.cities.add(new City("La Troncal", String.valueOf(38), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/7/7a/Bandera_de_La_Troncal.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Azogues", String.valueOf(42), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/28/Bandera_de_Azogues.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Cañar", String.valueOf(73), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c1/Bandera_del_Cant%C3%B3n_Ca%C3%B1ar.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Biblián", String.valueOf(128), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/49/Bandera_de_Bibli%C3%A1n.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("El Tambo", String.valueOf(136), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/24/Bandera_de_El_Tambo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Suscal", String.valueOf(197), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/7/71/Bandera_de_Suscal.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Déleg", String.valueOf(219), false, "Cañar", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/3/30/Bandera_de_D%C3%A9leg.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Tulcán", String.valueOf(21), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/90/Flag_of_Tulc%C3%A1n.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("San Gabriel", String.valueOf(70), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/e/ec/Bandera_de_Mont%C3%BAfar.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("El Ángel", String.valueOf(138), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_de_Espejo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Huaca", String.valueOf(148), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/5/57/Bandera_de_San_Pedro_de_Huaca.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Mira", String.valueOf(160), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9a/Bandera_de_Mira.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Bolívar", String.valueOf(163), false, "Carchi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a9/Bandera_del_Cant%C3%B3n_Bol%C3%ADvar_de_Carchi.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Bandera_Provincia_Ca%C3%B1ar.svg")));
        Constants.cities.add(new City("Riobamba", String.valueOf(13), false, "Chimborazo", "Sierra", new URL("https://commons.wikimedia.org/wiki/File:Bandera_de_Riobamba.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Cumandá", String.valueOf(96), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/94/Bandera_de_Cumand%C3%A1.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Guano", String.valueOf(104), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ad/Bandera_de_Guano.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Alausí", String.valueOf(116), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/16/Bandera_de_Alaus%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Chambo", String.valueOf(140), false, "Chimborazo", "Sierra", new URL("https://commons.wikimedia.org/wiki/File:Bandera_de_Chambo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Chunchi", String.valueOf(150), false, "Chimborazo", "Sierra", new URL("https://commons.wikimedia.org/wiki/File:Bandera_de_Chunchi.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Pallatanga", String.valueOf(151), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/25/Bandera_de_Pallatanga.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Guamote", String.valueOf(167), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_de_Guamote.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Villa La Unión (Cajabamba)", String.valueOf(170), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1e/Bandera_de_Colta.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Penipe", String.valueOf(205), false, "Chimborazo", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9e/Bandera_de_Penipe.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/14/Bandera_Provincia_Chimborazo.svg")));
        Constants.cities.add(new City("Latacunga", String.valueOf(20), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ac/BanderaLatacunga.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("La Maná", String.valueOf(51), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/06/Bandera_de_Salcedo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("San Miguel (Salcedo)", String.valueOf(78), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/06/Bandera_de_Salcedo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("Pujilí", String.valueOf(85), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/49/Bandera_del_Cant%C3%B3n_Pujil%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("Saquisilí", String.valueOf(109), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c2/Bandera_de_Saquisil%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("Sigchos", String.valueOf(180), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/3/31/Bandera_de_Sigchos.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("El Corazón", String.valueOf(184), false, "Cotopaxi", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c7/Bandera_de_Pangua.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Provincia_Cotopaxi.svg")));
        Constants.cities.add(new City("Machala", String.valueOf(5), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/e/eb/Machala_Flag.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Pasaje", String.valueOf(23), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/4b/BanderaPasaje.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Santa Rosa", String.valueOf(24), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/24/Bandera_de_Santa_Rosa.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Huaquillas", String.valueOf(26), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d8/Bandera_de_Huaquillas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("El Guabo", String.valueOf(55), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/9/90/Bandera_de_El_Guabo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Arenillas", String.valueOf(63), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/76/Bandera_de_Pi%C3%B1as.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Piñas", String.valueOf(69), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/76/Bandera_de_Pi%C3%B1as.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Zaruma", String.valueOf(90), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a1/Bandera_zaruma_2.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Portovelo", String.valueOf(102), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f7/Bandera_de_Portovelo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Balsas", String.valueOf(144), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2b/Bandera_de_Balsas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Marcabelí", String.valueOf(152), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/a/af/Bandera_de_Marcabel%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Paccha", String.valueOf(185), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/28/Bandera_atahualpa.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("La Victoria", String.valueOf(201), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f4/Bandera_de_Las_Lajas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Chilla", String.valueOf(207), false, "El Oro", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/51/Bandera_de_Chilla.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/2/20/Bandera_Provincia_El_Oro.svg")));
        Constants.cities.add(new City("Esmeraldas", String.valueOf(11), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Rosa Zárate (Quinindé)", String.valueOf(46), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_de_Quinind%C3%A9.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("San Lorenzo", String.valueOf(53), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/79/Bandera_de_San_Lorenzo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Atacames", String.valueOf(68), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2b/Bandera_de_Atacames.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Muisne", String.valueOf(125), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0a/Bandera_de_Muisne.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Valdez (Limones)", String.valueOf(132), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2c/Bandera_de_Eloy_Alfaro.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Rioverde", String.valueOf(157), false, "Esmeraldas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5d/Bandera_de_R%C3%ADo_Verde.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/91/Bandera_Provincia_Esmeraldas.svg")));
        Constants.cities.add(new City("Puerto Ayora", String.valueOf(80), false, "Galápagos", "Insular", new URL("https://upload.wikimedia.org/wikipedia/commons/9/93/Bandera_del_Cant%C3%B3n_Santa_Cruz.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/19/Bandera_Provincia_Gal%C3%A1pagos.svg")));
        Constants.cities.add(new City("Puerto Baquerizo Moreno", String.valueOf(113), false, "Galápagos", "Insular", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0c/Bandera_de_San_Crist%C3%B3bal.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/19/Bandera_Provincia_Gal%C3%A1pagos.svg")));
        Constants.cities.add(new City("Puerto Villamil", String.valueOf(176), false, "Galápagos", "Insular", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1c/Bandera_del_Cant%C3%B3n_Isabela.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/1/19/Bandera_Provincia_Gal%C3%A1pagos.svg")));
        Constants.cities.add(new City("Guayaquil", String.valueOf(1), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e3/Bandera_de_Guayaquil.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Durán", String.valueOf(6), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b3/Flag_of_Milagro.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Milagro", String.valueOf(14), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b3/Flag_of_Milagro.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Daule", String.valueOf(19), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0a/Flag_of_Daule.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Samborondón", String.valueOf(29), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5c/Flag_of_Samborond%C3%B3n.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Velasco Ibarra (El Empalme)", String.valueOf(37), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/fc/Flag_of_El_Empalme.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("El Triunfo", String.valueOf(39), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/56/Flag_of_El_Triunfo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("General Villamil (Playas)", String.valueOf(41), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/6b/Flag_of_General_Villamil.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Balzar", String.valueOf(47), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1e/Flag_of_Balzar.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Naranjito", String.valueOf(48), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/47/Flag_of_Naranjito.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Naranjal", String.valueOf(49), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/1/17/Flag_of_Italy_with_border.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Pedro Carbo", String.valueOf(59), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f8/Flag_of_Pedro_Carbo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Yaguachi", String.valueOf(61), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/81/Flag_of_Yaguachi.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Lomas de Sargentillo", String.valueOf(72), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c8/Flag_of_Lomas_de_Sargentillo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Salitre", String.valueOf(82), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/56/Flag_of_Urbina_Jado.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Balao", String.valueOf(91), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/84/Flag_of_Balao.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Santa Lucía", String.valueOf(95), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/43/Flag_of_Santa_Luc%C3%ADa.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Palestina", String.valueOf(97), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/3/3c/Flag_of_Palestina.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Alfredo Baquerizo Moreno (Jujan)", String.valueOf(98), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/21/Flag_of_Jujan.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Narcisa de Jesús (Nobol)", String.valueOf(99), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f4/Flag_of_Nobol.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Simón Bolívar", String.valueOf(106), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c2/Flag_of_Sim%C3%B3n_Bol%C3%ADvar.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Coronel Marcelino Maridueña", String.valueOf(110), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/e/ef/Flag_of_Marcelino_Maridue%C3%B1a.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Colimes", String.valueOf(120), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a8/Flag_of_Colimes.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("General Antonio Elizalde (Bucay)", String.valueOf(123), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/81/Flag_of_Bucay.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Isidro Ayora", String.valueOf(124), false, "Guayas", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b7/Flag_of_Isidro_Ayora.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/7/7c/Bandera_Provincia_Guayas.svg")));
        Constants.cities.add(new City("Ibarra", String.valueOf(15), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0d/Bandera_de_Ibarra.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Otavalo", String.valueOf(33), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/64/Bandera_de_Otavalo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Atuntaqui", String.valueOf(57), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ae/Bandera_de_Atuntaqui.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Cotacachi", String.valueOf(94), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a4/Bandera_de_Cotacachi.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Pimampiro", String.valueOf(133), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9c/Bandera_pimampiro.PNG"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Urcuquí", String.valueOf(155), false, "Imbabura", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0e/Bandera_de_San_Miguel_de_Urcuqu%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_Provincia_Imbabura.svg")));
        Constants.cities.add(new City("Loja", String.valueOf(9), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/11/Flag_of_Loja.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Catamayo", String.valueOf(54), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5c/Bandera_de_Catamayo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Cariamanga", String.valueOf(74), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_de_Calvas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Macará", String.valueOf(77), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/cd/Bandera_de_Macar%C3%A1.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Catacocha", String.valueOf(114), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_de_Paltas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Alamor", String.valueOf(139), false, "Loja", "Sierra", new URL("https://commons.wikimedia.org/wiki/File:Bandera_de_Puyango.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Celica", String.valueOf(142), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a6/Bandera_de_Celica.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Saraguro", String.valueOf(145), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/a7/Bandera_de_Saraguro.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Zapotillo", String.valueOf(169), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/48/Bandera_del_Cant%C3%B3n_Zapotillo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Pindal", String.valueOf(181), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2b/Bandera_de_Pindal.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Amaluza", String.valueOf(186), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/04/Bandera_de_Esp%C3%ADndola.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Gonzanamá", String.valueOf(190), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/01/Bandera_de_Gonzanam%C3%A1.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Chaguarpamba", String.valueOf(204), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9d/Bandera_de_Chaguarpamba.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Sozoranga", String.valueOf(210), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/7/71/Bandera_de_Sozoranga.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Quilanga", String.valueOf(213), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d4/Bandera_de_Olmedo_de_Loja.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Olmedo", String.valueOf(218), false, "Loja", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d4/Bandera_de_Olmedo_de_Loja.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/9/97/Bandera_Provincia_Loja.svg")));
        Constants.cities.add(new City("Manta", String.valueOf(7), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/80/Bandera_de_Manta.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Portoviejo", String.valueOf(8), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/8a/Flag_of_Portoviejo.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Chone", String.valueOf(22), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Bandera_Cantonal_de_Chone.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("El Carmen", String.valueOf(27), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/3/3f/Bandera_de_El_Carmen.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Montecristi", String.valueOf(28), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/67/Bandera_Montecristi.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Jipijapa", String.valueOf(31), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/8e/Bandera_de_Jipijapa.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Pedernales", String.valueOf(56), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5e/Bandera_de_Pedernales.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Bahía de Caráquez", String.valueOf(58), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/7a/Bandera_de_Bah%C3%ADa_de_Caraquez.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Calceta", String.valueOf(62), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/3/37/Bandera_de_Calceta.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Jaramijó", String.valueOf(64), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ab/Bandera_de_jaramijo.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Tosagua", String.valueOf(83), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/4c/Bandera_de_Tosagua.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Puerto López", String.valueOf(87), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/fb/Bandera_de_Puerto_L%C3%B3pez.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("San Vicente", String.valueOf(88), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/6b/BanderaSanVicente.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Santa Ana de Vuelta Larga", String.valueOf(89), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0e/Bandera_de_Santa_Ana_de_Vuelta_Larga.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Rocafuerte", String.valueOf(92), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/6/69/Bandera_de_Rocafuerte.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Paján", String.valueOf(111), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/0/09/Bandera_de_Paj%C3%A1n.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Flavio Alfaro", String.valueOf(119), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/b/b0/Bandera_de_Flavio_Alfaro.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Jama", String.valueOf(122), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/4/45/Bandera_de_Jama.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Junín", String.valueOf(130), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/0/03/Bandera_del_Cant%C3%B3n_Jun%C3%ADn.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Pichincha", String.valueOf(149), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ac/Bandera_del_Cant%C3%B3n_Pichincha.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Sucre", String.valueOf(164), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/7/73/Bandera_de_24_de_Mayo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Olmedo", String.valueOf(175), false, "Manabí", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_de_Olmedo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_Provincia_Manab%C3%AD.svg")));
        Constants.cities.add(new City("Macas", String.valueOf(60), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/7/79/Bandera_Macas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Sucúa", String.valueOf(103), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/8/84/Bandera_de_Suc%C3%BAa.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Gualaquiza", String.valueOf(107), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0b/Bandera_Canton_Gualaquiza.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("General Leonidas Plaza Gutiérrez (Limón)", String.valueOf(154), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/01/Banderadelimonindanza.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Palora", String.valueOf(159), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/2/25/Bandera_de_Palora.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Santiago de Méndez", String.valueOf(171), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/fb/Bandera_del_Cant%C3%B3n_Santiago.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Logroño", String.valueOf(188), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/6/65/Bandera_del_Cant%C3%B3n_Logro%C3%B1o.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("San Juan Bosco", String.valueOf(191), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/07/Bandera_del_Cant%C3%B3n_San_Juan_Bosco.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Santiago", String.valueOf(203), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2a/Bandera_de_Tiwintza.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Taisha", String.valueOf(206), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0c/Bandera_de_Taisha.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Huamboya", String.valueOf(212), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/5/59/Bandera_de_Huamboya.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Pablo Sexto", String.valueOf(217), false, "Morona Santiago", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/15/Bandera_del_Cant%C3%B3n_Pablo_XI.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bandera_Provincia_Morona_Santiago.svg")));
        Constants.cities.add(new City("Tena", String.valueOf(52), false, "Napo", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/5/51/Bandera_de_Tena.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/6a/Bandera_Provincia_Napo.svg")));
        Constants.cities.add(new City("Archidona", String.valueOf(129), false, "Napo", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/ff/Bandera_de_Archidona.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/6a/Bandera_Provincia_Napo.svg")));
        Constants.cities.add(new City("El Chaco", String.valueOf(146), false, "Napo", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c7/Bandera_de_El_Chaco.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/6a/Bandera_Provincia_Napo.svg")));
        Constants.cities.add(new City("Baeza", String.valueOf(183), false, "Napo", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/17/Bandera_de_Baeza.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/6a/Bandera_Provincia_Napo.svg")));
        Constants.cities.add(new City("Carlos Julio Arosemena Tola", String.valueOf(209), false, "Napo", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9c/Bandera_de_Carlos_Julio_Arosamena_Tola.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/6a/Bandera_Provincia_Napo.svg")));
        Constants.cities.add(new City("Puerto Francisco de Orellana", String.valueOf(30), false, "Orellana", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0d/Bandera_de_Puerto_Francisco_de_Orellana.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/0/0f/Bandera_Provincia_Orellana.svg")));
        Constants.cities.add(new City("La Joya de los Sachas", String.valueOf(81), false, "Orellana", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f2/Bandera_de_La_Joya_de_Los_Sachas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/0/0f/Bandera_Provincia_Orellana.svg")));
        Constants.cities.add(new City("Loreto", String.valueOf(156), false, "Orellana", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1b/Bandera_de_Aguarico.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/0/0f/Bandera_Provincia_Orellana.svg")));
        Constants.cities.add(new City("Nuevo Rocafuerte", String.valueOf(199), false, "Orellana", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1b/Bandera_de_Aguarico.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/0/0f/Bandera_Provincia_Orellana.svg")));
        Constants.cities.add(new City("Tiputini", String.valueOf(222), false, "Orellana", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1b/Bandera_de_Aguarico.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/0/0f/Bandera_Provincia_Orellana.svg")));
        Constants.cities.add(new City("Puyo", String.valueOf(43), false, "Pastaza", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e0/Bandera_del_Puyo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/3/3f/Bandera_Provincia_Pastaza.svg")));
        Constants.cities.add(new City("Santa Clara", String.valueOf(193), false, "Pastaza", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/fc/Bandera_del_Cant%C3%B3n_Santa_Clara.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/3/3f/Bandera_Provincia_Pastaza.svg")));
        Constants.cities.add(new City("Arajuno", String.valueOf(194), false, "Pastaza", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c2/Bandera_de_Arajuno.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/3/3f/Bandera_Provincia_Pastaza.svg")));
        Constants.cities.add(new City("Mera", String.valueOf(216), false, "Pastaza", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d2/Bandera_de_Mera.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/3/3f/Bandera_Provincia_Pastaza.svg")));
        Constants.cities.add(new City("Quito", String.valueOf(2), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Flag_of_Quito.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Sangolquí", String.valueOf(18), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/c/c9/Bandera_de_Sangolqu%C3%AD.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Cayambe", String.valueOf(34), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/d/d4/Bandera_de_Cayambe.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Machachi", String.valueOf(66), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/84/Bandera_de_Mej%C3%ADa.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Tabacundo", String.valueOf(86), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/a/ad/Bandera_de_Pedro_Moncayo.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Pedro Vicente Maldonado", String.valueOf(127), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/2/22/Bandera_Pedro_Vicente_Maldonado.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("San Miguel de Los Bancos", String.valueOf(135), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/8d/Bandera_San_Miguel.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Puerto Quito", String.valueOf(162), false, "Pichincha", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0d/Bandera_Puerto_Quito.jpg"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/82/Bandera_Provincia_Pichincha.svg")));
        Constants.cities.add(new City("Santo Domingo", String.valueOf(4), false, "Santo Domingo de los Tsáchilas", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/8/8b/Bandera_de_Sto._Domingo_de_los_Colorados.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/b/b2/Bandera_Provincia_Santo_Domingo_de_los_Ts%C3%A1chilas.svg")));
        Constants.cities.add(new City("La Concordia", String.valueOf(45), false, "Santo Domingo de los Tsáchilas", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/b/ba/La_Concordia_EC.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/b/b2/Bandera_Provincia_Santo_Domingo_de_los_Ts%C3%A1chilas.svg")));
        Constants.cities.add(new City("Nueva Loja", String.valueOf(25), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/e/ec/Bandera_Lago_Agrio.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("Shushufindi", String.valueOf(67), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/d/dc/Bandera_de_Shushufindi.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("Puerto El Carmen de Putumayo", String.valueOf(173), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e7/Bandera_del_Cant%C3%B3n_Putumayo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("El Dorado de Cascales", String.valueOf(177), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/fd/Bandera_de_Cascales.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("Lumbaqui", String.valueOf(178), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1a/Bandera_de_Lumbaqui.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("Tarapoa", String.valueOf(195), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/4/4f/Bandera_de_Cuyabeno.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("La Bonita", String.valueOf(220), false, "Sucumbíos", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/1/1b/Bandera_del_Cant%C3%B3n_Sucumb%C3%ADos.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/c/c0/Bandera_Provincia_Sucumb%C3%ADos.svg")));
        Constants.cities.add(new City("Ambato", String.valueOf(10), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Baños de Agua Santa", String.valueOf(75), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/1/12/Bandera_de_Ba%C3%B1os.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Pelileo", String.valueOf(84), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/9/98/Bandera_de_Pelileo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Píllaro", String.valueOf(105), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/4f/Bandera_de_P%C3%ADllaro.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Quero", String.valueOf(166), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/66/Bandera_de_Quero.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Cevallos", String.valueOf(168), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/7/7d/Bandera_del_Cant%C3%B3n_Cevallos.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Patate", String.valueOf(174), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/e/e3/Bandera_de_Patate.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Tisaleo", String.valueOf(196), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/6/68/Bandera_de_Tisaleo.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Mocha", String.valueOf(200), false, "Tungurahua", "Sierra", new URL("https://upload.wikimedia.org/wikipedia/commons/4/48/Bandera_de_Mocha.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/6/62/Bandera_Provincia_Tungurahua.svg")));
        Constants.cities.add(new City("Zamora", String.valueOf(79), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/b/bb/Bandera_del_Cant%C3%B3n_Zamora.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Yantzaza", String.valueOf(93), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/0/0e/Yantzazaflag.gif"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Zumba", String.valueOf(158), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/c/ca/Chinchipeflag.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("El Pangui", String.valueOf(161), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/6/66/Ec-z-pan.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Zumbi", String.valueOf(172), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/9/9f/Bandera_de_Centinela_del_C%C3%B3ndor.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Palanda", String.valueOf(179), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/7/7b/Bandera_de_Palanda.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Guayzimi", String.valueOf(182), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f1/Ec-z-nan.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("28 de Mayo (San José de Yacuambi)", String.valueOf(192), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/6/65/Yacuambiflag.gif"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("Paquisha", String.valueOf(208), false, "Zamora Chinchipe", "Oriente", new URL("https://upload.wikimedia.org/wikipedia/commons/5/5c/Bandera_de_Paquisha.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/5/58/Bandera_Provincia_Zamora_Chinchipe.svg")));
        Constants.cities.add(new City("La Libertad", String.valueOf(16), false, "Santa Elena", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/2d/Bandera_del_Cant%C3%B3n_La_Libertad.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/88/Santa_Elena_flag.png")));
        Constants.cities.add(new City("Santa Elena", String.valueOf(32), false, "Santa Elena", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/f/f9/Bandera_de_Santa_Elena.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/88/Santa_Elena_flag.png")));
        Constants.cities.add(new City("Salinas", String.valueOf(40), false, "Santa Elena", "Costa", new URL("https://upload.wikimedia.org/wikipedia/commons/2/22/Bandera_de_Salinas.png"), new URL("https://upload.wikimedia.org/wikipedia/commons/8/88/Santa_Elena_flag.png")));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        for (City city : Constants.cities) {
            Boolean isInteresting = sp.getBoolean(city.getCode(), false);
            if (isInteresting) {
                city.setInteresting(true);
            }
        }
    }

    public static boolean regexCheck(String expression, String value) {
        CharSequence inputStr = value;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean emailCheck(String value) {
        CharSequence inputStr = value;
        Pattern pattern = Pattern.compile(RGX_EMAIL);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean guiaRemisionValidator(String value){
        if(value == null) return false;
        Matcher matcher = Pattern.compile(RGX_GUIA_REMISION).matcher(value);
        return matcher.matches();
    }

    public static void setErrorView(View view) {
        setErrorView(view, "Campo obligatorio");
    }

    public static void setErrorView(View view, String error) {
        TextView errorText = (TextView) view;
        errorText.setError(error);
        errorText.setTextColor(Color.RED);
    }

    public static void resetErrorView(View... views) {
        if(views == null) return;
        for(View view : views){
            TextView errorText = (TextView) view;
            errorText.setTextColor(Color.BLACK);
            errorText.setError(null);
        }
    }

    public static void setVisibilityGone(View... views){
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    public static void setVisibilityVisible(View... views){
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setEmptyView(View view) {
        TextView errorText = (TextView) view;
        errorText.setTextColor(Color.BLACK);
        errorText.setText("");
    }

    public static void setEmptySelection(View view) {
        Spinner emptySpin = (Spinner) view;
        emptySpin.setSelection(-1);
    }

    public static boolean validateTextViewsRequired(TextView... textViews){
        boolean result = true;
        for(TextView textView : textViews){
            if (Utils.getTextValue(textView).equals("")) {
                Utils.setErrorView(textView);
                result = false;
            } else Utils.resetErrorView(textView);
        }
        return result;
    }

    public static boolean validateLengthRUC(TextView textView){
        boolean result = true;
        if(!Utils.validateTextViewsRequired(textView)) return false;

        Utils.resetErrorView(textView);
        if (textView.getText().length() != 13) {
            Utils.setErrorView(textView, "Ingrese los 13 dígitos del RUC.");
            result = false;
        }
        return result;
    }

    public static boolean validateLengthCedula(TextView textView){
        boolean result = true;
        if(!Utils.validateTextViewsRequired(textView)) return false;

        Utils.resetErrorView(textView);
        if (textView.getText().length() != 10) {
            Utils.setErrorView(textView, "Ingrese los 10 dígitos de la cédula.");
            result = false;
        }
        return result;
    }

    public static boolean validateSpinnersCatalogoItemRequired(Spinner... spinners){
        boolean result = true;
        for(Spinner spinner : spinners){
            CatalogoItemDTO catalogoItemDTO = (CatalogoItemDTO) Utils.getItemSpinner(spinner);
            if (catalogoItemDTO == null || catalogoItemDTO.getCatalogoItemIdCatalogo() == null) {
                result = false;
                Utils.setErrorView(spinner.getSelectedView());
            } else Utils.resetErrorView(spinner.getSelectedView());
        }
        return result;
    }

    public static boolean isErrorView(View... views) {
        boolean bn = false;
        for (View view : views) {
            TextView elemento = (TextView) view;
            if (elemento.getError() != null)
                bn = !elemento.getError().toString().isEmpty();
        }
        return bn;
    }

    public static void setEmptyCheck(View view) {
        CheckBox chkEmpty = (CheckBox) view;
        chkEmpty.setTextColor(Color.BLACK);
        chkEmpty.setSelected(false);
    }

    public static Object getItemSpinner(Spinner spinner) {
        if (spinner != null)
            if (spinner.getSelectedItem() != null)
                return spinner.getSelectedItem();
        return null;
    }

    public static Object getTextValue(TextView textView) {
        if (textView != null)
            if (textView.getText() != null)
                return String.valueOf(textView.getText());
        return "";
    }

    public static Integer getIntegerFromTextView(TextView textView) {
        if (textView != null)
            if (textView.getText() != null)
                try{
                    return Integer.valueOf((String)Utils.getTextValue(textView));
                }catch (Exception e){
                    return null;
                }
        return null;
    }

    public static Double getDoubleFromTextView(TextView textView){
        if (textView != null)
            if (textView.getText() != null)
                try{
                    return Double.valueOf((String) Utils.getTextValue(textView));
                }catch (Exception e){
                    return null;
                }
        return null;
    }

    public static BigDecimal getBigDecimalFromTextView(TextView textView){
        if (textView != null)
            if (textView.getText() != null)
                try{
                    return new BigDecimal((String) Utils.getTextValue(textView)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }catch (Exception e){
                    return BigDecimal.ZERO;
                }
        return BigDecimal.ZERO;
    }

    public static void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public static boolean isValidUser(Integer typeUser, int typeValidation) {
        if (typeUser != null) {
            List<Integer> validTypes = new ArrayList<>();
            if (typeValidation == 0) {
                validTypes.addAll(driverProfile());
                validTypes.addAll(customerProfile());
            } else if (typeValidation == 1) {
                validTypes.addAll(driverProfile());
            } else if (typeValidation == 2) {
                validTypes.addAll(customerProfile());
            }
            return validTypes.contains(typeUser);
        } else return false;
    }

    private static List<Integer> driverProfile() {
        List<Integer> driverType = new ArrayList<>();
        driverType.add(9);
        driverType.add(77);
        return driverType;
    }

    private static List<Integer> customerProfile() {
        List<Integer> customerType = new ArrayList<>();
        customerType.add(10);
        customerType.add(75);
        customerType.add(82);
        return customerType;
    }

    public static boolean isValidObject(Object object) {
        boolean ban = false;
        if (object == null)
            return ban;

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object objVal = field.get(object);

                if (objVal != null) {
                    ban = true;
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return ban;
    }

    public static void hideKeyboard(Activity activity){
        if(activity == null) return;
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
