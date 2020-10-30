export const urlProduccion = "http://www.tas-on.com/";
//export const URL_DEVELOP = "http://192.168.0.115:8080/";
export const URL_DEVELOP = "http://qa.tas-on.com/";



export const urlLogin = URL_DEVELOP + "tas-on-web-services/rest-login/login/movil";
export const urlUpdatePass = URL_DEVELOP + "tas-on-web-services/rest-usuarios/usuario/password/";
export const urlLocations = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/all/1/1";
export const urlFletesList = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitudes/#idState";
export const urlFleteDetail = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/";
export const urlMakeAnOfert = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertar";
export const urlMyOferts = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertas/";
export const urlMyOfertDetail = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/";
export const urlVehicle = URL_DEVELOP + "tas-on-web-services/rest-vehiculo/vehiculo/vehiculos/#idSolicitud";
export const urlVehicles = URL_DEVELOP + "tas-on-web-services/rest-vehiculo/vehiculo/vehiculos";
export const urlCreateVehicle = URL_DEVELOP + "tas-on-web-services/rest-vehiculo/vehiculo/create/";
export const urlUpdateVehicle = URL_DEVELOP + "tas-on-web-services/rest-vehiculo/vehiculo/update/";
export const urlActivateUser = URL_DEVELOP + "tas-on-web-services/rest-usuarios/usuario/activate-user/";
export const urlPreInvoiceAvailability = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/can-create-preinvoice/";

export const urlBancInfoUpdateReq = URL_DEVELOP + "tas-on-web-services/rest-security/security/banco/solicitud/update";

export const urlMyDriversToAprove = URL_DEVELOP + "tas-on-web-services/rest-usuarios/usuario/all-usuario-empresa/#rucEmpresa/#tipoUsuario/#estado";
export const urlgetFotoPorId = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/foto/#fotoId";

export const urlUpdateInfoBank = URL_DEVELOP + "tas-on-web-services/rest-client/client/banco/update/";

export const urlClienteConductor = URL_DEVELOP + "tas-on-web-services/rest-usuarios/usuario/read/#idClieCond";

export const urlInfoBancaria = URL_DEVELOP + "tas-on-web-services/rest-client/client/banco/info/";

export const urlMyDrivers = URL_DEVELOP + "tas-on-web-services/rest-conductor/conductor/conductores/#idSolicitud";
export const urlAllMyDrivers = URL_DEVELOP + "tas-on-web-services/rest-conductor/conductor/conductores";
export const urlUpdateOfert = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/update-oferta/";
export const urlReadEmpre = URL_DEVELOP + "tas-on-web-services/rest-client/client/read/#rucEmpresa";
export const urlClienteByToken = URL_DEVELOP + "tas-on-web-services/rest-client/client/read-auth";
export const urlGetCodeFactura = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/code-factura/";
export const urlCreateInvoice = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/create-preinvoice/";
export const urlInvoiceListByState = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/invoices/#estado";
export const urlInvoiceDetailByNumber = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/invoice-detail/#numbPreInv";
export const urlCreateDriver = URL_DEVELOP + "tas-on-web-services/rest-conductor/conductor/create/";
export const urlUpdateDriver = URL_DEVELOP + "tas-on-web-services/rest-conductor/conductor/update";

export const urlGenerateEBilling = URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/generate";
export const urlGetAdquiriente =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/adquiriente/#adquirienteId";
export const urlGetEBillingInfo =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/user-info";
export const urlGetMyEBillings =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/my";
export const urlSendMailEbilling =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/send-mail/#claveAcceso/#correo";

export const URL_OFERTAS_BY_SOLIC_AND_ESTADO = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertas-solicitud/#idSolicitud/#estado";
export const URL_OFERTA_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/oferta/#idOferta";

export const URL_CATALOGO_ITEM_BY_TYPE = URL_DEVELOP + "tas-on-web-services/rest-public/public/catalogo-item-all/#idCata";
export const URL_CATALOGO_ITEM_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-public/public/catalogo-item-read/#idCatItem";
export const URL_EMPRESA_CLIENT_BY_TYPE = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-clientes/#idTipo";

export const URL_LOCALIDAD_BY_PADRE = URL_DEVELOP + "tas-on-web-services/rest-public/public/localidad-all/#idPadre/#estado";
export const URL_LOCALIDAD_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/read/#idLocalidad";
export const URL_LOCALIDAD_ALL_CITIES = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/all-ciudades";

export const URL_CREA_USUARIO = URL_DEVELOP + "tas-on-web-services/rest-public/public/usuario-create/";
export const URL_CREA_EMPRESA = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-create/";
export const URL_READ_EMPRESA = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-read/#ruc";
export const URL_READ_EMPRESA_TRANSP = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-transporte-read/#ruc";

export const URL_READ_USUARIO = URL_DEVELOP + "tas-on-web-services/rest-public/public/usuario-read/#ruc";

export const URL_READ_USUARIO_BY_USERNAME = URL_DEVELOP + "tas-on-web-services/rest-public/public/user/#username";
export const URL_READ_USUARIO_BY_EMAIL = URL_DEVELOP + "tas-on-web-services/rest-public/public/email/#email";
export const URL_RESTABLECER_PASSWORD = URL_DEVELOP + "tas-on-web-services/rest-public/public/usuario-restablecer/#email/#identificador";
export const URL_UPDATE_PASSWORD =  URL_DEVELOP + "tas-on-web-services/rest-login/login/change-password";

export const URL_SECUENCIAL = URL_DEVELOP + "tas-on-web-services/rest-secuencia/secuencia/secuencia/#rucEmpresa";
export const URL_DIAS_HABILES = URL_DEVELOP + "tas-on-web-services/rest-secuencia/secuencia/dias-habiles/#fechaCaducidad";

export const URL_SOLICITUD_CREA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/create/";
export const URL_SOLICITUD_EDITA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/update/";
export const URL_SOLICITUD_CANCELAR = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud-cancel/";
export const URL_SOLICITUD_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/#idSolicitud";
export const URL_SOLICITUD_OFERTADA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitudes-ofertas";
export const URL_SOLICITUD_ACEPTA_OFERTA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud-oferta/";

export const URL_FCM_REGISTRO = URL_DEVELOP + "tas-on-web-services/rest-fcm/fcm-dispositivo/create";
export const URL_GET_IMAGE = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/documentos/";

export const URL_GET_CATALOGO_ITEM = URL_DEVELOP + "tas-on-web-services/rest-catalogo-item/catalogo-item/all/#idCatalogo";
export const URL_GET_LOCALIDAD_BY_PADRE = URL_DEVELOP + "tas-on-web-services/rest-public/public/localidad-all/#idLocalidadPadre/#estado";


export const ID_CATALOGO_PERSONA = 3;
export const ID_CATALOGO_DOCUMENTO = 4;

export const ID_SOLICITUD_ENVIO_ACTIVA = 22;
export const ID_SOLICITUD_ENVIO_CANCELADA = 26;

export const ID_OPCION_CODIGO_VENDEDOR = 274;
export const ID_CHECK = 0;
export const ID_EMPRESA_PROVEEDOR = 61;
export const ID_CONDUCTOR_INDEPENDIENTE = 276;
export const ID_LOCAL_PADRE_ECUADOR = 1;
export const ID_CATALOGO_LICENCIA = 12;
export const ID_CATALOGO_PESO = 8;
export const ID_USUARIO_CONDUCTOR = 9;
export const ID_USUARIO_CONDUCTOR_CLIENTE = 77;
export const ID_CATALOGO_VOLUMEN = 9;
export const ID_CATALOGO_CAMION = 11;
export const ID_CATALOGO_CARGA = 13;
export const ID_CATALOGO_BANCO = 16;
export const ID_CATALOGO_TIPO_CUENTA = 17;
export const ID_CATALOGO_FORMAS_PAGO = 29;
export const ID_PERSONA_NATURAL = 11;
export const ID_PERSONA_JURIDICA = 12;
export const ID_RUC = 13;
export const ID_COMISION_INMEDIATO = 160;
export const ID_FACTURA_NORMAL = 160;
export const ID_FACTURA_INMEDIATA = 161;
export const ID_ESTADO_ACTIVO = 1;
export const ID_ESTADO_USUARIO_CREADO = 5;
export const ID_ESTADO_USUARIO_PENDIENTE = 6;
export const ID_ESTADO_OFERTA_CREADA = 31;
export const ID_CATALOGO_UNIDAD_PIEZAS = 30;
export const PUBLIC_USER = "public";
export const PUBLIC_PASSWORD = "9a4fe825a7a5c0931159b3c53580a8f7caafe22d";
export const URL_SHIPPING = URL_DEVELOP + "tas-on-web-services/rest-public/public/solicitudes-all/";
export const URL_SHIPPING_DETAIL = URL_DEVELOP + "tas-on-web-services/rest-public/public/solicitud/";
export const HEADER_AUTHORIZATION = "Authorization";
export const HEADER_BASIC = "Basic";
export const HEADER_BEARER = "Bearer";
export const HEADER_PUBLIC = "cHVibGljOjlhNGZlODI1YTdhNWMwOTMxMTU5YjNjNTM1ODBhOGY3Y2FhZmUyMmQ=";
export const SHIPPING_REQUEST_CREATED = "22";
export const RUC_TASON = "1792885256001";

export const JSON_TYPE = "JSON";
export const URL_ENCODED_TYPE = "URL-ENCODED";
export const SUCCESS_STATUS_CODE = 200;

export const MY_OFERTS = "31";
export const MY_OFERTS_APPROVED = "32";
export const MY_OFERTS_TO_RECIEVE = "24";
export const MY_OFERTS_TO_DELIVER = "54";
export const MY_OFERTS_GENERATE_INVOICE = "62";
export const MY_OFERTS_DELIVER_INVOICE = "70";
export const MY_OFERTS_FINISHED = "36";
export const MY_OFERTS_CANCELED = "34";
export const MY_OFERTS_COLLECT = "35";

export const ESTADO_FOTO_POR_RECIBIR = 1;
export const ESTADO_FOTO_POR_ENTREGAR = 2;
export const FACT_RAPIDA = "rapida";
export const FACT_NORMAL = "normal";
export const VIBRATION_DURATION=1000;

export const ESTADO_USUARIO_CREADO = "5";
export const ESTADO_USUARIO_ACTIVO = "1";
export const SERVICE_ERROR_STATUS = 404;

export const idEmpresaType = 76;
export const idClienteType = 10;
export const idUsuarioConductorAdmin = 77;
export const idUsuarioConductor = 9;
export const idTipoClienteEmpresaTransportista = 61;
export const idLocalidadPadreEcuador = '1';
export const idCatalogoDocumento = '4';
export const idCatalogoLicencia = '12';
export const idCatalogoTipoCuenta = 17;
export const idCatalogoBanco = 16;
export const idCatalogoPesos = '8';
export const idCatalogoCarga = '13';
export const idCatalogoCamion = '11';
export const idPersonaJuridica = 12;

export const idEstadoUsuarioPendiente = 6;
export const idConductorType = '9';

export const idCatalogoPersona = '3';
export const idCatalogoPeriodo = '14';
export const idCatalogoTipoProducto = 27;
export const idCatalogoMedioDifusion = 28;
export const idCatalogoTiposSubasta = 32;

export const idArcsaAlimentos = 1;
export const idArcsaCosmeticos = 2;
export const idArcsaMedicamentos = 3;
export const idBasc= 4;
export const idPaseInternacional= 5;
export const idUsuarioEnviosAdmin= 344;
export const idUsuarioEnvios= 345;
export const idEstadoUsuarioCreado= 5;
export const idEstadoUsuarioEliminado= 3;