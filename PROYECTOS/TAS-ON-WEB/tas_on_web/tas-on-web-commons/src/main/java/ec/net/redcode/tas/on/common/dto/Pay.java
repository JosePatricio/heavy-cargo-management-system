package ec.net.redcode.tas.on.common.dto;


import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Pay {

    private String facturaId;
    private int pagoId;
    private int pagoTipoId;
    private String pagoTipo;
    private int pagoBancoId;
    private String pagoBanco;
    private String pagoNumeroDocumento;
    private Double pagoValor;
    private Timestamp pagoFechaDocumento;
    private Timestamp pagoFechaRegistro;
    private ComprobanteRetencion comprobanteRetencion;

}
