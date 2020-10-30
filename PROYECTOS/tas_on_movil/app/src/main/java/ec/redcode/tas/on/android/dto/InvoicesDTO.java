package ec.redcode.tas.on.android.dto;

public class InvoicesDTO {
    private String invoiceId;
    private String invoiceNumber;
    private Double invoiceAmount;
    private String company;
    private long invoiceDatePrefactura;
    private long invoiceDate;
    private float invoicesDiscount;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getInvoiceDatePrefactura() {
        return invoiceDatePrefactura;
    }

    public void setInvoiceDatePrefactura(long invoiceDatePrefactura) {
        this.invoiceDatePrefactura = invoiceDatePrefactura;
    }

    public long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public float getInvoicesDiscount() {
        return invoicesDiscount;
    }

    public void setInvoicesDiscount(float invoicesDiscount) {
        this.invoicesDiscount = invoicesDiscount;
    }
}
