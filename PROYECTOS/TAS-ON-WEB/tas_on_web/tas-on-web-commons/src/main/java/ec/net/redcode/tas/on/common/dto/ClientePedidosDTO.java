package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

@Data
public class ClientePedidosDTO {

    private int clienteId;
    private String clienteRazonSocial;
    private String clienteDireccion;
    private String clienteNombre;
    private String clienteTelefono;
    private String clienteLat;
    private String clienteLng;
    private String clienteRuc;
    private String clienteCorreo;
    private VendedorClienteDTO clienteVendedorAsignado;
    private Integer diaSemanaVisita;

}
