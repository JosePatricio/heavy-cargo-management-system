INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (276, 15, 'EMPRESA TRANSPORTISTA INDEPENDIENTE', null, 1);

#Se actualiza el valor del catalogo de conductor independiente y su tipo de retencion
UPDATE cliente SET cliente_tipo_cliente = 276, cliente_retencion = 1 WHERE cliente_tipo_cliente = 88;