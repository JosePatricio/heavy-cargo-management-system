package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "solicitud_envio_audit")
@NamedQueries({
        @NamedQuery(name = "SolicitudEnvioAudit.SolicitudEnvioAuditBySolicitudId", query = "select s from SolicitudEnvioAudit s where s.solicitudEnvioId = :solicitud " +
                "order by s.solicitudEnvioAuditTimestamp desc")
})
public class SolicitudEnvioAudit extends SolicitudEnvioCommon {

    private int solicitudEnvioAuditPK;
    private String solicitudEnvioId;
    private String solicitudEnvioAuditEvent;
    private String solicitudEnvioAuditTimestamp;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "solicitud_envio_audit_pk", nullable = false)
    public int getSolicitudEnvioAuditPK() {
        return solicitudEnvioAuditPK;
    }

    public void setSolicitudEnvioAuditPK(int solicitudEnvioAuditPK) {
        this.solicitudEnvioAuditPK = solicitudEnvioAuditPK;
    }

    @Basic
    @Column(name = "solicitud_envio_id", nullable = false)
    public String getSolicitudEnvioId() {
        return solicitudEnvioId;
    }

    public void setSolicitudEnvioId(String solicitudEnvioId) {
        this.solicitudEnvioId = solicitudEnvioId;
    }

    @Basic
    @Column(name = "solicitud_envio_audit_event", nullable = false)
    public String getSolicitudEnvioAuditEvent() {
        return solicitudEnvioAuditEvent;
    }

    public void setSolicitudEnvioAuditEvent(String solicitudEnvioAuditEvent) {
        this.solicitudEnvioAuditEvent = solicitudEnvioAuditEvent;
    }

    @Basic
    @Column(name = "solicitud_envio_audit_timestamp", nullable = false)
    public String getSolicitudEnvioAuditTimestamp() {
        return solicitudEnvioAuditTimestamp;
    }

    public void setSolicitudEnvioAuditTimestamp(String solicitudEnvioAuditTimestamp) {
        this.solicitudEnvioAuditTimestamp = solicitudEnvioAuditTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitudEnvioAudit that = (SolicitudEnvioAudit) o;
        return solicitudEnvioAuditPK == that.solicitudEnvioAuditPK &&
                Objects.equals(solicitudEnvioUsuarioId, that.solicitudEnvioUsuarioId) &&
                Objects.equals(solicitudEnvioPeso, that.solicitudEnvioPeso) &&
                Objects.equals(solicitudEnvioUnidadPeso, that.solicitudEnvioUnidadPeso) &&
                Objects.equals(solicitudEnvioVolumen, that.solicitudEnvioVolumen) &&
                Objects.equals(solicitudEnvioUnidadVolumen, that.solicitudEnvioUnidadVolumen) &&
                Objects.equals(solicitudEnvioNumeroPiesas, that.solicitudEnvioNumeroPiesas) &&
                Objects.equals(solicitudEnvioDireccionOrigen, that.solicitudEnvioDireccionOrigen) &&
                Objects.equals(solicitudEnvioLocalidadOrigen, that.solicitudEnvioLocalidadOrigen) &&
                Objects.equals(solicitudEnvioDireccionDestino, that.solicitudEnvioDireccionDestino) &&
                Objects.equals(solicitudEnvioLocalidadDestino, that.solicitudEnvioLocalidadDestino) &&
                Objects.equals(solicitudEnvioFechaRecoleccion, that.solicitudEnvioFechaRecoleccion) &&
                Objects.equals(solicitudEnvioNumeroEstibadores, that.solicitudEnvioNumeroEstibadores) &&
                Objects.equals(solicitudEnvioDiasValides, that.solicitudEnvioDiasValides) &&
                Objects.equals(solicitudEnvioFechaEntrega, that.solicitudEnvioFechaEntrega) &&
                Objects.equals(solicitudEnvioEstado, that.solicitudEnvioEstado) &&
                Objects.equals(solicitudEnvioOfertaId, that.solicitudEnvioOfertaId) &&
                Objects.equals(solicitudEnvioPersonaEntrega, that.solicitudEnvioPersonaEntrega) &&
                Objects.equals(solicitudEnvioPersonaRecibe, that.solicitudEnvioPersonaRecibe) &&
                Objects.equals(solicitudEnvioFechaCreacion, that.solicitudEnvioFechaCreacion) &&
                Objects.equals(solicitudEnvioFechaCaducidad, that.solicitudEnvioFechaCaducidad) &&
                Objects.equals(solicitudEnvioFechaPago, that.solicitudEnvioFechaPago) &&
                Objects.equals(solicitudEnvioFechaFacturacion, that.solicitudEnvioFechaFacturacion) &&
                Objects.equals(solicitudEnvioComentario, that.solicitudEnvioComentario) &&
                Objects.equals(solicitudEnvioObservaciones, that.solicitudEnvioObservaciones) &&
                Objects.equals(solicitudEnvioAuditEvent, that.solicitudEnvioAuditEvent) &&
                Objects.equals(solicitudEnvioAuditTimestamp, that.solicitudEnvioAuditTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solicitudEnvioAuditPK, solicitudEnvioAuditEvent, solicitudEnvioAuditTimestamp, solicitudEnvioUsuarioId, solicitudEnvioPeso, solicitudEnvioUnidadPeso, solicitudEnvioVolumen,
                solicitudEnvioUnidadVolumen, solicitudEnvioNumeroPiesas, solicitudEnvioDireccionOrigen, solicitudEnvioLocalidadOrigen,
                solicitudEnvioDireccionDestino, solicitudEnvioLocalidadDestino, solicitudEnvioFechaRecoleccion, solicitudEnvioNumeroEstibadores,
                solicitudEnvioDiasValides, solicitudEnvioFechaEntrega, solicitudEnvioEstado, solicitudEnvioOfertaId, solicitudEnvioPersonaEntrega,
                solicitudEnvioPersonaRecibe, solicitudEnvioFechaCreacion, solicitudEnvioFechaCaducidad, solicitudEnvioFechaPago,
                solicitudEnvioFechaFacturacion, solicitudEnvioComentario, solicitudEnvioObservaciones);
    }

}
