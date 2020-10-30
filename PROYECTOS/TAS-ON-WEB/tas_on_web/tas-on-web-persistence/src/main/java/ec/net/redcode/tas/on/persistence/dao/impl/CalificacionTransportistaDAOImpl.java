
package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.CalificacionTransportistaDAO;
import ec.net.redcode.tas.on.persistence.entities.CalificacionTransportista;
import ec.net.redcode.tas.on.persistence.entities.Usuario;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalificacionTransportistaDAOImpl extends GenericDAOImpl <CalificacionTransportista, Integer> implements CalificacionTransportistaDAO {

    public CalificacionTransportistaDAOImpl(){
        super(CalificacionTransportista.class);
    }

    @Override
    public List<CalificacionTransportistaDTO> getCalificacionTransportistasByUser(String nombreUsuario){//TODO construir un DTO que incluya datos de la solicitud
        Query query = em.createNativeQuery("select calificacion_transportista_id," +
                "       (select cliente_razon_social from cliente where cliente_ruc = (select usuario_ruc from usuario where usuario_id_documento = oferta_id_usuario))," +
                "       calificacion_transportista_valor," +
                "       calificacion_transportista_comentario," +
                "       solicitud_envio_id," +
                "       (select localidad_descripcion from localidad where localidad_id = solicitud_envio_localidad_origen)," +
                "       (select localidad_descripcion from localidad where localidad_id = solicitud_envio_localidad_destino)," +
                "       solicitud_envio_peso," +
                "       (select catalogo_item_descripcion from catalogo_item where catalogo_item_id = solicitud_envio_unidad_peso)," +
                "        solicitud_envio_numero_piesas," +
                "       oferta_fecha_entrega, " +
                "       solicitud_envio_numero_doc_cliente " +
                "from solicitud_envio, oferta, calificacion_transportista" +
                "  where solicitud_envio_usuario_id in(" +
                "  select usuario_id_documento from usuario where usuario_ruc in" +
                "        (select usuario_ruc from usuario where usuario_nombre_usuario = :nombreUsuario)" +
                ") " +
                "    and solicitud_envio_oferta_id in (select calificacion_transportista_oferta_id from calificacion_transportista)" +
                "    and solicitud_envio_oferta_id = oferta_id" +
                "    and oferta_id = calificacion_transportista_oferta_id" +
                "    order by oferta_fecha_entrega desc" +
                "    limit 10");
        query.setParameter("nombreUsuario", nombreUsuario);
        return getCalificacionTansportistas(query.getResultList());
    }

    @Override
    public boolean puedeCalificarTransportista(int calificacionId, String nombreUsuario){
        try{
            Query query = em.createQuery("select u from Usuario u where u.usuarioNombreUsuario = :nombreUsuario");
            Usuario usuarioCalificador = (Usuario) query.setParameter("nombreUsuario", nombreUsuario).getSingleResult();

            Query query2 = em.createQuery("select o.solicitudEnvioByOfertaIdSolicitud.usuarioBySolicitudEnvioUsuarioId from Oferta o where o.ofertaId IN " +
                    " (select c.calificacionTransportistaOfertaId from CalificacionTransportista c where c.calificacionTransportistaId = :calificacionId)");
            Usuario usuarioCreadorSolicitud = (Usuario) query2.setParameter("calificacionId", calificacionId).getSingleResult();

            return usuarioCalificador.getUsuarioRuc().equalsIgnoreCase(usuarioCreadorSolicitud.getUsuarioRuc());
        }catch (Exception e){
            return false;
        }
    }

    private List<CalificacionTransportistaDTO> getCalificacionTansportistas(List<Object[]> objects){
        List<CalificacionTransportistaDTO> result = new ArrayList<>();
        for(Object[] o : objects){
            int i = 0;
            CalificacionTransportistaDTO calificacionTransportistaDTO = new CalificacionTransportistaDTO();
            calificacionTransportistaDTO.setId(TasOnUtil.getIntFromObject(o[i++]));
            calificacionTransportistaDTO.setEmpresaRazonSocial(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setCalificacion(TasOnUtil.getIntFromObject(o[i++]));
            calificacionTransportistaDTO.setComentario(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioId(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioOrigen(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioDestino(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioPeso(TasOnUtil.getDoubleFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioUnidadPeso(TasOnUtil.getStringFromObject(o[i++]));
            calificacionTransportistaDTO.setSolicitudEnvioPiezas(TasOnUtil.getIntFromObject(o[i++]));
            calificacionTransportistaDTO.setOfertaFechaEntrega(TasOnUtil.getTimeStampFromObject(o[i++]));
            calificacionTransportistaDTO.setNumeroDocumento(TasOnUtil.getStringFromObject(o[i++]));
            result.add(calificacionTransportistaDTO);
        }
        return result;
    }

    @Override
    public Map<String, Number> getResultadoCalificacionesTransportista(int conductorId){
        Query query = em.createNativeQuery("select count(*), avg(calificacion_transportista_valor) from calificacion_transportista " +
                "            where calificacion_transportista_oferta_id in ( " +
                "                    select oferta_id from oferta where oferta_id_conductor = :conductorId" +
                "            )");
        query.setParameter("conductorId", conductorId);
        List<Object[]> queryResult = query.getResultList();
        Map<String, Number> result = new HashMap<>();
        for(Object[] o : queryResult){
            result.put("fletes", TasOnUtil.getIntFromObject(o[0]));
            result.put("calificacion", TasOnUtil.getDoubleFromObject(o[1]));
        }
        return result;
    }

}