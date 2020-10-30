ALTER TABLE secuencia MODIFY COLUMN secuencia_cliente_nemonico VARCHAR(6);

INSERT INTO secuencia (secuencia_cliente_id, secuencia_cliente_nemonico, secuencia, secuencia_incremental, secuencia_valor_inicial, secuencia_valor_final)
VALUES ('1792885256001', 'TAS-ON', 3000, 1, 3000, 999999);
