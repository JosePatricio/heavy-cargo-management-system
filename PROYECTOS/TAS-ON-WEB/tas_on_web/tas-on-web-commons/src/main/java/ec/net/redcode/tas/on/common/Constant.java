package ec.net.redcode.tas.on.common;

public class Constant {

    public static final int USER_ACTIVE = 1;
    public static final int USER_INACTIVE = 2;
    public static final int USER_ELIMINATED = 3;
    public static final int USER_BLOCKED = 4;
    public static final int USER_CREATED = 5;
    public static final int USER_PENDING = 6;

    public static final int USER_DRIVER = 9;
    public static final int USER_CLIENT = 10;
    public static final int USER_CLIENT_REDUCIDO = 82;
    public static final int USER_DRIVER_ADMIN = 77;
    public static final int USER_DRIVER_ENVIOS = 345;
    public static final int USER_ADMIN_ENVIOS = 344;
    public static final int USER_CLIENTE_ADMIN_ENVIOS = 346;
    public static final int USER_CLIENT_ADMIN = 75;
    public static final int USER_ACCOUNTANT = 72;
    public static final int USER_ADMIN = 8;
    public static final int USER_VENDEDOR_CLIENTE = 349;

    public static final int KEY_SIZE = 128;
    public static final int ITERATION_COUNT = 10000;
    public static final String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    public static final String KEY = ":AO7;H5G@spaC@3-;Q=T";
    public static final String PUBLIC_KEY = "TasOnApp";

    public static final int CATALOGO_EMAIL = 5;
    public static final int CATALOGO_SECURITY = 10;
    public static final int CATALOGO_FACTURACION = 22;
    public static final int CATALOGO_FORMAS_PAGO = 29;
    public static final int CATALOGO_MENSAJE_ENTREGA_FACTURAS = 294;

    public static final int CATALOGO_CEDULA = 14;
    public static final int CATALOGO_RUC = 13;
    public static final String CODIGO_SRI_RUC= "04";
    public static final String CODIGO_SRI_CEDULA= "05";

    public static final int SUBASTA_INVERSA_ABIERTA = 290;
    public static final int SUBASTA_INVERSA_VALOR_OBJETIVO = 291;
    public static final int PRECIO_FIJO = 295;

    public static final String EMAIL_HOST = "HOST";
    public static final String EMAIL_PORT = "PORT";
    public static final String EMAIL_PROTOCOL = "PROTOCOL";
    public static final String EMAIL_USERNAME = "USERNAME";
    public static final String EMAIL_PASSWORD = "PASSWORD";
    public static final String EMAIL_DEBUG = "DEBUG";
    public static final String EMAIL_STARTTLS = "STARTTLS";

    public static final String EMAIL_PLANTILLA_NUEVO = "nuevo.vm";
    public static final String EMAIL_PLANTILLA_RESTABLECER = "restablecer.vm";
    public static final String EMAIL_PLANTILLA_BLOQUEO = "bloqueo.vm";
    public static final String EMAIL_PLANTILLA_DESBLOQUEO = "desbloqueo.vm";
    public static final String EMAIL_PLANTILLA_FACTURA = "factura.vm";
    public static final String EMAIL_PLANTILLA_EBILLING = "ebilling.vm";
    public static final String EMAIL_PLANTILLA_RETENCION = "retencion.vm";
    public static final String EMAIL_PLANTILLA_CANCELAR = "cancelar.vm";
    public static final String EMAIL_PLANTILLA_CANCELAR_JOB = "cancelar-job.vm";
    public static final String EMAIL_PLANTILLA_PRONTO_PAGO = "pronto-pago.vm";
    public static final String EMAIL_PLANTILLA_PAGO_OFERTA = "pago-oferta.vm";
    public static final String EMAIL_PLANTILLA_NOTA_CREDITO = "nota_credito.vm";
    public static final String EMAIL_PLANTILLA_ASIGNAR_CONDUCTOR_VEHICULO = "asignar_conductor_vehiculo.vm";
    public static final String EMAIL_SUBJECT = "Notificaci\u00f3n TAS-ON";

    public static final String EMAIL_NUEVO = "NUEVO";
    public static final String EMAIL_RESTABLECER = "RESTABLECER";
    public static final String EMAIL_BLOQUEO = "BLOQUEO";
    public static final String EMAIL_DESBLOQUEO = "DESBLOQUEO";
    public static final String EMAIL_FACTURA = "FACTURA";
    public static final String EMAIL_NOTA_CREDITO = "NOTA_CREDITO";
    public static final String EMAIL_RETENCION = "RETENCION";
    public static final String EMAIL_CANCEL = "CANCEL";
    public static final String EMAIL_CANCEL_JOB = "CANCEL_JOB";
    public static final String EMAIL_PRONTO_PAGO = "PRONTO_PAGO";
    public static final String EMAIL_EBILLING ="EBILLING";
    public static final String EMAIL_PAGO_OFERTA ="PAGO_OFERTA";
    public static final String EMAIL_ASIGNAR_CONDUCTOR_VEHICULO = "ASIGNAR_CONDUCTOR_VEHICULO";

    public static final int OFERTA_CREATED = 31;
    public static final int OFERTA_ASSIGN = 32;
    public static final int OFERTA_RECEIVE = 24;
    public static final int OFERTA_DELIVER = 54;
    public static final int OFERTA_CHARGE = 55;
    public static final int OFERTA_TO_PAY = 35;
    public static final int OFERTA_FINISH = 36;
    public static final int OFERTA_DELIVERY_BILL = 55;
    public static final int OFERTA_CANCEL = 34;
    public static final int OFERTA_NOT_ACCEPTED = 33;
    public static final int OFERTA_GENERATE_BILL = 62;
    public static final int OFERTA_TRANSIT_BILL = 74;
    public static final int OFERTA_READY_TO_PAY = 84;
    public static final int OFERTA_CANCEL_ADMINISTRATION = 159;

    public static final int SOLICITUD_ENVIO_CREATE= 22;
    public static final int SOLICITUD_ENVIO_ASSIGN = 23;
    public static final int SOLICITUD_ENVIO_DISPATCH = 63;
    public static final int SOLICITUD_ENVIO_DELIVERY_PROCESS = 64;
    public static final int SOLICITUD_TO_PAY = 28;
    public static final int SOLICITUD_ENVIO_DELIVER = 25;
    public static final int SOLICITUD_ENVIO_CANCEL = 26;
    public static final int SOLICITUD_ENVIO_PAYED = 29;

    public static final String SECURITY_TIPO_TIEMPO = "TIPO TIEMPO";
    public static final String SECURITY_TIEMPO = "TIEMPO";

    public static final String SUBJECT = "TASON-LOGIN";
    public static final String SUBJECT_TOKEN = "TASON-TOKEN";

    public static final int CREATED = 1;

    public static final String RESPONSE_OK = "OK";
    public static final String RESPONSE_ERROR = "ERROR";

    public static final int INVOICE_PRE = 70;
    public static final int INVOICE_FINISH = 71;
    public static final int INVOICE_TO_PAY = 73;
    public static final int INVOICE_RECEIVABLE = 83;

    public static final String RUC_TASON = "1792885256001";
    public static final String NEMONICO_TASON = "TAS-ON";

    public static final String INVOICE_ID = "comprobante";
    public static final String INVOICE_VERSION = "1.0.0";
    public static final String INVOICE_ENVIROMENT = "1";
    public static final String INVOICE_EMISION = "1";
    public static final String INVOICE_DOCUMENT = "01";
    public static final String INVOICE_ESTALECIMIENTO = "001";
    public static final String INVOICE_PUNTO_ESTALECIMIENTO = "001";
    public static final String INVOICE_CODIGO_NUMERO = "00042018";
    public static final String INVOICE_MONEDA = "DOLAR";

    public static final int EMPRESA_CLIENTE = 60;

    public static final String PUBLIC_USER = "public";
    public static final String PUBLIC_PASSWORD = "9a4fe825a7a5c0931159b3c53580a8f7caafe22d";

    public static final int DIARIA = 56;
    public static final int SEMANAL = 57;
    public static final int QUINCENAL = 58;
    public static final int MENSUAL = 59;
    public static final String DAY_OF_WEEK = "";

    public static final int COMPROBANTE_RETENCION = 81;

    public static final int ADMIN_DIAS_GESTION = 87;
    public static final int ADMIN_PAGOS_INMEDIATO = 161;

    public static final String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
    public static final String FCM_KEY = "AAAA4Nt4MZc:APA91bGCjqrrW0e_tc21Laaf0w0ljzv6xFeqIf5Gxi18h_hZISh5fZZubBP9qCP0FUvIy2hCb2iUl3lW7jPL0CmpA6qWBAUAkMfq6v5xarYhpposz2qLCqxER6f2JpcFre7P0dmDRfyZ4_8q5a-jEU596jxsLQ_XTQ";
    public static final String FCM_KEY_V2 = "AAAAhULPBkw:APA91bG9oiR9CzthYL_51U4w1Pfo4AEJMA913hoF9rK14ZlqqnpVW4vK0kxu4glP7lnqm9Cc_E9MY4mEemTQEYXKpG9QyHAwcsDuNjOv8C7VWITc95gmdwnR-a2Zq4Yty9sagunzNGV0";

    public static final int FACTURAS_PROVEEDOR_PEDIENTE_PAGO = 172;
    public static final int FACTURAS_PROVEEDOR_LISTA_PAGO = 173;
    public static final int FACTURAS_PROVEEDOR_PAGADAS = 174;

    public static final String SECRET_KEY = "8Bz3edqCFnDTfklD0RU5mA==";

    public static final int CLIENTE_COMISION_DEFECTO = 12;
    public static final int CLIENTE_DIAS_CREDITO_DEFECTO = 30;

    public static final int CLIENTE_EMPRESA_TRANSPORTISTA = 61;
    public static final int CLIENTE_EMPRESA_TRANSPORTISTA_INDEPENDIENTE = 276;
    public static final int CLIENTE_EMPRESA_TRANSPORTISTA_ENVIOS = 343;

    public static final int CLIENTE_CUENTA_AHORROS = 65;
    public static final int CLIENTE_CUENTA_CORRIENTE = 66;

    public static final int CATALOGO_POLICY_CREACION_SOLICITUDES = 289;
    public static final int CATALOGO_MAIL_CONTADOR = 262;

    public static final int TONELADA_ID = 37;
    public static final int KILOGRAMO_ID = 39;
    public static final int LIBRA_ID = 40;

    public static final int ESTADO_TODAS_SOLICITUDES_ENTREGADAS = 999;

    public final static int ESTADO_FOTO_POR_RECIBIR = 1;
    public final static int ESTADO_FOTO_POR_ENTREGAR = 2;

    public final static int ESTADO_ENVIO_POR_RECIBIR = 338;
    public final static int ESTADO_ENVIO_POR_ENTREGAR = 339;
    public final static int ESTADO_ENVIO_FINALIZADO = 340;
    public final static int ESTADO_ENVIO_CANCELADO = 341;

    public final static int CATALOGO_HORAS_CADUCIDAD_SOLICITUD = 347;

    public final static int ESTADO_VISITA_PENDIENTE = 350;
    public final static int ESTADO_VISITA_PEDIDO_TOMADO = 351;
}
