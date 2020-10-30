package ec.net.redcode.tas.on.common.dto;

import ec.net.redcode.tas.on.common.util.JsonDateDeserializer;
import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Invoice {

    private String numberInvoice;
    private String rucSupplier;
    private Double amount;
    private String idInvoice;
    private String personInvoice;
    private String authNroInvoice;
    private int invoiceTypePay;
    private double invoiceDiscount;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Timestamp dateAuthInvoice;
    private List<Integer> offersId;

}
