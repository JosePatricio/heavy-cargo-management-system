package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Registro;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.dao.LocalidadDAO;
import ec.net.redcode.tas.on.persistence.entities.Localidad;


import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


public class LocalidadDAOImpl extends GenericDAOImpl<Localidad, Integer> implements LocalidadDAO{

    public LocalidadDAOImpl(){
        super(Localidad.class);
    }

    @Override
    public Localidad getLocalidadByLocalidadId(Integer localidadId, Integer localidadEstado) {
        Query query = em.createNamedQuery("Localidad.LocalidadBylocalidadId");
        query.setParameter("localidadId",localidadId);
        query.setParameter("localidadEstado",localidadEstado);
        Localidad localidad = (Localidad) query.getSingleResult();
        return localidad;

    }

    @Override
    public List<Localidad> getLocalidadByLocalidadIdPadre(Integer localidadIdPadre, Integer localidadEstado) {
        Query query = em.createNamedQuery("Localidad.LocalidadByLocalidadPadre");
        query.setParameter("localidadLocalidadPadre",localidadIdPadre);
        query.setParameter("localidadEstado",localidadEstado);
        List<Localidad> localidads = query.getResultList();
        return localidads;
    }

    @Override
    public List<Localidad> getAllCiudades() {
        List<Localidad> localidadList = new ArrayList<>();
        Query query = em.createNamedQuery("Localidad.AllCiudades");
        List<Object[]> resultList = query.getResultList();
        for(Object[] o : resultList){
            int i = 0;
            Localidad localidad = new Localidad();
            localidad.setLocalidadId((int)o[i++]);
            localidad.setLocalidadDescripcion(o[i++] + " / " + o[i++]);
            localidadList.add(localidad);
        }
        return localidadList;
    }

    @Override
    public List<Registro<Integer, String>> getLocalidadesDestinoSolicitudesCompletadas() {
        List<Registro<Integer, String>> registros = new ArrayList<>();
        Query query = em.createQuery("select count(s), s.localidadBySolicitudEnvioLocalidadDestino.localidadDescripcion " +
                " from SolicitudEnvio s where s.solicitudEnvioEstado = :estadoCompletada" +
                " group by s.solicitudEnvioLocalidadDestino order by 1");
        query.setParameter("estadoCompletada", Constant.SOLICITUD_ENVIO_PAYED);
        List<Object[]> resultList = query.getResultList();
        for(Object[] result : resultList){
            Registro<Integer, String> registro = new Registro<>();
            registro.setValor(TasOnUtil.getIntFromObject(result[0]));
            registro.setDetalle(TasOnUtil.getStringFromObject(result[1]));
            registros.add(registro);
        }
        return registros;
    }

    @Override
    public List<Registro<Integer, String>> getLocalidadesOrigenSolicitudesCompletadas() {
        List<Registro<Integer, String>> registros = new ArrayList<>();
        Query query = em.createQuery("select count(s), s.localidadBySolicitudEnvioLocalidadOrigen.localidadDescripcion " +
                " from SolicitudEnvio s where s.solicitudEnvioEstado = :estadoCompletada" +
                " group by s.solicitudEnvioLocalidadOrigen order by 1");
        query.setParameter("estadoCompletada", Constant.SOLICITUD_ENVIO_PAYED);
        List<Object[]> resultList = query.getResultList();
        for(Object[] result : resultList){
            Registro<Integer, String> registro = new Registro<>();
            registro.setValor(TasOnUtil.getIntFromObject(result[0]));
            registro.setDetalle(TasOnUtil.getStringFromObject(result[1]));
            registros.add(registro);
        }
        return registros;
    }
}
