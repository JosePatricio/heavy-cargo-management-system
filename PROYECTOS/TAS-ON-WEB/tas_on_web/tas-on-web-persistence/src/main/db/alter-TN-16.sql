ALTER TABLE factura ADD factura_xml text NULL;
ALTER TABLE factura MODIFY factura_nro_autorizacion varchar(50);
ALTER TABLE factura MODIFY factura_id varchar(20) NOT NULL;

ALTER TABLE factura_detalle MODIFY factura_detalle_factura_id varchar(20);

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (22, 'FACTURACION', 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (160, 20, 'NORMAL', '0', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (161, 20, 'PAGO INMEDIATO', '10', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (162, 22, 'AMBIENTE', '2', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (163, 22, 'EMISION', '1', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (164, 22, 'DOCUMENTO', '01', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (165, 22, 'ESTABLECIMIENTO', '001', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (166, 22, 'PUNTO ESTABLECIMIENTO', '001', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (167, 22, 'VERSION', '1.0.0', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (168, 22, 'ENDPOINT AUTH', 'https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (169, 22, 'ENDPOINT RECEP', 'https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (170, 22, 'POLICY', '0+0/5+*+*+*+?', 1);
