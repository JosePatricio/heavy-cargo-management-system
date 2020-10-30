package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "contrato")
@NamedQueries({

        @NamedQuery(name = "Contrato.ContratoByContratoDocumentoCliente", query = "select c from Contrato c" +
                " where c.contratoDocumentoCliente = :contratoDocumentoCliente  and c.contratoEstado =:contratoEstado"),
        @NamedQuery(name = "Contrato.ContratoByContratoDocumentoConductor", query = "select c from Contrato c" +
                " where c.contratoDocumentoConductor = :contratoDocumentoConductor  and c.contratoEstado =:contratoEstado"),
        @NamedQuery(name = "Contrato.ContratoByContratoIdSolicitud", query = "select c from Contrato c" +
                " where c.contratoIdSolicitud = :contratoIdSolicitud  and c.contratoEstado =:contratoEstado"),
        @NamedQuery(name = "Contrato.ContratoByContratoFechaContrato", query = "select c from Contrato c" +
                " where c.contratoFechaContrato BETWEEN :fechaInicio AND :fechaFin " +
                " and c.contratoEstado =:contratoEstado")
})
public class Contrato {
    private int contratoId;
    private String contratoDocumentoCliente;
    private String contratoDocumentoConductor;
    private Integer contratoIdSolicitud;
    private String contratoIpCliente;
    private String contratoIpConductor;
    private String contratoImeiConductor;
    private Timestamp contratoFechaContrato;
    private Double contratoValor;
    private Integer contratoEstado;
    private Usuario usuarioByContratoDocumentoCliente;
    private Usuario usuarioByContratoDocumentoConductor;
    private SolicitudEnvio solicitudEnvioByContratoIdSolicitud;
    private CatalogoItem catalogoItemByContratoEstado;

    @Id
    @Column(name = "contrato_id", nullable = false)
    public int getContratoId() {
        return contratoId;
    }

    public void setContratoId(int contratoId) {
        this.contratoId = contratoId;
    }

    @Basic
    @Column(name = "contrato_documento_cliente", nullable = true, length = 13)
    public String getContratoDocumentoCliente() {
        return contratoDocumentoCliente;
    }

    public void setContratoDocumentoCliente(String contratoDocumentoCliente) {
        this.contratoDocumentoCliente = contratoDocumentoCliente;
    }

    @Basic
    @Column(name = "contrato_documento_conductor", nullable = true, length = 13)
    public String getContratoDocumentoConductor() {
        return contratoDocumentoConductor;
    }

    public void setContratoDocumentoConductor(String contratoDocumentoConductor) {
        this.contratoDocumentoConductor = contratoDocumentoConductor;
    }

    @Basic
    @Column(name = "contrato_id_solicitud", nullable = true)
    public Integer getContratoIdSolicitud() {
        return contratoIdSolicitud;
    }

    public void setContratoIdSolicitud(Integer contratoIdSolicitud) {
        this.contratoIdSolicitud = contratoIdSolicitud;
    }

    @Basic
    @Column(name = "contrato_ip_cliente", nullable = true)
    public String getContratoIpCliente() {
        return contratoIpCliente;
    }

    public void setContratoIpCliente(String contratoIpCliente) {
        this.contratoIpCliente = contratoIpCliente;
    }

    @Basic
    @Column(name = "contrato_ip_conductor", nullable = true)
    public String getContratoIpConductor() {
        return contratoIpConductor;
    }

    public void setContratoIpConductor(String contratoIpConductor) {
        this.contratoIpConductor = contratoIpConductor;
    }

    @Basic
    @Column(name = "contrato_imei_conductor", nullable = true, length = 15)
    public String getContratoImeiConductor() {
        return contratoImeiConductor;
    }

    public void setContratoImeiConductor(String contratoImeiConductor) {
        this.contratoImeiConductor = contratoImeiConductor;
    }

    @Basic
    @Column(name = "contrato_fecha_contrato", nullable = true)
    public Timestamp getContratoFechaContrato() {
        return contratoFechaContrato;
    }

    public void setContratoFechaContrato(Timestamp contratoFechaContrato) {
        this.contratoFechaContrato = contratoFechaContrato;
    }

    @Basic
    @Column(name = "contrato_valor", nullable = true, precision = 0)
    public Double getContratoValor() {
        return contratoValor;
    }

    public void setContratoValor(Double contratoValor) {
        this.contratoValor = contratoValor;
    }

    @Basic
    @Column(name = "contrato_estado", nullable = true)
    public Integer getContratoEstado() {
        return contratoEstado;
    }

    public void setContratoEstado(Integer contratoEstado) {
        this.contratoEstado = contratoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contrato contrato = (Contrato) o;
        return contratoId == contrato.contratoId &&
                Objects.equals(contratoDocumentoCliente, contrato.contratoDocumentoCliente) &&
                Objects.equals(contratoDocumentoConductor, contrato.contratoDocumentoConductor) &&
                Objects.equals(contratoIdSolicitud, contrato.contratoIdSolicitud) &&
                Objects.equals(contratoIpCliente, contrato.contratoIpCliente) &&
                Objects.equals(contratoIpConductor, contrato.contratoIpConductor) &&
                Objects.equals(contratoImeiConductor, contrato.contratoImeiConductor) &&
                Objects.equals(contratoFechaContrato, contrato.contratoFechaContrato) &&
                Objects.equals(contratoValor, contrato.contratoValor) &&
                Objects.equals(contratoEstado, contrato.contratoEstado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(contratoId, contratoDocumentoCliente, contratoDocumentoConductor, contratoIdSolicitud, contratoIpCliente, contratoIpConductor, contratoImeiConductor, contratoFechaContrato, contratoValor, contratoEstado);
    }

    @ManyToOne
    @JoinColumn(name = "contrato_documento_cliente", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByContratoDocumentoCliente() {
        return usuarioByContratoDocumentoCliente;
    }

    public void setUsuarioByContratoDocumentoCliente(Usuario usuarioByContratoDocumentoCliente) {
        this.usuarioByContratoDocumentoCliente = usuarioByContratoDocumentoCliente;
    }

    @ManyToOne
    @JoinColumn(name = "contrato_documento_conductor", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByContratoDocumentoConductor() {
        return usuarioByContratoDocumentoConductor;
    }

    public void setUsuarioByContratoDocumentoConductor(Usuario usuarioByContratoDocumentoConductor) {
        this.usuarioByContratoDocumentoConductor = usuarioByContratoDocumentoConductor;
    }

    @ManyToOne
    @JoinColumn(name = "contrato_id_solicitud", referencedColumnName = "solicitud_envio_id", insertable = false, updatable = false)
    public SolicitudEnvio getSolicitudEnvioByContratoIdSolicitud() {
        return solicitudEnvioByContratoIdSolicitud;
    }

    public void setSolicitudEnvioByContratoIdSolicitud(SolicitudEnvio solicitudEnvioByContratoIdSolicitud) {
        this.solicitudEnvioByContratoIdSolicitud = solicitudEnvioByContratoIdSolicitud;
    }

    @ManyToOne
    @JoinColumn(name = "contrato_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoItemByContratoEstado() {
        return catalogoItemByContratoEstado;
    }

    public void setCatalogoItemByContratoEstado(CatalogoItem catalogoItemByContratoEstado) {
        this.catalogoItemByContratoEstado = catalogoItemByContratoEstado;
    }
}
