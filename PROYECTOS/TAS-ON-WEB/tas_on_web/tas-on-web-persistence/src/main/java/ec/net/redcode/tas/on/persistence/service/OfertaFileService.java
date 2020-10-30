package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.OfertaFile;

public interface OfertaFileService {

    void create(OfertaFile ofertaFile);
    OfertaFile read(int ofertaFileId);
    void update(OfertaFile ofertaFile);

}
