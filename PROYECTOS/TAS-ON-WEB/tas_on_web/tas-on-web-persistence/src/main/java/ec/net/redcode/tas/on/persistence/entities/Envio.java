package ec.net.redcode.tas.on.persistence.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "envio")
public class Envio {
    private int envioId;
    private String envioTipo;
    private int envioOrigen;
    private String envioDireccionOrigen;
    private int envioDestino;
    private String envioDireccionDestino;
    private String envioNumeroDocumento;
    private int envioNumeroPiezas;
    private Integer envioNumeroEstibadores;
    private String envioObservaciones;
    private Timestamp envioFechaRecoleccion;
    private Timestamp envioFechaEntrega;
    private Timestamp envioFechaCreacion;
    private Timestamp envioFechaRecoleccionCompletada;
    private Timestamp envioFechaEntregaCompletada;
    private int envioEstado;
    private int envioConductor;
    private int envioVehiculo;
    private String envioCliente;
    private String envioUsuarioCrea;
    private Double envioPago;

    @Ignore private Localidad localidadByEnvioOrigen;
    @Ignore private Localidad localidadByEnvioDestino;
    @Ignore private CatalogoItem catalogoByEnvioEstado;
    @Ignore private Conductor conductorByEnvio;
    @Ignore private Vehiculo vehiculoByEnvio;
    @Ignore private Cliente clienteByEnvio;
    @Ignore private Usuario usuarioByEnvioUsuarioCrea;
    @Ignore private Collection<EnvioFile> fotos;


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "envio_id")
    public int getEnvioId() {
        return envioId;
    }

    public void setEnvioId(int envioId) {
        this.envioId = envioId;
    }

    @Column(name = "envio_tipo")
    public String getEnvioTipo() {
        return envioTipo;
    }

    public void setEnvioTipo(String envioTipo) {
        this.envioTipo = envioTipo;
    }

    @Column(name = "envio_direccion_origen")
    public String getEnvioDireccionOrigen() {
        return envioDireccionOrigen;
    }

    public void setEnvioDireccionOrigen(String envioDireccionOrigen) {
        this.envioDireccionOrigen = envioDireccionOrigen;
    }

    @Column(name = "envio_direccion_destino")
    public String getEnvioDireccionDestino() {
        return envioDireccionDestino;
    }

    public void setEnvioDireccionDestino(String envioDireccionDestino) {
        this.envioDireccionDestino = envioDireccionDestino;
    }

    @Column(name = "envio_numero_documento")
    public String getEnvioNumeroDocumento() {
        return envioNumeroDocumento;
    }

    public void setEnvioNumeroDocumento(String envioNumeroDocumento) {
        this.envioNumeroDocumento = envioNumeroDocumento;
    }

    @Column(name = "envio_numero_piezas")
    public int getEnvioNumeroPiezas() {
        return envioNumeroPiezas;
    }

    public void setEnvioNumeroPiezas(int envioNumeroPiezas) {
        this.envioNumeroPiezas = envioNumeroPiezas;
    }

    @Column(name = "envio_numero_estibadores")
    public Integer getEnvioNumeroEstibadores() {
        return envioNumeroEstibadores;
    }

    public void setEnvioNumeroEstibadores(Integer envioNumeroEstibadores) {
        this.envioNumeroEstibadores = envioNumeroEstibadores;
    }

    @Column(name = "envio_observaciones")
    public String getEnvioObservaciones() {
        return envioObservaciones;
    }

    public void setEnvioObservaciones(String envioObservaciones) {
        this.envioObservaciones = envioObservaciones;
    }

    @Column(name = "envio_fecha_recoleccion")
    public Timestamp getEnvioFechaRecoleccion() {
        return envioFechaRecoleccion;
    }

    public void setEnvioFechaRecoleccion(Timestamp envioFechaRecoleccion) {
        this.envioFechaRecoleccion = envioFechaRecoleccion;
    }

    @Column(name = "envio_fecha_entrega")
    public Timestamp getEnvioFechaEntrega() {
        return envioFechaEntrega;
    }

    public void setEnvioFechaEntrega(Timestamp envioFechaEntrega) {
        this.envioFechaEntrega = envioFechaEntrega;
    }

    @Column(name = "envio_fecha_creacion")
    public Timestamp getEnvioFechaCreacion() {
        return envioFechaCreacion;
    }

    public void setEnvioFechaCreacion(Timestamp envioFechaCreacion) {
        this.envioFechaCreacion = envioFechaCreacion;
    }

    @Column(name = "envio_origen")
    public int getEnvioOrigen() {
        return envioOrigen;
    }

    public void setEnvioOrigen(int envioOrigen) {
        this.envioOrigen = envioOrigen;
    }

    @Column(name = "envio_destino")
    public int getEnvioDestino() {
        return envioDestino;
    }

    public void setEnvioDestino(int envioDestino) {
        this.envioDestino = envioDestino;
    }

    @Column(name = "envio_estado")
    public int getEnvioEstado() {
        return envioEstado;
    }

    public void setEnvioEstado(int envioEstado) {
        this.envioEstado = envioEstado;
    }

    @Column(name = "envio_conductor")
    public int getEnvioConductor() {
        return envioConductor;
    }

    public void setEnvioConductor(int envioConductor) {
        this.envioConductor = envioConductor;
    }

    @Column(name = "envio_vehiculo")
    public int getEnvioVehiculo() {
        return envioVehiculo;
    }

    public void setEnvioVehiculo(int envioVehiculo) {
        this.envioVehiculo = envioVehiculo;
    }

    @Column(name = "envio_cliente")
    public String getEnvioCliente() {
        return envioCliente;
    }

    public void setEnvioCliente(String envioCliente) {
        this.envioCliente = envioCliente;
    }

    @Column(name = "envio_usuario_crea")
    public String getEnvioUsuarioCrea() {
        return envioUsuarioCrea;
    }

    public void setEnvioUsuarioCrea(String envioUsuarioCrea) {
        this.envioUsuarioCrea = envioUsuarioCrea;
    }

    @ManyToOne
    @JoinColumn(name = "envio_origen", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadByEnvioOrigen() {
        return localidadByEnvioOrigen;
    }

    public void setLocalidadByEnvioOrigen(Localidad localidadByEnvioOrigen) {
        this.localidadByEnvioOrigen = localidadByEnvioOrigen;
    }

    @ManyToOne
    @JoinColumn(name = "envio_destino", referencedColumnName = "localidad_id", insertable = false, updatable = false)
    public Localidad getLocalidadByEnvioDestino() {
        return localidadByEnvioDestino;
    }

    public void setLocalidadByEnvioDestino(Localidad localidadByEnvioDestino) {
        this.localidadByEnvioDestino = localidadByEnvioDestino;
    }

    @ManyToOne
    @JoinColumn(name = "envio_estado", referencedColumnName = "catalogo_item_id", insertable = false, updatable = false)
    public CatalogoItem getCatalogoByEnvioEstado() {
        return catalogoByEnvioEstado;
    }

    public void setCatalogoByEnvioEstado(CatalogoItem catalogoByEnvioEstado) {
        this.catalogoByEnvioEstado = catalogoByEnvioEstado;
    }

    @ManyToOne
    @JoinColumn(name = "envio_usuario_crea", referencedColumnName = "usuario_id_documento", insertable = false, updatable = false)
    public Usuario getUsuarioByEnvioUsuarioCrea() {
        return usuarioByEnvioUsuarioCrea;
    }

    public void setUsuarioByEnvioUsuarioCrea(Usuario usuarioByEnvioUsuarioCrea) {
        this.usuarioByEnvioUsuarioCrea = usuarioByEnvioUsuarioCrea;
    }

    @ManyToOne
    @JoinColumn(name = "envio_cliente", referencedColumnName = "cliente_ruc", insertable = false, updatable = false)
    public Cliente getClienteByEnvio() {
        return clienteByEnvio;
    }

    public void setClienteByEnvio(Cliente clienteByEnvio) {
        this.clienteByEnvio = clienteByEnvio;
    }

    @ManyToOne
    @JoinColumn(name = "envio_conductor", referencedColumnName = "conductor_id", insertable = false, updatable = false)
    public Conductor getConductorByEnvio() {
        return conductorByEnvio;
    }

    public void setConductorByEnvio(Conductor conductorByEnvio) {
        this.conductorByEnvio = conductorByEnvio;
    }

    @ManyToOne
    @JoinColumn(name = "envio_vehiculo", referencedColumnName = "vehiculo_id", insertable = false, updatable = false)
    public Vehiculo getVehiculoByEnvio() {
        return vehiculoByEnvio;
    }

    public void setVehiculoByEnvio(Vehiculo vehiculoByEnvio) {
        this.vehiculoByEnvio = vehiculoByEnvio;
    }

    @OneToMany(mappedBy = "envioByFoto")
    public Collection<EnvioFile> getFotos() {
        return fotos;
    }

    public void setFotos(Collection<EnvioFile> fotos) {
        this.fotos = fotos;
    }

    @Column(name = "envio_fecha_recoleccion_completada")
    public Timestamp getEnvioFechaRecoleccionCompletada() {
        return envioFechaRecoleccionCompletada;
    }

    public void setEnvioFechaRecoleccionCompletada(Timestamp envioFechaRecoleccionCompletada) {
        this.envioFechaRecoleccionCompletada = envioFechaRecoleccionCompletada;
    }

    @Column(name = "envio_fecha_entrega_completada")
    public Timestamp getEnvioFechaEntregaCompletada() {
        return envioFechaEntregaCompletada;
    }

    public void setEnvioFechaEntregaCompletada(Timestamp envioFechaEntregaCompletada) {
        this.envioFechaEntregaCompletada = envioFechaEntregaCompletada;
    }

    @Column(name = "envio_pago")
    public Double getEnvioPago() {
        return envioPago;
    }

    public void setEnvioPago(Double envioPago) {
        this.envioPago = envioPago;
    }

}
