INSERT INTO cliente (
   cliente_ruc, cliente_tipo_id, cliente_localidad_id,
   cliente_razon_social, cliente_direccion, cliente_dias_credito,
   cliente_dias_periodicidad, cliente_tipo_cliente, cliente_comision,
   cliente_cuenta, cliente_banco, cliente_tipo_cuenta,
   cliente_retencion, cliente_iva, cliente_correo,
   cliente_tipo_producto, cliente_conocimiento_registro, cliente_codigo_vendedor
   ) VALUES ('1700000000001', 12, 208,
             'DISTRIBUIDORA LATAM S.A.', 'QUITO', 30,
             56, 60, 12,
             null, 0, 0,
             0, 0, null,
             268, null, null),
            ('1700000001001', 12, 208,
             'COMERCIALIZADORA 2X1', 'QUITO', 30,
             56, 60, 12,
             null, 0, 0,
             0, 0, null,
             265, null, null),
            ('1700000002001', 12, 208,
             'IMPORTADORA RUEDA S.A.', 'QUITO', 30,
             56, 60, 12,
             null, 0, 0,
             0, 0, null,
             264, null, null);
INSERT INTO usuario (
  usuario_id_documento, usuario_tipo_documento, usuario_nombres,
  usuario_apellidos, usuario_celular, usuario_convecional,
  usuario_ruc, usuario_direccion, usuario_localidad,
  usuario_nombre_usuario, usuario_contrasenia, usuario_estado,
  usuario_mail, usuario_tipo_usuario, usuario_principal,
  usuario_fcm_token, usuario_ultima_sesion)
VALUES ('1715659810', 14, 'JUAN JOSE',
         'GALLARDO HERRERA', '0988880310', '026263910',
         '1700000000001', null, null,
         'jgallardo', '9a4fe825a7a5e0931159b3c53580a8f7caafe22d', 1,
         'jgallardo@yopmail.com', 75, 1,
         '', null),
        ('1715659811', 14, 'PEDRO JOSE',
         'VIDAL', '0988880311', '026263911',
         '1700000001001', null, null,
         'pvidal', '9a4fe825a7a5e1931159b3c53580a8f7caafe22d', 1,
         'pvidal@yopmail.com', 75, 1,
         '', null),
        ('1715659812', 14, 'ROBERTO PAUL',
         'LORENZO LOPEZ', '0988880312', '026263912',
         '1700000002001', null, null,
         'rlorenzo', '9a4fe825a7a5e1931159b3c53590a8f7caafe22d', 1,
         'rlorenzo@yopmail.com', 75, 1,
         '', null);

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (31, 'SOLICITUDES', 1);
INSERT INTO catalogo_item (
                           catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion,
                           catalogo_item_valor, catalogo_item_estado)
                           VALUES (
                                   289, 31, 'POLICY creacion solicitudes',
                                   '0+7+8,12,16,18+?+*+*+*', 1);