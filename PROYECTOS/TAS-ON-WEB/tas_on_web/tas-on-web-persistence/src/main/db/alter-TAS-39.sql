ALTER TABLE catalogo_item MODIFY catalogo_item_valor VARCHAR (1000);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
VALUES (294, 22, 'MENSAJE ENTREGA FACTURAS',
        'Estimado Socio, agradecemos el habernos confirmado la entrega de la mercadería. El pago de este servicio será realizado el #fechaPago, mismo que será acreditado directamente a su cuenta bancaria. Recuerde que un habilitante para el pago es haber entregado la factura del servicio en nuestras oficinas: Nicolas Clemente Ponce N45-119 Y Vicente Piedrahita, Edificio Fenix Oficina 104 de 9:00 a 13:00 horas', 1);