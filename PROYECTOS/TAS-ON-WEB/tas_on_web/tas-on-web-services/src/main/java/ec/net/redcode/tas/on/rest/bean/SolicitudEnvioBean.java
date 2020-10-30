package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.DireccionDTO;
import ec.net.redcode.tas.on.common.dto.NuevaSolicitud;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.rest.dto.Solicitudes;
import ec.net.redcode.tas.on.rest.util.ServicesUtil;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class SolicitudEnvioBean extends CommonBean {

    public void create(SolicitudEnvio solicitudEnvio, String username)throws TasOnException{
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        validarModoSubastaCliente(usuario.getUsuarioRuc(), solicitudEnvio);
        validarFechasSolicitud(solicitudEnvio);
        solicitudEnvio.setSolicitudEnvioUsuarioId(usuario.getUsuarioIdDocumento());
        solicitudEnvio.setSolicitudEnvioFechaCreacion(new Timestamp(new Date().getTime()));
        solicitudEnvio.setSolicitudEnvioFechaCaducidad(getFechaCaducidadSolicitud(solicitudEnvio.getSolicitudEnvioFechaRecoleccion()));
        solicitudEnvio.setSolicitudEnvioDiasValides(1); //TODO eliminar este campo cuando se lance la nueva app movil
        solicitudEnvioService.create(solicitudEnvio);
    }

    private void validarModoSubastaCliente(String rucEmpresa, SolicitudEnvio solicitudEnvio) throws TasOnException{
        Cliente cliente = clienteService.readCliente(rucEmpresa);
        boolean debeEmitirNotaCredito = CommonBean.debeEmitirNotaCredito(cliente);

        if(solicitudEnvio.getSolicitudEnvioTipoSubasta() == null){
            solicitudEnvio.setSolicitudEnvioTipoSubasta(Constant.SUBASTA_INVERSA_ABIERTA);
        }

        if(debeEmitirNotaCredito && solicitudEnvio.getSolicitudEnvioTipoSubasta() != Constant.SUBASTA_INVERSA_VALOR_OBJETIVO){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Solo puede crear solicitudes con valor objetivo");
        }

        if(debeEmitirNotaCredito || debeTenerValorObjetivoSolicitud(solicitudEnvio)){
            if(solicitudEnvio.getSolicitudEnvioValorObjetivo() == null || solicitudEnvio.getSolicitudEnvioValorObjetivo() <= 0){
                throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Debe ingresar un valor objetivo para la solicitud");
            }
        }
        if(!debeTenerValorObjetivoSolicitud(solicitudEnvio)){
            solicitudEnvio.setSolicitudEnvioValorObjetivo(null);
        }
    }

    private void validarFechasSolicitud(SolicitudEnvio solicitudEnvio) throws TasOnException{
        LocalDateTime fechaRecoleccion = solicitudEnvio.getSolicitudEnvioFechaRecoleccion().toLocalDateTime();
        LocalDateTime fechaEntrega = solicitudEnvio.getSolicitudEnvioFechaEntrega().toLocalDateTime();
        if(fechaRecoleccion.isBefore(LocalDateTime.now())){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No puede ingresar una solicitud con fecha de recolección menor a la fecha actual");
        }
        if(fechaRecoleccion.isBefore(LocalDateTime.now().plusMinutes(60))){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No puede ingresar una solicitud con fecha de recolección muy próxima a la hora actual");
        }
        if(fechaRecoleccion.isAfter(fechaEntrega)){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No puede ingresar una solicitud con fecha de recolección mayor a la fecha de entrega");
        }
        if(fechaRecoleccion.plusMinutes(30).isAfter(fechaEntrega) || fechaRecoleccion.isEqual(fechaEntrega)){
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No existe tiempo suficiente entre la fecha de recolección y la fecha de entrega");
        }
    }

    private boolean debeTenerValorObjetivoSolicitud(SolicitudEnvio solicitudEnvio){
        return solicitudEnvio != null &&
                solicitudEnvio.getSolicitudEnvioTipoSubasta() != null &&
                solicitudEnvio.getSolicitudEnvioTipoSubasta() != Constant.SUBASTA_INVERSA_ABIERTA;
    }

    public SolicitudEnvio read(String idSolicitudEnvio){
        return solicitudEnvioService.read(idSolicitudEnvio);
    }

    public void update(SolicitudEnvio solicitudEnvio, String username) throws TasOnException{
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        validarModoSubastaCliente(usuario.getUsuarioRuc(), solicitudEnvio);
        validarFechasSolicitud(solicitudEnvio);
        solicitudEnvio.setSolicitudEnvioUsuarioId(usuario.getUsuarioIdDocumento());
        solicitudEnvio.setSolicitudEnvioFechaCaducidad(getFechaCaducidadSolicitud(solicitudEnvio.getSolicitudEnvioFechaRecoleccion()));
        SolicitudEnvio solicitudEnvioRead = solicitudEnvioService.read(solicitudEnvio.getSolicitudEnvioId());
        validarUsuarioActualizaSolicitud(usuario, solicitudEnvioRead);
        //Valores que no se pueden actualizar
        solicitudEnvio.setSolicitudEnvioTipoSubasta(solicitudEnvioRead.getSolicitudEnvioTipoSubasta());
        solicitudEnvio.setSolicitudEnvioValorObjetivo(solicitudEnvioRead.getSolicitudEnvioValorObjetivo());
        solicitudEnvio.setSolicitudEnvioFechaCreacion(solicitudEnvioRead.getSolicitudEnvioFechaCreacion());
        solicitudEnvio.setSolicitudEnvioDiasValides(1); //TODO eliminar este campo cuando se lance la nueva app movil
        solicitudEnvioService.update(solicitudEnvio);
    }

    private void validarUsuarioActualizaSolicitud(Usuario usuario, SolicitudEnvio solicitudEnvio) throws TasOnException{
        if(!usuario.getClienteByUsuarioRuc().getClienteRuc().equals(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc().getClienteRuc())){
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                    Constant.RESPONSE_ERROR,
                    "No tiene permitido editar la solicitud");
        }
    }

    /**
     * Permite obtener la lista de las solicitudes por estado y usuario, si la solicitud de envio tiene ofertas, no se muestra,
     * sirve para los conductores
     */
    protected List<Solicitudes> getSolicitudesEnvioByOferta(Integer estado, Usuario usuario){
        List<SolicitudEnvio> solicitudEnvios = solicitudEnvioService.getSolicitudEnvioBySolicitudEstado(estado);
        return solicitudEnvios.stream().map(solicitud -> externalToSolicitudesOferta.apply(solicitud, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected void updateAcceptOferta(String solicutud, int idOferta, Double amount) throws TasOnException{
        Oferta oferta = ofertaService.read(idOferta);
        if(!(TasOnUtil.roundDouble(oferta.getOfertaValor()+oferta.getOfertaComision(),2).equals(TasOnUtil.roundDouble(amount, 2)))){
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(),
                    Constant.RESPONSE_ERROR, "Error al seleccionar la oferta como ganadora debido a que la oferta ha cambiado su valor");
        }
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicutud);
        solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_ASSIGN);
        solicitudEnvio.setSolicitudEnvioOfertaId(idOferta);
        if(solicitudEnvio.getSolicitudEnvioTipoSubasta() == Constant.SUBASTA_INVERSA_VALOR_OBJETIVO
                && debeEmitirNotaCredito(solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc())){
            solicitudEnvio.setSolicitudEnvioAhorroValorObjetivo(TasOnUtil.roundDouble(
                    solicitudEnvio.getSolicitudEnvioValorObjetivo()-(oferta.getOfertaValor()+oferta.getOfertaComision()),2));
        }
        solicitudEnvioService.update(solicitudEnvio);
        oferta.setOfertaEstado(Constant.OFERTA_ASSIGN);
        Date date = new Date();
        oferta.setOfertaFechaAceptada(new Timestamp(date.getTime()));
        ofertaService.update(oferta);
        List<Oferta> ofertas = ofertaService.getOfertasBySolicitudAndEstado(solicutud, Constant.OFERTA_CREATED);
        for (Oferta o: ofertas){
            o.setOfertaEstado(Constant.OFERTA_NOT_ACCEPTED);
            o.setOfertaFechaFinalizada(new Timestamp(date.getTime()));
            ofertaService.update(o);
        }
    }

    /**
     * Permite obtener las solicitudes que solo tienen oferta
     */
    protected List<Solicitudes> getSolictudEnvioWithOfertas(Usuario usuario){
        List<SolicitudEnvio> solicitudEnvios = solicitudEnvioService.getSolictudEnvioWithOfertas(usuario.getUsuarioRuc());
        return solicitudEnvios.stream().map(solicitud -> externalToSolicitudes.apply(solicitud, usuario)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected void cancelSolicitud(SolicitudEnvio solicitud){
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicitud.getSolicitudEnvioId());
        solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_CANCEL);
        solicitudEnvio.setSolicitudEnvioComentario(solicitud.getSolicitudEnvioComentario());
        solicitudEnvioService.update(solicitudEnvio);
        List<Oferta> ofertas = ofertaService.getOfertaBySolicitudAOrder(solicitud.getSolicitudEnvioId());
        for (Oferta oferta: ofertas){
            oferta.setOfertaEstado(Constant.OFERTA_CANCEL_ADMINISTRATION);
            ofertaService.update(oferta);
        }
    }

    /**
     * Function que permite obtener las solicitudes con el filtro de que si tiene ofertas creadas o cancelada no lo devuelve,
     * esto sirve para los conductores
     * También filtra que solo pueda ofertar si tiene conductores y/o vehiculos acreditados por parte del cliente
     */
    private BiFunction<SolicitudEnvio, Usuario, Solicitudes> externalToSolicitudesOferta = (solicitudEnvio, usuario) -> {
        Oferta oferta = ofertaService.getOfertaBySolicitudUserAndEstado(solicitudEnvio.getSolicitudEnvioId(), usuario.getUsuarioIdDocumento(), Constant.OFERTA_CREATED);
        if (oferta == null) {
            if(solicitudEnvio.getSolicitudEnvioEstado() == Constant.SOLICITUD_ENVIO_CREATE){
                if(!ServicesUtil.puedeUsuarioOfertar(usuario, solicitudEnvio, conductorService, vehiculoService)) return null;
            }
            return parseToSolicitudes(solicitudEnvio, usuario);
        }
        return null;
    };


    protected NuevaSolicitud getDefaultValuesNuevaSolicitud(Usuario usuario){
        NuevaSolicitud nuevaSolicitud = new NuevaSolicitud();
        SecuenciaBean secuenciaBean = new SecuenciaBean(secuenciaService);
        nuevaSolicitud.setIdSolicitud(secuenciaBean.getSecuenciaTASON());
        SolicitudEnvio ultimaSolicitud = solicitudEnvioService.getUltimaSolicitud(usuario.getUsuarioRuc());
        if(ultimaSolicitud != null){
            Localidad localidadOrigen = ultimaSolicitud.getLocalidadBySolicitudEnvioLocalidadOrigen();
            nuevaSolicitud.setCiudadOrigen(localidadOrigen.getLocalidadId());
            nuevaSolicitud.setProvinciaOrigen(localidadOrigen.getLocalidadLocalidadPadre());
            Localidad localidadDestino = ultimaSolicitud.getLocalidadBySolicitudEnvioLocalidadDestino();
            nuevaSolicitud.setCiudadDestino(localidadDestino.getLocalidadId());
            nuevaSolicitud.setProvinciaDestino(localidadDestino.getLocalidadLocalidadPadre());
            nuevaSolicitud.setUnidadPeso(ultimaSolicitud.getSolicitudEnvioUnidadPeso());
            nuevaSolicitud.setUnidadVolumen(ultimaSolicitud.getSolicitudEnvioUnidadVolumen());
        } else {
            Localidad localidadOrigen = usuario.getClienteByUsuarioRuc().getLocalidadByClienteLocalidad();
            nuevaSolicitud.setCiudadOrigen(localidadOrigen.getLocalidadId());
            nuevaSolicitud.setProvinciaOrigen(localidadOrigen.getLocalidadLocalidadPadre());
        }
        return nuevaSolicitud;
    }

    protected List<DireccionDTO> getDatosOrigenUltimosEnvios(Integer idOrigen, Usuario usuario){
        List<DireccionDTO> response = solicitudEnvioService.getDatosOrigenUltimosEnvios(idOrigen, usuario.getUsuarioRuc());
        if(response == null || response.isEmpty()){
            response = new ArrayList<>();
            if(solicitudEnvioService.getUltimaSolicitud(usuario.getUsuarioRuc()) == null){
                response.add(new DireccionDTO(usuario.getUsuarioNombres()+" "+usuario.getUsuarioApellidos(),usuario.getClienteByUsuarioRuc().getClienteDireccion()));
            }
        }
        return response;
    }

    /*
    * Permite crear solicitudes de envio de forma aleatoria
    */
    protected String crearSolicitudEnvioAleatoria() {
        SecuenciaBean secuenciaBean = new SecuenciaBean(secuenciaService);
        SolicitudEnvio solicitudEnvio = new SolicitudEnvio();
        Random random = new Random();
        Map<Integer, String[]> mapaLocalidadesOrigen = new HashMap<>();
        mapaLocalidadesOrigen.put(131, new String[]{"Avenida Jaime Roldos Aguilera y Rosa Muñoz", "Pedro Paul Solorzano"}); //Zapotillo (LOJA)
        mapaLocalidadesOrigen.put(119, new String[]{"Calle Nicolás García y José Maria Peña", " José Manuel Diaz Calvo"}); //Loja (LOJA)
        mapaLocalidadesOrigen.put(133, new String[]{"Frente al parque central", "Jaime Dominguez Cruz"}); //Quilanga (LOJA)
        mapaLocalidadesOrigen.put(29, new String[]{"El Pan", "Adrián Rovira Carrasco"}); //El Pan (AZUAY)
        mapaLocalidadesOrigen.put(40, new String[]{"Calle 16 de Abril (mercado)", "Jesús Font Nieto"}); //SIGSIG (AZUAY)
        mapaLocalidadesOrigen.put(28, new String[]{"Entre calle Cayambe y avenida de las Américas", "Luis Alvarez Marin"}); ////CUENCA (AZUAY)
        mapaLocalidadesOrigen.put(196, new String[]{"Entre calle boyacá y 12 de Octubre", "Biel Grau Ramirez"}); //Huaquillas (EL ORO)
        mapaLocalidadesOrigen.put(203, new String[]{"Avenida El Oro y Rocafuerte", "Unai Ortiz Lorenzo"}); //Zaruma (EL ORO)
        mapaLocalidadesOrigen.put(199, new String[]{"Junto al estadio de la Liga Cantonal de Marcabelí", "Jesús Serrano Peña"}); //Marcabeli (EL ORO)
        Map<Integer, String[]> mapaLocalidadesDestino = new HashMap<>();
        mapaLocalidadesDestino.put(53, new String[]{"Entre avenida Sucre y Calderón, calle Brasil", "Ignacio Cruz Cortes"}); // TULCAN (CARCHI)
        mapaLocalidadesDestino.put(48, new String[]{"Junto a la Basílica del señor de la Buena Esperanza", "Mohamed Fernandez Garcia"}); // Bolívar (Carchi)
        mapaLocalidadesDestino.put(50, new String[]{"Junto a la gasolinera de Mira", "Mateo Moya Castillo"}); // Mira (Carchi)
        mapaLocalidadesDestino.put(84, new String[]{"Entre calle Esmeraldas y Atahualpa, Esquina", " Eduardo Gutierrez Santana"}); //San Lorenzo (ESMERALDAS)
        mapaLocalidadesDestino.put(82, new String[]{"Calle 9 de Octubre y Samaniego", "Asier Sala Benitez"}); //Quininde (Esmeraldas)
        mapaLocalidadesDestino.put(79, new String[]{"Junto al parque infantil Roberto Cervantes, calle Mejía", "Adam Vila Lozano"}); //Eloy Alfaro (Esmeraldas)
        mapaLocalidadesDestino.put(61, new String[]{"Plaza principal de Alausí", "Iván Fuentes"}); //Alausí (Chimborazo)
        mapaLocalidadesDestino.put(64, new String[]{"Universidad Jatun Yachay", " César Muñoz Cabrera"}); //Colta (Chimborazo)
        mapaLocalidadesDestino.put(67, new String[]{"Parque de Guano", "Laia Costa Santos"}); // Guano (Chimborazo)

        List<Integer> idLocalidadesOrigenList = new ArrayList<>();
        List<Integer> idLocalidadesDestinoList = new ArrayList<>();
        mapaLocalidadesOrigen.forEach((k, v) -> idLocalidadesOrigenList.add(k));
        mapaLocalidadesDestino.forEach((k, v) -> idLocalidadesDestinoList.add(k));

        String solicitudEnvioId = secuenciaBean.getSecuenciaTASON();
        solicitudEnvio.setSolicitudEnvioId(solicitudEnvioId);
        solicitudEnvio.setSolicitudEnvioPeso(Double.parseDouble(random.nextInt(5) + 1 + ""));
        solicitudEnvio.setSolicitudEnvioUnidadPeso(37);
        solicitudEnvio.setSolicitudEnvioVolumen(Double.parseDouble(random.nextInt(8) + 8 + ""));
        solicitudEnvio.setSolicitudEnvioUnidadVolumen(42);
        solicitudEnvio.setSolicitudEnvioNumeroPiesas(random.nextInt(20) + 3);

        int keyOrigen = idLocalidadesOrigenList.get(random.nextInt(idLocalidadesOrigenList.size()));
        solicitudEnvio.setSolicitudEnvioLocalidadOrigen(keyOrigen);
        solicitudEnvio.setSolicitudEnvioDireccionOrigen(mapaLocalidadesOrigen.get(keyOrigen)[0]);
        solicitudEnvio.setSolicitudEnvioPersonaEntrega(mapaLocalidadesOrigen.get(keyOrigen)[1]);

        int keyDestino = idLocalidadesDestinoList.get(random.nextInt(idLocalidadesDestinoList.size()));
        solicitudEnvio.setSolicitudEnvioLocalidadDestino(keyDestino);
        solicitudEnvio.setSolicitudEnvioDireccionDestino(mapaLocalidadesDestino.get(keyDestino)[0]);
        solicitudEnvio.setSolicitudEnvioPersonaRecibe(mapaLocalidadesDestino.get(keyDestino)[1]);
        solicitudEnvio.setSolicitudEnvioNumeroEstibadores(random.nextInt(4) + 1);

        String[] arrayUsuarios = new String[]{"1715659810", "1715659811", "1715659812"};
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime fechaEnvio = localDateTimeNow.plusDays(random.nextInt(3)+1);
        solicitudEnvio.setSolicitudEnvioFechaRecoleccion(Timestamp.valueOf(fechaEnvio));
        solicitudEnvio.setSolicitudEnvioFechaEntrega(Timestamp.valueOf(fechaEnvio));
        solicitudEnvio.setSolicitudEnvioFechaCaducidad(getFechaCaducidadSolicitud(Timestamp.valueOf(fechaEnvio)));
        solicitudEnvio.setSolicitudEnvioDiasValides(1);
        solicitudEnvio.setSolicitudEnvioNumeroDocCliente("");
        solicitudEnvio.setSolicitudEnvioUsuarioId(arrayUsuarios[random.nextInt(arrayUsuarios.length)]);
        solicitudEnvio.setSolicitudEnvioEstado(Constant.SOLICITUD_ENVIO_CREATE);
        solicitudEnvio.setSolicitudEnvioObservaciones("");
        solicitudEnvio.setSolicitudEnvioFechaCreacion(new Timestamp(new Date().getTime()));
        solicitudEnvio.setSolicitudEnvioTipoSubasta(Constant.SUBASTA_INVERSA_ABIERTA);
        solicitudEnvioService.create(solicitudEnvio);
        return solicitudEnvioId;
    }

    private Timestamp getFechaCaducidadSolicitud(Timestamp fechaRecoleccion){
        LocalDateTime fechaCaducidad = fechaRecoleccion.toLocalDateTime().minus(getHorasCaducidadSolicitud(), ChronoUnit.HOURS);
        return Timestamp.valueOf(fechaCaducidad);
    }

}
