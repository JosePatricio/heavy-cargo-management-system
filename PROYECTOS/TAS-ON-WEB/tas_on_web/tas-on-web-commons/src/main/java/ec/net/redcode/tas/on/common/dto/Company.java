package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Company implements Serializable {

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

}
