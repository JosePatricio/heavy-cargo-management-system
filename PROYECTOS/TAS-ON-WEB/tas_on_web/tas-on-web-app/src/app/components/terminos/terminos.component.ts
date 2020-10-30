import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-terminos',
  templateUrl: './terminos.component.html',
  styles: []
})
export class TerminosComponent implements OnInit {

  public show: boolean = false;

  htmlTerms = '<h4>TÉRMINOS, CONDICIONES Y POLÍTICAS DE PRIVACIDAD</h4>\n' +
    '  <p>\n' +
    '    <b>TASON S.A.</b> es una compañía constituida bajo las leyes y normas de regulación ecuatorianas,\n' +
    '    cuyo objeto es la prestación del servicio de transporte comercial de carga pesada dentro del\n' +
    '    territorio ecuatoriano.\n' +
    '  </p>\n' +
    '  <h4>INFORMACIÓN GENERAL.-</h4>\n' +
    '  <p>\n' +
    '    Este sitio web es de propiedad y operado por <b>TASON S.A.</b>, incluye toda la información, herramientas y\n' +
    '    servicios\n' +
    '    disponibles en este sitio, el usuario está condicionado a la aceptación de todos los términos, condiciones,\n' +
    '    políticas y notificaciones aquí establecidas.\n' +
    '\n' +
    '    Al visitar nuestro sitio y/o transaccionar en el mismo, participa en nuestro “Servicio” y acepta los siguientes\n' +
    '    términos, condiciones y políticas de privacidad, incluidos todos los términos y condiciones adicionales y las\n' +
    '    políticas a las que se hace referencia en el presente documento y/o disponible a través de hipervínculos. Estas\n' +
    '    Condiciones de Servicio se aplican a todos los usuarios del sitio, incluyendo sin limitación a usuarios que sean\n' +
    '    navegadores, proveedores, clientes, comerciantes, y/o colaboradores de contenido.\n' +
    '\n' +
    '    Al acceder o utilizar cualquier parte del sitio, estás aceptando los términos, condiciones y políticas de\n' +
    '    privacidad. Si no estás de acuerdo con todos los términos y condiciones de este acuerdo, entonces no deberías\n' +
    '    acceder a la página web o usar cualquiera de los servicios.\n' +
    '\n' +
    '    Cualquier función nueva o herramienta que se añadan también estarán sujetas a estos términos, condiciones y\n' +
    '    políticas de privacidad. Nos reservamos el derecho de actualizar, cambiar o reemplazar cualquier parte de los\n' +
    '    términos, condiciones y políticas de privacidad mediante la publicación de actualizaciones y/o cambios en nuestro\n' +
    '    sitio web. Es su responsabilidad revisar esta página periódicamente para verificar cambios. Su uso o el acceso al\n' +
    '    sitio web después de la publicación de cualquier cambio constituyen la aceptación de dichos cambios.\n' +
    '  </p>\n' +
    '  <h4>CONDICIONES GENERALES.-</h4>\n' +
    '  <p>\n' +
    '    Nos reservamos el derecho de rechazar la prestación de servicio a cualquier persona, por cualquier motivo y en\n' +
    '    cualquier momento.\n' +
    '\n' +
    '    Este sitio web sólo recogerá información que contenga los datos personales que sean suministrados por los Usuarios y\n' +
    '    que hayan sido consignados con su consentimiento. Esta información personal será almacenada en una base de datos y\n' +
    '    compartida con los prestadores del servicio, garantizando su confidencialidad de acuerdo a la norma vigente sobre\n' +
    '    protección de datos de carácter personal. Los Usuarios podrán solicitar la actualización, rectificación o\n' +
    '    eliminación de los datos proporcionados.\n' +
    '\n' +
    '    <b>TASON S.A.</b> ha establecido los niveles de seguridad más apropiados para garantizar la seguridad de la\n' +
    '    información\n' +
    '    proporcionada por los Usuarios, así como ha instalado todos los medios de seguridad perimetral y medidas para evitar\n' +
    '    el robo, pérdida, mal uso, alteración o acceso no autorizado a datos personales. No obstante, es necesario que se\n' +
    '    señale que estas medidas de seguridad pueden ser objetos de ataques y vulneradas. <b>TASON S.A.</b> se reserva el\n' +
    '    derecho\n' +
    '    de entregar la información personal en caso de orden judicial.\n' +
    '\n' +
    '    Los Usuarios aceptan no reproducir, duplicar, copiar, vender, revender o explotar cualquier parte de la página web,\n' +
    '    sin el expreso permiso por escrito de nuestra parte.\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>CONTENIDO.-</h4>\n' +
    '  <p>\n' +
    '    <b>TASON S.A.</b> es una intermediadora en la prestación del servicio, por lo tanto será de exclusiva\n' +
    '    responsabilidad del conductor la prestación del servicio y de los Usuarios la licitud de la carga a ser\n' +
    '    transportada; en tal virtud, <b>TASON S.A.</b> queda exonerada de cualquier responsabilidad frente al Usuario,\n' +
    '    transportista y terceros.\n' +
    '\n' +
    '    <b>TASON S.A.</b> es una intermediadora en la prestación del servicio, por lo tanto será de exclusiva\n' +
    '    responsabilidad del conductor la prestación del servicio y de los Usuarios la licitud de la\n' +
    '    carga a ser transportada; en tal virtud, <b>TASON S.A.</b> queda exonerada de cualquier\n' +
    '    responsabilidad frente al Usuario, transportista y terceros.\n' +
    '\n' +
    '    <b>TASON S.A.</b> se reserva el derecho de actualizar, modificar, adecuar, configurar el contenido del sitio web,\n' +
    '    así como también no garantiza que los contenidos del sitio web estén en todo caso correctos, completos o\n' +
    '    actualizados.\n' +
    '  </p>\n' +
    '  <h4>ACCESO Y USO.-</h4>\n' +
    '  <p>\n' +
    '    El acceso al sitio web está limitado a Usuarios mayores de 18 años. Los Usuarios podrán acceder al sitio web de\n' +
    '    manera voluntaria, libre y gratuita, que lo realiza bajo su consentimiento y su exclusiva responsabilidad, por lo\n' +
    '    que libera de cualquier compromiso a <b>TASON S.A.</b> Los Usuarios se comprometen hacer un uso adecuado y lícito\n' +
    '    del sitio\n' +
    '    web, por lo que el manejo de su cuenta e información será personal, no podrá ceder a terceros o a menores de edad, y\n' +
    '    exime de cualquier responsabilidad a <b>TASON S.A.</b>\n' +
    '\n' +
    '    Los Usuarios se comprometen a respetar la moral y buenas costumbres generalmente aceptadas y el ordenamiento\n' +
    '    jurídico. El Usuario deberá abstenerse de hacer un uso no autorizado o fraudulento del sitio web y/o de los\n' +
    '    contenidos; acceder o intentar acceder a recursos o áreas restringidas del sitio web, sin cumplir las condiciones\n' +
    '    exigidas para dicho acceso; utilizar el sitio web y/o los contenidos con fines o efectos ilícitos, ilegales,\n' +
    '    contrarios a lo establecido en las presentes Condiciones Generales, a la buena fe y al orden público, lesivos de los\n' +
    '    derechos e intereses de terceros, o que de cualquier forma puedan dañar, inutilizar o sobrecargar el sitio web o\n' +
    '    impedir la normal utilización o disfrute del sitio web; provocar daños en los sistemas físicos o lógicos del sitio\n' +
    '    web, de sus proveedores o de terceros; introducir o difundir en la red virus informáticos o cualesquiera otros\n' +
    '    sistemas físicos o lógicos que sean susceptibles de provocar daños en los sistemas físicos o lógicos del sitio web,\n' +
    '    de sus proveedores o de terceros, intentar acceder, utilizar y/o manipular los datos del sitio web, terceros\n' +
    '    proveedores y otros usuarios; reproducir o copiar, distribuir, permitir el acceso del público a través de cualquier\n' +
    '    modalidad de comunicación pública, transformar o modificar los Contenidos, a menos que se cuente con la autorización\n' +
    '    del titular de los correspondientes derechos o ello resulte legalmente permitido; suprimir, ocultar o manipular las\n' +
    '    notas sobre derechos de propiedad intelectual o industrial y demás datos identificativos de los derechos del sitio\n' +
    '    web o de terceros incorporados a los Contenidos, así como los dispositivos técnicos de protección o cualesquiera\n' +
    '    mecanismos de información que puedan insertarse en los Contenidos; obtener e intentar obtener los Contenidos\n' +
    '    empleando para ello medios o procedimientos distintos de los que, según los casos, se hayan puesto a su disposición\n' +
    '    a este efecto o se hayan indicado expresamente en las páginas web donde se encuentren los Contenidos o, en general,\n' +
    '    de los que se empleen habitualmente en internet por no entrañar un riesgo de daño o inutilización del sitio web y/o\n' +
    '    de los contenidos.\n' +
    '  </p>\n' +
    '  <h4>CONDICIONES Y PROHIBICIONES ADICIONALES.-</h4>\n' +
    '  <p>El acceso al servicio estará sujeto a un previo registro en el sitio web, con información legal, actual, veraz y\n' +
    '    fidedigna, previo proceso de validación, que será facilitado por los Usuario o terceros de manera libre y\n' +
    '    voluntaria. <b>TASON S.A.</b> una vez analizada la información se reserva el derecho de aceptar o rechazar la\n' +
    '    solicitud de\n' +
    '    registro por parte del Usuario o de un tercero. Posterior al proceso de validación se proporcionará una contraseña,\n' +
    '    personal e intransferible, que servirá para el uso del servicio, los Usuarios o terceros serán responsables del\n' +
    '    Usuario y Contraseña, incluso por su uso, acceso y custodia, respondiendo, asumiendo e indemnizando en caso de\n' +
    '    causar daños y perjuicios por su indebida aplicación, extravío, cesión o revelación. En caso de olvido de la\n' +
    '    contraseña o cualquier otra circunstancia que suponga un riesgo de acceso y/o utilización por parte de terceros no\n' +
    '    autorizados, los Usuarios o terceros deberán comunicarlo inmediatamente a <b>TASON S.A.</b> a fin de que ésta\n' +
    '    proceda\n' +
    '    inmediatamente al bloqueo y sustitución de la misma.\n' +
    '\n' +
    '    En adición a otras prohibiciones como se establece en los Términos de Servicio, se prohíbe el uso del sitio o su\n' +
    '    contenido: (a) para ningún propósito ilegal; (b) para pedirle a otros que realicen o participen en actos ilícitos;\n' +
    '    (c) para violar cualquier regulación, reglas, leyes internacionales, federales, provinciales o estatales, u\n' +
    '    ordenanzas locales; (d) para infringir o violar el derecho de propiedad intelectual nuestro o de terceras partes;\n' +
    '    (e) para acosar, abusar, insultar, dañar, difamar, calumniar, desprestigiar, intimidar o discriminar por razones de\n' +
    '    género, orientación sexual, religión, etnia, raza, edad, nacionalidad o discapacidad; (f) para presentar información\n' +
    '    falsa o engañosa; (g) para cargar o transmitir virus o cualquier otro tipo de código malicioso que sea o pueda ser\n' +
    '    utilizado en cualquier forma que pueda comprometer la funcionalidad o el funcionamiento del Servicio o de cualquier\n' +
    '    sitio web relacionado, otros sitios o Internet; (h) para recopilar o rastrear información personal de otros; (i)\n' +
    '    para generar spam, phish, pharm, pretext, spider, crawl, or scrape; (j) para cualquier propósito obsceno o inmoral;\n' +
    '    o (k) para interferir con o burlar los elementos de seguridad del Servicio o cualquier sitio web relacionado¿ otros\n' +
    '    sitios o Internet. Nos reservamos el derecho de suspender el uso del Servicio o de cualquier sitio web relacionado\n' +
    '    por violar cualquiera de los ítems de los usos prohibidos.\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>CONDICIONES DE PAGO Y FACTURACIÓN.-</h4>\n' +
    '  <p>Para el Usuario Cliente que busca trasladar su carga el uso del sitio web es gratuito, sin embargo, el Usuario\n' +
    '    Cliente se compromete a cancelar el valor de la oferta que el cliente selecccione como ganadora. Las cargas\n' +
    '    realizadas a través de la aplicación serán facturadas por <b>TASON S.A.</b> de acuerdo a la forma que el Usuario\n' +
    '    Cliente\n' +
    '    haya elegido el momento del enrolamiento: por cada traslado, de forma diaria, de forma semanal, quincenal o mensual.\n' +
    '    Pudiendo solicitar a través del sitio web el cambio de esta opción, cuando el cliente lo requiera. El Usuario\n' +
    '    cliente, se compromete a cancelar el valor de la factura, por cualquiera de los medios tradicionales de pago de\n' +
    '    servicios, en el periodo de crédito otorgado por <b>TASON S.A.</b> Estas tarifas, una vez cobradas, no serán\n' +
    '    reembolsables.\n' +
    '\n' +
    '    Para el Usuario Transportista, el uso del sitio web es gratuito. <b>TASON S.A</b> se reserva el derecho de modificar\n' +
    '    en\n' +
    '    cualquier momento las condiciones, plazos, montos y formas de pago por el uso de la Plataforma.\n' +
    '\n' +
    '    Las facturas serán entregadas al Usuario Cliente de forma electrónica en formato PDF y XML al correo registrado en\n' +
    '    la aplicación. El Usuario Cliente deberá entregar la retención de forma electrónica al correo info@tas-on.com en el\n' +
    '    plazo establecido por ley. Las retenciones serán enviadas electrónicamente al Usuario Transportista de forma\n' +
    '    electrónica en formato PDF y XML, al momento que se entregue físicamente la factura en las oficinas de <b>TASON\n' +
    '      S.A.</b>\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>DERECHOS DE PROPIEDAD INTELECTUAL E INDUSTRIAL.-</h4>\n' +
    '  <p>\n' +
    '    Los Usuarios y terceros reconocen y aceptan que todos los derechos de propiedad industrial e intelectual sobre los\n' +
    '    Contenidos y/o cualesquiera otros elementos insertados en el sitio web (incluyendo sin limitación, marcas,\n' +
    '    logotipos, nombres comerciales, textos, imágenes, gráficos, diseños, sonidos, bases de datos, software, diagramas de\n' +
    '    flujo, presentación, “look-and-feel”, audio y vídeo), pertenecen a <b>TASON S.A.</b> y/o a terceros.\n' +
    '\n' +
    '    En ningún caso el acceso al sitio web implica ningún tipo de renuncia, transmisión, licencia o cesión total ni\n' +
    '    parcial de dichos derechos, salvo que se establezca expresamente lo contrario. Las presentes Condiciones, Términos y\n' +
    '    Políticas de Privacidad no confieren a los Usuarios o terceros ningún otro derecho de utilización, alteración,\n' +
    '    explotación, reproducción, distribución o comunicación pública del sitio web y/o de sus contenidos distintos de los\n' +
    '    aquí expresamente previstos. Cualquier otro uso o explotación de cualesquiera derechos estará sujeto a la previa y\n' +
    '    expresa autorización específicamente otorgada a tal efecto por <b>TASON S.A.</b> o el tercero titular de los\n' +
    '    derechos\n' +
    '    afectados.\n' +
    '\n' +
    '    <b>TASON S.A.</b> autoriza a los Usuarios a utilizar, visualizar, imprimir, descargar y almacenar los contenidos y/o\n' +
    '    los\n' +
    '    elementos insertados en el sitio web exclusivamente para su uso personal, privado y no lucrativo; siempre que en\n' +
    '    todo caso se indique el origen y/o autor de los mismos y que, en su caso, aparezca el símbolo del copyright y/o\n' +
    '    notas de propiedad industrial de sus titulares. Queda terminantemente prohibida la utilización de tales elementos,\n' +
    '    su reproducción, comunicación y/o distribución con fines comerciales o lucrativos, así como su modificación,\n' +
    '    alteración o descompilación. Para cualquier otro uso distinto de los expresamente permitidos, será necesario obtener\n' +
    '    el consentimiento previo por escrito del titular de los derechos de que se trate.\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>INFORMACIÓN PERSONAL Y PROTECCIÓN.-</h4>\n' +
    '  <p>\n' +
    '    La información personal proporcionada por el Usuario o terceros, de forma libre y voluntaria, será integrada a\n' +
    '    nuestra base datos que es de responsabilidad de <b>TASON S.A.</b>, la cual tendrá los niveles de protección\n' +
    '    adecuados para\n' +
    '    garantizar su reserva e integridad.\n' +
    '  </p>\n' +
    '  <h4>ENLACES DE TERCERAS PARTES.-</h4>\n' +
    '  <p>\n' +
    '    Cierto contenido, productos y servicios disponibles vía nuestro Servicio pueden incluir material de terceras partes.\n' +
    '\n' +
    '    Enlaces de terceras partes en este sitio pueden direccionarte a sitios web de terceras partes que no están afiliadas\n' +
    '    con nosotros. No nos responsabilizamos de examinar o evaluar el contenido o exactitud y no garantizamos ni tendremos\n' +
    '    ninguna obligación o responsabilidad por cualquier material de terceros o sitios web, o de cualquier material,\n' +
    '    productos o servicios de terceros.\n' +
    '\n' +
    '    No nos hacemos responsables de cualquier daño o daños relacionados con la adquisición o utilización de bienes,\n' +
    '    servicios, recursos, contenidos, o cualquier otra transacción realizadas en conexión con sitios web de terceros. Por\n' +
    '    favor revisa cuidadosamente las políticas y prácticas de terceros y asegúrate de entenderlas antes de participar en\n' +
    '    cualquier transacción. Quejas, reclamos, inquietudes o preguntas con respecto a productos de terceros deben ser\n' +
    '    dirigidas a la tercera parte.\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>ERRORES, INEXACTITUDES Y OMISIONES.-</h4>\n' +
    '  <p>\n' +
    '    De vez en cuando puede haber información en nuestro sitio o en el Servicio que contiene errores tipográficos,\n' +
    '    inexactitudes u omisiones que puedan estar relacionadas con las descripciones de productos, precios, promociones,\n' +
    '    ofertas, gastos de envío del producto, el tiempo de tránsito y la disponibilidad. Nos reservamos el derecho de\n' +
    '    corregir los errores, inexactitudes u omisiones y de cambiar o actualizar la información o cancelar pedidos si\n' +
    '    alguna información en el Servicio o en cualquier sitio web relacionado es inexacta en cualquier momento sin previo\n' +
    '    aviso (incluso después de que hayas enviado tu orden).\n' +
    '\n' +
    '    No asumimos ninguna obligación de actualizar, corregir o aclarar la información en el Servicio o en cualquier sitio\n' +
    '    web relacionado, incluyendo, sin limitación, la información de precios, excepto cuando sea requerido por la ley.\n' +
    '    Ninguna especificación actualizada o fecha de actualización aplicada en el Servicio o en cualquier sitio web\n' +
    '    relacionado, debe ser tomada para indicar que toda la información en el Servicio o en cualquier sitio web\n' +
    '    relacionado ha sido modificada o actualizada.\n' +
    '  </p>\n' +
    '  <h4>EXCLUSIÓN DE GARANTÍAS; LIMITACIÓN DE RESPONSABILIDAD.-</h4>\n' +
    '  <p>\n' +
    '    No garantizamos ni aseguramos que el uso de nuestro servicio será ininterrumpido, puntual, seguro o libre de\n' +
    '    errores.\n' +
    '\n' +
    '    No garantizamos que los resultados que se puedan obtener del uso del servicio serán exactos o confiables.\n' +
    '\n' +
    '    Los Usuarios aceptan que de vez en cuando podemos quitar el servicio por períodos de tiempo indefinidos o cancelar\n' +
    '    el servicio en cualquier momento sin previo aviso. Así como que el uso de, o la posibilidad de utilizar, el servicio\n' +
    '    es bajo tu propio riesgo. El servicio y todos los productos y servicios proporcionados a través del servicio son\n' +
    '    (salvo lo expresamente manifestado por nosotros) proporcionados “tal cual” y “según esté disponible” para su uso,\n' +
    '    sin ningún tipo de representación, garantías o condiciones de ningún tipo, ya sea expresa o implícita, incluidas\n' +
    '    todas las garantías o condiciones implícitas de comercialización, calidad comercializable, la aptitud para un\n' +
    '    propósito particular, durabilidad, título y no infracción.\n' +
    '\n' +
    '    En ningún caso <b>TASON S.A.</b>, nuestros directores, funcionarios, empleados, afiliados, agentes, contratistas,\n' +
    '    internos,\n' +
    '    proveedores, prestadores de servicios o trabajadores serán responsables por cualquier daño, pérdida, reclamo, o\n' +
    '    daños directos, indirectos, incidentales, punitivos, especiales o consecuentes de cualquier tipo, incluyendo, sin\n' +
    '    limitación, pérdida de beneficios, pérdida de ingresos, pérdida de ahorros, pérdida de datos, costos de reemplazo, o\n' +
    '    cualquier daño similar, ya sea basado en contrato, agravio (incluyendo negligencia), responsabilidad estricta o de\n' +
    '    otra manera, como consecuencia del uso de cualquiera de los servicios o productos adquiridos mediante el servicio, o\n' +
    '    por cualquier otro reclamo relacionado de alguna manera con el uso del servicio, idoneidad del conductor, o\n' +
    '    cualquier otro percance, incluyendo pero no limitado, a cualquier error u omisión en cualquier contenido, o\n' +
    '    cualquier pérdida o daño de cualquier tipo incurridos como resultados de la utilización del servicio o cualquier\n' +
    '    contenido (o producto) publicado, transmitido, o que se pongan a disposición a través del servicio, incluso si se\n' +
    '    avisa de su posibilidad. Debido a que algunos estados o jurisdicciones no permiten la exclusión o la limitación de\n' +
    '    responsabilidad por daños consecuenciales o incidentales, en tales estados o jurisdicciones, nuestra responsabilidad\n' +
    '    se limitará en la medida máxima permitida por la ley.\n' +
    '\n' +
    '  </p>\n' +
    '  <h4>ACUERDO COMPLETO.-</h4>\n' +
    '  <p>\n' +
    '    Alguna omisión en estos Términos, Condiciones y Políticas de Privacidad no constituirá una renuncia a derecho o\n' +
    '    disposición.\n' +
    '\n' +
    '    Estos términos, condiciones y políticas o reglas de operación publicadas por nosotros en este sitio o con respecto\n' +
    '    al servicio constituyen el acuerdo completo y el entendimiento entre el Usuarios, terceros y nosotros, y rigen el\n' +
    '    uso del servicio y reemplaza cualquier acuerdo, comunicaciones y propuestas anteriores o contemporáneas, ya sea oral\n' +
    '    o escrita.\n' +
    '\n' +
    '    Cualquier ambigüedad en la interpretación de estos términos, condiciones y políticas de privacidad no se\n' +
    '    interpretará en contra de <b>TASON S.A.</b>\n' +
    '  </p>\n' +
    '  <h4>LEGISLACIÓN APLICABLE Y JURISDICCIÓN.-</h4>\n' +
    '  <p>\n' +
    '    Estas condiciones términos y políticas de privacidad, y, cualquier acuerdo adicional al servicio se regirán e\n' +
    '    interpretarán en conformidad con las leyes de Ecuador.\n' +
    '\n' +
    '    Para la solución de conflictos, los Usuarios y terceros renuncian a fuero y acuerdan someterse a un Centro de\n' +
    '    Mediación y Arbitraje o a los Juzgados y/o Tribunales de la ciudad de Quito D.M., salvo exclusión de jurisdicción y\n' +
    '    competencia por territorio y materia o lugar del cometimiento del acto ilícito.\n' +
    '  </p>\n' +
    '  <h4>CAMBIOS EN LOS TÉRMINOS DE SERVICIO.-</h4>\n' +
    '  <p>\n' +
    '    Puedes revisar la versión más actualizada de los Términos de Servicio en cualquier momento en esta página.\n' +
    '\n' +
    '    Nos reservamos el derecho, a nuestra sola discreción, de actualizar, modificar o reemplazar cualquier parte de estas\n' +
    '    Condiciones del servicio mediante la publicación de las actualizaciones y los cambios en nuestro sitio web. Es tu\n' +
    '    responsabilidad revisar nuestro sitio web periódicamente para verificar los cambios. El uso continuo de o el acceso\n' +
    '    a nuestro sitio web o el servicio después de la publicación de cualquier cambio en estas Condiciones de servicio\n' +
    '    implica la aceptación de dichos cambios.\n' +
    '  </p>\n' +
    '  <h4>CESIÓN.-</h4>\n' +
    '  <p>\n' +
    '    Los Usuarios no podrán ceder sus derechos y obligaciones asumidos, salvo autorización expresa de <b>TASON S.A.</b>,\n' +
    '    <b>TASON S.A.</b> podrá ceder sus derechos y obligaciones, sin consentimiento del usuario o de terceros, a sus socios o\n' +
    '    accionistas o cualquier entidad comprometida dentro de su giro del negocio, o en su defecto a quien le suceda en\n' +
    '    caso cesión, liquidación, modificación o extinción.\n' +
    '  </p>\n' +
    '  <h4>NOTIFICACIONES E INFORMACIÓN DE CONTACTO.-</h4>\n' +
    '  <p>\n' +
    '    <b>TASON S.A.</b> podrá notifica a sus Usuario o terceros a través del correo electrónico, sitio web, aplicativo o\n' +
    '    cualquier medio telemático proporcionado en la información del Usuario o de terceros registrada en la base datos.\n' +
    '\n' +
    '    <b>TASON S.A.</b> recibirá notificaciones o preguntas acerca de los Términos, Condiciones y Políticas de Privacidad a\n' +
    '    correo electrónico info@tas-on.com.\n' +
    '  </p>';

  @ViewChild('previewTerms') previewTerms: ElementRef;
  @ViewChild('dialogTerms') dialogTerms: ElementRef;

  constructor() {
  }

  ngOnInit() {
    this.previewTerms.nativeElement.innerHTML = this.htmlTerms;
    this.dialogTerms.nativeElement.innerHTML = this.htmlTerms;
  }


  showTerms(show) {
    this.show = show;
    if (this.show)
      $("#myModal").modal('show');
    else $("#myModal").modal('hide');
  }
}
