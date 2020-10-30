package ec.redcode.tas.on.android.models;

/**
 * Created by Josue Ortiz on 29/12/2017.
 * Datos para una fila preview de un flete
 */

public class FleteShort {

    private String id;
    private String originCity;
    private String destinationCity;
    private float weight;
    private int days;
    private Integer dayPay;
    private float amount;
    private String date;
    private String weightType;
    private String idSolicitud;
    private int idOferta;
    private int numeroPiezas;
    private float descuento;
    private long fechaEntrega;
    private String personaRecibe;

    public FleteShort(String id, String originCity, String destinationCity) {
        this.id = id;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
    }

    public FleteShort() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Integer getDayPay() {
        return dayPay;
    }

    public void setDayPay(Integer dayPay) {
        this.dayPay = dayPay;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(int numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public long getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(long fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getPersonaRecibe() {
        return personaRecibe;
    }

    public void setPersonaRecibe(String personaRecibe) {
        this.personaRecibe = personaRecibe;
    }

    @Override
    public String toString() {
        return "FleteShort{" +
                "id='" + id + '\'' +
                ", originCity='" + originCity + '\'' +
                ", destinationCity='" + destinationCity + '\'' +
                ", weight=" + weight +
                ", days=" + days +
                ", dayPay=" + dayPay +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", weightType='" + weightType + '\'' +
                ", idSolicitud='" + idSolicitud + '\'' +
                ", idOferta=" + idOferta +
                ", numeroPiezas=" + numeroPiezas +
                ", descuento=" + descuento +
                ", fechaEntrega=" + fechaEntrega +
                ", personaRecibe='" + personaRecibe + '\'' +
                '}';
    }
}
