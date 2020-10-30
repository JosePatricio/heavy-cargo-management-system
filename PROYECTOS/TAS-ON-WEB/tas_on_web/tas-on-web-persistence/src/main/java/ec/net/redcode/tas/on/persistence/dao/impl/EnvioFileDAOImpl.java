package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.EnvioFileDAO;
import ec.net.redcode.tas.on.persistence.entities.EnvioFile;

import javax.persistence.Query;


public class EnvioFileDAOImpl extends GenericDAOImpl<EnvioFile, Integer> implements EnvioFileDAO {

    public EnvioFileDAOImpl(){
        super(EnvioFile.class);
    }

    @Override
    public EnvioFile readByFileId(int fileId){
        try{
            Query query = em.createQuery("select o from EnvioFile o where o.envioFileFileId = :fileId");
            query.setParameter("fileId", fileId);
            return (EnvioFile)query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

}
