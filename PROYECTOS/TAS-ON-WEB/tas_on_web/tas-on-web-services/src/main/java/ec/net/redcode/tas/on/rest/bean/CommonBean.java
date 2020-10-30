package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Company;
import ec.net.redcode.tas.on.common.dto.EstadisticaHome;
import ec.net.redcode.tas.on.common.dto.Offer;
import ec.net.redcode.tas.on.common.dto.Registro;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.dto.Solicitud;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public abstract class CommonBean extends TasOnResponse {

    @Setter protected LocalidadService localidadService;
    @Setter protected CatalogoItemService catalogoItemService;
    @Setter protected ClienteService clienteService;
    @Setter protected UsuarioService usuarioService;
    @Setter protected VehiculoService vehiculoService;
    @Setter protected ConductorService conductorService;
    @Setter protected SolicitudEnvioService solicitudEnvioService;
    @Setter protected OfertaService ofertaService;
    @Setter protected FacturaDetalleService facturaDetalleService;
    @Setter protected SecuenciaService secuenciaService;
    @Setter protected EstadisticasService estadisticasService;

    private static EstadisticaHome estadisticaHome;
    private static Integer horasCaducidadSolicitud;

    //Permite obtener la lista de una localidad Padre
    public List<Localidad> getLocalidadByPadre(int idLocalidadPadre, int estado){
        return localidadService.getLocalidadByLocalidadIdPadre(idLocalidadPadre, estado);
    }

    //Permite obtener la lista de un catalogo item
    public List<CatalogoItem> getCatalogoItemByCatalogo(int idCatalogoItem){
        return catalogoItemService.getCatalogoItemByCatalogo(idCatalogoItem);
    }

    //Permite obtener el detalle
    public CatalogoItem readCatalogo(int idCatalogoItem){
        return this.catalogoItemService.read(idCatalogoItem);
    }

    //Permite crear la Empresa con un usuario administrador
    public void createEmpresaCliente(Map<String, Object> data) throws TasOnException {
        ObjectMapper mapper = new ObjectMapper();

        Cliente cliente = mapper.convertValue( data.get("empresa"), Cliente.class);
        if (clienteService.readCliente(cliente.getClienteRuc()) != null)
            throw new TasOnException(Response.Status.NOT_IMPLEMENTED.getStatusCode(), Constant.RESPONSE_ERROR, "La empresa ya se encuentra registrada");

        Usuario usuario = mapper.convertValue( data.get("cliente"), Usuario.class);
        if (usuarioService.readUsuario(usuario.getUsuarioIdDocumento()) != null)
            throw new TasOnException(Response.Status.NOT_IMPLEMENTED.getStatusCode(), Constant.RESPONSE_ERROR, "El cliente ya se encuentra registrado");

        //Valores por defecto
        cliente.setClienteComision(Constant.CLIENTE_COMISION_DEFECTO);
        cliente.setClienteDiasCredito(Constant.CLIENTE_DIAS_CREDITO_DEFECTO);
        cliente.setClienteFechaRegistro(new Timestamp(new Date().getTime()));

        if(cliente.getClienteTipoCliente() == Constant.CLIENTE_EMPRESA_TRANSPORTISTA_INDEPENDIENTE
                || cliente.getClienteTipoCliente() == Constant.CLIENTE_EMPRESA_TRANSPORTISTA)
            cliente.setClienteRetencion(1);

        clienteService.createCliente(cliente);

        usuario.setUsuarioRuc(cliente.getClienteRuc());
        usuario.setUsuarioPrincipal(Boolean.TRUE);
        usuario.setUsuarioEstado(Constant.USER_CREATED);
        createUsuario(usuario);
    }

    //Genera un secuencial cuando el secuencial ya existe y necesita uno nuevo
    protected String generateRandomSecuencial(String razonSocial){
        int length = 1;
        boolean useLetters = true;
        boolean useNumbers = false;

        //Si no tiene letras suficientes para armar un secuencial retorna un secuencial con 5 letras aleatorias
        if(razonSocial == null || razonSocial.isEmpty() || razonSocial.length() < 4){
            return RandomStringUtils.random(5, useLetters, useNumbers).toUpperCase();
        }

        String base = razonSocial.substring(0, 4).toUpperCase().trim();
        //Si tiene mas de 5 letras obtiene la 5ta letra a partir de una letra de su nombre
        if(razonSocial.length() > 5){
            String letrasPosibles = razonSocial.substring(5).toUpperCase();
            //Convertimos lo restante de la razon social en un array
            char[] tempArray = letrasPosibles.trim().toUpperCase().toCharArray();
            //Ordenamos el array
            Arrays.sort(tempArray);
            //Convertimos el array a un string
            letrasPosibles = String.valueOf(tempArray).trim();
            //Eliminamos letras repetidas y espacios
            letrasPosibles = letrasPosibles.replaceAll("(.)\\1{1,}", "$1");
            //Eliminamos lo que no sea una letra
            letrasPosibles = letrasPosibles.replaceAll("[^A-Z]", "");

            //Intenta generar un secuencial con las letras restantes de su nombre
            char[] letrasPosiblesArray  = letrasPosibles.toCharArray();
            for(char letra : letrasPosiblesArray){
                if(secuenciaService.getSecuenciaByNemonico(base.concat(String.valueOf(letra))) == null){
                    return base.concat(String.valueOf(letra));
                }
            }
        }

        //Genera un secuencial con la 5ta letra aleatoria
        String secuencial;
        int contador = 0;
        do {
            //Si despues de varios intentos no logra obtener un secuencial nuevo retorna uno con las 5 letras aleatorias
            if(contador == 26){
                return RandomStringUtils.random(5, useLetters, useNumbers).toUpperCase();
            }
            secuencial = base.concat(RandomStringUtils.random(length, useLetters, useNumbers)).toUpperCase();
            contador++;
        } while(secuenciaService.getSecuenciaByNemonico(secuencial) != null);
        return secuencial;
    }

    //Obtiene las lista de cliente por el tipo de cliente
    protected List<Company> getAllClienteByTipo(int tipoCliente){
        List<Cliente> clientes = clienteService.getCienteByTipo(tipoCliente);
        return clientes.stream().map(clientFunction).filter(Objects::nonNull).collect(Collectors.toList());
    }

    //Permite obtener un cliente con el ruc
    public Cliente readCliente(String ruc){
        return clienteService.readCliente(ruc);
    }

    //Parsea de la Entidad Cliente al dto Company
    Function<Cliente, Company> clientFunction = cliente -> {
        Company client = new Company();
        client.setRuc(cliente.getClienteRuc());
        client.setIdTipo(cliente.getClienteTipoId());
        CatalogoItem catalogoItem = catalogoItemService.read(cliente.getClienteTipoId());
        client.setTipo(catalogoItem.getCatalogoItemDescripcion());
        client.setIdLocalidad(cliente.getClienteLocalidadId());
        Localidad localidad = localidadService.readLocalidad(cliente.getClienteLocalidadId());
        client.setLocalidad(localidad.getLocalidadDescripcion());
        client.setRazonSocial(cliente.getClienteRazonSocial());
        client.setDireccion(cliente.getClienteDireccion());
        client.setDiasCredito(cliente.getClienteDiasCredito());
        client.setPeriodoFacturacion(cliente.getClienteDiasPeriodicidad());
        catalogoItem = catalogoItemService.read(cliente.getClienteTipoCliente());
        client.setIdCompanyType(cliente.getClienteTipoCliente());
        client.setCompanyType(catalogoItem.getCatalogoItemDescripcion());
        return client;
    };

    //Metodo que permite insertar un nuevo usuario, cuando es transportista inserta datos de vehiculo y conductor
    protected void createUsuario(Usuario usuario) {
        if (usuario.getUsuarioTipoUsuario() == Constant.USER_DRIVER ||
                usuario.getUsuarioTipoUsuario() == Constant.USER_CLIENT ||
                usuario.getUsuarioTipoUsuario() == Constant.USER_DRIVER_ADMIN ||
                usuario.getUsuarioTipoUsuario() == Constant.USER_CLIENT_ADMIN){
            long admin = usuarioService.hasAdmin(usuario.getUsuarioRuc());
            if (admin == 0) {
                usuario.setUsuarioPrincipal(Boolean.TRUE);
                if (usuario.getUsuarioTipoUsuario() == Constant.USER_DRIVER)
                    usuario.setUsuarioTipoUsuario(Constant.USER_DRIVER_ADMIN);
                if (usuario.getUsuarioTipoUsuario() == Constant.USER_CLIENT)
                    usuario.setUsuarioTipoUsuario(Constant.USER_CLIENT_ADMIN);
                usuarioService.createUsuario(usuario);
            } else {
                usuario.setUsuarioPrincipal(Boolean.FALSE);
                Cliente cliente = clienteService.readCliente(usuario.getUsuarioRuc());
                if(cliente.getClienteTipoCliente() == Constant.CLIENTE_EMPRESA_TRANSPORTISTA_ENVIOS)
                    usuario.setUsuarioTipoUsuario(Constant.USER_DRIVER_ENVIOS);
                usuarioService.createUsuario(usuario);
            }
            List<Conductor> conductores = usuario.getConductorsByUsuarioIdDocumento();
            if(conductores != null)
                conductores.forEach(conductor -> {
                    conductor.setConductorUsuario(usuario.getUsuarioIdDocumento());
                    if(usuario.getUsuarioTipoDocumento() == Constant.CATALOGO_CEDULA && conductores.size() == 1) conductor.setConductorCedula(usuario.getUsuarioIdDocumento());
                    conductor.setConductorEstado(1);
                    conductorService.create(conductor);
                });
        }
    }


    protected Usuario getUsarioByUsername(String username){
        return usuarioService.getUsuariosByUserName(username);
    }

    protected Usuario getUsuarioByEmail(String email){
        return usuarioService.getUsuarioByEmail(email);
    }

    //Permite obtener el usuario por su identificacion
    public Usuario readUsuario(String identificador) {
        return usuarioService.readUsuario(identificador);
    }

    //Permite restablecer la password del usuario
    protected void validarCambioPassword(String identificador, String email) throws TasOnException {
        Usuario usuario = usuarioService.getUsuarioByIdentificadorAndEmail(identificador, email);
        if (usuario == null){
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Constant.RESPONSE_ERROR, "El usuario no existe");
        } else if(Constant.USER_ACTIVE != usuario.getUsuarioEstado() && Constant.USER_CREATED != usuario.getUsuarioEstado()){
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No tiene permitido cambiar la constrase\u00f1a");
        }
    }

    public List<Solicitudes> getAllSolicitudesEntregadas(String username){
        List<Solicitudes> solicitudesCompletadas = new ArrayList<>();
        solicitudesCompletadas.addAll(getSolicitudesEnvio(Constant.SOLICITUD_ENVIO_DELIVER, username));
        solicitudesCompletadas.addAll(getSolicitudesEnvio(Constant.SOLICITUD_TO_PAY, username));
        solicitudesCompletadas.addAll(getSolicitudesEnvio(Constant.SOLICITUD_ENVIO_PAYED, username));
        return solicitudesCompletadas;
    }

    //Permite obtener la lista de las solicitudes por estado y usuario
    public List<Solicitudes> getSolicitudesEnvio(Integer estado, String username){
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        List<SolicitudEnvio> solicitudEnvios;
        if ((Constant.PUBLIC_USER.equals(username) && estado == Constant.SOLICITUD_ENVIO_CREATE)
                || usuario.getUsuarioTipoUsuario() == Constant.USER_ACCOUNTANT
                || usuario.getUsuarioTipoUsuario() == Constant.USER_ADMIN) {
            solicitudEnvios = solicitudEnvioService.getSolicitudEnvioBySolicitudEstado(estado);
        }
        else {
            solicitudEnvios = solicitudEnvioService.getSolicitudEnvioByUsuarioAndEstado(usuario.getUsuarioRuc(), estado);
        }
        return solicitudEnvios.stream().map(solicitud -> externalToSolicitudes.apply(solicitud, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    //Permite obtener el detalle de la solicitud
    public Solicitud getSolicitud(String idSolicitud, Usuario usuarioConsulta){
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        return externalToSolicitud.apply(solicitudEnvio, usuarioConsulta);
    }

    private BiFunction<SolicitudEnvio, Usuario, Solicitud> externalToSolicitud = (solicitudEnvio, usuarioConsulta) -> {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(solicitudEnvio.getSolicitudEnvioId());
        solicitud.setIdOrigen(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
        solicitud.setOrigen(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion());
        solicitud.setProvinciaOrigen(localidadService.readLocalidad(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        solicitud.setIdDestino(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
        solicitud.setDestino(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion());
        solicitud.setProvinciaDestino(localidadService.readLocalidad(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        solicitud.setFechaEnvio(solicitudEnvio.getSolicitudEnvioFechaRecoleccion());
        solicitud.setDireccion(solicitudEnvio.getSolicitudEnvioDireccionOrigen());
        solicitud.setTipo("");
        if(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId() != null) solicitud.setTipo(obtenerDescripcionTipoProducto(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId()));
        solicitud.setNumeroPiezas(solicitudEnvio.getSolicitudEnvioNumeroPiesas());
        solicitud.setVolumen(solicitudEnvio.getSolicitudEnvioVolumen());
        solicitud.setTipoVolumen(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadVolumen().getCatalogoItemDescripcion());
        solicitud.setPeso(solicitudEnvio.getSolicitudEnvioPeso());
        solicitud.setTipoPeso(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadPeso().getCatalogoItemDescripcion());
        solicitud.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());
        solicitud.setDireccionEntrega(solicitudEnvio.getSolicitudEnvioDireccionDestino());
        solicitud.setNumeroEstibadores(solicitudEnvio.getSolicitudEnvioNumeroEstibadores());
        solicitud.setPersonaEntrega(solicitudEnvio.getSolicitudEnvioPersonaEntrega());
        solicitud.setPersonaRecibe(solicitudEnvio.getSolicitudEnvioPersonaRecibe());
        solicitud.setDiasValidez(solicitudEnvio.getSolicitudEnvioDiasValides());
        solicitud.setFechaCaducidad(solicitudEnvio.getSolicitudEnvioFechaCaducidad() != null ? solicitudEnvio.getSolicitudEnvioFechaCaducidad() : new Timestamp(new Date().getTime()));
        solicitud.setFechaCreacion(solicitudEnvio.getSolicitudEnvioFechaCreacion() != null ? solicitudEnvio.getSolicitudEnvioFechaCreacion() : new Timestamp(new Date().getTime()));
        solicitud.setUsuario(solicitudEnvio.getSolicitudEnvioUsuarioId());
        Usuario usuarioSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId();
        solicitud.setTelefonoContacto(usuarioSolicitud.getUsuarioCelular());
        solicitud.setCorreoContacto(usuarioSolicitud.getUsuarioMail());
        solicitud.setEstado(solicitudEnvio.getSolicitudEnvioEstado());
        solicitud.setComments(solicitudEnvio.getSolicitudEnvioComentario() != null ? solicitudEnvio.getSolicitudEnvioComentario() : "");
        solicitud.setObservaciones(solicitudEnvio.getSolicitudEnvioObservaciones() != null ? solicitudEnvio.getSolicitudEnvioObservaciones() : "");
        solicitud.setNumeroDocCliente(solicitudEnvio.getSolicitudEnvioNumeroDocCliente());
        solicitud.setFotosRecoleccion(new ArrayList<>());
        solicitud.setFotosEntrega(new ArrayList<>());
        if (usuarioConsulta != null) {
            llenarDatosOfertaGanadora(solicitud, solicitudEnvio, usuarioConsulta);
            llenarEstadisticasOfertas(solicitud, solicitudEnvio, usuarioConsulta);
            if (Constant.SOLICITUD_ENVIO_CREATE == solicitudEnvio.getSolicitudEnvioEstado()){
                solicitud.setDiasPago(getDiasPago(solicitudEnvio.getSolicitudEnvioUsuarioId()));
            }
            //TODO analizar si es necesario incluir este dato y en que pantallas se lo esta usando
            if(TasOnUtil.esUsuarioAdmin(usuarioConsulta.getUsuarioTipoUsuario()) || TasOnUtil.esUsuarioContador(usuarioConsulta.getUsuarioTipoUsuario())){
                List<Offer> offers = new ArrayList<>();
                List<Oferta> ofertas = (List<Oferta>) solicitudEnvio.getOfertasBySolicitudEnvioId();
                for (Oferta o : ofertas) {
                    Offer offer = parseOfertaToOffer(o, solicitudEnvio.getSolicitudEnvioId(), usuarioConsulta);
                    offers.add(offer);
                }
                solicitud.setOffers(offers);
            }
        }
        return solicitud;
    };

    private void llenarDatosOfertaGanadora(Solicitud solicitud, SolicitudEnvio solicitudEnvio, Usuario usuarioConsulta){
        //Estos datos solo pueden ser visualizados por un
        // usuario admin, contador, la empresa que envio una oferta y la solicitud aun esta como creada, la empresa que creo la solicitud o gano la oferta
        Oferta oferta = solicitudEnvio.getOfertaBySolicitudEnvioOfertaId();
        if(oferta == null && TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
            oferta = ofertaService.getOfertaBySolicitudUser(solicitudEnvio.getSolicitudEnvioId(), usuarioConsulta.getUsuarioIdDocumento());
        }
        if(oferta != null &&
                (TasOnUtil.esUsuarioAdmin(usuarioConsulta.getUsuarioTipoUsuario()) ||
                TasOnUtil.esUsuarioContador(usuarioConsulta.getUsuarioTipoUsuario()) ||
                (solicitud.getEstado() == Constant.SOLICITUD_ENVIO_CREATE && TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())) ||
                usuarioConsulta.getUsuarioRuc().equals(solicitudEnvio.getOfertaBySolicitudEnvioOfertaId().getUsuarioByOfertaIdConductor().getUsuarioRuc()) ||
                usuarioConsulta.getUsuarioRuc().equals(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc()))){

            Offer offer = parseOfertaToOffer(oferta, solicitudEnvio.getSolicitudEnvioId(), usuarioConsulta);
            Cliente empresaTransporte = oferta.getUsuarioByOfertaIdConductor().getClienteByUsuarioRuc();
            offer.setEmpresaTransportista(empresaTransporte.getClienteRazonSocial());
            solicitud.setOffer(offer);

            Double max = ofertaService.getOfertaMaxBySolicitud(solicitudEnvio.getSolicitudEnvioId());
            Double valorAhorrado = max - oferta.getOfertaValor();
            solicitud.setMaxValorOferta(valorAhorrado);

            Double porcentaje = (valorAhorrado * 100) / max;
            solicitud.setPorcentajeAhorro(porcentaje);

            List<OfertaFile> fotosSolicitud = (List<OfertaFile>) oferta.getFotos();
            if(fotosSolicitud != null){
                solicitud.setFotosRecoleccion(fotosSolicitud.stream().filter(f -> f.getOfertaFileTipo() == 1).map(OfertaFile::getOfertaFileFileId).collect(Collectors.toList()));
                solicitud.setFotosEntrega(fotosSolicitud.stream().filter(f -> f.getOfertaFileTipo() == 2).map(OfertaFile::getOfertaFileFileId).collect(Collectors.toList()));
            }
        }
    }

    private void llenarEstadisticasOfertas(Solicitud solicitud, SolicitudEnvio solicitudEnvio, Usuario usuarioConsulta){
        DoubleSummaryStatistics estadisticasOferta;
        List<Oferta> ofertas = (List<Oferta>) solicitudEnvio.getOfertasBySolicitudEnvioId();
        if(TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
            estadisticasOferta = ofertas.stream().mapToDouble(Oferta::getOfertaValor).summaryStatistics();
        } else {
            estadisticasOferta = ofertas.stream().mapToDouble(o -> o.getOfertaValor() + o.getOfertaComision()).summaryStatistics();
        }

        //Las estadisticas las ve solo un usuario admin, contador o la empresa que creo la solicitud
        if(TasOnUtil.esUsuarioAdmin(usuarioConsulta.getUsuarioTipoUsuario()) ||
                TasOnUtil.esUsuarioContador(usuarioConsulta.getUsuarioTipoUsuario()) ||
                usuarioConsulta.getUsuarioRuc().equals(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc())){
            solicitud.setBestOffer(estadisticasOferta.getCount() == 0 || usuarioConsulta.getUsuarioTipoUsuario() == Constant.USER_CLIENT_REDUCIDO ? null : TasOnUtil.roundDouble(estadisticasOferta.getMin(), 2));
            solicitud.setAverageOffer(estadisticasOferta.getCount() == 0 || usuarioConsulta.getUsuarioTipoUsuario() == Constant.USER_CLIENT_REDUCIDO ? null : TasOnUtil.roundDouble(estadisticasOferta.getAverage() , 2));
            solicitud.setTotalOffer(estadisticasOferta.getCount());
            solicitud.setTipoSubasta(solicitudEnvio.getSolicitudEnvioTipoSubasta());
            solicitud.setTipoSubastaDescripcion(solicitudEnvio.getCatalogoItemBySolicitudEnvioTipoSubasta() != null ? solicitudEnvio.getCatalogoItemBySolicitudEnvioTipoSubasta().getCatalogoItemDescripcion() : null);
            solicitud.setValorObjetivo(solicitudEnvio.getSolicitudEnvioValorObjetivo());

            return;
        }

        //Cuando se crea la solicitud las estadisticas que ven los transportistas
        if(Constant.SOLICITUD_ENVIO_CREATE == solicitudEnvio.getSolicitudEnvioEstado() && TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
            if(solicitudEnvio.getSolicitudEnvioTipoSubasta() != null && estadisticasOferta.getCount() == 0 &&
                    (solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.SUBASTA_INVERSA_VALOR_OBJETIVO
                            || solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.PRECIO_FIJO)){
                solicitud.setBestOffer(TasOnUtil.getMaxValorSubastaValorObjetivo(solicitudEnvio.getSolicitudEnvioValorObjetivo(), solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc().getClienteComision()));
                solicitud.setAverageOffer(solicitud.getBestOffer());
                solicitud.setTotalOffer(1);
            } else {
                solicitud.setBestOffer(estadisticasOferta.getCount() == 0 ? null : TasOnUtil.roundDouble(estadisticasOferta.getMin(), 2));
                solicitud.setAverageOffer(estadisticasOferta.getCount() == 0 ? null : TasOnUtil.roundDouble(estadisticasOferta.getAverage() , 2));
                solicitud.setTotalOffer(estadisticasOferta.getCount());
            }
        }

    }

    private Offer parseOfertaToOffer(Oferta oferta, String idSolicitudEnvio, Usuario usuarioConsulta){
        Offer offer = new Offer();
        if (oferta != null) {
            offer.setIdSolicitud(idSolicitudEnvio);
            offer.setComments(oferta.getOfertaObservacion());
            offer.setIdOferta(oferta.getOfertaId());
            offer.setDate(oferta.getOfertaFecha());
            offer.setIdConductor(oferta.getOfertaIdConductor());
            offer.setIdVehiculo(oferta.getOfertaIdVehiculo());
            offer.setState(oferta.getOfertaEstado());

            if(TasOnUtil.esUsuarioCliente(usuarioConsulta.getUsuarioTipoUsuario())){
                offer.setAmount(oferta.getOfertaValor()+oferta.getOfertaComision());
                offer.setComision(0D);
                if(usuarioConsulta.getUsuarioTipoUsuario() == Constant.USER_CLIENT_REDUCIDO){
                    offer.setAmount(null);
                    offer.setComision(null);
                }
            } else if(TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
                offer.setAmount(oferta.getOfertaValor());
                offer.setComision(0D);
            } else {
                offer.setAmount(oferta.getOfertaValor());
                offer.setComision(oferta.getOfertaComision());
            }

            if (oferta.getOfertaIdConductor() != 0) {
                Conductor conductor = conductorService.read(oferta.getOfertaIdConductor());
                offer.setConductor(conductor.getConductorNombre().concat(" ").concat(conductor.getConductorApellido()));
                offer.setTelefonoContacto(conductor.getConductorTelefono());
                offer.setCorreoContacto(conductor.getConductorEmail());
            }
            if (oferta.getOfertaIdVehiculo() != 0) {
                Vehiculo vehiculo = vehiculoService.read(oferta.getOfertaIdVehiculo());
                offer.setVehiculo(vehiculo.getVehiculoPlaca());
            }
        }
        return offer;
    }

    BiFunction<SolicitudEnvio, Usuario, Solicitudes> externalToSolicitudes = this::parseToSolicitudes;

    protected Solicitudes parseToSolicitudes(SolicitudEnvio solicitudEnvio, Usuario usuarioConsulta){
        Solicitudes solicitud = new Solicitudes();
        solicitud.setIdSolicitud(solicitudEnvio.getSolicitudEnvioId());
        solicitud.setIdOrigen(solicitudEnvio.getSolicitudEnvioLocalidadOrigen());
        solicitud.setOrigen(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadDescripcion());
        solicitud.setProvinciaOrigen(localidadService.readLocalidad(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadOrigen().getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        solicitud.setIdDestino(solicitudEnvio.getSolicitudEnvioLocalidadDestino());
        solicitud.setDestino(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadDescripcion());
        solicitud.setProvinciaDestino(localidadService.readLocalidad(solicitudEnvio.getLocalidadBySolicitudEnvioLocalidadDestino().getLocalidadLocalidadPadre()).getLocalidadDescripcion());
        solicitud.setPeso(solicitudEnvio.getSolicitudEnvioPeso());
        solicitud.setMedida(solicitudEnvio.getCatalogoItemBySolicitudEnvioUnidadPeso().getCatalogoItemDescripcion());
        solicitud.setDias(solicitudEnvio.getSolicitudEnvioDiasValides());//TODO eliminar este campo cuando se lance la nueva app movil
        solicitud.setNumeroPiezas(solicitudEnvio.getSolicitudEnvioNumeroPiesas());
        solicitud.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());
        solicitud.setFechaRecepcion(solicitudEnvio.getSolicitudEnvioFechaRecoleccion());
        solicitud.setNumeroDocCliente(solicitudEnvio.getSolicitudEnvioNumeroDocCliente());
        solicitud.setDescuento(0D);
        //TN-38
        if (solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_CREATE ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_CANCEL){
            solicitud.setPersonaRecibe(solicitudEnvio.getSolicitudEnvioPersonaRecibe());
        }
        if (solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_CANCEL){
            solicitud.setFechaCancelada(solicitudEnvio.getSolicitudEnvioFechaCaducidad());
        }
        if (solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_DELIVERY_PROCESS ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_DELIVER ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_ASSIGN ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_DISPATCH ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_TO_PAY ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_PAYED) {
            Oferta oferta = solicitudEnvio.getOfertaBySolicitudEnvioOfertaId();

            if(TasOnUtil.esUsuarioCliente(usuarioConsulta.getUsuarioTipoUsuario())){
                solicitud.setOfertaValor(TasOnUtil.roundDouble(oferta.getOfertaValor()+oferta.getOfertaComision(), 2));
                solicitud.setComision(0D);
                if(usuarioConsulta.getUsuarioTipoUsuario() == Constant.USER_CLIENT_REDUCIDO){
                    solicitud.setOfertaValor(null);
                    solicitud.setComision(null);
                }
            } else if(TasOnUtil.esUsuarioConductor(usuarioConsulta.getUsuarioTipoUsuario())){
                solicitud.setOfertaValor(oferta.getOfertaValor());
                solicitud.setComision(0D);
            } else {
                solicitud.setOfertaValor(oferta.getOfertaValor());
                solicitud.setComision(oferta.getOfertaComision());
            }

            solicitud.setFechaEntrega(solicitudEnvio.getSolicitudEnvioFechaEntrega());
            solicitud.setCliente(oferta.getUsuarioByOfertaIdConductor().getClienteByUsuarioRuc().getClienteRazonSocial());
            solicitud.setFechaFacturacion(solicitudEnvio.getSolicitudEnvioFechaFacturacion());
            if (oferta.getOfertaIdConductor() != 0) {
                Conductor conductor = conductorService.read(oferta.getOfertaIdConductor());
                solicitud.setTransportista(conductor.getConductorNombre().concat(" ").concat(conductor.getConductorApellido()));
            }
        }
        if (solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_TO_PAY ||
                solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_PAYED){
            FacturaDetalle facturaDetalle = facturaDetalleService.getFacturaDetalleByOfertaId(solicitudEnvio.getSolicitudEnvioOfertaId(), 10);
            solicitud.setFacturaId(facturaDetalle != null ? facturaDetalle.getFacturaDetalleFacturaId() : "");
            Date now = new Date();
            Date fechaEntregaDate = new Date(solicitud.getFechaFacturacion().getTime());
            int dias = 0;
            if (now.before(fechaEntregaDate))
                dias = (int) ((fechaEntregaDate.getTime() - now.getTime()) / 86400000);
            solicitud.setDiasVencidaFactura(dias);
            if(debeEmitirNotaCredito(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc())){
                Double ahorroValorObjetivo = solicitudEnvio.getSolicitudEnvioAhorroValorObjetivo() == null ? 0D: solicitudEnvio.getSolicitudEnvioAhorroValorObjetivo();
                solicitud.setDescuento(ahorroValorObjetivo);
                solicitud.setComision(0D);
                solicitud.setOfertaValor(solicitudEnvio.getSolicitudEnvioValorObjetivo());
            }

        }
        if (Constant.SOLICITUD_ENVIO_CREATE == solicitudEnvio.getSolicitudEnvioEstado()){
            solicitud.setDiasPagos(getDiasPago(solicitudEnvio.getSolicitudEnvioUsuarioId()));
        }
        return solicitud;
    }

    public static boolean debeEmitirNotaCredito(Cliente cliente){
        return cliente != null &&
                cliente.getClienteNotaCredito() != null &&
                cliente.getClienteNotaCredito() == 1;
    }

    private int getDiasPago(String usuarioId){
        Usuario usuario = usuarioService.readUsuario(usuarioId);
        Cliente cliente = usuario.getClienteByUsuarioRuc();
        return cliente.getClienteDiasCredito() + Integer.valueOf(catalogoItemService.read(Constant.ADMIN_DIAS_GESTION).getCatalogoItemValor());
    }

    //Permite controlar que las placas no se repitan
    public Vehiculo getVehiculoByPlaca(String placa) {
        return vehiculoService.getVehiculoyPlaca(placa);
    }

    private String obtenerDescripcionTipoProducto(Usuario usuario){
        if(usuario == null) return "";
        Cliente cliente = usuario.getClienteByUsuarioRuc();
        if(cliente == null || cliente.getClienteTipoProducto() == null) return "";
        CatalogoItem catalogoItem = catalogoItemService.read(cliente.getClienteTipoProducto());
        if(catalogoItem == null) return "";
        return(catalogoItem.getCatalogoItemDescripcion());
    }

    protected EstadisticaHome getEstadisticaHome() {
        if(estadisticaHome == null || estadisticaHome.getFechaConsulta().isBefore(LocalDate.now())){
            estadisticaHome = new EstadisticaHome();
            List<Registro<Integer, String>> localidadesDestino = estadisticasService.getLocalidadesDestinoSolicitudesCompletadas();
            estadisticaHome.setAhorroGenerado(estadisticasService.getTotalAhorradoSolicitudesCompletadas());
            estadisticaHome.setToneladasTrasladadas(estadisticasService.getToneladasEnviadasSolicitudesCompletadas());
            estadisticaHome.setDestinos(localidadesDestino.size());
            estadisticaHome.setFechaConsulta(LocalDate.now());
        }
        return estadisticaHome;
    }

    protected Integer getHorasCaducidadSolicitud(){
        if(horasCaducidadSolicitud == null){
            horasCaducidadSolicitud = Integer.parseInt(catalogoItemService.read(Constant.CATALOGO_HORAS_CADUCIDAD_SOLICITUD).getCatalogoItemValor());
        }
        return horasCaducidadSolicitud;
    }
}
