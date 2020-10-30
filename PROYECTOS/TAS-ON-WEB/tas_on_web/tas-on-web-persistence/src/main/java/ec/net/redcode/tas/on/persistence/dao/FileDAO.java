package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.File;

import java.util.List;

public interface FileDAO extends GenericDAO<File, Integer> {

    List<File> getFilesByIds(int... fileId);

}
