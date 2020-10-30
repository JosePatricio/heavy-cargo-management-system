#CREACION REGISTROS EN CATALOGO
#TIPO DE PRODUCTO
INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (27, 'TIPO_PRODUCTO', 1);
#MEDIO DE DIFUSION
INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (28, 'MEDIO_DIFUSION', 1);


#CREACION ITEMS PARA TIPO DE PRODUCTO
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (263, 27, 'Alimentos', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (264, 27, 'Autorepuestos', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (265, 27, 'Consumo', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (266, 27, 'Muebles', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (267, 27, 'Medicamentos', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (268, 27, 'Otros', null, 1);


#CREACION ITEMS PARA MEDIO DIFUSION
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (269, 28, 'Redes sociales', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (270, 28, 'Radio', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (271, 28, 'Prensa', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (272, 28, 'TV', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (273, 28, 'Referido', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (274, 28, 'Vendedor', null, 1);
INSERT INTO catalogo_item
(catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (275, 28, 'Otro', null, 1);


#Creacion campos en tabla cliente
ALTER TABLE cliente ADD COLUMN cliente_tipo_producto int NULL;
ALTER TABLE cliente ADD COLUMN cliente_conocimiento_registro int NULL;
ALTER TABLE cliente ADD COLUMN cliente_codigo_vendedor varchar(15) NULL;

#Agregar el tipo de producto Medicamentos al cliente LIFE (VERIFICAR RUC)
UPDATE cliente SET  cliente_tipo_producto = 267 WHERE cliente_ruc = '1790013502001';
