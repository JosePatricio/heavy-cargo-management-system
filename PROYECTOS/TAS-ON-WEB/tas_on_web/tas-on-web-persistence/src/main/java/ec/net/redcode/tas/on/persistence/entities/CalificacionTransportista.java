package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "calificacion_transportista")
public class CalificacionTransportista {
    private int calificacionTransportistaId;
    private int calificacionTransportistaOfertaId;
    private int calificacionTransportistaValor;
    private String calificacionTransportistaComentario;
    private String calificacionTransportistaUsuario;
    private Timestamp calificacionTransportistaFechaCalificacion;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "calificacion_transportista_id")
    public int getCalificacionTransportistaId() {
        return calificacionTransportistaId;
    }

    public void setCalificacionTransportistaId(int calificacionTransportistaId) {
        this.calificacionTransportistaId = calificacionTransportistaId;
    }

    @Column(name = "calificacion_transportista_valor")
    public int getCalificacionTransportistaValor() {
        return calificacionTransportistaValor;
    }

    public void setCalificacionTransportistaValor(int calificacionTransportistaValor) {
        this.calificacionTransportistaValor = calificacionTransportistaValor;
    }

    @Column(name = "calificacion_transportista_comentario")
    public String getCalificacionTransportistaComentario() {
        return calificacionTransportistaComentario;
    }

    public void setCalificacionTransportistaComentario(String calificacionTransportistaComentario) {
        this.calificacionTransportistaComentario = calificacionTransportistaComentario;
    }

    @Column(name = "calificacion_transportista_fecha_calificacion")
    public Timestamp getCalificacionTransportistaFechaCalificacion() {
        return calificacionTransportistaFechaCalificacion;
    }

    public void setCalificacionTransportistaFechaCalificacion(Timestamp calificacionTransportistaFechaCalificacion) {
        this.calificacionTransportistaFechaCalificacion = calificacionTransportistaFechaCalificacion;
    }

    @Column(name = "calificacion_transportista_oferta_id")
    public int getCalificacionTransportistaOfertaId() {
        return calificacionTransportistaOfertaId;
    }

    public void setCalificacionTransportistaOfertaId(int calificacionTransportistaOfertaId) {
        this.calificacionTransportistaOfertaId = calificacionTransportistaOfertaId;
    }

    @Column(name = "calificacion_transportista_usuario")
    public String getCalificacionTransportistaUsuario() {
        return calificacionTransportistaUsuario;
    }

    public void setCalificacionTransportistaUsuario(String calificacionTransportistaUsuario) {
        this.calificacionTransportistaUsuario = calificacionTransportistaUsuario;
    }
}
