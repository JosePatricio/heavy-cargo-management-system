package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.FileDAO;
import ec.net.redcode.tas.on.persistence.entities.File;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class FileDAOImpl extends GenericDAOImpl<File, Integer> implements FileDAO {

    public FileDAOImpl(){
        super(File.class);
    }

    @Override
    public List<File> getFilesByIds(int... fileId) {
        Query query = em.createQuery("select f from File f where f.fileId in :fileId");
        query.setParameter("fileId", fileId);
        List<File> files;
        try {
            files = query.getResultList();
        } catch (Exception e){
            files = Collections.emptyList();
        }
        return files;
    }
}
