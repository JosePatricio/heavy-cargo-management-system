package ec.net.redcode.tas.on.common;

public class MethodConstant {

    public final static String USUARIO_LOGIN = "login";
    public final static String USUARIO_LOGIN_MOVIL = "loginMovil";
    public final static String USUARIO_CREATE = "createUsuario";
    public final static String USUARIO_READ = "readUsuario";
    public final static String USUARIO_UPDATE = "updateUsuario";
    public final static String USUARIO_UPDATE_PASSWORD = "updatePassword";
    public final static String USUARIO_DELETE = "deleteUsuario";
    public final static String USUARIO_ALL_BY_TIPO = "getAllUserByTipoPersona";
    public final static String USUARIO_BY_USERNAME = "getUsuarioByUsername";
    public final static String USUARIO_BY_EMAIL = "getUsuarioByEmail";
    public final static String USUARIO_ALL_BY_TIPO_ESTADO = "getAllUserByTipoUsuarioAndEstado";
    public final static String USUARIO_ALL_BY_EMPRESA_TIPO_ESTADO = "getAllUserByEmpresaTipoUsuarioAndEstado";
    public final static String USUARIO_ACTIVATE = "activateUsuario";
    public final static String USUARIO_RESTABLECER = "restablecerPassword";
    public final static String USUARIO_EMPRESA = "getUsuarioByEmpresaAndEstado";
    public final static String USUARIO_TOKEN = "getTokenByUsername";
    public final static String GET_HOME ="getHome";

    public final static String SECURITY_SOLICITAR_ACTUALIZAR_INFO_BANCARIA = "solicitarActualizarInfoBancaria";

    public final static String CATALOGO_CREATE = "createCatalogo";
    public final static String CATALOGO_READ = "readCatalogo";
    public final static String CATALOGO_UPDATE = "updateCatalogo";
    public final static String CATALOGO_DELETE = "deleteCatalogo";
    public final static String CATALOGO_ALL = "getAllCatalogo";

    public final static String CATALOGO_ITEM_CREATE = "createCatalogoItem";
    public final static String CATALOGO_ITEM_READ = "readCatalogoItem";
    public final static String CATALOGO_ITEM_UPDATE = "updateCatalogoItem";
    public final static String CATALOGO_ITEM_DELETE = "deleteCatalogoItem";
    public final static String CATALOGO_ITEM_ALL_BY_CATALOGO = "getCatalogoItemByCatalogo";

    public final static String LOCALIDAD_CREATE = "createLocalidad";
    public final static String LOCALIDAD_READ = "readLocalidad";
    public final static String LOCALIDAD_UPDATE = "updateLocalidad";
    public final static String LOCALIDAD_DELETE = "deleteLocalidad";
    public final static String LOCALIDAD_ALL_BY_PADRE = "getLocalidadByPadre";
    public final static String LOCALIDAD_ALL_CIUDADES = "getAllCiudades";

    public final static String SOLICITUD_ENVIO_CREATE = "createSolicitudEnvio";
    public final static String SOLICITUD_ENVIO_READ = "readSolicitudEnvio";
    public final static String SOLICITUD_ENVIO_UPDATE = "updateSolicitudEnvio";
    public final static String SOLICITUD_ENVIO_DELETE = "deleteSolicitudEnvio";
    public final static String SOLICITUD_ENVIO_ORIGEN_DESTINO = "getSolicitudEnvioByOrigenDestino";
    public final static String SOLICITUD_ENVIO_OFERTA = "getSolicitudEnvioBySolicitudEnvioOferta";
    public final static String SOLICITUD_ENVIO_ESTADO = "getSolicitudEnvioBySolicitudEstado";
    public final static String SOLICITUD_SOLICITUDES = "getSolicitudes";
    public final static String SOLICITUD_ALL_SOLICITUDES = "getAllSolicitudes";
    public final static String SOLICITUD_SOLICITUD = "getSolicitud";
    public final static String SOLICITUD_ACCEPT_OFERTA = "updateAcceptOferta";
    public final static String SOLICITUD_WITH_OFERTAS = "getSolicitudesOferta";
    public final static String SOLICITUD_ENVIO_CANCEL = "cancelSolicitud";
    public final static String SOLICITUD_ESTADOS = "getEstadoSolicitudes";
    public final static String SOLICITUD_ADMIN_BY = "getSolicitudesAdminBy";
    public final static String SOLICITUD_NUEVA = "getDatosNuevaSolicitud";
    public final static String SOLICITUD_ULTIMOS_DATOS_ORIGEN = "getUltimosDatosOrigen";
    public final static String SOLICITUD_ULTIMOS_DATOS_DESTINO = "getUltimosDatosDestino";

    public final static String ENVIO_CARGAR_ARCHIVO = "cargarArchivo";
    public final static String ENVIO_VALIDAR_ARCHIVO = "validarArchivo";
    public final static String ENVIO_PENDIENTES = "consultarEnviosPendientes";
    public final static String ENVIO_BY = "consultarEnviosBy";
    public final static String ENVIO_ACTUALIZAR_FOTOS = "actualizarFotosEnvio";
    public final static String ENVIO_CONSULTAR_FOTOS = "consultarFotosEnvio";
    public final static String ENVIO_GET_FOTO = "getFotoEnvio";

    public final static String SOLICITUD_ENVIO_RANDOM = "crearSolicitudRandom";

    public final static String CALIFICACION_TRANSPORTISTA_BY_USER = "getCalificacionesByUser";
    public final static String CALIFICACION_TRANSPORTISTA_UPDATE = "updateCalificacionTransportista";

    public final static String OFERTA_OFFER = "offerValue";
    public final static String OFERTA_OFFER_UDPATE = "updateOffer";
    public final static String OFERTA_ALL_OFERTA_BY_USER_AND_ESTADO = "getOfertasByUserAndEstado";
    public final static String OFERTA_ALL_OFERTA_ESTADO = "getOfertasByEstado";
    public final static String OFERTAS_BY = "getOfertasBy";
    public final static String OFERTA_OFERTAS_BY_SOLICITUD = "getOfertasBySolicitud";
    public final static String OFERTA_GET_DOCUMENT = "getDocumentos"; // TODO eliminar cuando quede obsoleto
    public final static String OFERTA_GET_FOTO = "getFoto";
    public final static String OFERTA_GET_OFFER = "getOferta";
    public final static String OFERTA_GENERATE_CASH_FILE = "generateCashManagementFile";
    public final static String OFERTA_DOWNLOAD_CASH_FILE = "downloadCashManagementFile";

    public final static String CLIENT_GET_ALL = "getAllCliente";
    public final static String CLIENT_GET_CLIENTE_BY_TIPO = "getAllClienteByTipo";
    public final static String CLIENT_CREATE = "createCliente";
    public final static String CLIENT_READ = "readCliente";
    public final static String CLIENT_TRANSPORTE_READ = "readTransporteCliente";
    public final static String CLIENT_READ_BY_TOKEN = "getClienteByToken";
    public final static String CLIENT_UPDATE = "updateCliente";
    public final static String CLIENT_DELETE = "deleteCliente";
    public final static String CLIENT_UPDATE_INFO_BANCARIA = "updateInfoBancaria";
    public final static String CLIENT_GET_INFO_BANCARIA = "getInfoBancaria";

    public final static String VEHICULO_CREATE = "createVehiculo";
    public final static String VEHICULO_READ = "readVehiculo";
    public final static String VEHICULO_UPDATE = "updateVehiculo";
    public final static String VEHICULO_DELETE = "deleteVehiculo";
    public final static String VEHICULO_VEHICULOS = "getVehiculosByUser";
    public final static String VEHICULO_PLACA = "getVehiculoByPlaca";
    public final static String VEHICULO_GET_BY_USUARIO_Y_SOLICITUD = "getVehiculosByUsuarioYSolicitud";

    public final static String CONDUCTOR_CREATE = "createConductor";
    public final static String CONDUCTOR_READ = "readConductor";
    public final static String CONDUCTOR_UPDATE = "updateConductor";
    public final static String CONDUCTOR_DELETE = "deleteConductor";
    public final static String CONDUCTOR_CONDUCTORES = "getConductoresByUser";
    public final static String CONDUCTOR_GET_BY_USUARIO_Y_SOLICITUD = "getConductoresByUsuarioYSolicitud";

    public final static String FACTURA_CREATE = "createFactura";
    public final static String FACTURA_CODE = "getCodeFactura";
    public final static String FACTURA_PUEDE_CREAR_PREFACTURA = "puedeCrearPrefactura";
    public final static String FACTURA_CREATE_PREINVOICE = "createPreInvoice";
    public final static String FACTURA_LIST = "getInvoicesByUserEstado";
    public final static String FACTURA_DETAIl = "getInvoiceDetail";
    public final static String FACTURA_LIST_ALL = "getInvoicesByEstado";
    public final static String FACTURA_LIST_ALL_BY = "getAllInvoicesBy";
    public final static String FACTURA_UPDATE = "updateInvoice";
    public final static String FACTURA_MANUAL_CREATE = "createManualInvoice";
    public final static String FACTURA_MANUAL_ALL_BY = "getAllManualInvoicesBy";
    public final static String FACTURA_MANUAL_DOWNLOAD_XML = "downloadFacturaManualXML";
    public final static String FACTURA_RETENCION_GET_BY = "getFacturaRetencionBy";
    public final static String FACTURA_RETENCION_DOWNLOAD_XML = "downloadRetencionXML";

    public final static String EBILLING_CREATE_ADQUIRIENTE = "createAdquiriente";
    public final static String EBILLING_GET_ADQUIRIENTE = "getAdquiriente";
    public final static String EBILLING_GET_USUARIO = "getUsuarioEbilling";
    public final static String EBILLING_GENERATE = "generateEbilling";
    public final static String EBILLING_SEND = "sendEbilling";
    public final static String EBILLING_UPDATE_USUARIO ="updateUsuarioEbilling";
    public final static String EBILLING_GET_USER_INFO = "getEbillingUserInfo";
    public final static String EBILLING_GET_ALL = "getAllEbillings";
    public final static String EBILLING_GET_MY = "getMyEbillings";
    public final static String EBILLING_DOWNLOAD_RIDE = "downloadRIDE";
    public final static String EBILLING_DOWNLOAD_XML = "downloadXML";
    public final static String EBILLING_SEND_MAIL = "sendEbillingMail";

    public final static String PAGO_CREATE = "createPago";
    public final static String PAGO_CREATE_RETENCION = "createPagoRetencion";
    public final static String PAGO_PAGOS = "getPagos";
    public final static String PAGO_DETAIL = "getPagoDetail";
    public final static String PAGO_RETENCION = "getRetencion";
    public final static String PAGO_NOTA_CREDITO_INFO = "getNotaCreditoInfo";

    public final static String PUBLIC_CREATE_EMPRESA_CLIENTE = "createEmpresaCliente";

    public final static String SECUENCIA_SECUENCIA = "getSecuenciaByCliente";
    public final static String SECUENCIA_DIAS_HABILES = "getDiasHabiles";

    public final static String FACTURA_PROVEEDOR_CREATE = "create";
    public final static String FACTURA_PROVEEDOR_READ = "read";
    public final static String FACTURA_PROVEEDOR_UPDATE = "update";
    public final static String FACTURA_PROVEEDOR_BY_ESTADO = "getByEstado";
    public final static String FACTURA_PROVEEDOR_BY_NUMERO_RUC = "getByNumeroFacturaAndRuc";

    public final static String PEDIDO_CONSULTAR_VENDEDORES = "consultarVendedoresCliente";
    public final static String PEDIDO_CONSULTAR_CLIENTES_BY = "consultarClientesPedidosBy";
    public final static String PEDIDO_REGISTRAR_VISITA = "registrarNuevaVisita";
    public final static String PEDIDO_AGENDARME_VISITAS = "agendarmeVisitas";
    public final static String PEDIDO_CONSULTAR_MIS_VISITAS_BY = "consultarMisVisitasBy";
    public final static String PEDIDO_CONSULTAR_VISITAS_BY = "consultarVisitasBy";
    public final static String PEDIDO_ACTUALIZAR_CLIENTE = "actualizarClientePedidos";
    public final static String PEDIDO_GUARDAR = "guardarPedido";
    public final static String PEDIDO_CONSULTAR_POR_VISITA = "getPedidoByVisita";
    public final static String PEDIDO_CONSULTAR_ALL_BY = "consultarAllPedidosBy";
    public final static String PEDIDO_NUEVO_CLIENTE = "guardarNuevoCliente";
    public final static String PEDIDO_CONSULTAR_CATEGORIAS = "consultarCategorias";
    public final static String PEDIDO_CONSULTAR_PRODUCTOS = "consultarProductos";
    public final static String PEDIDO_PRODUCTO_NUEVO = "guardarProducto";
    public final static String PEDIDO_PRODUCTO_ACTUALIZAR = "actualizarProducto";
    public final static String PEDIDO_CREDITO_GET_DOCUMENTOS = "getDocumentosCredito";

    public final static String RETENCION_ENVIAR_SRI = "enviarRetencionSRI";
}
