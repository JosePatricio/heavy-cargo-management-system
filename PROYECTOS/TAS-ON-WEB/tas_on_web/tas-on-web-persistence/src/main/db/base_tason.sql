-- we don't know how to generate schema (class Schema) :(
create table catalogo
(
	catalogo_id int not null
		primary key,
	catalogo_descripcion varchar(20) null,
	catalogo_estado int null
)
;

create table catalogo_item
(
	catalogo_item_id int not null
		primary key,
	catalogo_item_id_catalogo int null,
	catalogo_item_descripcion varchar(50) null,
	catalogo_item_valor varchar(100) null,
	catalogo_item_estado int null,
	constraint catalogo_item__catalogo_item_id_catalogo_fk
	foreign key (catalogo_item_id_catalogo) references catalogo (catalogo_id)
)
;

create index catalogo_item__catalogo_item_id_catalogo_fk
	on catalogo_item (catalogo_item_id_catalogo)
;

create table fcm_dispositivo
(
	fcm_dispositivo_token varchar(300) not null
		primary key
)
;

create table localidad
(
	localidad_id int not null
		primary key,
	localidad_descripcion varchar(100) null,
	localidad_code int default '0' not null,
	localidad_localidad_padre int null,
	localidad_estado int null,
	constraint localidad_catalogo_item_catalogo_item_id_fk
	foreign key (localidad_estado) references catalogo_item (catalogo_item_id)
)
;

create table cliente
(
	cliente_ruc varchar(13) not null
		primary key,
	cliente_tipo_id int null,
	cliente_localidad_id int null,
	cliente_razon_social varchar(100) null,
	cliente_direccion varchar(250) null,
	cliente_dias_credito int null,
	cliente_dias_periodicidad int null,
	cliente_tipo_cliente int null,
	cliente_comision int default '0' null,
	cliente_cuenta varchar(10) default '0' null,
	cliente_banco int default '0' null,
	cliente_tipo_cuenta int default '0' null,
	cliente_retencion int default '0' null,
	cliente_iva int default '0' null,
	constraint cliente_cliente_ruc_uindex
	unique (cliente_ruc),
	constraint cliente_tipo_id_fk
	foreign key (cliente_tipo_id) references catalogo_item (catalogo_item_id),
	constraint cliente_localida_id_fk
	foreign key (cliente_localidad_id) references localidad (localidad_id),
	constraint cliente_catalogo_item_catalogo_item_id_fk
	foreign key (cliente_dias_periodicidad) references catalogo_item (catalogo_item_id)
		on update cascade on delete cascade,
	constraint cliente_tipo_cliente_catalogo_item_catalogo_item_id_fk
	foreign key (cliente_tipo_cliente) references catalogo_item (catalogo_item_id)
)
;

create index cliente_catalogo_item_catalogo_item_id_fk
	on cliente (cliente_dias_periodicidad)
;

create index cliente_localida_id_fk
	on cliente (cliente_localidad_id)
;

create index cliente_tipo_cliente_catalogo_item_catalogo_item_id_fk
	on cliente (cliente_tipo_cliente)
;

create index cliente_tipo_id_fk
	on cliente (cliente_tipo_id)
;

create index localidad_catalogo_item_catalogo_item_id_fk
	on localidad (localidad_estado)
;

create table secuencia
(
	secuencia_id int auto_increment
		primary key,
	secuencia_cliente_id varchar(13) null,
	secuencia_cliente_nemonico varchar(5) null,
	secuencia int null,
	secuencia_incremental int null,
	secuencia_valor_inicial int null,
	secuencia_valor_final int null,
	constraint secuencia_secuencia_id_uindex
	unique (secuencia_id)
)
;

create index secuencia_secuencia_cliente_id_index
	on secuencia (secuencia_cliente_id)
;

create table usuario
(
	usuario_id_documento varchar(13) not null
		primary key,
	usuario_tipo_documento int null,
	usuario_nombres varchar(50) null,
	usuario_apellidos varchar(50) null,
	usuario_celular varchar(10) null,
	usuario_convecional varchar(10) null,
	usuario_ruc varchar(13) null,
	usuario_direccion varchar(100) null,
	usuario_localidad int null,
	usuario_nombre_usuario varchar(30) null,
	usuario_contrasenia varchar(300) null,
	usuario_estado int null,
	usuario_mail varchar(100) null,
	usuario_tipo_usuario int null,
	usuario_principal tinyint(1) default '0' null,
	constraint usuarios_localidad_id_localidad_fk
	foreign key (usuario_localidad) references localidad (localidad_id),
	constraint usuarios_catalogos_item_id_catalogo_fk
	foreign key (usuario_estado) references catalogo_item (catalogo_item_id),
	constraint usuario_catalogo_item_catalogo_tipo_usuario_id_fk
	foreign key (usuario_tipo_usuario) references catalogo_item (catalogo_item_id)
)
;

create table conductor
(
	conductor_id int auto_increment
		primary key,
	conductor_usuario varchar(13) null,
	conductor_cedula varchar(9) null,
	conductor_nombre varchar(50) null,
	conductor_apellido varchar(50) null,
	conductor_tipo_licencia int null,
	conductor_telefono varchar(20) null,
	conductor_email varchar(30) null,
	conductor_licencia text null,
	constraint conductor_usuario_usuario_fk
	foreign key (conductor_usuario) references usuario (usuario_id_documento),
	constraint conductor_catalogo_item_fk
	foreign key (conductor_tipo_licencia) references catalogo_item (catalogo_item_id)
)
;

create index conductor_catalogo_item_fk
	on conductor (conductor_tipo_licencia)
;

create index conductor_usuario_usuario_fk
	on conductor (conductor_usuario)
;

create table factura
(
	factura_id varchar(15) not null
		primary key,
	factura_nro varchar(20) null,
	factura_usuario_id varchar(13) null,
	factura_fecha_emision_prefactura datetime null,
	factura_fecha_emision datetime null,
	factura_fecha_autorizacion datetime null,
	factura_nro_autorizacion varchar(44) null,
	factura_tipo_factura int null,
	factura_persona_entrega varchar(100) null,
	factura_estado int null,
	factura_guia_remision varchar(20) null,
	factura_subtotal double null,
	factura_total double null,
	factura_comision double null,
	factura_usuario_recibe varchar(15) null,
	factura_fecha_recepcion datetime null,
	factura_empresa varchar(13) not null,
	constraint facturas_usuarios_id_documento_fk
	foreign key (factura_usuario_id) references usuario (usuario_id_documento),
	constraint factura_catalogo_item_fk
	foreign key (factura_estado) references catalogo_item (catalogo_item_id)
)
;

create index facturas_catalogos_id_catalogo_fk
	on factura (factura_estado)
;

create index facturas_usuarios_id_documento_fk
	on factura (factura_usuario_id)
;

create table pago
(
	pago_id int auto_increment
		primary key,
	pago_factura_id varchar(50) null,
	pago_tipo_id int null,
	pago_banco_id int null,
	pago_numero_documento varchar(50) null,
	pago_fecha_documento datetime null,
	pago_fecha_registro datetime null,
	pago_valor double null,
	pago_retencion_xml text null,
	constraint pago_pago_id_uindex
	unique (pago_id),
	constraint pago_factura_id_fk
	foreign key (pago_factura_id) references factura (factura_id),
	constraint pago_tipo_id_fk
	foreign key (pago_tipo_id) references catalogo_item (catalogo_item_id),
	constraint pago_banco_id_fk
	foreign key (pago_banco_id) references catalogo_item (catalogo_item_id)
)
;

create index pago_banco_id_fk
	on pago (pago_banco_id)
;

create index pago_factura_id_fk
	on pago (pago_factura_id)
;

create index pago_tipo_id_fk
	on pago (pago_tipo_id)
;

create index usuario_catalogo_item_catalogo_tipo_usuario_id_fk
	on usuario (usuario_tipo_usuario)
;

create index usuarios_catalogos_id_catalogo_fk
	on usuario (usuario_estado)
;

create index usuarios_localidad_id_localidad_fk
	on usuario (usuario_localidad)
;

create table vehiculo
(
	vehiculo_id int auto_increment
		primary key,
	vehiculo_usuario varchar(13) null,
	vehiculo_modelo varchar(15) null,
	vehiculo_anio int null,
	vehiculo_placa varchar(8) null,
	vehiculo_matricula text null,
	vehiculo_tipo_carga int null,
	vehiculo_tipo_camion int null,
	vehiculo_certificado_arcsa tinyint(1) null,
	vehiculo_capacidad double null,
	vehiculo_tipo_capacidad int null,
	constraint vehiculo_usuario_id_fk
	foreign key (vehiculo_usuario) references usuario (usuario_id_documento),
	constraint vehiculo_camion_catalogo_item_catalogo_item_id_fk
	foreign key (vehiculo_tipo_camion) references catalogo_item (catalogo_item_id),
	constraint vehiculo_carga_catalogo_item_catalogo_item_id_fk
	foreign key (vehiculo_tipo_camion) references catalogo_item (catalogo_item_id),
	constraint vehiculo_capacidad_catalogo_item_catalogo_item_id_fk
	foreign key (vehiculo_tipo_capacidad) references catalogo_item (catalogo_item_id)
)
;

create table oferta
(
	oferta_id int auto_increment
		primary key,
	oferta_valor double null,
	oferta_observacion varchar(100) null,
	oferta_id_usuario varchar(13) null,
	oferta_id_solicitud varchar(15) null,
	oferta_id_conductor int null,
	oferta_id_vehiculo int null,
	oferta_fecha datetime null,
	oferta_fecha_aceptada datetime null,
	oferta_fecha_finalizada datetime null,
	oferta_fecha_recepcion datetime null,
	oferta_fecha_entrega datetime null,
	oferta_fecha_cancelada datetime null,
	oferta_archivo_entrega longtext null,
	oferta_archivo_recepcion longtext null,
	oferta_estado int null,
	oferta_nro_transferencia varchar(30) null,
	oferta_comision double default '0' null,
	oferta_retencion double default '0' null,
	oferta_iva double default '0' null,
	constraint oferta_usuario_is_fk
	foreign key (oferta_id_usuario) references usuario (usuario_id_documento),
	constraint oferta_vehiculo_id_fk
	foreign key (oferta_id_vehiculo) references vehiculo (vehiculo_id),
	constraint ofertas_catalogos_oferta__fk
	foreign key (oferta_estado) references catalogo_item (catalogo_item_id)
)
;

create table factura_detalle
(
	factura_detalle_id int auto_increment
		primary key,
	factura_detalle_factura_id varchar(15) null,
	factura_detalle_oferta_id int null,
	constraint factura_detalle_factura_id_fk
	foreign key (factura_detalle_factura_id) references factura (factura_id),
	constraint factura_detalle_oferta_id_fk
	foreign key (factura_detalle_oferta_id) references oferta (oferta_id)
)
;

create index detalle_factura_facturas_id_factura_fk
	on factura_detalle (factura_detalle_factura_id)
;

create index factura_detalle_oferta_id_fk
	on factura_detalle (factura_detalle_oferta_id)
;

create index oferta_solicitud_envio_solicitud_envio_id_fk
	on oferta (oferta_id_solicitud)
;

create index oferta_usuario_is_fk
	on oferta (oferta_id_usuario)
;

create index oferta_vehiculo_id_fk
	on oferta (oferta_id_vehiculo)
;

create index ofertas_catalogos_oferta__fk
	on oferta (oferta_estado)
;

create index ofertas_usuarios_id_documento_fk
	on oferta (oferta_id_conductor)
;

create table solicitud_envio
(
	solicitud_envio_id varchar(15) not null
		primary key,
	solicitud_envio_peso double null,
	solicitud_envio_unidad_peso int null,
	solicitud_envio_volumen double null,
	solicitud_envio_unidad_volumen int null,
	solicitud_envio_numero_piesas int null,
	solicitud_envio_direccion_origen varchar(250) null,
	solicitud_envio_localidad_origen int null,
	solicitud_envio_direccion_destino varchar(250) null,
	solicitud_envio_localidad_destino int null,
	solicitud_envio_fecha_recoleccion datetime null,
	solicitud_envio_numero_estibadores int null,
	solicitud_envio_dias_valides int null,
	solicitud_envio_fecha_entrega datetime null,
	solicitud_envio_estado int null,
	solicitud_envio_oferta_id int null,
	solicitud_envio_persona_entrega varchar(70) null,
	solicitud_envio_persona_recibe varchar(70) null,
	solicitud_envio_usuario_id varchar(13) null,
	solicitud_envio_fecha_creacion datetime null,
	solicitud_envio_fecha_caducidad datetime null,
	solicitud_envio_fecha_pago datetime null,
	solicitud_envio_fecha_facturacion datetime null,
	constraint solicitud_envio_catalogo_item_peso__fk
	foreign key (solicitud_envio_unidad_peso) references catalogo_item (catalogo_item_id),
	constraint solicitud_envio_catalogo_item_volumen__fk
	foreign key (solicitud_envio_unidad_volumen) references catalogo_item (catalogo_item_id),
	constraint solicitud_envio_localidad_localidad_id_fk
	foreign key (solicitud_envio_localidad_origen) references localidad (localidad_id),
	constraint solicitud_envio_localidad_localidad_destino_fk
	foreign key (solicitud_envio_localidad_destino) references localidad (localidad_id),
	constraint solicitud_envio_catalogos_item_id_catalogo_fk
	foreign key (solicitud_envio_estado) references catalogo_item (catalogo_item_id),
	constraint solicitud_envio_ofertas_id_oferta_fk
	foreign key (solicitud_envio_oferta_id) references oferta (oferta_id),
	constraint solicitud_envio_usuario_id_fk
	foreign key (solicitud_envio_usuario_id) references usuario (usuario_id_documento)
)
;

create table contrato
(
	contrato_id int not null
		primary key,
	contrato_documento_cliente varchar(13) null,
	contrato_documento_conductor varchar(13) null,
	contrato_id_solicitud varchar(15) null,
	contrato_ip_cliente varchar(15) null,
	contrato_ip_conductor varchar(15) null,
	contrato_imei_conductor varchar(15) null,
	contrato_fecha_contrato datetime null,
	contrato_valor double null,
	contrato_estado int null,
	constraint contratos_usuarios_id_documento_fk
	foreign key (contrato_documento_cliente) references usuario (usuario_id_documento),
	constraint contratos_usuarios_id_documento_conductor_fk
	foreign key (contrato_documento_conductor) references usuario (usuario_id_documento),
	constraint contratos_solicitud_envio_id_solicitud_fk
	foreign key (contrato_id_solicitud) references solicitud_envio (solicitud_envio_id),
	constraint contratos_catalogos_id_catalogo_fk
	foreign key (contrato_estado) references catalogo_item (catalogo_item_id)
)
;

create index contratos_catalogos_id_catalogo_fk
	on contrato (contrato_estado)
;

create index contratos_solicitud_envio_id_solicitud_fk
	on contrato (contrato_id_solicitud)
;

create index contratos_usuarios_id_documento_conductor_fk
	on contrato (contrato_documento_conductor)
;

create index contratos_usuarios_id_documento_fk
	on contrato (contrato_documento_cliente)
;

alter table oferta
	add constraint oferta_solicitud_envio_solicitud_envio_id_fk
foreign key (oferta_id_solicitud) references solicitud_envio (solicitud_envio_id)
;

create index solicitud_envio_catalogo_item_peso__fk
	on solicitud_envio (solicitud_envio_unidad_peso)
;

create index solicitud_envio_catalogo_item_volumen__fk
	on solicitud_envio (solicitud_envio_unidad_volumen)
;

create index solicitud_envio_catalogos_id_catalogo_fk
	on solicitud_envio (solicitud_envio_estado)
;

create index solicitud_envio_localidad_localidad_destino_fk
	on solicitud_envio (solicitud_envio_localidad_destino)
;

create index solicitud_envio_localidad_localidad_id_fk
	on solicitud_envio (solicitud_envio_localidad_origen)
;

create index solicitud_envio_ofertas_id_oferta_fk
	on solicitud_envio (solicitud_envio_oferta_id)
;

create index solicitud_envio_usuario_id_fk
	on solicitud_envio (solicitud_envio_usuario_id)
;

create index vehiculo_camion_catalogo_item_catalogo_item_id_fk
	on vehiculo (vehiculo_tipo_camion)
;

create index vehiculo_capacidad_catalogo_item_catalogo_item_id_fk
	on vehiculo (vehiculo_tipo_capacidad)
;

create index vehiculo_usuario_id_fk
	on vehiculo (vehiculo_usuario)
;