package ec.redcode.tas.on.android.models;

import lombok.Data;

/**
 * Created by User on 10/11/2017.
 */

@Data
public class Flete {

    private String id;
    private String originCity;
    private String destinationCity;
    private String producType;
    private int quantityOfPieces;

    private float volume;
    private float weight;
    private long deliveryDate;
    private long ShippingDate;
    private String receiverName;

    private String receiverPhone;
    private String receiverEmail;
    private String receiverAddress;
    private String city;

    private String address;
    private String senderName;
    private String bestOffer;
    private String averageOffer;
    private String totalOffer;

    private Integer diasPago;
    private String tipoVolumen;
    private String numeroEstibadores;
    private String fechaEntrega;
    private String fechaEnvio;

    private String tipoPeso;
    private String observaciones;

}