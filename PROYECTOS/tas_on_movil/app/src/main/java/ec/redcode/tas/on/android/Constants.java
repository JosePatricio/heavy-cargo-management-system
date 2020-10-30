package ec.redcode.tas.on.android;

import java.util.List;

import ec.redcode.tas.on.android.dto.User;
import ec.redcode.tas.on.android.models.City;

/**
 * Created by Josue Oritz on 23/11/2017.
 * Direcciones URL y servicios disponibles
 */

public class Constants {

    public static final String urlProduccion = "http://www.tas-on.com/";
   // public static final String URL_DEVELOP = "http://192.168.1.3:8080/";
   public static final String URL_DEVELOP = "http://qa.tas-on.com/";

    public static final String urlLogin = URL_DEVELOP + "tas-on-web-services/rest-login/login/movil";
    public static final String urlUpdatePass = URL_DEVELOP + "tas-on-web-services/rest-login/login/change-password/";
    public static final String urlLocations = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/all/1/1";
    public static final String urlFletesList = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitudes/#idState";
    public static final String urlFleteDetail = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/";
    public static final String urlMakeAnOfert = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertar";
    public static final String urlMyOferts = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertas/";
    public static final String urlMyOfertDetail = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/";
    public static final String urlVehicle = URL_DEVELOP + "tas-on-web-services/rest-vehiculo/vehiculo/vehiculos/#idSolicitud";
    public static final String urlMyDrivers = URL_DEVELOP + "tas-on-web-services/rest-conductor/conductor/conductores/#idSolicitud";
    public static final String urlUpdateOfert = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/update-oferta/";
    public static final String urlReadEmpre = URL_DEVELOP + "tas-on-web-services/rest-client/client/read/#rucEmpresa";
    public static final String urlClienteByToken = URL_DEVELOP + "tas-on-web-services/rest-client/client/read-auth";
    public static final String urlGetCodeFactura = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/code-factura/";
    public static final String urlCreateInvoice = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/create-preinvoice/";
    public static final String urlInvoiceListByState = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/invoices/#estado";
    public static final String urlInvoiceDetailByNumber = URL_DEVELOP + "tas-on-web-services/rest-factura/factura/invoice-detail/#numbPreInv";

    public static final String urlGenerateEBilling = URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/generate";
    public static final String urlGetAdquiriente =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/adquiriente/#adquirienteId";
    public static final String urlGetEBillingInfo =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/user-info";
    public static final String urlGetMyEBillings =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/my";
    public static final String urlSendMailEbilling =  URL_DEVELOP + "tas-on-web-services/rest-ebilling/ebilling/send-mail/#claveAcceso/#correo";

    public static final String URL_OFERTAS_BY_SOLIC_AND_ESTADO = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/ofertas-solicitud/#idSolicitud/#estado";
    public static final String URL_OFERTA_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/oferta/#idOferta";

    public static final String URL_CATALOGO_ITEM_BY_TYPE = URL_DEVELOP + "tas-on-web-services/rest-public/public/catalogo-item-all/#idCata";
    public static final String URL_CATALOGO_ITEM_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-public/public/catalogo-item-read/#idCatItem";
    public static final String URL_EMPRESA_CLIENT_BY_TYPE = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-clientes/#idTipo";

    public static final String URL_LOCALIDAD_BY_PADRE = URL_DEVELOP + "tas-on-web-services/rest-public/public/localidad-all/#idPadre/#estado";
    public static final String URL_LOCALIDAD_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/read/#idLocalidad";
    public static final String URL_LOCALIDAD_ALL_CITIES = URL_DEVELOP + "tas-on-web-services/rest-localidad/localidad/all-ciudades";

    public static final String URL_CREA_USUARIO = URL_DEVELOP + "tas-on-web-services/rest-public/public/usuario-create/";
    public static final String URL_CREA_EMPRESA = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-create/";
    public static final String URL_READ_EMPRESA = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-read/#ruc";
    public static final String URL_READ_EMPRESA_TRANSP = URL_DEVELOP + "tas-on-web-services/rest-public/public/empresa-transporte-read/#ruc";

    public static final String URL_READ_USUARIO_BY_USERNAME = URL_DEVELOP + "tas-on-web-services/rest-public/public/user/#username";
    public static final String URL_READ_USUARIO_BY_EMAIL = URL_DEVELOP + "tas-on-web-services/rest-public/public/email/#email";
    public static final String URL_RESTABLECER_PASSWORD = URL_DEVELOP + "tas-on-web-services/rest-public/public/usuario-restablecer/#email/#identificador";

    public static final String URL_SECUENCIAL = URL_DEVELOP + "tas-on-web-services/rest-secuencia/secuencia/secuencia/#rucEmpresa";
    public static final String URL_DIAS_HABILES = URL_DEVELOP + "tas-on-web-services/rest-secuencia/secuencia/dias-habiles/#fechaCaducidad";

    public static final String URL_SOLICITUD_CREA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/create/";
    public static final String URL_SOLICITUD_EDITA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/update/";
    public static final String URL_SOLICITUD_CANCELAR = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud-cancel/";
    public static final String URL_SOLICITUD_BY_ID = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud/#idSolicitud";
    public static final String URL_SOLICITUD_OFERTADA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitudes-ofertas";
    public static final String URL_SOLICITUD_ACEPTA_OFERTA = URL_DEVELOP + "tas-on-web-services/rest-solicitud-envio/solicitud-envio/solicitud-oferta/";

    public static final String URL_FCM_REGISTRO = URL_DEVELOP + "tas-on-web-services/rest-fcm/fcm-dispositivo/create";
    public static final String URL_GET_IMAGE = URL_DEVELOP + "tas-on-web-services/rest-oferta/oferta/documentos/";
    public static final Integer ID_CATALOGO_PERSONA = 3;
    public static final Integer ID_CATALOGO_DOCUMENTO = 4;

    public static final Integer ID_SOLICITUD_ENVIO_ACTIVA = 22;
    public static final Integer ID_SOLICITUD_ENVIO_CANCELADA = 26;

    public static final Integer ID_CHECK = 0;
    public static final Integer ID_EMPRESA_PROVEEDOR = 61;
    public static final Integer ID_CONDUCTOR_INDEPENDIENTE = 276;
    public static final Integer ID_LOCAL_PADRE_ECUADOR = 1;
    public static final Integer ID_CATALOGO_LICENCIA = 12;
    public static final Integer ID_CATALOGO_PESO = 8;
    public static final Integer ID_USUARIO_CONDUCTOR = 9;
    public static final Integer ID_USUARIO_CONDUCTOR_CLIENTE = 77;
    public static final Integer ID_CATALOGO_VOLUMEN = 9;
    public static final Integer ID_CATALOGO_CAMION = 11;
    public static final Integer ID_CATALOGO_CARGA = 13;
    public static final Integer ID_CATALOGO_BANCO = 16;
    public static final Integer ID_CATALOGO_TIPO_CUENTA = 17;
    public static final Integer ID_CATALOGO_FORMAS_PAGO = 29;
    public static final Integer ID_PERSONA_NATURAL = 11;
    public static final Integer ID_PERSONA_JURIDICA = 12;
    public static final Integer ID_RUC = 13;
    public static final Integer ID_COMISION_INMEDIATO = 160;
    public static final Integer ID_FACTURA_NORMAL = 160;
    public static final Integer ID_FACTURA_INMEDIATA = 161;
    public static final Integer ID_ESTADO_ACTIVO = 1;
    public static final Integer ID_ESTADO_USUARIO_CREADO = 5;
    public static final Integer ID_ESTADO_USUARIO_PENDIENTE = 6;
    public static final Integer ID_ESTADO_OFERTA_CREADA = 31;
    public static final Integer ID_CATALOGO_UNIDAD_PIEZAS = 30;
    public static final Integer ESTADO_FOTO_POR_RECIBIR = 1;
    public static final Integer ESTADO_FOTO_POR_ENTREGAR = 2;
    public static final String PUBLIC_USER = "public";
    public static final String PUBLIC_PASSWORD = "9a4fe825a7a5c0931159b3c53580a8f7caafe22d";
    public static final String URL_SHIPPING = URL_DEVELOP + "tas-on-web-services/rest-public/public/solicitudes-all/";
    public static final String URL_SHIPPING_DETAIL = URL_DEVELOP + "tas-on-web-services/rest-public/public/solicitud/";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BASIC = "Basic";
    public static final String HEADER_BEARER = "Bearer";
    public static final String HEADER_PUBLIC = "cHVibGljOjlhNGZlODI1YTdhNWMwOTMxMTU5YjNjNTM1ODBhOGY3Y2FhZmUyMmQ=";
    public static final String SHIPPING_REQUEST_CREATED = "22";
    public static final String RUC_TASON = "1792885256001";
    public static List<City> cities;
    public static User user;
    public static int stateToShow;
}