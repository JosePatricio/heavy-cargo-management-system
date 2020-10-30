package ec.redcode.tas.on.android.dto;

/**
 * Created by Walter on 21/3/18.
 */

public class VehiculoDTO {
    private int vehiculoId;
    private String vehiculoUsuario;
    private String vehiculoModelo;
    private int vehiculoAnio;
    private String vehiculoPlaca;
    private int vehiculoTipoCarga;
    private int vehiculoTipoCamion;
    private boolean vehiculoCertificadoArcsa;
    private double vehiculoCapacidad;
    private int vehiculoTipoCapacidad;

    public int getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(int vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getVehiculoUsuario() {
        return vehiculoUsuario;
    }

    public void setVehiculoUsuario(String vehiculoUsuario) {
        this.vehiculoUsuario = vehiculoUsuario;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    public int getVehiculoAnio() {
        return vehiculoAnio;
    }

    public void setVehiculoAnio(int vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public int getVehiculoTipoCarga() {
        return vehiculoTipoCarga;
    }

    public void setVehiculoTipoCarga(int vehiculoTipoCarga) {
        this.vehiculoTipoCarga = vehiculoTipoCarga;
    }

    public int getVehiculoTipoCamion() {
        return vehiculoTipoCamion;
    }

    public void setVehiculoTipoCamion(int vehiculoTipoCamion) {
        this.vehiculoTipoCamion = vehiculoTipoCamion;
    }

    public boolean isVehiculoCertificadoArcsa() {
        return vehiculoCertificadoArcsa;
    }

    public void setVehiculoCertificadoArcsa(boolean vehiculoCertificadoArcsa) {
        this.vehiculoCertificadoArcsa = vehiculoCertificadoArcsa;
    }

    public double getVehiculoCapacidad() {
        return vehiculoCapacidad;
    }

    public void setVehiculoCapacidad(double vehiculoCapacidad) {
        this.vehiculoCapacidad = vehiculoCapacidad;
    }

    public int getVehiculoTipoCapacidad() {
        return vehiculoTipoCapacidad;
    }

    public void setVehiculoTipoCapacidad(int vehiculoTipoCapacidad) {
        this.vehiculoTipoCapacidad = vehiculoTipoCapacidad;
    }
}
