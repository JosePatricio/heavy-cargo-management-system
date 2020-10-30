package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Offers {

    private int idOferta;
    private String idSolicitud;
    private int numeroPiezas;
    private String provinciaOrigen;
    private String origen;
    private String provinciaDestino;
    private String destino;
    private Double peso;
    private String tipoPeso;
    private Double amount;
    private String date;
    private String dateCanceled;
    private String comments;
    private String invoiceId;
    private String invoiceClientId;
    private String supplier;
    private Date datePay;
    private Timestamp fechaEntrega;
    private int expiredDay;
    private String numeroPrefactura;
    private Double comision = 0.0;
    private Double descuento = 0.0;
    private String typePay;
    private int invoicesTypePay;
    private String numeroDocCliente;
    private Date fechaPago;
    private Double valorObjetivo;
}
