CREATE TABLE cliente_pedidos
(
  cliente_pedidos_id                    INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  cliente_pedidos_ruc                   VARCHAR(13)  NULL,
  cliente_pedidos_razon_social          VARCHAR(150) NOT NULL,
  cliente_pedidos_nombre                VARCHAR(100) NULL,
  cliente_pedidos_telefono              VARCHAR(10)  NULL,
  cliente_pedidos_correo                VARCHAR(100) NULL,
  cliente_pedidos_localidad_id          INT          NULL,
  cliente_pedidos_lat                   VARCHAR(100) NULL,
  cliente_pedidos_lng                   VARCHAR(100) NULL,
  cliente_pedidos_direccion             VARCHAR(500) NULL,
  cliente_pedidos_foto_id               INT          NULL,
  cliente_pedidos_fecha_creacion        DATETIME     NOT NULL,
  cliente_pedidos_usuario_crea          VARCHAR(13)  NOT NULL,
  cliente_pedidos_vendedor_asignado     VARCHAR(13)  NULL,
  cliente_pedidos_dia_semana_visita     INT          NULL,
  cliente_pedidos_estado                INT          NOT NULL, -- 1 o 0
  constraint cliente_pedidos_localidad_fk
    foreign key (cliente_pedidos_localidad_id) references localidad (localidad_id),
  constraint cliente_pedidos_foto_fk
    foreign key (cliente_pedidos_foto_id) references file (file_id),
  constraint cliente_pedidos_usuario_fk
    foreign key (cliente_pedidos_usuario_crea) references usuario (usuario_id_documento),
  constraint cliente_pedidos_vendedor_asignado_fk
    foreign key (cliente_pedidos_vendedor_asignado) references usuario (usuario_id_documento)
)charset=latin1
;

CREATE TABLE categoria_producto(
  categoria_producto_id             INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  categoria_producto_nombre         VARCHAR(100) NOT NULL,
  categoria_producto_fecha_creacion DATETIME NOT NULL,
  categoria_producto_usuario_crea   VARCHAR(13)  NOT NULL,
  categoria_producto_estado         INT NOT NULL, -- 1 o 0
  constraint categoria_producto_usuario_fk
   foreign key (categoria_producto_usuario_crea) references usuario (usuario_id_documento)
)charset=latin1
;

CREATE TABLE producto
(
  producto_id                        INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
  producto_codigo                    VARCHAR(15) NULL,
  producto_categoria                 INT NULL,
  producto_nombre                    VARCHAR(100) NOT NULL,
  producto_descripcion               VARCHAR(500) NULL,
  producto_volumen                   DOUBLE NULL,
  producto_unidad_volumen            INT NULL,
  producto_unidades_por_caja         INT NULL,
  producto_precio_sin_imp            DOUBLE NOT NULL,
  producto_precio_con_imp            DOUBLE NOT NULL,
  producto_precio_pvp_sin_imp        DOUBLE NULL,
  producto_precio_pvp_con_imp        DOUBLE NULL,
  producto_fecha_creacion            DATETIME NOT NULL,
  producto_usuario_crea              VARCHAR(13) NOT NULL,
  producto_estado                    INT NOT NULL,  -- 1 o 0
  constraint producto_catalogo_item_fk
    foreign key (producto_unidad_volumen) references catalogo_item (catalogo_item_id),
  constraint producto_categoria_fk
    foreign key (producto_categoria) references categoria_producto (categoria_producto_id),
  constraint producto_usuario_fk
    foreign key (producto_usuario_crea) references usuario (usuario_id_documento)
)charset=latin1
;

CREATE TABLE visita(
  visita_id                             INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  visita_cliente_pedidos                INT NOT NULL,
  visita_vendedor_usuario               VARCHAR(13)  NOT NULL,
  visita_estado                         INT NOT NULL, -- CREAR ESTOS ESTADOS EN LA CATALOGO ITEM
  visita_fecha                          DATETIME NOT NULL,
  visita_fecha_creacion                 DATETIME NOT NULL,
  visita_usuario_crea                   VARCHAR(13)  NOT NULL,
  constraint visita_usuario_fk
    foreign key (visita_usuario_crea) references usuario (usuario_id_documento),
  constraint visita_cliente_pedidos_fk
    foreign key (visita_cliente_pedidos) references cliente_pedidos (cliente_pedidos_id),
  constraint visita_vendedor_usuario_fk
    foreign key (visita_vendedor_usuario) references usuario (usuario_id_documento),
  constraint visita_estado_catalogo_item_id_fk
    foreign key (visita_estado) references catalogo_item (catalogo_item_id)
)charset=latin1
;

CREATE TABLE pedido(
   pedido_id                          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
   pedido_fecha_entrega_desde         DATETIME NULL,
   pedido_fecha_entrega_hasta         DATETIME NULL,
   pedido_persona_recibe              VARCHAR(250) NULL,
   pedido_solicita_credito            INT NULL,
   pedido_total                       DOUBLE NULL,
   pedido_lat                         VARCHAR(100) NULL,
   pedido_lng                         VARCHAR(100) NULL,
   pedido_visita_id                   INT NOT NULL,
   pedido_fecha_creacion              DATETIME NOT NULL,
   pedido_usuario_crea                VARCHAR(13) NOT NULL,
   constraint pedido_usuario_fk
     foreign key (pedido_usuario_crea) references usuario (usuario_id_documento),
   constraint pedido_visita_id_fk
     foreign key (pedido_visita_id) references visita (visita_id)
)charset=latin1
;

CREATE TABLE pedido_documentos_credito
(
  pedido_documentos_credito_id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  pedido_documentos_credito_pedido_id       INT NOT NULL,
  pedido_documentos_file_id                 INT NOT NULL,
  constraint pedido_documentos_credito_fk
    foreign key (pedido_documentos_credito_pedido_id) references pedido (pedido_id),
  constraint pedido_documentos_file_fk
    foreign key (pedido_documentos_file_id) references file (file_id)
)charset=latin1
;

CREATE TABLE pedido_detalle
(
  pedido_detalle_id                          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  pedido_detalle_pedido_id                   INT NOT NULL,
  pedido_detalle_producto_id                 INT NOT NULL,
  pedido_detalle_producto_cantidad           INT NOT NULL,
  pedido_detalle_precio_con_imp              DOUBLE NOT NULL,
  pedido_detalle_precio_sin_imp              DOUBLE NOT NULL,
  pedido_detalle_total_sin_imp               DOUBLE NOT NULL,
  pedido_detalle_total_con_imp               DOUBLE NOT NULL,
  constraint pedido_detalle_pedido_id_fk
    foreign key (pedido_detalle_pedido_id) references pedido (pedido_id),
  constraint pedido_detalle_producto_id_fk
    foreign key (pedido_detalle_producto_id) references producto (producto_id)
)charset=latin1
;

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                          VALUES (349, 2, 'VENDEDOR CLIENTE',
                                  null, 1);

-- Los usuarios que se crean deben tener el rol 349 para ser vendedores

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (34, 'ESTADOS VISITAS', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                          VALUES (350, 34, 'PENDIENTE', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                          VALUES (351, 34, 'PEDIDO TOMADO', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                          VALUES (352, 34, 'SIN PEDIDO', null, 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                          VALUES (353, 34, 'CANCELADA', null, 1);