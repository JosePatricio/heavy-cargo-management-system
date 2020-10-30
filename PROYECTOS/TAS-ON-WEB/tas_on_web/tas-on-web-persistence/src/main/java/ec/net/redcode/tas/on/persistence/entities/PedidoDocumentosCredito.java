package ec.net.redcode.tas.on.persistence.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pedido_documentos_credito")
public class PedidoDocumentosCredito {
    private int pedidoDocumentosCreditoId;
    private int pedidoDocumentosCreditoPedidoId;
    private int pedidoDocumentosFileId;
    @JsonIgnore private Pedido pedidoByPedidoDocumentosCreditoPedidoId;
    @JsonIgnore private File fileByPedidoDocumentosFileId;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "pedido_documentos_credito_id")
    public int getPedidoDocumentosCreditoId() {
        return pedidoDocumentosCreditoId;
    }

    public void setPedidoDocumentosCreditoId(int pedidoDocumentosCreditoId) {
        this.pedidoDocumentosCreditoId = pedidoDocumentosCreditoId;
    }

    @Basic
    @Column(name = "pedido_documentos_credito_pedido_id")
    public int getPedidoDocumentosCreditoPedidoId() {
        return pedidoDocumentosCreditoPedidoId;
    }

    public void setPedidoDocumentosCreditoPedidoId(int pedidoDocumentosCreditoPedidoId) {
        this.pedidoDocumentosCreditoPedidoId = pedidoDocumentosCreditoPedidoId;
    }

    @Basic
    @Column(name = "pedido_documentos_file_id")
    public int getPedidoDocumentosFileId() {
        return pedidoDocumentosFileId;
    }

    public void setPedidoDocumentosFileId(int pedidoDocumentosFileId) {
        this.pedidoDocumentosFileId = pedidoDocumentosFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoDocumentosCredito that = (PedidoDocumentosCredito) o;
        return pedidoDocumentosCreditoId == that.pedidoDocumentosCreditoId &&
                pedidoDocumentosCreditoPedidoId == that.pedidoDocumentosCreditoPedidoId &&
                pedidoDocumentosFileId == that.pedidoDocumentosFileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoDocumentosCreditoId, pedidoDocumentosCreditoPedidoId, pedidoDocumentosFileId);
    }

    @ManyToOne
    @JoinColumn(name = "pedido_documentos_credito_pedido_id", referencedColumnName = "pedido_id", nullable = false, insertable = false, updatable = false)
    public Pedido getPedidoByPedidoDocumentosCreditoPedidoId() {
        return pedidoByPedidoDocumentosCreditoPedidoId;
    }

    public void setPedidoByPedidoDocumentosCreditoPedidoId(Pedido pedidoByPedidoDocumentosCreditoPedidoId) {
        this.pedidoByPedidoDocumentosCreditoPedidoId = pedidoByPedidoDocumentosCreditoPedidoId;
    }

    @ManyToOne
    @JoinColumn(name = "pedido_documentos_file_id", referencedColumnName = "file_id", nullable = false, insertable = false, updatable = false)
    public File getFileByPedidoDocumentosFileId() {
        return fileByPedidoDocumentosFileId;
    }

    public void setFileByPedidoDocumentosFileId(File fileByPedidoDocumentosFileId) {
        this.fileByPedidoDocumentosFileId = fileByPedidoDocumentosFileId;
    }
}
