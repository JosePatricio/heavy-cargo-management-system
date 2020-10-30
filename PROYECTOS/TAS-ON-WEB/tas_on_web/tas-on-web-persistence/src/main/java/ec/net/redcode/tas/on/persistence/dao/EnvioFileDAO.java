package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.EnvioFile;


public interface EnvioFileDAO extends GenericDAO<EnvioFile, Integer> {

    EnvioFile readByFileId(int fileId);

}
