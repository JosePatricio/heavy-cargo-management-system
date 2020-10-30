package ec.net.redcode.tas.on.persistence.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

@Entity
@Table(name = "envio_file")
public class EnvioFile {
    private int envioFileId;
    private int envioFileTipo;
    private int envioFileEnvioId;
    private int envioFileFileId;
    @Ignore private File foto;
    @Ignore private Envio envioByFoto;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "envio_file_id")
    public int getEnvioFileId() {
        return envioFileId;
    }

    public void setEnvioFileId(int envioFileId) {
        this.envioFileId = envioFileId;
    }

    @Column(name = "envio_file_tipo")
    public int getEnvioFileTipo() {
        return envioFileTipo;
    }

    public void setEnvioFileTipo(int envioFileTipo) {
        this.envioFileTipo = envioFileTipo;
    }

    @Column(name = "envio_file_envio_id")
    public int getEnvioFileEnvioId() {
        return envioFileEnvioId;
    }

    public void setEnvioFileEnvioId(int envioFileEnvioId) {
        this.envioFileEnvioId = envioFileEnvioId;
    }

    @Column(name = "envio_file_file_id")
    public int getEnvioFileFileId() {
        return envioFileFileId;
    }

    public void setEnvioFileFileId(int envioFileFileId) {
        this.envioFileFileId = envioFileFileId;
    }

    @OneToOne
    @JoinColumn(name = "envio_file_file_id", referencedColumnName = "file_id", insertable = false, updatable = false)
    public File getFoto() {
        return foto;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }

    @ManyToOne
    @JoinColumn(name = "envio_file_envio_id", referencedColumnName = "envio_id", insertable = false, updatable = false)
    public Envio getEnvioByFoto() {
        return envioByFoto;
    }

    public void setEnvioByFoto(Envio envioByFoto) {
        this.envioByFoto = envioByFoto;
    }

}
