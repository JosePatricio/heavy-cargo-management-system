package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class SolicitudEnvioDAOTest extends CommonTest {

    Logger logger = LoggerFactory.getLogger(SolicitudEnvioDAOTest.class);

    @Test
    @Ignore
    public void testCrudUsuarios(){

        SolicitudEnvioDAO solicitudEnvioDAO = (SolicitudEnvioDAO) getBean("solicitudEnvioDAO");
        SolicitudEnvio solicitudEnvio = new SolicitudEnvio();

        solicitudEnvio.setSolicitudEnvioId("002");
        solicitudEnvio.setSolicitudEnvioDireccionOrigen("direcion Origen");
        solicitudEnvio.setSolicitudEnvioLocalidadOrigen(1);
        solicitudEnvio.setSolicitudEnvioDireccionDestino("Direccion Destino");
        solicitudEnvio.setSolicitudEnvioLocalidadDestino(2);
        solicitudEnvio.setSolicitudEnvioEstado(1);
        solicitudEnvio.setSolicitudEnvioDiasValides(2);
        solicitudEnvio.setSolicitudEnvioFechaEntrega(null);
        solicitudEnvio.setSolicitudEnvioNumeroEstibadores(2);
        solicitudEnvio.setSolicitudEnvioPeso(12.1);
        solicitudEnvio.setSolicitudEnvioUnidadPeso(1);
        solicitudEnvio.setSolicitudEnvioVolumen(12.2);
        solicitudEnvio.setSolicitudEnvioUnidadVolumen(1);
        solicitudEnvio.setSolicitudEnvioFechaRecoleccion(null);
        solicitudEnvio.setSolicitudEnvioPersonaEntrega("Marcus Vera");
        solicitudEnvio.setSolicitudEnvioPersonaRecibe("Mario Heredia");
        //usuarios.setLocalidad(1);
        logger.info("Creating SolicitudEnvio with ID " + solicitudEnvio.getSolicitudEnvioId());
        solicitudEnvioDAO.create(solicitudEnvio);

        solicitudEnvio.setSolicitudEnvioDireccionDestino("XXXXXXXXXXXXXXXXXX");
        solicitudEnvioDAO.update(solicitudEnvio);
        logger.info("Updating user with document " + solicitudEnvio.getSolicitudEnvioId());
        solicitudEnvioDAO.update(solicitudEnvio);
        SolicitudEnvio solicitudEnvioRead = solicitudEnvioDAO.read("002");
              solicitudEnvioDAO.delete(solicitudEnvio);
    }


    @Test
    @Ignore
    public void testGetSolicitudEnvioByOrigenDestino(){
        SolicitudEnvioDAO solicitudEnvioDAO = (SolicitudEnvioDAO) getBean("solicitudEnvioDAO");

        List <SolicitudEnvio> solicitudEnvioList = solicitudEnvioDAO.getSolicitudEnvioByOrigenDestino(1,2,1);
        Assert.assertNotNull(solicitudEnvioList);
        logger.info("show Solicitud by origen destino with Id solicitud " + solicitudEnvioList.get(0).getSolicitudEnvioId());

    }

    @Test
    @Ignore
    public void testGetSolicitudEnvioBySolicitudEnvioOferta(){
        SolicitudEnvioDAO solicitudEnvioDAO = (SolicitudEnvioDAO) getBean("solicitudEnvioDAO");
        List <SolicitudEnvio> solicitudEnvioList = solicitudEnvioDAO.getSolicitudEnvioBySolicitudEnvioOferta(11,1);
        Assert.assertNotNull(solicitudEnvioList);
       logger.info("show Solicitud by Oferta with Id solicitud " + solicitudEnvioList.size());
       logger.info("show Solicitud by Oferta with Persona Entrega " + solicitudEnvioList.size());

    }

    @Test
    @Ignore
    public void testGetSolicitudEnvioBySolicitudEstado(){
        SolicitudEnvioDAO solicitudEnvioDAO = (SolicitudEnvioDAO) getBean("solicitudEnvioDAO");
        List <SolicitudEnvio> solicitudEnvioList = solicitudEnvioDAO.getSolicitudEnvioBySolicitudEstado(1);
        Assert.assertNotNull(solicitudEnvioList);
        logger.info("show Solicitud by Oferta with Id solicitud " + solicitudEnvioList.get(0).getSolicitudEnvioId());

    }


}
