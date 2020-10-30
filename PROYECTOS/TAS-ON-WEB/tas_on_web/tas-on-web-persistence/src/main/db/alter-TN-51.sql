update vehiculo set vehiculo_anio = 0, vehiculo_capacidad = 0, vehiculo_certificado_arcsa = 0, vehiculo_tipo_camion = 0, vehiculo_tipo_capacidad = 0, vehiculo_tipo_carga = 0
where vehiculo_id = 0;

UPDATE conductor  SET conductor_tipo_licencia = 0 WHERE conductor_id = 0;