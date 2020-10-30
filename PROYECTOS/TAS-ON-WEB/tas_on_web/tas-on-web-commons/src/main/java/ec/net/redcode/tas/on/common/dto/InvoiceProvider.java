package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class InvoiceProvider {

    private String facturaProveedorNumero;
    private String facturaProveedorRucCliente;
    private String facturaProveedorRucClienteName;
    private Timestamp facturaProveedorFechaEmision;
    private Double facturaProveedorTotal;
    private Timestamp facturaProveedorFechaPago;
    private int facturaProveedorFormaPago;
    private String facturaProveedorFormaPagoDesc;
    private int facturaProveedorCompra;
    private String facturaProveedorCompraDesc;
    private int facturaProveedorState;
    private String facturaProveedorTransfer;

}
