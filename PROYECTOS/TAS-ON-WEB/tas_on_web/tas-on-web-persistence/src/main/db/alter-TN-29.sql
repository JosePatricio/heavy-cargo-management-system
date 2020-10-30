ALTER TABLE factura ADD factura_retencion double DEFAULT 0 NULL;

update cliente set cliente_retencion = 1 where cliente_tipo_cliente = 61;

INSERT INTO secuencia (secuencia_id, secuencia_cliente_id, secuencia_cliente_nemonico, secuencia, secuencia_incremental, secuencia_valor_inicial, secuencia_valor_final) VALUES (4, '1792885256001', 'RETE', 0, 1, 0, 999999999);

CREATE TABLE factura_retencion
(
    factura_retencion_id varchar(50) PRIMARY KEY NOT NULL,
    factura_retencion_factura_id varchar(25),
    factura_retencion_estado varchar(10),
    factura_retencion_fecha_autorizacion datetime,
    factura_retencion_xml text,
    CONSTRAINT factura_retencion_factura_id_fk FOREIGN KEY (factura_retencion_factura_id) REFERENCES factura (factura_id)
);
CREATE INDEX factura_retencion_id_index ON factura_retencion (factura_retencion_id);
