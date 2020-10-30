package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class InvoiceDetail {

    private String invoiceId;
    private String invoiceNumer;
    private String authorizationNumber;
    private String referenceNumber;
    private Timestamp invoiceDatePreFactura;
    private Timestamp invoiceDateFactura;
    private int invoiceTypePay;
    private double invoiceDiscount;
    private Company tason;
    private Company client;
    private List<Offers> offers;

}
