package ec.redcode.tas.on.android.dto.requests;

/**
 * Created by User on 05/01/2018.
 */

public class OfertRequestDTO {

    String idSolicitud;
    String amount;
    String comments;

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
