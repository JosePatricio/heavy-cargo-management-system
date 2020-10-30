package ec.redcode.tas.on.android.dto;

/**
 * Created by Walter on 20/3/18.
 */

public class ClienteDTO {

    private String ruc;
    private int idTipo;
    private String tipo;
    private int idLocalidad;
    private String localidad;
    private String razonSocial;
    private String direccion;
    private int diasCredito;
    private int periodoFacturacion;
    private int idCompanyType;
    private String companyType;

    public ClienteDTO(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public ClienteDTO() {
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }

    public int getPeriodoFacturacion() {
        return periodoFacturacion;
    }

    public void setPeriodoFacturacion(int periodoFacturacion) {
        this.periodoFacturacion = periodoFacturacion;
    }

    public int getIdCompanyType() {
        return idCompanyType;
    }

    public void setIdCompanyType(int idCompanyType) {
        this.idCompanyType = idCompanyType;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
}
