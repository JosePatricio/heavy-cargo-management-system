CREATE TABLE nota_credito
(
  nota_credito_id int auto_increment primary key not null,
  nota_credito_clave_acceso varchar(50) null,
  nota_credito_factura_id varchar(25) not null,
  nota_credito_valor double null,
  nota_credito_estado varchar(10) null,
  nota_credito_fecha_autorizacion datetime,
  nota_credito_xml text,
  CONSTRAINT nota_credito_factura_id_fk FOREIGN KEY (nota_credito_factura_id) REFERENCES factura (factura_id)
)charset=latin1
;

INSERT INTO secuencia (secuencia_cliente_id, secuencia_cliente_nemonico, secuencia,
                       secuencia_incremental, secuencia_valor_inicial, secuencia_valor_final)
                       VALUES ('1792885256001', 'NOTAC',
                               0, 1, 0, 999999999);


INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
VALUES (292, 22, 'POLICY-NC', '0+0/20+*+*+*+?', 1);
