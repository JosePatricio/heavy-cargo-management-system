create table calificacion_transportista
(
  calificacion_transportista_id int auto_increment primary key not null,
  calificacion_transportista_oferta_id int not null unique,
  calificacion_transportista_valor int not null,
  calificacion_transportista_comentario varchar(500) null,
  calificacion_transportista_usuario varchar(13) null,
  calificacion_transportista_fecha_calificacion datetime null,
  constraint calificacion_transportista_oferta_id_fk
    foreign key (calificacion_transportista_oferta_id) references oferta (oferta_id),
  constraint calificacion_transportista_usuario_fk
    foreign key (calificacion_transportista_usuario) references usuario (usuario_id_documento)
)
charset=latin1
;


insert into calificacion_transportista(calificacion_transportista_oferta_id, calificacion_transportista_valor, calificacion_transportista_comentario, calificacion_transportista_usuario, calificacion_transportista_fecha_calificacion)
select oferta_id, 5, '', null, CURRENT_TIMESTAMP
from oferta where oferta_archivo_entrega is not null;
