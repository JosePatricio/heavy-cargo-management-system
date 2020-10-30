ALTER TABLE solicitud_envio ADD solicitud_envio_observaciones text NULL;

-- tabla de audit para la solicitud de envio
create table solicitud_envio_audit
(
  solicitud_envio_id                 varchar(15)  not null,
  solicitud_envio_peso               double       null,
  solicitud_envio_unidad_peso        int          null,
  solicitud_envio_volumen            double       null,
  solicitud_envio_unidad_volumen     int          null,
  solicitud_envio_numero_piesas      int          null,
  solicitud_envio_direccion_origen   varchar(250) null,
  solicitud_envio_localidad_origen   int          null,
  solicitud_envio_direccion_destino  varchar(250) null,
  solicitud_envio_localidad_destino  int          null,
  solicitud_envio_fecha_recoleccion  datetime     null,
  solicitud_envio_numero_estibadores int          null,
  solicitud_envio_dias_valides       int          null,
  solicitud_envio_fecha_entrega      datetime     null,
  solicitud_envio_estado             int          null,
  solicitud_envio_oferta_id          int          null,
  solicitud_envio_persona_entrega    varchar(70)  null,
  solicitud_envio_persona_recibe     varchar(70)  null,
  solicitud_envio_usuario_id         varchar(13)  null,
  solicitud_envio_fecha_creacion     datetime     null,
  solicitud_envio_fecha_caducidad    datetime     null,
  solicitud_envio_fecha_pago         datetime     null,
  solicitud_envio_fecha_facturacion  datetime     null,
  solicitud_envio_comentario         text         null,
  solicitud_envio_observaciones      text         null,
  solicitud_envio_audit_event        enum('insert','update','delete') not null,
  solicitud_envio_audit_timestamp    datetime     null,
  solicitud_envio_audit_pk           int(11) not null  primary key auto_increment,
  index (solicitud_envio_id),
  index (solicitud_envio_audit_timestamp)
)

-- TRIGGER INSERT
DELIMITER @@
CREATE TRIGGER solicitud_envio_insert AFTER INSERT ON solicitud_envio
  FOR EACH ROW BEGIN

  INSERT INTO solicitud_envio_audit (solicitud_envio_id, solicitud_envio_peso, solicitud_envio_unidad_peso, solicitud_envio_volumen,
                                     solicitud_envio_unidad_volumen, solicitud_envio_numero_piesas, solicitud_envio_direccion_origen,
                                     solicitud_envio_localidad_origen, solicitud_envio_direccion_destino, solicitud_envio_localidad_destino,
                                     solicitud_envio_fecha_recoleccion, solicitud_envio_numero_estibadores, solicitud_envio_dias_valides,
                                     solicitud_envio_fecha_entrega, solicitud_envio_estado, solicitud_envio_oferta_id, solicitud_envio_persona_entrega,
                                     solicitud_envio_persona_recibe, solicitud_envio_usuario_id, solicitud_envio_fecha_creacion, solicitud_envio_fecha_caducidad,
                                     solicitud_envio_fecha_pago, solicitud_envio_fecha_facturacion, solicitud_envio_comentario, solicitud_envio_observaciones, solicitud_envio_audit_event, solicitud_envio_audit_timestamp)
  VALUES (NEW.solicitud_envio_id, NEW.solicitud_envio_peso, NEW.solicitud_envio_unidad_peso, NEW.solicitud_envio_volumen, NEW.solicitud_envio_unidad_volumen,
          NEW.solicitud_envio_numero_piesas, NEW.solicitud_envio_direccion_origen, NEW.solicitud_envio_localidad_origen, NEW.solicitud_envio_direccion_destino,
          NEW.solicitud_envio_localidad_destino, NEW.solicitud_envio_fecha_recoleccion, NEW.solicitud_envio_numero_estibadores, NEW.solicitud_envio_dias_valides,
          NEW.solicitud_envio_fecha_entrega, NEW.solicitud_envio_estado, NEW.solicitud_envio_oferta_id, NEW.solicitud_envio_persona_entrega, NEW.solicitud_envio_persona_recibe,
          NEW.solicitud_envio_usuario_id, NEW.solicitud_envio_fecha_creacion, NEW.solicitud_envio_fecha_caducidad, NEW.solicitud_envio_fecha_pago, NEW.solicitud_envio_fecha_facturacion,
          NEW.solicitud_envio_comentario, NEW.solicitud_envio_observaciones, 'insert', CURRENT_TIMESTAMP);

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
                                     solicitud_envio_fecha_pago, solicitud_envio_fecha_facturacion, solicitud_envio_comentario, solicitud_envio_observaciones, solicitud_envio_audit_event, solicitud_envio_audit_timestamp)
     VALUES (NEW.solicitud_envio_id, NEW.solicitud_envio_peso, NEW.solicitud_envio_unidad_peso, NEW.solicitud_envio_volumen, NEW.solicitud_envio_unidad_volumen,
             NEW.solicitud_envio_numero_piesas, NEW.solicitud_envio_direccion_origen, NEW.solicitud_envio_localidad_origen, NEW.solicitud_envio_direccion_destino,
             NEW.solicitud_envio_localidad_destino, NEW.solicitud_envio_fecha_recoleccion, NEW.solicitud_envio_numero_estibadores, NEW.solicitud_envio_dias_valides,
             NEW.solicitud_envio_fecha_entrega, NEW.solicitud_envio_estado, NEW.solicitud_envio_oferta_id, NEW.solicitud_envio_persona_entrega, NEW.solicitud_envio_persona_recibe,
             NEW.solicitud_envio_usuario_id, NEW.solicitud_envio_fecha_creacion, NEW.solicitud_envio_fecha_caducidad, NEW.solicitud_envio_fecha_pago, NEW.solicitud_envio_fecha_facturacion,
             NEW.solicitud_envio_comentario, NEW.solicitud_envio_observaciones, 'update', CURRENT_TIMESTAMP);

END;
@@
