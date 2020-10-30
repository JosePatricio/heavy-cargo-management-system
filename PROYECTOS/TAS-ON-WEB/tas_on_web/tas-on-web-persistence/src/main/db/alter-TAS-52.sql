CREATE TABLE file
(
  file_id                INT AUTO_INCREMENT PRIMARY KEY,
  file_content           LONGTEXT           NOT NULL,
  file_name              VARCHAR(100)       NULL,
  file_size              INT                NULL,
  file_type              VARCHAR(100)       NULL,
  file_upload_date       TIMESTAMP          NULL,
  file_oferta_id         INT                NOT NULL,
  file_tipo              INT                NOT NULL
)
;

CREATE TABLE oferta_file
(
  oferta_file_id         INT AUTO_INCREMENT PRIMARY KEY,
  oferta_file_oferta_id  INT                NOT NULL,
  oferta_file_file_id    INT                NOT NULL,
  oferta_file_tipo       INT                NOT NULL,
  CONSTRAINT oferta_file_oferta_id_fk FOREIGN KEY (oferta_file_oferta_id) REFERENCES oferta (oferta_id),
  CONSTRAINT oferta_file_file_id_fk FOREIGN KEY (oferta_file_file_id) REFERENCES file (file_id)
)
;

-- carga archivos de recepcion
insert into file(file_content, file_name, file_size, file_type, file_upload_date, file_oferta_id, file_tipo) (
  select oferta_archivo_recepcion, null, null, null, oferta_fecha_recepcion, oferta_id, 1
  from oferta where oferta_fecha_recepcion is not null and oferta_archivo_recepcion is not null
);
-- carga archivos de entrega
insert into file(file_content, file_name, file_size, file_type, file_upload_date, file_oferta_id, file_tipo) (
  select oferta_archivo_entrega, null, null, null, oferta_fecha_entrega, oferta_id, 2
  from oferta where oferta_fecha_entrega is not null and oferta_archivo_entrega is not null
);
-- se llena la tabla que une a las ofertas con los archivos
insert into oferta_file(oferta_file_oferta_id, oferta_file_file_id, oferta_file_tipo)  (
  select file_oferta_id, file_id, file_tipo
  from file
);
-- se eliminan las columnas temporales
alter table file drop column file_tipo;
alter table file drop column file_oferta_id;

-- se elimina columnas oferta_archivo_recepcion y oferta_archivo_entrega en la tabla oferta
alter table oferta drop column  oferta_archivo_entrega;
alter table oferta drop column  oferta_archivo_recepcion;