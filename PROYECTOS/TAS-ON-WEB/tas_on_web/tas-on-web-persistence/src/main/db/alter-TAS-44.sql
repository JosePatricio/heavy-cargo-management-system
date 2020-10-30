INSERT INTO catalogo_item values (295, 32, 'PRECIO FIJO','',1);

update solicitud_envio SET solicitud_envio.solicitud_envio_tipo_subasta = 290 where solicitud_envio.solicitud_envio_tipo_subasta is null;

UPDATE catalogo_item SET catalogo_item_valor = 'Los transportistas pueden ofertar libremente' WHERE catalogo_item_id = 290;
UPDATE catalogo_item SET catalogo_item_valor = 'Los transportistas pueden ofertar valores menores al valor ingresado' WHERE catalogo_item_id = 291;
UPDATE catalogo_item SET catalogo_item_valor = 'Los transportistas aceptarán el envío por el precio ingresado' WHERE catalogo_item_id = 295;

ALTER TABLE solicitud_envio ADD COLUMN solicitud_envio_tipo_subasta int null;

ALTER TABLE solicitud_envio_audit ADD COLUMN solicitud_envio_tipo_subasta int null;
ALTER TABLE solicitud_envio_audit ADD COLUMN solicitud_envio_valor_objetivo DOUBLE NULL;
ALTER TABLE solicitud_envio_audit ADD COLUMN solicitud_envio_ahorro_valor_objetivo DOUBLE NULL;


-- ACTUALIZACION DE TRIGGERS PARA TABLA SOLICITUD_ENVIO

-- TRIGGER INSERT

DROP TRIGGER solicitud_envio_insert;
DROP TRIGGER solicitud_envio_update;


DELIMITER @@
CREATE TRIGGER solicitud_envio_insert AFTER INSERT ON solicitud_envio
  FOR EACH ROW BEGIN

  INSERT INTO solicitud_envio_audit (solicitud_envio_id, solicitud_envio_peso, solicitud_envio_unidad_peso, solicitud_envio_volumen,
                                     solicitud_envio_unidad_volumen, solicitud_envio_numero_piesas, solicitud_envio_direccion_origen,
                                     solicitud_envio_localidad_origen, solicitud_envio_direccion_destino, solicitud_envio_localidad_destino,
                                     solicitud_envio_fecha_recoleccion, solicitud_envio_numero_estibadores, solicitud_envio_dias_valides,
                                     solicitud_envio_fecha_entrega, solicitud_envio_estado, solicitud_envio_oferta_id, solicitud_envio_persona_entrega,
                                     solicitud_envio_persona_recibe, solicitud_envio_usuario_id, solicitud_envio_fecha_creacion, solicitud_envio_fecha_caducidad,
                                     solicitud_envio_fecha_pago, solicitud_envio_fecha_facturacion, solicitud_envio_comentario, solicitud_envio_observaciones,
                                     solicitud_envio_numero_doc_cliente, solicitud_envio_valor_objetivo, solicitud_envio_ahorro_valor_objetivo, solicitud_envio_tipo_subasta,
                                     solicitud_envio_audit_event, solicitud_envio_audit_timestamp)
  VALUES (NEW.solicitud_envio_id, NEW.solicitud_envio_peso, NEW.solicitud_envio_unidad_peso, NEW.solicitud_envio_volumen, NEW.solicitud_envio_unidad_volumen,
          NEW.solicitud_envio_numero_piesas, NEW.solicitud_envio_direccion_origen, NEW.solicitud_envio_localidad_origen, NEW.solicitud_envio_direccion_destino,
          NEW.solicitud_envio_localidad_destino, NEW.solicitud_envio_fecha_recoleccion, NEW.solicitud_envio_numero_estibadores, NEW.solicitud_envio_dias_valides,
          NEW.solicitud_envio_fecha_entrega, NEW.solicitud_envio_estado, NEW.solicitud_envio_oferta_id, NEW.solicitud_envio_persona_entrega, NEW.solicitud_envio_persona_recibe,
          NEW.solicitud_envio_usuario_id, NEW.solicitud_envio_fecha_creacion, NEW.solicitud_envio_fecha_caducidad, NEW.solicitud_envio_fecha_pago, NEW.solicitud_envio_fecha_facturacion,
          NEW.solicitud_envio_comentario, NEW.solicitud_envio_observaciones,
          NEW.solicitud_envio_numero_doc_cliente, NEW.solicitud_envio_valor_objetivo, NEW.solicitud_envio_ahorro_valor_objetivo, NEW.solicitud_envio_tipo_subasta,
          'insert', CURRENT_TIMESTAMP);

END;
@@

-- TRIGGER UPDATE
DELIMITER @@
CREATE TRIGGER solicitud_envio_update AFTER UPDATE ON solicitud_envio
  FOR EACH ROW BEGIN

  INSERT INTO solicitud_envio_audit (solicitud_envio_id, solicitud_envio_peso, solicitud_envio_unidad_peso, solicitud_envio_volumen,
                                     solicitud_envio_unidad_volumen, solicitud_envio_numero_piesas, solicitud_envio_direccion_origen,
                                     solicitud_envio_localidad_origen, solicitud_envio_direccion_destino, solicitud_envio_localidad_destino,
                                     solicitud_envio_fecha_recoleccion, solicitud_envio_numero_estibadores, solicitud_envio_dias_valides,
                                     solicitud_envio_fecha_entrega, solicitud_envio_estado, solicitud_envio_oferta_id, solicitud_envio_persona_entrega,
                                     solicitud_envio_persona_recibe, solicitud_envio_usuario_id, solicitud_envio_fecha_creacion, solicitud_envio_fecha_caducidad,
                                     solicitud_envio_fecha_pago, solicitud_envio_fecha_facturacion, solicitud_envio_comentario, solicitud_envio_observaciones,
                                     solicitud_envio_numero_doc_cliente, solicitud_envio_valor_objetivo, solicitud_envio_ahorro_valor_objetivo, solicitud_envio_tipo_subasta,
                                     solicitud_envio_audit_event, solicitud_envio_audit_timestamp)
  VALUES (NEW.solicitud_envio_id, NEW.solicitud_envio_peso, NEW.solicitud_envio_unidad_peso, NEW.solicitud_envio_volumen, NEW.solicitud_envio_unidad_volumen,
          NEW.solicitud_envio_numero_piesas, NEW.solicitud_envio_direccion_origen, NEW.solicitud_envio_localidad_origen, NEW.solicitud_envio_direccion_destino,
          NEW.solicitud_envio_localidad_destino, NEW.solicitud_envio_fecha_recoleccion, NEW.solicitud_envio_numero_estibadores, NEW.solicitud_envio_dias_valides,
          NEW.solicitud_envio_fecha_entrega, NEW.solicitud_envio_estado, NEW.solicitud_envio_oferta_id, NEW.solicitud_envio_persona_entrega, NEW.solicitud_envio_persona_recibe,
          NEW.solicitud_envio_usuario_id, NEW.solicitud_envio_fecha_creacion, NEW.solicitud_envio_fecha_caducidad, NEW.solicitud_envio_fecha_pago, NEW.solicitud_envio_fecha_facturacion,
          NEW.solicitud_envio_comentario, NEW.solicitud_envio_observaciones,
          NEW.solicitud_envio_numero_doc_cliente, NEW.solicitud_envio_valor_objetivo, NEW.solicitud_envio_ahorro_valor_objetivo, NEW.solicitud_envio_tipo_subasta,
          'update', CURRENT_TIMESTAMP);

END;
@@

-- Se elimina columna que se vuelve innecesaria al realizar este JIRA
alter table cliente drop foreign key cliente_tipo_subasta_fk;
alter table cliente drop column cliente_tipo_subasta;