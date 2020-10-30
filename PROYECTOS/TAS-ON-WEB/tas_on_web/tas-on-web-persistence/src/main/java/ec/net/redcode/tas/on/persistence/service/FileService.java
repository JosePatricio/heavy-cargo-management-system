package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.File;

public interface FileService {

    void create(File file);
    File read(int fileId);
    void update(File file);

}
