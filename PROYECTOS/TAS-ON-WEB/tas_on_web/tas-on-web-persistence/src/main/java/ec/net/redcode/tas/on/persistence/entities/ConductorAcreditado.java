package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "conductor_acreditado")
public class ConductorAcreditado {
    private int conductorAcreditadoId;
    private int conductorAcreditadoConductorId;
    private String conductorAcreditadoClienteRuc;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "conductor_acreditado_id")
    public int getConductorAcreditadoId() {
        return conductorAcreditadoId;
    }

    public void setConductorAcreditadoId(int conductoresAcreditadosId) {
        this.conductorAcreditadoId = conductoresAcreditadosId;
    }

    @Column(name = "conductor_acreditado_conductor_id")
    public int getConductorAcreditadoConductorId() {
        return conductorAcreditadoConductorId;
    }

    public void setConductorAcreditadoConductorId(int conductoresAcreditadosConductorId) {
        this.conductorAcreditadoConductorId = conductoresAcreditadosConductorId;
    }

    @Column(name = "conductor_acreditado_cliente_ruc")
    public String getConductorAcreditadoClienteRuc() {
        return conductorAcreditadoClienteRuc;
    }

    public void setConductorAcreditadoClienteRuc(String conductoresAcreditadosClienteRuc) {
        this.conductorAcreditadoClienteRuc = conductoresAcreditadosClienteRuc;
    }
}
