package ec.redcode.tas.on.android.dto;

import java.sql.Timestamp;
import java.util.List;

public class InvoiceDTO {
    String numberInvoice;
    String rucSupplier;
    Double amount;
    String idInvoice;
    String personInvoice;
    String authNroInvoice;
    int invoiceTypePay;
    double invoiceDiscount;
    Timestamp dateAuthInvoice;
    List<Integer> offersId;

    public String getNumberInvoice() {
        return numberInvoice;
    }

    public void setNumberInvoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
    }

    public String getRucSupplier() {
        return rucSupplier;
    }

    public void setRucSupplier(String rucSupplier) {
        this.rucSupplier = rucSupplier;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getPersonInvoice() {
        return personInvoice;
    }

    public void setPersonInvoice(String personInvoice) {
        this.personInvoice = personInvoice;
    }

    public String getAuthNroInvoice() {
        return authNroInvoice;
    }

    public void setAuthNroInvoice(String authNroInvoice) {
        this.authNroInvoice = authNroInvoice;
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

    public Timestamp getDateAuthInvoice() {
        return dateAuthInvoice;
    }

    public void setDateAuthInvoice(Timestamp dateAuthInvoice) {
        this.dateAuthInvoice = dateAuthInvoice;
    }

    public List<Integer> getOffersId() {
        return offersId;
    }

    public void setOffersId(List<Integer> offersId) {
        this.offersId = offersId;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "numberInvoice='" + numberInvoice + '\'' +
                ", rucSupplier='" + rucSupplier + '\'' +
                ", amount=" + amount +
                ", idInvoice='" + idInvoice + '\'' +
                ", personInvoice='" + personInvoice + '\'' +
                ", authNroInvoice='" + authNroInvoice + '\'' +
                ", invoiceTypePay=" + invoiceTypePay +
                ", invoiceDiscount=" + invoiceDiscount +
                ", dateAuthInvoice=" + dateAuthInvoice +
                ", offersId=" + offersId +
                '}';
    }
}
