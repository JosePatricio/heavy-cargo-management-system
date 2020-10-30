ALTER TABLE cliente ADD COLUMN cliente_acreditar_conductor INT NULL;
ALTER TABLE cliente ADD COLUMN cliente_acreditar_vehiculo INT NULL;

CREATE TABLE conductor_acreditado
(
  conductor_acreditado_id int auto_increment primary key not null,
  conductor_acreditado_conductor_id int not null,
  conductor_acreditado_cliente_ruc varchar(13) not null,
  CONSTRAINT conductor_acreditado_conductor_fk FOREIGN KEY (conductor_acreditado_conductor_id) REFERENCES conductor (conductor_id),
  CONSTRAINT conductor_acreditado_cliente_fk FOREIGN KEY (conductor_acreditado_cliente_ruc) REFERENCES cliente (cliente_ruc)
)charset=latin1
;

CREATE TABLE vehiculo_acreditado
(
  vehiculo_acreditado_id int auto_increment primary key not null,
  vehiculo_acreditado_vehiculo_id int not null,
  vehiculo_acreditado_cliente_ruc varchar(13) not null,
  CONSTRAINT vehiculo_acreditado_vehiculo_fk FOREIGN KEY (vehiculo_acreditado_vehiculo_id) REFERENCES vehiculo (vehiculo_id),
  CONSTRAINT vehiculo_acreditado_cliente_fk FOREIGN KEY (vehiculo_acreditado_cliente_ruc) REFERENCES cliente (cliente_ruc)
)charset=latin1
;
