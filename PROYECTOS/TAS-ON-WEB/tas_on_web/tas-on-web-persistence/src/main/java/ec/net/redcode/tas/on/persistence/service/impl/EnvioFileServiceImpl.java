package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.EnvioFileDAO;
import ec.net.redcode.tas.on.persistence.entities.EnvioFile;
import ec.net.redcode.tas.on.persistence.service.EnvioFileService;
import lombok.Setter;

public class EnvioFileServiceImpl implements EnvioFileService {

@Setter private EnvioFileDAO envioFileDAO;

    @Override
    public void create(EnvioFile envioFile) {
        envioFileDAO.create(envioFile);
    }

    @Override
    public EnvioFile read(int envioFileId) {
        return envioFileDAO.read(envioFileId);
    }

    @Override
    public void update(EnvioFile envioFile) {
        envioFileDAO.update(envioFile);
    }

    @Override
    public EnvioFile readByFileId(int fileId){
        return envioFileDAO.readByFileId(fileId);
    }

}
