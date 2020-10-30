package ec.redcode.tas.on.facturacion.processor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SRIEnum {

    FORMATO_FECHA("dd/MM/yyyy"),
    ACTIVIDAD_COMERCIAL("Servicio de transporte de carga pesada a través de subasta On-Line"),

    IDENTIFICACION_RUC("04"),
    IDENTIFICACION_CEDULA("05"),
    IDENTIFICACION_PASAPORTE("06"),

    COMPROBANTE_FACTURA("01"),
    COMPROBANTE_NOTA_CREDITO("04"),
    COMPROBANTE_NOTA_DEBITO("05"),
    COMPROBANTE_GUIA_REMISION("06"),
    COMPROBANTE_COMPROBANTE_RETENCION("07"),

    IMPUESTO_IVA("2"),
    IMPUESTO_ICE("3"),
    IMPUESTO_IRBPNR("5"),

    PORCENTAJE_IVA_0("0"),
    PORCENTAJE_IVA_12("2"),
    PORCENTAJE_IVA_14("3"),
    PORCENTAJE_IVA_NO_OBJETO_DE_IMPUESTO("6"),
    PORCENTAJE_IVA_EXENTO("7"),

    MONEDA("DOLAR"),

    UNIDAD_TIEMPO_DIAS("dias"),

    FORMA_PAGO_SIN_UTILIZACION_SISTEMA_FINANCIERO("01"),
    FORMA_PAGO_TARJETA_DEBITO("16"),
    FORMA_PAGO_TARJETA_CREDITO("19"),
    FORMA_PAGO_OTROS_CON_UTILIZACION_SISTEMA_FINANCIERO("20")

    ;

    @Getter private String value;

}
