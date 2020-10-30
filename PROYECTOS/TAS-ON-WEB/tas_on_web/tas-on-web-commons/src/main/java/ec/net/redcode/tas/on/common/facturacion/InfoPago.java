package ec.net.redcode.tas.on.common.facturacion;

import ec.gob.sri.comprobantes.modelo.rentencion.ComprobanteRetencion;
import lombok.Data;

@Data
public class InfoPago {

    private String facturaId;
    private ComprobanteRetencion comprobanteRetencion;
    private String comprobanteXML;

}
