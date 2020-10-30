package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "secuencia")
@NamedQueries({
        @NamedQuery(name = "Secuencia.SecuenciaByCliente", query = "select s from Secuencia s where s.secuenciaClienteId = :idCliente"),
        @NamedQuery(name = "Secuencia.SecuenciaByNemonico", query = "select s from Secuencia s where s.secuenciaClienteNemonico = :nemonico")
})
public class Secuencia {

    private int secuenciaId;
    private String secuenciaClienteId;
    private String secuenciaClienteNemonico;
    private int secuencia;
    private int secuenciaIncremental;
    private int secuenciaValorInicial;
    private int secuenciaValorFinal;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "secuencia_id", nullable = false)
    public int getSecuenciaId() {
        return secuenciaId;
    }

    public void setSecuenciaId(int secuenciaId) {
        this.secuenciaId = secuenciaId;
    }

    @Basic
    @Column(name = "secuencia_cliente_id", nullable = true)
    public String getSecuenciaClienteId() {
        return secuenciaClienteId;
    }

    public void setSecuenciaClienteId(String secuenciaClienteId) {
        this.secuenciaClienteId = secuenciaClienteId;
    }

    @Basic
    @Column(name = "secuencia_cliente_nemonico", nullable = true)
    public String getSecuenciaClienteNemonico() {
        return secuenciaClienteNemonico;
    }

    public void setSecuenciaClienteNemonico(String secuenciaClienteNemonico) {
        this.secuenciaClienteNemonico = secuenciaClienteNemonico;
    }

    @Basic
    @Column(name = "secuencia", nullable = true)
    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    @Basic
    @Column(name = "secuencia_incremental", nullable = true)
    public int getSecuenciaIncremental() {
        return secuenciaIncremental;
    }

    public void setSecuenciaIncremental(int secuenciaIncremental) {
        this.secuenciaIncremental = secuenciaIncremental;
    }

    @Basic
    @Column(name = "secuencia_valor_inicial", nullable = true)
    public int getSecuenciaValorInicial() {
        return secuenciaValorInicial;
    }

    public void setSecuenciaValorInicial(int secuenciaValorInicial) {
        this.secuenciaValorInicial = secuenciaValorInicial;
    }

    @Basic
    @Column(name = "secuencia_valor_final", nullable = true)
    public int getSecuenciaValorFinal() {
        return secuenciaValorFinal;
    }

    public void setSecuenciaValorFinal(int secuenciaValorFinal) {
        this.secuenciaValorFinal = secuenciaValorFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Secuencia secuencia1 = (Secuencia) o;
        return secuenciaId == secuencia1.secuenciaId &&
                secuencia == secuencia1.secuencia &&
                secuenciaIncremental == secuencia1.secuenciaIncremental &&
                secuenciaValorInicial == secuencia1.secuenciaValorInicial &&
                secuenciaValorFinal == secuencia1.secuenciaValorFinal &&
                Objects.equals(secuenciaClienteId, secuencia1.secuenciaClienteId);
    }

}
