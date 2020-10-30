package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.FileDAO;
import ec.net.redcode.tas.on.persistence.entities.File;
import ec.net.redcode.tas.on.persistence.service.FileService;
import lombok.Setter;

public class FileServiceImpl implements FileService {

@Setter private FileDAO fileDAO;

    @Override
    public void create(File file) {
        fileDAO.create(file);
    }

    @Override
    public File read(int fileId) {
        return fileDAO.read(fileId);
    }

    @Override
    public void update(File file) {
        fileDAO.update(file);
    }

}
