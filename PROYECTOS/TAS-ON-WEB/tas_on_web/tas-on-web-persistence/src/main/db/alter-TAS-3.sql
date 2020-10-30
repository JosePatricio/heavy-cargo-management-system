-- No crea foreing keys por el siguiente error:
-- ...foreign key constraint failed. There is no index in the referenced table where the referenced columns appear as the first columns.
show character set;
SHOW COLLATION;
-- Revisar si las columnas a las que se referencia tienen el valor en Collation
SHOW FULL COLUMNS FROM usuario;
SHOW FULL COLUMNS FROM adquiriente;
-- Si no lo tienen se lo puede cambiar de esta forma:
alter table adquiriente convert to character set latin1 collate latin1_swedish_ci;

create table factura_manual
(
  factura_manual_id                  int auto_increment primary key,
  factura_manual_usuario_id          varchar(13)      not null,
  factura_manual_secuencial          varchar(15)      null,
  factura_manual_clave_acceso        varchar(50)      null,
  factura_manual_fecha_emision       datetime         null,
  factura_manual_fecha_autorizacion  datetime         null,
  factura_manual_estado              varchar(250)     null,
  factura_manual_total               double           null,
  factura_manual_adquiriente         varchar(13)      not null,
  factura_manual_xml                 text             null,
  constraint factura_manual_usuario_id_fk
    foreign key (factura_manual_usuario_id) references usuario (usuario_id_documento),
  constraint factura_manual_adquiriente_fk
    foreign key (factura_manual_adquiriente) references adquiriente (adquiriente_id_documento)
)
charset=latin1
;
