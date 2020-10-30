#SE AGREGA NUMERO DE CUENTA A TASON
ALTER TABLE cliente MODIFY COLUMN cliente_cuenta VARCHAR(11);
update cliente set cliente_cuenta = '02005093682' where cliente_ruc = '1792885256001';

#CODIGO DE BANCO SEGUN PRODUBANCO
#BANCO PICHINCHA C.A.
UPDATE catalogo_item SET catalogo_item_valor = '0010' WHERE catalogo_item_id = 67;
#BANCO PROMERICA S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0036' WHERE catalogo_item_id = 68;
#BANCO DEL PACIFICO S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0030' WHERE catalogo_item_id = 69;
#BANCO DE GUAYAQUIL S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0017' WHERE catalogo_item_id = 88;
#BANCO INTERNACIONAL S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0032' WHERE catalogo_item_id = 89;
#BANCO AMAZONAS S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0034' WHERE catalogo_item_id = 90;
#BANCO AUSTRO S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0035' WHERE catalogo_item_id = 91;
#BANCO BOLIVARIANO C.A.
UPDATE catalogo_item SET catalogo_item_valor = '0037' WHERE catalogo_item_id = 92;
#BANCO CAPITAL S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0061' WHERE catalogo_item_id = 93;
#BANCO CENTRAL DEL ECUADOR - SCP
UPDATE catalogo_item SET catalogo_item_valor = '0001' WHERE catalogo_item_id = 94;
#BANCO COMERCIAL DE MANABI S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0039' WHERE catalogo_item_id = 97;
#BANCO DE LOJA S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0029' WHERE catalogo_item_id = 99;
#BANCO DE MACHALA S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0025' WHERE catalogo_item_id = 100;
#BANCO DEL LITORAL S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0043' WHERE catalogo_item_id = 101;
#BANCO DELBANK S.A.
UPDATE catalogo_item SET catalogo_item_valor = '9966' WHERE catalogo_item_id = 103;
#BANCO GENERAL RUMIÑAHUI S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0042' WHERE catalogo_item_id = 104;
#BANCO PARA LA ASISTENCIA COMUNITARIA FINCA S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0123' WHERE catalogo_item_id = 105;
#BANCO PROCREDIT S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0060' WHERE catalogo_item_id = 106;
#BANCO SOLIDARIO S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0059' WHERE catalogo_item_id = 107;
#CITIBANK (CITIGROUP)
UPDATE catalogo_item SET catalogo_item_valor = '0024' WHERE catalogo_item_id = 111;
#BANCO D-MIRO S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0183' WHERE catalogo_item_id = 113;
#BANECUADOR B.P. ANTIGUO BNF
UPDATE catalogo_item SET catalogo_item_valor = '0066' WHERE catalogo_item_id = 115;
#CORPORACION FINANCIERA NACIONAL
UPDATE catalogo_item SET catalogo_item_valor = '9963' WHERE catalogo_item_id = 116;
#DINERS CLUB DEL ECUADOR S.A.
UPDATE catalogo_item SET catalogo_item_valor = '9967' WHERE catalogo_item_id = 117;
#MUTUALISTA AMBATO
UPDATE catalogo_item SET catalogo_item_valor = '0087' WHERE catalogo_item_id = 118;
#MUTUALISTA IMBABURA
UPDATE catalogo_item SET catalogo_item_valor = '0085' WHERE catalogo_item_id = 119;
#MUTUALISTA PICHINCHA
UPDATE catalogo_item SET catalogo_item_valor = '0086' WHERE catalogo_item_id = 120;
#MUTUALISTA AZUAY
UPDATE catalogo_item SET catalogo_item_valor = '0088' WHERE catalogo_item_id = 121;
#COOP. DE AH. Y CR. 11 DE JUNIO
UPDATE catalogo_item SET catalogo_item_valor = '0155' WHERE catalogo_item_id = 122;
#COOP. DE AH. Y CR. 23 DE JULIO
UPDATE catalogo_item SET catalogo_item_valor = '9975' WHERE catalogo_item_id = 123;
#COOP. DE AH. Y CR. 9 DE OCTUBRE
UPDATE catalogo_item SET catalogo_item_valor = '0143' WHERE catalogo_item_id = 125;
#COOP. DE AH. Y CR. 29 DE OCTUBRE
UPDATE catalogo_item SET catalogo_item_valor = '9995' WHERE catalogo_item_id = 124;
#COOP. DE AH. Y CR. ALIANZA DEL VALLE
UPDATE catalogo_item SET catalogo_item_valor = '9977' WHERE catalogo_item_id = 126;
#COOP. DE AH. Y CR. ANDALUCIA
UPDATE catalogo_item SET catalogo_item_valor = '9998' WHERE catalogo_item_id = 127;
#BANCO DE LA PRODUCCION S.A.
UPDATE catalogo_item SET catalogo_item_valor = '0036' WHERE catalogo_item_id = 98;
#COOP. DE AH. Y CR. ATUNTAQUI
UPDATE catalogo_item SET catalogo_item_valor = '9983' WHERE catalogo_item_id = 128;
#COOP. DE AH. Y CR. BIBLIAN
UPDATE catalogo_item SET catalogo_item_valor = '0144' WHERE catalogo_item_id = 129;
#COOP. DE AH. Y CR. CACPE LOJA
UPDATE catalogo_item SET catalogo_item_valor = '0185' WHERE catalogo_item_id = 130;
#COOP. DE AH. Y CR. CACPECO
UPDATE catalogo_item SET catalogo_item_valor = '9982' WHERE catalogo_item_id = 131;
#COOP. DE AH. Y CR. CALCETA
UPDATE catalogo_item SET catalogo_item_valor = '9971' WHERE catalogo_item_id = 132;
#COOP. DE AH. Y CR. CAMARA DE COMERC. AMBATO
UPDATE catalogo_item SET catalogo_item_valor = '0233' WHERE catalogo_item_id = 133;
#COOP. DE AH. Y CR. CHONE
UPDATE catalogo_item SET catalogo_item_valor = '9981' WHERE catalogo_item_id = 134;
#COOP. DE AH. Y CR. CONSTRUCCIÓN, COMERCIO Y PRODUC
UPDATE catalogo_item SET catalogo_item_valor = '9972' WHERE catalogo_item_id = 137;
#COOP. DE AH. Y CR. COOPROGRESO
UPDATE catalogo_item SET catalogo_item_valor = '9965' WHERE catalogo_item_id = 139;
#COOP. DE AH. Y CR. COTOCOLLAO
UPDATE catalogo_item SET catalogo_item_valor = '9994' WHERE catalogo_item_id = 140;
#COOP. DE AH. Y CR. EL SAGRARIO
UPDATE catalogo_item SET catalogo_item_valor = '9986' WHERE catalogo_item_id = 141;
#COOP. DE AH. Y CR. GUARANDA
UPDATE catalogo_item SET catalogo_item_valor = '9984' WHERE catalogo_item_id = 142;
#COOP. DE AH. Y CR. JARDÍN AZUAYO
UPDATE catalogo_item SET catalogo_item_valor = '9993' WHERE catalogo_item_id = 143;
#COOP. DE AH. Y CR. JUVENTUD ECUATORIANA PROGRESIST
UPDATE catalogo_item SET catalogo_item_valor = '9974' WHERE catalogo_item_id = 144;
#COOP. DE AH. Y CR. LA DOLOROSA
UPDATE catalogo_item SET catalogo_item_valor = '9988' WHERE catalogo_item_id = 145;
#COOP. DE AH. Y CR. OSCUS
UPDATE catalogo_item SET catalogo_item_valor = '9987' WHERE catalogo_item_id = 147;
#COOP. DE AH. Y CR. PABLO MUÑOZ VEGA
UPDATE catalogo_item SET catalogo_item_valor = '9970' WHERE catalogo_item_id = 148;
#COOP. DE AH. Y CR. PADRE JULIÁN LORENTE
UPDATE catalogo_item SET catalogo_item_valor = '0205' WHERE catalogo_item_id = 149;
#COOP. DE AH. Y CR. QUINCE DE ABRIL
UPDATE catalogo_item SET catalogo_item_valor = '0100' WHERE catalogo_item_id = 151;
#COOP. DE AH. Y CR. RIOBAMBA
UPDATE catalogo_item SET catalogo_item_valor = '9979', catalogo_item_estado = 1 WHERE catalogo_item_id = 152;
#COOP. DE AH. Y CR. SAN FRANCISCO
UPDATE catalogo_item SET catalogo_item_valor = '9992' WHERE catalogo_item_id = 153;
#COOP. DE AH. Y CR. SAN FRANCISCO DE ASÍS
UPDATE catalogo_item SET catalogo_item_valor = '0109' WHERE catalogo_item_id = 154;
#COOP. DE AH. Y CR. SAN JOSÉ
UPDATE catalogo_item SET catalogo_item_valor = '9991' WHERE catalogo_item_id = 155;
#COOP. DE AH. Y CR. SANTA ANA
UPDATE catalogo_item SET catalogo_item_valor = '9976' WHERE catalogo_item_id = 156;
#COOP. DE AH. Y CR. SANTA ROSA
UPDATE catalogo_item SET catalogo_item_valor = '9985' WHERE catalogo_item_id = 157;
#COOP. DE AH. Y CR. TULCÁN
UPDATE catalogo_item SET catalogo_item_valor = '9969' WHERE catalogo_item_id = 158;

#BANCO SUDAMERICANO S.A. Se cambia de estado porque ya no existe
UPDATE catalogo_item SET catalogo_item_estado = 0 WHERE catalogo_item_id = 108;
#BANCO COFIEC S.A. Se cambia de estado porque ya no existe
UPDATE catalogo_item SET catalogo_item_estado = 0 WHERE catalogo_item_id = 96;
#BANCO TERRITORIAL S.A. Se cambia de estado porque ya no existe
UPDATE catalogo_item SET catalogo_item_estado = 0 WHERE catalogo_item_id = 109;
#BANCO UNIVERSAL S.A. UNIBANCO Se cambia de estado porque porque se fusiono con banco Solidario
UPDATE catalogo_item SET catalogo_item_estado = 0 WHERE catalogo_item_id = 110;
#LLOYDS  TSB BANK PLC. (SUCURSAL ECUADOR) Se cambia de estado porque ya no existe
UPDATE catalogo_item SET catalogo_item_estado = 0 WHERE catalogo_item_id = 112;