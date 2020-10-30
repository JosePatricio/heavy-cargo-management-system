package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "vehiculo_acreditado")
public class VehiculoAcreditado {
    private int vehiculoAcreditadoId;
    private int vehiculoAcreditadoVehiculoId;
    private String vehiculoAcreditadoClienteRuc;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "vehiculo_acreditado_id")
    public int getVehiculoAcreditadoId() {
        return vehiculoAcreditadoId;
    }

    public void setVehiculoAcreditadoId(int vehiculosAcreditadosId) {
        this.vehiculoAcreditadoId = vehiculosAcreditadosId;
    }

    @Column(name = "vehiculo_acreditado_vehiculo_id")
    public int getVehiculoAcreditadoVehiculoId() {
        return vehiculoAcreditadoVehiculoId;
    }

    public void setVehiculoAcreditadoVehiculoId(int vehiculosAcreditadosVehiculoId) {
        this.vehiculoAcreditadoVehiculoId = vehiculosAcreditadosVehiculoId;
    }

    @Column(name = "vehiculo_acreditado_cliente_ruc")
    public String getVehiculoAcreditadoClienteRuc() {
        return vehiculoAcreditadoClienteRuc;
    }

    public void setVehiculoAcreditadoClienteRuc(String vehiculosAcreditadosClienteRuc) {
        this.vehiculoAcreditadoClienteRuc = vehiculosAcreditadosClienteRuc;
    }
}
