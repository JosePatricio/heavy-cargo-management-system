ALTER TABLE solicitud_envio ADD COLUMN solicitud_envio_valor_objetivo DOUBLE NULL;
ALTER TABLE solicitud_envio ADD COLUMN solicitud_envio_ahorro_valor_objetivo DOUBLE NULL;

INSERT INTO catalogo VALUES (32, 'TIPOS SUBASTA',1);
INSERT INTO catalogo_item values (290, 32, 'SUBASTA INVERSA ABIERTA','',1);
INSERT INTO catalogo_item values (291, 32, 'SUBASTA INVERSA VALOR OBJETIVO','',1);

ALTER TABLE cliente ADD COLUMN cliente_tipo_subasta INT NULL;
ALTER TABLE cliente ADD CONSTRAINT cliente_tipo_subasta_fk FOREIGN KEY (cliente_tipo_subasta) REFERENCES catalogo_item (catalogo_item_id);

UPDATE cliente SET cliente_tipo_subasta = 290 WHERE cliente_tipo_cliente = 60;

ALTER TABLE cliente ADD COLUMN cliente_min_facturacion DOUBLE NULL;
ALTER TABLE cliente ADD COLUMN cliente_max_facturacion DOUBLE NULL;
ALTER TABLE cliente ADD COLUMN cliente_dia_facturacion INT NULL;
ALTER TABLE cliente ADD COLUMN cliente_nota_credito INT NULL;
ALTER TABLE cliente ADD COLUMN cliente_max_dia_facturacion INT NULL;

UPDATE cliente SET cliente.cliente_min_facturacion = 4000 WHERE cliente_ruc = '1790013502001';
UPDATE cliente SET cliente.cliente_max_facturacion = 5000 WHERE cliente_ruc = '1790013502001';

