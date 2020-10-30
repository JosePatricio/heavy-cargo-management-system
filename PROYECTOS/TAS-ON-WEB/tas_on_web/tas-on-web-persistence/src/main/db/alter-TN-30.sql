#TN-30
INSERT INTO `catalogo` (`catalogo_id`, `catalogo_descripcion`, `catalogo_estado`) VALUES (20, 'TIPO PAGO PREFACTURA', 1);

INSERT INTO `catalogo_item` (`catalogo_item_id`, `catalogo_item_id_catalogo`, `catalogo_item_descripcion`, `catalogo_item_valor`, `catalogo_item_estado`) VALUES (160, 20, 'NORMAL', '0', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`, `catalogo_item_id_catalogo`, `catalogo_item_descripcion`, `catalogo_item_valor`, `catalogo_item_estado`) VALUES (161, 20, 'PAGO INMEDIATO', '0.10', 1);

ALTER TABLE factura ADD factura_tipo_pago int DEFAULT 0 NULL;
ALTER TABLE factura ADD factura_descuento double DEFAULT 0 NULL;

update factura set factura_tipo_pago = 160 where factura_tipo_factura = 9;
update factura set factura_descuento = 0;

ALTER TABLE oferta ADD oferta_descuento double DEFAULT 0 NULL;