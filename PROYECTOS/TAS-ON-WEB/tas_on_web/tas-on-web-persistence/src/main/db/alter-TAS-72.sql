CREATE TABLE envio
(
  envio_id                            INT AUTO_INCREMENT PRIMARY KEY,
  envio_tipo                          VARCHAR(1)     NOT NULL,
  envio_origen                        INT            NOT NULL,
  envio_direccion_origen              VARCHAR(500)   NULL,
  envio_destino                       INT            NOT NULL,
  envio_direccion_destino             VARCHAR(500)   NULL,
  envio_numero_documento              VARCHAR(300)   NULL,
  envio_numero_piezas                 INT            NOT NULL,
  envio_numero_estibadores            INT            NULL,
  envio_observaciones                 VARCHAR(1000)  NULL,
  envio_cliente                       VARCHAR(13)    NOT NULL,
  envio_conductor                     INT            NOT NULL,
  envio_vehiculo                      INT            NOT NULL,
  envio_fecha_recoleccion             DATETIME       NULL,
  envio_fecha_entrega                 DATETIME       NULL,
  envio_estado                        INT            NOT NULL,
  envio_usuario_crea                  VARCHAR(13)    NOT NULL,
  envio_fecha_creacion                DATETIME       NOT NULL,
  envio_fecha_recoleccion_completada  DATETIME       NULL,
  envio_fecha_entrega_completada      DATETIME       NULL,
  CONSTRAINT envio_origen_localidad_fk
    FOREIGN KEY (envio_origen) REFERENCES localidad (localidad_id),
  CONSTRAINT envio_destino_localidad_fk
    FOREIGN KEY (envio_destino) REFERENCES localidad (localidad_id),
  CONSTRAINT envio_estado_catalogo_item_fk
    FOREIGN KEY (envio_estado) REFERENCES catalogo_item (catalogo_item_id),
  CONSTRAINT envio_conductor_fk
    FOREIGN KEY (envio_conductor) REFERENCES conductor (conductor_id),
  CONSTRAINT envio_vehiculo_fk
    FOREIGN KEY (envio_vehiculo) REFERENCES vehiculo (vehiculo_id),
  CONSTRAINT envio_cliente_fk
    FOREIGN KEY (envio_cliente) REFERENCES cliente (cliente_ruc),
  CONSTRAINT envio_usuario_id_fk
    FOREIGN KEY (envio_usuario_crea) REFERENCES usuario (usuario_id_documento)
)charset=latin1;



INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (33, 'ESTADOS ENVIOS', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (338, 33, 'POR RECIBIR', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (339, 33, 'POR ENTREGAR', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (340, 33, 'FINALIZADO', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (341, 33, 'CANCELADO', null, 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (343, 15, 'EMPRESA TRANSPORTISTA ENVIOS', null, 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (344, 2, 'ADMINISTRADOR TRANSPORTE ENVIOS', null, 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (345, 2, 'CONDUCTOR ENVIOS', null, 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (346, 2, 'CLIENTE ADMINISTRADOR ENVIOS', null, 1);

CREATE TABLE envio_file
(
  envio_file_id         INT AUTO_INCREMENT PRIMARY KEY,
  envio_file_envio_id   INT                NOT NULL,
  envio_file_file_id    INT                NOT NULL,
  envio_file_tipo       INT                NOT NULL,
  CONSTRAINT envio_file_envio_id_fk FOREIGN KEY (envio_file_envio_id) REFERENCES envio (envio_id),
  CONSTRAINT envio_file_file_id_fk FOREIGN KEY (envio_file_file_id) REFERENCES file (file_id)
);

alter table envio add column envio_pago DOUBLE NULL;