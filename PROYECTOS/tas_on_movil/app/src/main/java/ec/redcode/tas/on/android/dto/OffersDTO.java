package ec.redcode.tas.on.android.dto;

public class OffersDTO {
    private int idOferta;
    private String idSolicitud;
    private String origen;
    private String destino;
    private Double peso;
    private String tipoPeso;
    private Double amount;
    private String date;
    private String dateCanceled;
    private String comments;
    private String invoiceId;
    private String supplier;
    private Long datePay;
    private Long fechaEntrega;
    private int expiredDay;
    private String numeroPrefactura;
    private Double comision = 0.0;
    private int numeroPiezas;
    private Double descuento = 0.0;
    private String typePay;
    private int invoicesTypePay;

    public int getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getTipoPeso() {
        return tipoPeso;
    }

    public void setTipoPeso(String tipoPeso) {
        this.tipoPeso = tipoPeso;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateCanceled() {
        return dateCanceled;
    }

    public void setDateCanceled(String dateCanceled) {
        this.dateCanceled = dateCanceled;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Long getDatePay() {
        return datePay;
    }

    public void setDatePay(Long datePay) {
        this.datePay = datePay;
    }

    public Long getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Long fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(int expiredDay) {
        this.expiredDay = expiredDay;
    }

    public String getNumeroPrefactura() {
        return numeroPrefactura;
    }

    public void setNumeroPrefactura(String numeroPrefactura) {
        this.numeroPrefactura = numeroPrefactura;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(int numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getTypePay() {
        return typePay;
    }

    public void setTypePay(String typePay) {
        this.typePay = typePay;
    }

    public int getInvoicesTypePay() {
        return invoicesTypePay;
    }

    public void setInvoicesTypePay(int invoicesTypePay) {
        this.invoicesTypePay = invoicesTypePay;
    }
}
