package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.CommonTest;
import ec.net.redcode.tas.on.persistence.entities.Contrato;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ContratoDAOTest extends CommonTest {
    Logger logger = LoggerFactory.getLogger(LocalidadDAOTest.class);
    @Test
    public void testCrudContreatos(){
        ContratoDAO contratoDAO = (ContratoDAO) getBean("contratoDAO");
        Contrato contrato = new Contrato();
        contrato.setContratoId(1);
        contrato.setContratoDocumentoCliente("1308930808");
        contrato.setContratoDocumentoConductor("1310479231");
        contrato.setContratoFechaContrato(null);
        contrato.setContratoEstado(1);
        contrato.setContratoIdSolicitud(11);
        contrato.setContratoImeiConductor("32323232323");
        contrato.setContratoIpCliente("19216801");
        contrato.setContratoIpConductor("20001");

                contratoDAO.create(contrato);

        logger.info("Creating Contrato with ContratoId " + contrato.getContratoId());


        logger.info("Updating user with ContratoId Padre origen :" + contrato.getContratoId());
        contrato.setContratoValor(1000.00);
        contratoDAO.update(contrato);
        Contrato contratoReadUno = contratoDAO.read(1);

        logger.info("Updating Contrato with  ContratoId  :" + contratoReadUno.getContratoId() + " valor " + contratoReadUno.getContratoValor());


        Contrato contratoRead = contratoDAO.read(001);
        logger.info("Reading Contrato with ContratoId " + contratoRead.getContratoId());
        logger.info("Deleting Contrato with ContratoId " + contratoRead.getContratoId());
        contratoDAO.delete(contrato);

    }

    @Test
    @Ignore
    public void testGetContratoByContratoDocumentoCliente(){
        ContratoDAO contratoDao = (ContratoDAO) getBean("contratoDAO");
        List <Contrato> contrato = contratoDao.getContratoByContratoDocumentoCliente("1310479231",1);
        Assert.assertNotNull(contrato);
        logger.info("show Contrato by cliente with ContratoId " + contrato.get(0).getContratoId());

    }
    @Test
    @Ignore
    public void testGetContratoByContratoDocumentoConductor(){
        ContratoDAO contratoDao = (ContratoDAO) getBean("contratoDAO");
        List <Contrato> contrato = contratoDao.getContratoByContratoDocumentoConductor("1308930808",1);
        Assert.assertNotNull(contrato);
        logger.info("show Contrato by Conductor with ContratoId " + contrato.get(0).getContratoId());

    }
    @Test
    @Ignore
    public void testGetContratoByContratoIdSolicitud(){
        ContratoDAO contratoDao = (ContratoDAO) getBean("contratoDAO");
        List <Contrato> contrato = contratoDao.getContratoByContratoIdSolicitud(11,1);
        Assert.assertNotNull(contrato);
        logger.info("show Contrato by Conductor with ContratoId " + contrato.get(0).getContratoId());

    }

}
