package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.OfertaFileDAO;
import ec.net.redcode.tas.on.persistence.entities.OfertaFile;
import ec.net.redcode.tas.on.persistence.service.OfertaFileService;
import lombok.Setter;

public class OfertaFileServiceImpl implements OfertaFileService {

@Setter private OfertaFileDAO ofertaFileDAO;

    @Override
    public void create(OfertaFile ofertaFile) {
        ofertaFileDAO.create(ofertaFile);
    }

    @Override
    public OfertaFile read(int ofertaFileId) {
        return ofertaFileDAO.read(ofertaFileId);
    }

    @Override
    public void update(OfertaFile ofertaFile) {
        ofertaFileDAO.update(ofertaFile);
    }

}
