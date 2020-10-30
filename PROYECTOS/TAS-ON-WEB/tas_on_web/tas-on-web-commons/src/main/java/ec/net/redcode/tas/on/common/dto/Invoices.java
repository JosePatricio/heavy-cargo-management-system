package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Invoices {

    private String invoiceId;
    private String invoiceNumber;
    private Double invoiceAmount;
    private String company;
    private Timestamp invoiceDatePrefactura;
    private Timestamp invoiceDate;
    private Timestamp invoiceEmisionDate;
    private int diasFacturaVencida;
    private int invoicesTypePay;
    private String invoicesTypePayDesc;
    private double invoicesDiscount;
    private double invoiceBalanceDue;
    private double invoiceDescuento;

}
