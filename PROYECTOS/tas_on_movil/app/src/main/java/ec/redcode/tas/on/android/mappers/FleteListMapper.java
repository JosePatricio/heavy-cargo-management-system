package ec.redcode.tas.on.android.mappers;

import ec.redcode.tas.on.android.dto.FleteListDTO;
import ec.redcode.tas.on.android.dto.OffersDTO;
import ec.redcode.tas.on.android.dto.SolicitudDTO;
import ec.redcode.tas.on.android.dto.responses.OfertListDTO;
import ec.redcode.tas.on.android.models.FleteShort;

/**
 * Created by Josue Ortiz on 29/12/2017.
 * Mapeo de respuestas de ofertas y fletes a FleteShort
 */

public class FleteListMapper {

    public static FleteShort responseToFleteList(FleteListDTO response) {

        FleteShort flete = new FleteShort();

        flete.setDays(Integer.parseInt(response.getDias()));
        flete.setDayPay(response.getDiasPagos());
        flete.setWeight(Float.parseFloat(response.getPeso()));
        flete.setDestinationCity(response.getDestino());
        flete.setOriginCity(response.getOrigen());
        flete.setId(response.getIdSolicitud());
        flete.setWeightType(response.getMedida());
        flete.setNumeroPiezas(response.getNumeroPiezas());
        flete.setFechaEntrega(response.getFechaEntrega());
        flete.setPersonaRecibe(response.getPersonaRecibe());

        return flete;
    }

    public static FleteShort responseToFleteList(OfertListDTO response) {
        FleteShort flete = new FleteShort();

        flete.setDate(response.getDate());
        flete.setAmount(Float.parseFloat(response.getAmount()));
        flete.setWeight(Float.parseFloat(response.getPeso()));
        flete.setDestinationCity(response.getDestino());
        flete.setOriginCity(response.getOrigen());
        flete.setId(response.getIdSolicitud());
        flete.setIdSolicitud(response.getIdSolicitud());
        flete.setIdOferta(response.getIdOferta());
        flete.setNumeroPiezas(response.getNumeroPiezas());
        flete.setDescuento(response.getDescuento());
        return flete;
    }

    public static FleteShort responseToFleteList(OffersDTO response) {
        FleteShort flete = new FleteShort();

        flete.setDate(response.getDate());
        flete.setAmount(response.getAmount().floatValue());
        flete.setId(response.getIdSolicitud());
        flete.setIdOferta(response.getIdOferta());
        flete.setOriginCity(response.getOrigen());
        flete.setDestinationCity(response.getDestino());
        flete.setWeight(response.getPeso() != null ? response.getPeso().floatValue() : 0f);
        flete.setDays(response.getExpiredDay());
        flete.setDayPay(response.getDatePay() != null ? response.getDatePay().intValue() : null);
        flete.setAmount(response.getAmount().floatValue());
        flete.setDate(response.getDate());
        flete.setWeightType(response.getTipoPeso());
        flete.setIdSolicitud(response.getIdSolicitud());
        flete.setNumeroPiezas(response.getNumeroPiezas());
        flete.setDescuento(response.getDescuento().floatValue());
        flete.setFechaEntrega(response.getFechaEntrega());

        return flete;
    }

    public static FleteShort responseToFleteList(SolicitudDTO response) {
        FleteShort flete = new FleteShort();
        flete.setId(response.getIdSolicitud());
        flete.setOriginCity(response.getOrigen());
        flete.setDestinationCity(response.getDestino());
        flete.setWeight(response.getPeso().floatValue());
        flete.setDays(response.getDiasPago());
        flete.setWeightType(response.getTipoPeso());
        flete.setIdSolicitud(response.getIdSolicitud());
        flete.setNumeroPiezas(response.getNumeroPiezas());
        flete.setFechaEntrega(response.getFechaEntrega());

        return flete;
    }
}
