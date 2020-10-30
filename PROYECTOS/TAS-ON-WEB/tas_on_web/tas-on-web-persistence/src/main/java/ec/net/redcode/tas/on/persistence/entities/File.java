package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "file")
public class File {
    private int fileId;
    private String fileContent;
    private String fileName;
    private Integer fileSize;
    private String fileType;
    private Timestamp fileUploadDate;
    @JsonIgnore private Collection<ClientePedidos> clientePedidosByFileId;
    @JsonIgnore private Collection<PedidoDocumentosCredito> pedidoDocumentosCreditosByFileId;
    @JsonIgnore private OfertaFile ofertaFileByFileId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "file_id")
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Column(name = "file_content")
    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "file_size")
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(name = "file_upload_date")
    public Timestamp getFileUploadDate() {
        return fileUploadDate;
    }

    public void setFileUploadDate(Timestamp fileUploadDate) {
        this.fileUploadDate = fileUploadDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return fileId == file.fileId &&
                Objects.equals(fileContent, file.fileContent) &&
                Objects.equals(fileName, file.fileName) &&
                Objects.equals(fileSize, file.fileSize) &&
                Objects.equals(fileType, file.fileType) &&
                Objects.equals(fileUploadDate, file.fileUploadDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileContent, fileName, fileSize, fileType, fileUploadDate);
    }

    @OneToMany(mappedBy = "fileByClientePedidosFotoId")
    public Collection<ClientePedidos> getClientePedidosByFileId() {
        return clientePedidosByFileId;
    }

    public void setClientePedidosByFileId(Collection<ClientePedidos> clientePedidosByFileId) {
        this.clientePedidosByFileId = clientePedidosByFileId;
    }

    @OneToMany(mappedBy = "fileByPedidoDocumentosFileId")
    public Collection<PedidoDocumentosCredito> getPedidoDocumentosCreditosByFileId() {
        return pedidoDocumentosCreditosByFileId;
    }

    public void setPedidoDocumentosCreditosByFileId(Collection<PedidoDocumentosCredito> pedidoDocumentosCreditosByFileId) {
        this.pedidoDocumentosCreditosByFileId = pedidoDocumentosCreditosByFileId;
    }

    @OneToOne(mappedBy = "foto")
    public OfertaFile getOfertaFileByFileId() {
        return ofertaFileByFileId;
    }

    public void setOfertaFileByFileId(OfertaFile ofertaFileByFileId) {
        this.ofertaFileByFileId = ofertaFileByFileId;
    }

}
