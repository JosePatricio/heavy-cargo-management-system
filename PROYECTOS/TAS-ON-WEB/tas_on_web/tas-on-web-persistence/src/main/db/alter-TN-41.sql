UPDATE catalogo_item t
SET t.catalogo_item_descripcion = 'EMPRESA TRANSPORTISTA'
WHERE t.catalogo_item_id = 61;

INSERT INTO catalogo_item (catalogo_item_id,
                           catalogo_item_id_catalogo,
                           catalogo_item_descripcion,
                           catalogo_item_valor,
                           catalogo_item_estado)
VALUES (null, 15, 'EMPRESA PROVEEDOR', 'NULL', 1);

create table factura_proveedor
(
	factura_proveedor_id int auto_increment primary key not null,
	factura_proveedor_usuario varchar(13) not null,
	factura_proveedor_ruc_cliente varchar(13) null,
	factura_proveedor_numero varchar(30) null,
	factura_proveedor_numero_autorizacion varchar(50) null,
	factura_proveedor_fecha_auntorizacion datetime null,
	factura_proveedor_fecha_emision datetime null,
	factura_proveedor_fecha_recepcion datetime null,
	factura_proveedor_subtotal double null,
	factura_proveedor_iva double null,
	factura_proveedor_retencion double null,
	factura_proveedor_total double null,
	factura_proveedor_fecha_pago datetime null,
	factura_proveedor_forma_pago int null,
	factura_proveedor_compra int null,
	factura_proveedor_estado int null,
	factura_proveedor_transferencia varchar(20) null,
  constraint factura_proveedor_usuario_id_fk
		foreign key (factura_proveedor_usuario) references usuario (usuario_id_documento),
	constraint factura_proveedor_catalogo_id_fk
		foreign key (factura_proveedor_forma_pago) references catalogo_item (catalogo_item_id)
)
charset=latin1
;

ALTER TABLE factura_retencion ADD factura_retencion_factura_proveedor_id int NULL;

ALTER TABLE factura_retencion
ADD CONSTRAINT factura_retencion_factura_proveedor_id_fk
FOREIGN KEY (factura_retencion_factura_proveedor_id) REFERENCES factura_proveedor (factura_proveedor_id);

INSERT INTO catalogo (catalogo_id, catalogo_descripcion, catalogo_estado) VALUES (23, 'FACTURA PROVEEDOR', 1);

INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (172, 23, 'PENDIENTE DE PAGO', 'NULL', NULL);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (173, 23, 'LISTA PAGO', 'NULL', NULL);
INSERT INTO catalogo_item (catalogo_item_id, catalogo_item_id_catalogo, catalogo_item_descripcion, catalogo_item_valor, catalogo_item_estado) VALUES (174, 23, 'PAGADAS', 'NULL', NULL);

ALTER TABLE cliente ADD cliente_correo varchar(20) NULL;

ALTER TABLE cliente MODIFY cliente_correo varchar(50);

ALTER TABLE factura_proveedor ADD factura_proveedor_subtotal12 double NULL;
ALTER TABLE factura_proveedor ADD factura_proveedor_subtotal0 double NULL;

/*Octubre 01*/
INSERT INTO `catalogo` (`catalogo_id`, `catalogo_descripcion`, `catalogo_estado`)
VALUES (25, 'RENTA', 1);
INSERT INTO `catalogo` (`catalogo_id`, `catalogo_descripcion`, `catalogo_estado`)
VALUES (26, 'IVA', 1);


INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (179, 26, 'RET. IVA 10%', '10|9', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (180, 26, 'RET. IVA 20%', '20|10', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (181, 26, 'RET. IVA 30%', '30|1', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (182, 26, 'RET. IVA 50%', '50|11', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (183, 26, 'RET. IVA 70%', '70|2', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (184, 26, 'RET. IVA 100%', '100|3', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (185, 26, 'RET. IVA 0%', '0|7', 1);
INSERT INTO `catalogo_item` (`catalogo_item_id`,
                             `catalogo_item_id_catalogo`,
                             `catalogo_item_descripcion`,
                             `catalogo_item_valor`,
                             `catalogo_item_estado`)
VALUES (186, 26, 'NO RETIENE IVA', '0|8', 1);

ALTER TABLE catalogo_item MODIFY catalogo_item_descripcion text;

INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (187, 25, 'Honorarios profesionales y demás pagos por servicios relacionados con el título profesional','10|303', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (188, 25, 'Servicios predomina el intelecto no relacionados con el título profesional','8|304', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (189, 25, 'Comisiones y demás pagos por servicios predomina intelecto no relacionados con el título profesional','8|304A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (190, 25, 'Pagos a notarios y registradores de la propiedad y mercantil por sus actividades ejercidas como tales','8|304B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (191, 25, 'Pagos a deportistas, entrenadores, árbitros, miembros del cuerpo técnico por sus actividades ejercidas como tales','8|304C', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (192, 25, 'Pagos a artistas por sus actividades ejercidas como tales','8|304D', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (193, 25, 'Honorarios y demás pagos por servicios de docencia','8|304E', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (194, 25, 'Servicios predomina la mano de obra','2|307', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (195, 25, 'Utilización o aprovechamiento de la imagen o renombre','10|308', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (196, 25, 'Servicios prestados por medios de comunicación y agencias de publicidad','1|309', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (197, 25, 'Servicio de transporte privado de pasajeros o transporte público o privado de carga','1|310', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (198, 25, 'Pagos a través de liquidación de compra (nivel cultural o rusticidad)','2|311', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (199, 25, 'Transferencia de bienes muebles de naturaleza corporal','1|312', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (200, 25, 'Compra de bienes de origen agrícola, avícola, pecuario, apícola, cunícula, bioacuático, y forestal','1|312A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (201, 25, 'Impuesto a la Renta único para la actividad de producción y cultivo de palma aceitera','1|312B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (202, 25, 'Regalías por concepto de franquicias de acuerdo a Ley de Propiedad Intelectual - pago a personas naturales','8|314A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (203, 25, 'Cánones, derechos de autor,  marcas, patentes y similares de acuerdo a Ley de Propiedad Intelectual – pago a personas naturales','8|314B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (204, 25, 'Regalías por concepto de franquicias de acuerdo a Ley de Propiedad Intelectual  - pago a sociedades','8|314C', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (205, 25, 'Cánones, derechos de autor,  marcas, patentes y similares de acuerdo a Ley de Propiedad Intelectual – pago a sociedades','8|314D', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (206, 25, 'Cuotas de arrendamiento mercantil (prestado por sociedades), inclusive la de opción de compra','1|319', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (207, 25, 'Arrendamiento bienes inmuebles','8|320', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (208, 25, 'Seguros y reaseguros (primas y cesiones)','1|322', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (209, 25, 'Rendimientos financieros pagados a naturales y sociedades  (No a IFIs)','2|323', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (210, 25, 'Rendimientos financieros: depósitos Cta. Corriente','2|323A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (211, 25, 'Rendimientos financieros:  depósitos Cta. Ahorros Sociedades','2|323B1', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (212, 25, 'Rendimientos financieros: depósito a plazo fijo  gravados','2|323E', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (213, 25, 'Rendimientos financieros: depósito a plazo fijo exentos','0|323E2', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (214, 25, 'Rendimientos financieros: operaciones de reporto - repos','2|323F', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (215, 25, 'Inversiones (captaciones) rendimientos distintos de aquellos pagados a IFIs','2|323G', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (216, 25, 'Rendimientos financieros: obligaciones','2|323H', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (217, 25, 'Rendimientos financieros: bonos convertible en acciones','2|323I', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (218, 25, 'Rendimientos financieros: Inversiones en títulos valores en renta fija gravados ','2|323 M', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (219, 25, 'Rendimientos financieros: Inversiones en títulos valores en renta fija exentos','0|323 N', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (220, 25, 'Intereses y demás rendimientos financieros pagados a bancos y otras entidades sometidas al control de la Superintendencia de Bancos y de la Economía Popular y Solidaria','0|323 O', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (221, 25, 'Intereses pagados por entidades del sector público a favor de sujetos pasivos','2|323 P', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (222, 25, 'Otros intereses y rendimientos financieros gravados ','2|323Q', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (223, 25, 'Otros intereses y rendimientos financieros exentos','0|323R', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (224, 25, 'Pagos y créditos en cuenta efectuados por el BCE y los depósitos centralizados de valores, en calidad de intermediarios, a instituciones del sistema financiero por cuenta de otras personas naturales y sociedades','2|323S', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (225, 25, 'Rendimientos financieros originados en la deuda pública ecuatoriana','0|323T', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (226, 25, 'Rendimientos financieros originados en títulos valores de obligaciones de 360 días o más para el financiamiento de proyectos públicos en asociación público-privada','0|323U', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (227, 25, 'Intereses y comisiones en operaciones de crédito entre instituciones del sistema financiero y entidades economía popular y solidaria.','1|324A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (228, 25, 'Inversiones entre instituciones del sistema financiero y entidades economía popular y solidaria','1|324B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (229, 25, 'Pagos y créditos en cuenta efectuados por el BCE y los depósitos centralizados de valores, en calidad de intermediarios, a instituciones del sistema financiero por cuenta de otras instituciones del sistema financiero','1|324C', 1);

INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (230, 25, 'Dividendos distribuidos a sociedades residentes','0|328', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (231, 25, 'Dividendos distribuidos a fideicomisos residentes','0|329', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (232, 25, 'Dividendos exentos distribuidos en acciones (reinversión de utilidades con derecho a reducción tarifa IR) ','0|331', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (233, 25, 'Otras compras de bienes y servicios no sujetas a retención','0|332', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (234, 25, 'Compra de bienes inmuebles','0|332B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (235, 25, 'Transporte público de pasajeros','0|332C', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (236, 25, 'Pagos en el país por transporte de pasajeros o transporte internacional de carga, a compañías nacionales o extranjeras de aviación o marítimas','0|332D', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (237, 25, 'Valores entregados por las cooperativas de transporte a sus socios','0|332E', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (238, 25, 'Compraventa de divisas distintas al dólar de los Estados Unidos de América','0|332F', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (239, 25, 'Pagos con tarjeta de crédito ','0|332G', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (240, 25, 'Pago al exterior tarjeta de crédito reportada por la Emisora de tarjeta de crédito, solo RECAP','0|332H', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (241, 25, 'Pago a través de convenio de debito (Clientes IFI`s)','0|332I', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (242, 25, 'Enajenación de derechos representativos de capital y otros derechos no cotizados en bolsa ecuatoriana','1|334', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (243, 25, 'Loterías, rifas, apuestas y similares','15|335', 1);

INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (244, 25, 'Otras retenciones aplicables el 1%','1|343', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (245, 25, 'Energía eléctrica','1|343A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (246, 25, 'Actividades de construcción de obra material inmueble, urbanización, lotización o actividades similares','1|343B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (247, 25, 'Impuesto Redimible a las botellas plásticas - IRBP','1|343C', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (248, 25, 'Otras retenciones aplicables el 2%','2|344', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (249, 25, 'Pago local tarjeta de crédito reportada por la Emisora de tarjeta de crédito, solo RECAP','2|344A', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (250, 25, 'Adquisición de sustancias minerales dentro del territorio nacional','2|344B', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (251, 25, 'Otras retenciones aplicables el 8%','8|345', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (252, 25, 'Pago a no residentes- Dividendos distribuidos a personas naturales (domicilados o no en paraiso fiscal) o a sociedades sin beneficiario efectivo persona natural residente en Ecuador (ni domiciladas en paraíso fiscal)','0|504', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (253, 25, 'Pago a no residentes - Dividendos a sociedades domiciladas en paraísos fiscales o regímenes de menor imposición (con o sin beneficiario efectivo persona natural residente en el Ecuador)','10|504C', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (254, 25, 'Pago a no residentes - Dividendos a fideicomisos domiciladas en paraísos fiscales o regímenes de menor imposición (con o sin beneficiario efectivo persona natural residente en el Ecuador)','10|504D', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (255, 25, 'Pago a no residentes - Anticipo dividendos (domiciliadas en paraísos fiscales o regímenes de menor imposición)','28|504F', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (256, 25, 'Pago a no residentes - Préstamos accionistas, beneficiarios o partìcipes (domiciladas en paraísos fiscales o regímenes de menor imposición)','28|504H', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (257, 25, 'Pago a no residentes - Préstamos no comerciales a partes relacionadas  (domiciladas en paraísos fiscales o regímenes de menor imposición)','28|504J', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (258, 25, 'Pago a no residentes - Intereses por financiamiento de proveedores externos','25|505D', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (259, 25, 'Pago a no residentes - Intereses de otros créditos externos','25|505E', 1);
INSERT INTO catalogo_item (catalogo_item_id,catalogo_item_id_catalogo,catalogo_item_descripcion,catalogo_item_valor,catalogo_item_estado) VALUES (260, 25, 'Pago a no residentes - Por las empresas de transporte marítimo o aéreo y por empresas pesqueras de alta mar, por su actividad.','0|520E', 1);
