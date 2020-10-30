package ec.redcode.tas.on.android.mappers;

import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.models.Flete;

/**
 * Created by User on 29/12/2017.
 */

public class FleteMapper {


    public static Flete fleteDTOtoFlete(FleteDTO response) {
        Flete flete = new Flete();

        flete.setOriginCity(response.getOrigen() == null ? "" : response.getOrigen());
        flete.setDestinationCity(response.getDestino() == null ? "" : response.getDestino());
        flete.setShippingDate(response.getFechaEnvio() == null ? 0 : Long.parseLong(response.getFechaEnvio()));
        flete.setAddress(response.getDireccion() == null ? "" : response.getDireccion());
        flete.setSenderName(response.getPersonaEntrega() == null ? "" : response.getPersonaEntrega());

        flete.setDiasPago(response.getDiasPago());

        flete.setProducType(response.getTipo() == null ? "" : response.getTipo());
        flete.setQuantityOfPieces(response.getNumeroPiezas() == null ? 0 : Integer.parseInt(response.getNumeroPiezas()));
        flete.setWeight(response.getPeso() == null ? 0 : Float.parseFloat(response.getPeso()));
        flete.setVolume(response.getVolumen() == null ? 0 : Float.parseFloat(response.getVolumen()));
        flete.setDeliveryDate(response.getFechaEntrega() == null ? 0 : Long.parseLong(response.getFechaEntrega()));

        flete.setReceiverName(response.getPersonaRecibe() == null ? "" : response.getPersonaRecibe());
        flete.setReceiverPhone(null);
        flete.setReceiverEmail(null);
        flete.setReceiverAddress(response.getDireccionEntrega() == null ? "" : response.getDireccionEntrega());
        flete.setBestOffer(response.getBestOffer() == null ? "" : response.getBestOffer());
        flete.setAverageOffer(response.getAverageOffer() == null ? "" : response.getAverageOffer());
        flete.setTotalOffer(response.getTotalOffer() == null ? "" : response.getTotalOffer());

        flete.setTipoVolumen(response.getTipoVolumen());
        flete.setNumeroEstibadores(response.getNumeroEstibadores());
        flete.setFechaEntrega(response.getFechaEntrega() == null ? "" : response.getFechaEntrega());
        flete.setFechaEnvio(response.getFechaEnvio() == null ? "" : response.getFechaEnvio());

        flete.setTipoPeso(response.getTipoPeso() == null ? "" : response.getTipoPeso());
        flete.setObservaciones(response.getObservaciones());
        return flete;
    }

}
