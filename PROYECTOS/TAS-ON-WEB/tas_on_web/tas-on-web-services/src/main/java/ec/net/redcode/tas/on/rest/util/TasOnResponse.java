package ec.net.redcode.tas.on.rest.util;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.exception.TasOnErrorMessage;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import org.apache.camel.Exchange;
import org.apache.cxf.rs.security.oauth2.common.*;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TasOnResponse {

    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    protected Response response(Object object){
        return Response.status(Response.Status.OK)
                .entity(object)
                .build();
    }

    protected Response responseNoContent(){
        return Response.status(Response.Status.NO_CONTENT)
                .entity(null)
                .build();
    }

    protected Response responseGeneric(Object object, Response.Status status){
        return Response.status(status)
                .entity(object)
                .build();
    }

    public void webFault(int code, String reason, Exchange exchange, boolean stop) {
        TasOnException exception = new TasOnException(code, reason, reason);
        TasOnErrorMessage errorMessage = new TasOnErrorMessage(exception);
        Response r = Response.status(code).entity(errorMessage).build();
        if (stop) {
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
        throw new WebApplicationException(r);
    }

    protected void webFault(int code, String reason, Exception e, Exchange exchange, boolean stop) {
        TasOnException exception = new TasOnException(code, reason, e.getLocalizedMessage());
        TasOnErrorMessage errorMessage = new TasOnErrorMessage(exception);
        Response r = Response.status(code).entity(errorMessage).build();
        throw new WebApplicationException(r);
    }

    protected void webFault(TasOnException ex, Exchange exchange, boolean stop){
        TasOnErrorMessage errorMessage = new TasOnErrorMessage(ex);
        Response r = Response.status(ex.getStatus()).entity(errorMessage).build();
        if (stop) {
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
        throw new WebApplicationException(r);
    }

    /**
     *
     * @param periocidad
     * @return
     */
    protected Timestamp getFechaFacturacion(int periocidad){
        Calendar calendar = Calendar.getInstance();
        Timestamp timestamp = null;
        switch (periocidad){
            case Constant.DIARIA:
                timestamp = new Timestamp(calendar.getTimeInMillis());
                break;
            case Constant.SEMANAL:
                int day = calendar.get(Calendar.DAY_OF_WEEK);
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
                        timestamp = new Timestamp(calendar.getTimeInMillis());;
                        break;
                }
                break;
            case Constant.QUINCENAL:
                Calendar calendarLess = Calendar.getInstance();
                calendarLess = getThursdayWeekly(calendarLess, calendar);
                timestamp = new Timestamp(calendarLess.getTimeInMillis());
                break;
            case Constant.MENSUAL:
                Calendar calendarNow = Calendar.getInstance();
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

    /**
     *
     * @param calendar
     * @return
     */
    private Calendar getLastThursdayMonthly(Calendar calendar){
        int lastDay = calendar.get(Calendar.DAY_OF_WEEK);
        while (lastDay != Calendar.THURSDAY){
            calendar.add(Calendar.DATE, -1);
            lastDay = calendar.get(Calendar.DAY_OF_WEEK);
        }
        return calendar;
    }

    /**
     *
     * @param calendar
     * @param fechaEntrega
     * @return
     */
    private Calendar getThursdayWeekly(Calendar calendar, Calendar fechaEntrega){
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

    /**
     * Permite crear el token
     *
     * @param usuario
     * @param subject
     * @return
     */
    protected String generateToken(Usuario usuario, String subject, OAuthDataProvider oAuthDataProvider){
        Client client = new Client();
        client.setClientId(usuario.getUsuarioNombreUsuario());
        client.setClientSecret(usuario.getUsuarioContrasenia());
        client.setConfidential(Boolean.TRUE);
        UserSubject userSubject = new UserSubject();
        userSubject.setId(subject);
        userSubject.setAuthenticationMethod(AuthenticationMethod.PASSWORD);
        client.setSubject(userSubject);
        AccessTokenRegistration tokenRegistration = new AccessTokenRegistration();
        tokenRegistration.setClient(client);
        tokenRegistration.setSubject(userSubject);
        ServerAccessToken token = oAuthDataProvider.createAccessToken(tokenRegistration);
        return token.getTokenKey();
    }

}
