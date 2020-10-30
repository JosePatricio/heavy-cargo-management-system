package ec.net.redcode.tas.on.persistence.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

@Entity
@Table(name = "oferta_file")
public class OfertaFile {
    private int ofertaFileId;
    private int ofertaFileOfertaId;
    private int ofertaFileFileId;
    private int ofertaFileTipo;
    @Ignore private Oferta ofertaByFoto;
    @Ignore private File foto;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "oferta_file_id")
    public int getOfertaFileId() {
        return ofertaFileId;
    }

    public void setOfertaFileId(int ofertaFileId) {
        this.ofertaFileId = ofertaFileId;
    }

    @Column(name = "oferta_file_tipo")
    public int getOfertaFileTipo() {
        return ofertaFileTipo;
    }

    public void setOfertaFileTipo(int ofertaFileTipo) {
        this.ofertaFileTipo = ofertaFileTipo;
    }

    @Column(name = "oferta_file_oferta_id")
    public int getOfertaFileOfertaId() {
        return ofertaFileOfertaId;
    }

    public void setOfertaFileOfertaId(int ofertaFileOfertaId) {
        this.ofertaFileOfertaId = ofertaFileOfertaId;
    }

    @Column(name = "oferta_file_file_id")
    public int getOfertaFileFileId() {
        return ofertaFileFileId;
    }

    public void setOfertaFileFileId(int ofertaFileFileId) {
        this.ofertaFileFileId = ofertaFileFileId;
    }

    @ManyToOne
    @JoinColumn(name = "oferta_file_oferta_id", referencedColumnName = "oferta_id", insertable = false, updatable = false)
    public Oferta getOfertaByFoto() {
        return ofertaByFoto;
    }

    public void setOfertaByFoto(Oferta ofertaByFoto) {
        this.ofertaByFoto = ofertaByFoto;
    }

    @OneToOne
    @JoinColumn(name = "oferta_file_file_id", referencedColumnName = "file_id", insertable = false, updatable = false)
    public File getFoto() {
        return foto;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }
}
