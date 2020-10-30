package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.DireccionDTO;
import ec.net.redcode.tas.on.common.dto.EstadoSolicitudesDTO;
import ec.net.redcode.tas.on.common.dto.SolicitudAdminDTO;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.SolicitudEnvioDAO;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import lombok.extern.log4j.Log4j;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;


@Log4j
public class SolicitudEnvioDAOImpl extends GenericDAOImpl<SolicitudEnvio, Integer> implements SolicitudEnvioDAO {

    public SolicitudEnvioDAOImpl() {
        super(SolicitudEnvio.class);
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByOrigenDestino(Integer origen, Integer destino, Integer estado) {
        Query query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioByOrigenDestino");
        query.setParameter("solicitudEnvioLocalidadOrigen", origen);
        query.setParameter("solicitudEnvioLocalidadDestino", destino);
        query.setParameter("solicitudEnvioEstado", estado);
        List<SolicitudEnvio> solicitudEnvio = query.getResultList();
        return solicitudEnvio;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEnvioOferta(Integer solicitudEnvioOfertaId, Integer estado) {
        Query query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioBySolicitudEnvioOferta");
        query.setParameter("solicitudEnvioOfertaId", solicitudEnvioOfertaId);
        query.setParameter("solicitudEnvioEstado", estado);
        List<SolicitudEnvio> solicitudEnvio = query.getResultList();
        return solicitudEnvio;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioBySolicitudEstado(Integer estado) {
        Query query;
        query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioBySolicitudEstado");
        query.setParameter("solicitudEnvioEstado", estado);
        List<SolicitudEnvio> solicitudEnvio = query.getResultList();
        return solicitudEnvio;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByUsuarioAndEstado(String usuario, int estado) {
        Query query = em.createQuery("select s from SolicitudEnvio s, Usuario u, Cliente c " +
                "where s.solicitudEnvioUsuarioId = u.usuarioIdDocumento and u.usuarioRuc = c.clienteRuc " +
                "and u.usuarioRuc = :usuario and s.solicitudEnvioEstado = :estado");
        query.setParameter("usuario", usuario);
        query.setParameter("estado", estado);
        List<SolicitudEnvio> solicitudEnvios;
        try {
            solicitudEnvios = query.getResultList();
        } catch (Exception e){
            solicitudEnvios = Collections.emptyList();
        }
        return solicitudEnvios;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioPorFacturarse(String clienteRuc) {
        Query query = em.createNativeQuery("select s.* from solicitud_envio s, cliente c, usuario u " +
                "where cast(s.solicitud_envio_fecha_facturacion as date) <= :fechaFin " +
                " and s.solicitud_envio_estado = :estado " +
                " and c.cliente_ruc = :cliente " +
                " and u.usuario_id_documento = s.solicitud_envio_usuario_id " +
                " and u.usuario_ruc = c.cliente_ruc", SolicitudEnvio.class);
        query.setParameter("fechaFin", LocalDate.now());
        query.setParameter("estado", Constant.SOLICITUD_ENVIO_DELIVER);
        query.setParameter("cliente", clienteRuc);
        List<SolicitudEnvio> solicitudEnvios;
        try {
            solicitudEnvios = query.getResultList();
        } catch (Exception e){
            solicitudEnvios = Collections.emptyList();
        }
        return solicitudEnvios;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioCaducadas() {
        Query query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioByFechaCaducidad");
        List<SolicitudEnvio> solicitudEnvios;
        try {
            solicitudEnvios = query.getResultList();
        } catch (Exception e){
            solicitudEnvios = Collections.emptyList();
        }
        return solicitudEnvios;
    }

    @Override
    public List<SolicitudEnvio> getSolictudEnvioWithOfertas(String usuario) {
        Query query = em.createQuery("select s from SolicitudEnvio s, Oferta o, Usuario u, Cliente c " +
                "where s.solicitudEnvioId = o.ofertaIdSolicitud and s.solicitudEnvioEstado = :solicitudEnvioEstado and o.ofertaEstado = :ofertaEstado " +
                "and s.solicitudEnvioUsuarioId = u.usuarioIdDocumento and u.usuarioRuc = c.clienteRuc " +
                "and u.usuarioRuc = :usuario group by s.solicitudEnvioId");
        query.setParameter("solicitudEnvioEstado", Constant.SOLICITUD_ENVIO_CREATE);
        query.setParameter("ofertaEstado", Constant.OFERTA_CREATED);
        query.setParameter("usuario", usuario);
        List<SolicitudEnvio> solicitudEnvios;
        try {
            solicitudEnvios = query.getResultList();
        } catch (Exception e){
            solicitudEnvios = Collections.emptyList();
        }
        return solicitudEnvios;
    }

    @Override
    public List<SolicitudEnvio> getSolicitudEnvioByFechaCaducidadNowEstado(Integer estado) {
        Query query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioByFechaCaducidadNowEstado");
        query.setParameter("estado", estado);
        List<SolicitudEnvio> solicitudEnvios;
        try {
            solicitudEnvios = query.getResultList();
        } catch (Exception e){
            solicitudEnvios = Collections.emptyList();
        }
        return solicitudEnvios;
    }

    @Override
    public SolicitudEnvio getSolicitudEnvioByOferta(Integer oferta) {
        Query query = em.createNamedQuery("SolicitudEnvio.SolicitudEnvioByOferta");
        query.setParameter("oferta", oferta);
        SolicitudEnvio solicitudEnvio = (SolicitudEnvio) query.getSingleResult();
        return solicitudEnvio;
    }

    @Override
    public double getToneladasEnviadasSolicitudesCompletadas(){
        double toneladasEnviadas = 0;
        Query query = em.createQuery("select sum(s.solicitudEnvioPeso), s.solicitudEnvioUnidadPeso " +
                " from SolicitudEnvio s " +
                " where s.solicitudEnvioEstado = :estadoSolicitudCompletada " +
                " group by solicitudEnvioUnidadPeso");
        query.setParameter("estadoSolicitudCompletada", Constant.SOLICITUD_ENVIO_PAYED);
        List<Object[]> resultList = query.getResultList();
        for(Object[] result : resultList){
            int unidadPeso = TasOnUtil.getIntFromObject(result[1]);
            switch (unidadPeso){
                case Constant.TONELADA_ID:
                    toneladasEnviadas += TasOnUtil.getDoubleFromObject(result[0]);
                    break;
                case Constant.KILOGRAMO_ID:
                    toneladasEnviadas += TasOnUtil.transformarKgATon(TasOnUtil.getDoubleFromObject(result[0]));
                    break;
                case Constant.LIBRA_ID:
                    toneladasEnviadas += TasOnUtil.transformarLbATon(TasOnUtil.getDoubleFromObject(result[0]));
                    break;
            }
        }
        return TasOnUtil.roundDouble(toneladasEnviadas, 2);
    }

    @Override
    public double getTotalAhorradoSolicitudesCompletadas(){
        Map<String, Double> maximosValoresOfertadosMap = getMaximosValoresOfertadosSolicitudesCompletadas();
        Map<String, Double> valoresOfertadosMap = getValoresOfertasGanadorasSolicitudesCompletadas();
        double result = maximosValoresOfertadosMap.get("valor") + maximosValoresOfertadosMap.get("comision")
                - valoresOfertadosMap.get("valor") - valoresOfertadosMap.get("comision");
        return TasOnUtil.roundDouble(result, 2);
    }

    @Override
    public List<EstadoSolicitudesDTO> getEstadoSolicitudes(String rucCliente) {
        List<EstadoSolicitudesDTO> estadoDTOList = new ArrayList<>();
        StringBuilder querySB = new StringBuilder("select count(s), s.solicitudEnvioEstado, s.catalogoBySolicitudEnvioEstado.catalogoItemDescripcion "+
                " from SolicitudEnvio s ");
        if(!TasOnUtil.isStringNullOrEmpty(rucCliente)){
            querySB.append(" where s.usuarioBySolicitudEnvioUsuarioId.usuarioRuc = :rucCliente ");
        }
        querySB.append(" group by s.solicitudEnvioEstado order by s.catalogoBySolicitudEnvioEstado.catalogoItemValor");

        Query query = em.createQuery(querySB.toString());
        if(!TasOnUtil.isStringNullOrEmpty(rucCliente)){
            query.setParameter("rucCliente", rucCliente);
        }
        List<Object[]> resultList = query.getResultList();
        for(Object[] result : resultList){
            EstadoSolicitudesDTO estadoSolicitudesDTO = new EstadoSolicitudesDTO();
            int i = 0;
            estadoSolicitudesDTO.setCantidadSolicitudes(TasOnUtil.getIntFromObject(result[i++]));
            estadoSolicitudesDTO.setIdEstado(TasOnUtil.getIntFromObject(result[i++]));
            estadoSolicitudesDTO.setEstado(TasOnUtil.getStringFromObject(result[i++]));
            estadoDTOList.add(estadoSolicitudesDTO);
        }
        return estadoDTOList;
    }

    @Override
    public List<SolicitudAdminDTO> getSolicitudesAdminBy(Integer estado, String rucCliente) {
        StringBuilder querySB = new StringBuilder("select solicitud_envio_id, " +
                "       (select cliente_razon_social from cliente where cliente_ruc = (select usuario_ruc from usuario where usuario_id_documento = solicitud_envio_usuario_id)) as EMPRESA, " +
                "       solicitud_envio_peso, " +

                "       (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = solicitud_envio_unidad_peso) as UNIDAD_PESO, " +
                "       solicitud_envio_volumen, " +
                "       (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = solicitud_envio_unidad_volumen) as UNIDAD_VOLUMEN, " +

                "       solicitud_envio_numero_piesas, " +
                "       (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = (select cliente_tipo_producto from cliente where cliente_ruc = (select usuario_ruc from usuario where usuario_id_documento = solicitud_envio_usuario_id))) as TIPO_PRODUCTO, " +
                "       solicitud_envio_numero_estibadores, " +

                "       (select localidad_descripcion from localidad where localidad_id = solicitud_envio_localidad_origen) as ORIGEN, " +
                "       (select localidad_descripcion from localidad where localidad_id = solicitud_envio_localidad_destino) as DESTINO, " +
                "       solicitud_envio_fecha_creacion, " +

                "       solicitud_envio_fecha_caducidad, " +
                "       solicitud_envio_fecha_recoleccion, " +
                "       solicitud_envio_fecha_entrega, " +

                "       solicitud_envio_observaciones, " +
                "       (select count(*) from oferta where oferta_id_solicitud = solicitud_envio_id) as OFERTAS_RECIBIDAS, " +
                "       (select max(oferta_valor+oferta_comision) from oferta where oferta_id_solicitud = solicitud_envio_id) as MAXIMA_OFERTA, " +

                "       (select min(oferta_valor+oferta_comision) from oferta where oferta_id_solicitud = solicitud_envio_id) as MINIMA_OFERTA, " +
                "       (select oferta_valor+oferta_comision from oferta where oferta_id = solicitud_envio_oferta_id) as VALOR_OFERTA_GANADORA, " +
                "       (select cliente_razon_social from cliente where cliente_ruc = (select usuario_ruc from usuario where usuario_id_documento = (select oferta_id_usuario from oferta where oferta_id = solicitud_envio_oferta_id))) as TRANSPORTISTA_GANADOR, " +

                "       (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = solicitud_envio_estado) as ESTADO "+

                "       from solicitud_envio ");

        String queryEstado = "solicitud_envio_estado = :estado";
        String queryRucCliente = "(select usuario_ruc from usuario where usuario_id_documento = solicitud_envio_usuario_id) = :rucCliente";

        if(estado != null && !TasOnUtil.isStringNullOrEmpty(rucCliente))
            querySB.append(" where ".concat(queryEstado).concat(" and ").concat(queryRucCliente));
        else if(estado != null)
            querySB.append(" where ".concat(queryEstado));
        else if(!TasOnUtil.isStringNullOrEmpty(rucCliente))
            querySB.append(" where ".concat(queryRucCliente));

        Query query = em.createNativeQuery(querySB.toString());

        if(estado != null)
            query.setParameter("estado", estado);
        if(!TasOnUtil.isStringNullOrEmpty(rucCliente))
            query.setParameter("rucCliente", rucCliente);

        return getSolicitudAdminDTOListFromResultList(query.getResultList());
    }

    private List<SolicitudAdminDTO> getSolicitudAdminDTOListFromResultList(List<Object[]> resultList){
        List<SolicitudAdminDTO> solicitudAdminDTOList = new ArrayList<>();
        for(Object[] result : resultList){
            SolicitudAdminDTO solicitudAdminDTO = new SolicitudAdminDTO();
            int i = 0;
            solicitudAdminDTO.setIdSolicitud(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setCliente(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setPeso(TasOnUtil.roundDouble(TasOnUtil.getDoubleFromObject(result[i++]), 2));

            solicitudAdminDTO.setUnidadPeso(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setVolumen(TasOnUtil.roundDouble(TasOnUtil.getDoubleFromObject(result[i++]), 2));
            solicitudAdminDTO.setUnidadVolumen(TasOnUtil.getStringFromObject(result[i++]));

            solicitudAdminDTO.setPiezas(TasOnUtil.getIntFromObject(result[i++]));
            solicitudAdminDTO.setTipoProducto(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setEstibadores(TasOnUtil.getIntFromObject(result[i++]));

            solicitudAdminDTO.setOrigen(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setDestino(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setFechaCreacion(TasOnUtil.getTimeStampFromObject(result[i++]));

            solicitudAdminDTO.setFechaCaducidad(TasOnUtil.getTimeStampFromObject(result[i++]));
            solicitudAdminDTO.setFechaRecoleccion(TasOnUtil.getTimeStampFromObject(result[i++]));
            solicitudAdminDTO.setFechaEntrega(TasOnUtil.getTimeStampFromObject(result[i++]));

            solicitudAdminDTO.setObservaciones(TasOnUtil.getStringFromObject(result[i++]));
            solicitudAdminDTO.setOfertasRecibidas(TasOnUtil.getIntFromObject(result[i++]));
            solicitudAdminDTO.setValorMaximaOferta(TasOnUtil.roundDouble(TasOnUtil.getDOUBLEFromObject(result[i++]), 2));

            solicitudAdminDTO.setValorMinimaOferta(TasOnUtil.roundDouble(TasOnUtil.getDOUBLEFromObject(result[i++]), 2));
            solicitudAdminDTO.setValorOfertaGanadora(TasOnUtil.roundDouble(TasOnUtil.getDOUBLEFromObject(result[i++]), 2));
            solicitudAdminDTO.setTransportistaGanador(TasOnUtil.getStringFromObject(result[i++]));

            solicitudAdminDTO.setEstado(TasOnUtil.getStringFromObject(result[i++]));

            solicitudAdminDTOList.add(solicitudAdminDTO);
        }
        return solicitudAdminDTOList;
    }

    private Map<String, Double> getMaximosValoresOfertadosSolicitudesCompletadas(){
        Query query = em.createNativeQuery("select sum(total), sum(comision) " +
                " from (select max(oferta_valor) as total, max(oferta_comision) as comision from oferta where oferta_id_solicitud in ( " +
                "         select solicitud_envio_id from solicitud_envio where solicitud_envio_estado = :estadoSolicitudCompletada" +
                "       ) group by oferta_id_solicitud" +
                ") as resultado");
        query.setParameter("estadoSolicitudCompletada", Constant.SOLICITUD_ENVIO_PAYED);
        List<Object[]> resultList = query.getResultList();
        Map<String, Double> result = new HashMap<>();
        result.put("valor", TasOnUtil.getDoubleFromObject(resultList.get(0)[0]));
        result.put("comision", TasOnUtil.getDoubleFromObject(resultList.get(0)[1]));
        return result;
    }

    private Map<String, Double> getValoresOfertasGanadorasSolicitudesCompletadas(){
        Query query = em.createQuery("select sum(s.ofertaBySolicitudEnvioOfertaId.ofertaValor), sum(s.ofertaBySolicitudEnvioOfertaId.ofertaComision) " +
                " from SolicitudEnvio s where s.solicitudEnvioEstado = :estadoSolicitudCompletada");
        query.setParameter("estadoSolicitudCompletada", Constant.SOLICITUD_ENVIO_PAYED);
        List<Object[]> resultList = query.getResultList();
        Map<String, Double> result = new HashMap<>();
        result.put("valor", TasOnUtil.getDoubleFromObject(resultList.get(0)[0]));
        result.put( "comision", TasOnUtil.getDoubleFromObject(resultList.get(0)[1]));
        return result;
    }

    @Override
    public SolicitudEnvio getUltimaSolicitud(String rucCliente){
        Query query = em.createQuery("select s from SolicitudEnvio s where s.usuarioBySolicitudEnvioUsuarioId.usuarioRuc = :rucCliente " +
                " order by solicitudEnvioFechaCreacion desc ");
        query.setMaxResults(1);
        query.setParameter("rucCliente", rucCliente);
        try{
            return (SolicitudEnvio) query.getResultList().get(0);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<DireccionDTO> getDatosOrigenUltimosEnvios(Integer idOrigen, String rucCliente){
        List<DireccionDTO> response = new ArrayList<>();
        Query query = em.createQuery("select s from SolicitudEnvio s where s.usuarioBySolicitudEnvioUsuarioId.usuarioRuc = :rucCliente " +
                " and s.solicitudEnvioLocalidadOrigen = :origen order by solicitudEnvioFechaCreacion desc ");
        query.setMaxResults(25);
        query.setParameter("rucCliente", rucCliente);
        query.setParameter("origen", idOrigen);
        List<SolicitudEnvio> solicitudEnvioList = query.getResultList();
        if(solicitudEnvioList == null || solicitudEnvioList.isEmpty()) return response;
        solicitudEnvioList.forEach(s -> {
            DireccionDTO direccionDTO = new DireccionDTO(s.getSolicitudEnvioPersonaEntrega(), s.getSolicitudEnvioDireccionOrigen());
            if(!response.contains(direccionDTO)) response.add(direccionDTO);
        });
        return response;
    }

    @Override
    public List<DireccionDTO> getDatosDestinoUltimosEnvios(Integer idDestino, String rucCliente){
        List<DireccionDTO> response = new ArrayList<>();
        Query query = em.createQuery("select s from SolicitudEnvio s where s.usuarioBySolicitudEnvioUsuarioId.usuarioRuc = :rucCliente " +
                " and s.solicitudEnvioLocalidadDestino = :destino order by solicitudEnvioFechaCreacion desc ");
        query.setMaxResults(25);
        query.setParameter("rucCliente", rucCliente);
        query.setParameter("destino", idDestino);
        List<SolicitudEnvio> solicitudEnvioList = query.getResultList();
        if(solicitudEnvioList == null || solicitudEnvioList.isEmpty()) return response;
        solicitudEnvioList.forEach(s -> {
            DireccionDTO direccionDTO = new DireccionDTO(s.getSolicitudEnvioPersonaRecibe(), s.getSolicitudEnvioDireccionDestino());
            if(!response.contains(direccionDTO)) response.add(direccionDTO);
        });
        return response;
    }

}
