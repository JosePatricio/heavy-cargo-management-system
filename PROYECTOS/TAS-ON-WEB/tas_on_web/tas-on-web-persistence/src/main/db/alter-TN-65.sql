create table adquiriente
(
  adquiriente_id_documento   varchar(13)  not null primary key,
  adquiriente_tipo_documento int          null,
  adquiriente_razon_social   varchar(250) null,
  adquiriente_direccion      varchar(150) null,
  adquiriente_telefono       varchar(13)  null,
  adquiriente_mail           varchar(100) null,
  adquiriente_persona_contacto varchar(100) null
);

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (29, 'FORMAS DE PAGO', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (277, 29, 'SIN UTILIZACION DEL SISTEMA FINANCIERO', '01', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (278, 29, 'COMPENSACIÓN DE DEUDAS', '15', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (279, 29, 'TARJETA DE DÉBITO', '16', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (280, 29, 'DINERO ELECTRÓNICO', '17', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (281, 29, 'TARJETA PREPAGO', '18', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (282, 29, 'TARJETA DE CRÉDITO', '19', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (283, 29, 'OTROS CON UTILIZACION DEL SISTEMA FINANCIERO', '20', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
  VALUES (284, 29, 'ENDOSO DE TÍTULOS', '21', 1);


create table usuario_ebilling
(
  usuario_ebilling_id                varchar(13)      not null primary key,
  usuario_ebilling_id_usuario        varchar(13)      not null,
  usuario_ebilling_fecha_firma       datetime         null,
  usuario_ebilling_firma             longtext         not null,
  usuario_ebilling_logo              longtext         null,
  usuario_ebilling_establecimiento   varchar(3)       not null,
  usuario_ebilling_punto_emision     varchar(3)       not null,
  usuario_ebilling_secuencia         int              not null,
  usuario_ebilling_estado            int              null,
  usuario_ebilling_actividad_comercial varchar(100)   not null
);

create table ebilling
(
  ebilling_id                      int auto_increment primary key,
  ebilling_numero                  varchar(20)      not null,
  ebilling_usuario_ebilling        varchar(13)      null,
  ebilling_usuario_id              varchar(13)      null,
  ebilling_fecha_emision           datetime         null,
  ebilling_fecha_autorizacion      datetime         null,
  ebilling_clave_acceso            varchar(50)      null,
  ebilling_estado                  varchar(250)     null,
  ebilling_guia_remision           varchar(20)      null,
  ebilling_subtotal                double           null,
  ebilling_descuento               double default 0 null,
  ebilling_total                   double           null,
  ebilling_adquiriente             varchar(13)      not null,
  ebilling_tipo_pago               int    default 0 null,
  ebilling_xml                     text             null,
  ebilling_ride                    longtext         null,
  constraint ebilling_catalogo_item_fk
    foreign key (ebilling_tipo_pago) references catalogo_item (catalogo_item_id),
  constraint ebilling_usuarios_ebilling_id_fk
    foreign key (ebilling_usuario_ebilling) references usuario_ebilling (usuario_ebilling_id)
);

create table ebilling_detalle
(
  ebilling_detalle_id                     int auto_increment primary key,
  ebilling_detalle_ebilling_id            int              not null,
  ebilling_detalle_piezas                 int              null,
  ebilling_detalle_unidad_piezas          int              null,
  ebilling_detalle_origen                 int              null,
  ebilling_detalle_destino                int              null,
  ebilling_detalle_detalles_adicionales   varchar(250)     null,
  ebilling_detalle_precio                 double           not null,
  ebilling_detalle_descuento              double default 0 null,
  constraint ebilling_detalle_catalogo_item_fk
    foreign key (ebilling_detalle_unidad_piezas) references catalogo_item (catalogo_item_id),
  constraint ebilling_detalle_localidad_origen_fk
    foreign key (ebilling_detalle_origen) references localidad (localidad_id),
  constraint ebilling_detalle_localidad_destino_fk
    foreign key (ebilling_detalle_destino) references localidad (localidad_id),
  constraint ebilling_detalle_ebilling_id_fk
    foreign key (ebilling_detalle_ebilling_id) references ebilling (ebilling_id)
);

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (30, 'UNIDAD PIEZAS', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (285, 30, 'Cajas', '', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (286, 30, 'Kilogramos', '', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (287, 30, 'Litros', '', 1);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado)
VALUES (288, 30, 'Toneladas', '', 1);