package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.OfertaFileDAO;
import ec.net.redcode.tas.on.persistence.entities.OfertaFile;

public class OfertaFileDAOImpl extends GenericDAOImpl<OfertaFile, Integer> implements OfertaFileDAO {

    public OfertaFileDAOImpl(){
        super(OfertaFile.class);
    }

}
