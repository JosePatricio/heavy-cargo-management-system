ALTER TABLE cliente ADD COLUMN cliente_banco_tipo_identificacion INT NULL;
ALTER TABLE cliente ADD CONSTRAINT cliente_banco_tipo_identificacion_fk FOREIGN KEY (cliente_banco_tipo_identificacion) REFERENCES catalogo_item (catalogo_item_id);
ALTER TABLE cliente ADD COLUMN cliente_banco_identificacion VARCHAR(13) NULL;
ALTER TABLE cliente ADD COLUMN cliente_banco_nombres VARCHAR(40) NULL;
ALTER TABLE cliente ADD COLUMN cliente_banco_ultima_actualizacion TIMESTAMP NULL;
ALTER TABLE cliente ADD COLUMN cliente_banco_solic_actualizar TIMESTAMP NULL;
ALTER TABLE cliente ADD COLUMN cliente_banco_intento_actualizar INT NULL;
ALTER TABLE cliente ADD CONSTRAINT cliente_tipo_cuenta_fk FOREIGN KEY (cliente_tipo_cuenta) REFERENCES catalogo_item (catalogo_item_id);
ALTER TABLE cliente ADD CONSTRAINT cliente_banco_fk FOREIGN KEY (cliente_banco) REFERENCES catalogo_item (catalogo_item_id);
