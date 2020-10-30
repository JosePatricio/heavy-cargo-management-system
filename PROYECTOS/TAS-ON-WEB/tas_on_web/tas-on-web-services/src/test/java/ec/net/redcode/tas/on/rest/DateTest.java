package ec.net.redcode.tas.on.rest;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.Cliente;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {

    public static void main(String[] args) throws ParseException {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaInicial=dateFormat.parse("2018-02-23");
        Date fechaFinal= new Date();

        int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

        System.out.println("Hay "+dias+" dias de diferencia");*/

        System.out.println("-------------------------------------------");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -33);
        Timestamp nowTimestamp = new Timestamp(calendar.getTimeInMillis());
        System.out.println("Fecha de entrega: " + nowTimestamp);
        Timestamp timestamp = getFechaFacturacion(58, nowTimestamp);
        System.out.println("Fecha de facturacion: " + new Date(timestamp.getTime()));
    }

    /**
     *
     * @param periocidad
     * @param fechaEntrega
     * @return
     */
    public static Timestamp getFechaFacturacion(int periocidad, Timestamp fechaEntrega){
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(fechaEntrega.getTime());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(day);
        Timestamp timestamp = null;
        switch (periocidad){
            case Constant.DIARIA:
                timestamp = new Timestamp(calendar.getTimeInMillis());
                break;
            case Constant.SEMANAL:
                switch (day){
                    case Calendar.FRIDAY:
                        calendar.add(Calendar.DATE, 6);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.SATURDAY:
                        calendar.add(Calendar.DATE, 5);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.SUNDAY:
                        calendar.add(Calendar.DATE, 4);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.MONDAY:
                        calendar.add(Calendar.DATE, 3);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.TUESDAY:
                        calendar.add(Calendar.DATE, 2);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.WEDNESDAY:
                        calendar.add(Calendar.DATE, 1);
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                    case Calendar.THURSDAY:
                        timestamp = new Timestamp(calendar.getTimeInMillis());
                        break;
                }
                break;
            case Constant.QUINCENAL:
                System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
                System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
                System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
                System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
                System.out.println("getActualMaximum: " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Calendar calendarLess = Calendar.getInstance();
                //calendarLess.setTimeInMillis(fechaEntrega.getTime());
                calendarLess = getThursdayWeekly(calendarLess, calendar);
                timestamp = new Timestamp(calendarLess.getTimeInMillis());
                break;
            case Constant.MENSUAL:
                Calendar calendarNow = Calendar.getInstance();
                //calendarNow.setTimeInMillis(fechaEntrega.getTime());
                int lastDate = calendar.getActualMaximum(Calendar.DATE);
                calendarNow.set(Calendar.DATE, lastDate);
                calendarNow = getLastThursdayMonthly(calendarNow);
                if (calendar.before(calendarNow) || calendar.equals(calendarNow))
                    timestamp = new Timestamp(calendarNow.getTimeInMillis());
                else {
                    calendarNow.add(Calendar.MONTH, 1);
                    lastDate = calendarNow.getActualMaximum(Calendar.DATE);
                    calendarNow.set(Calendar.DATE, lastDate);
                    calendarNow = getLastThursdayMonthly(calendarNow);
                    timestamp = new Timestamp(calendarNow.getTimeInMillis());
                }
                break;
        }
        return timestamp;
    }

    private static Calendar getThursdayWeekly(Calendar calendar, Calendar fechaEntrega){
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth <= 15) {
            int less = 15 - dayOfMonth;
            calendar.add(Calendar.DATE, less);
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY){
                calendar.add(Calendar.DATE, -1);
            }
        }
        else {
            int lastDate = calendar.getActualMaximum(Calendar.DATE);
            calendar.set(Calendar.DATE, lastDate);
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY){
                calendar.add(Calendar.DATE, -1);
            }
            if (calendar.before(fechaEntrega)){
                calendar.add(Calendar.DATE, 15);
                calendar = getThursdayWeekly(calendar, fechaEntrega);
            }
        }
        return calendar;
    }

    private static Calendar getLastThursdayMonthly(Calendar calendar){
        int lastDay = calendar.get(Calendar.DAY_OF_WEEK);
        while (lastDay != Calendar.THURSDAY){
            calendar.add(Calendar.DATE, -1);
            lastDay = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println("Last Date: " + calendar.getTime());
            System.out.println("Last Day : " + lastDay);
        }
        return calendar;
    }

}
