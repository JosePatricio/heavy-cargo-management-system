package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.EnvioFile;

public interface EnvioFileService {

    void create(EnvioFile envioFile);
    EnvioFile read(int envioFileId);
    EnvioFile readByFileId(int fileId);
    void update(EnvioFile envioFile);

}
