package ec.redcode.tas.on.android.dto;

import java.util.List;

public class InvoiceDetailDTO {
    String invoiceId;
    String invoiceNumer;
    String authorizationNumber;
    String referenceNumber;
    long invoiceDatePreFactura;
    long invoiceDateFactura;
    int invoiceTypePay;
    double invoiceDiscount;
    ClienteDTO tason;
    ClienteDTO client;
    List<OffersDTO> offers;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumer() {
        return invoiceNumer;
    }

    public void setInvoiceNumer(String invoiceNumer) {
        this.invoiceNumer = invoiceNumer;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public long getInvoiceDatePreFactura() {
        return invoiceDatePreFactura;
    }

    public void setInvoiceDatePreFactura(long invoiceDatePreFactura) {
        this.invoiceDatePreFactura = invoiceDatePreFactura;
    }

    public long getInvoiceDateFactura() {
        return invoiceDateFactura;
    }

    public void setInvoiceDateFactura(long invoiceDateFactura) {
        this.invoiceDateFactura = invoiceDateFactura;
    }

    public int getInvoiceTypePay() {
        return invoiceTypePay;
    }

    public void setInvoiceTypePay(int invoiceTypePay) {
        this.invoiceTypePay = invoiceTypePay;
    }

    public double getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(double invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
    }

    public ClienteDTO getTason() {
        return tason;
    }

    public void setTason(ClienteDTO tason) {
        this.tason = tason;
    }

    public ClienteDTO getClient() {
        return client;
    }

    public void setClient(ClienteDTO client) {
        this.client = client;
    }

    public List<OffersDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<OffersDTO> offers) {
        this.offers = offers;
    }
}
