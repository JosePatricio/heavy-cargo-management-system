package ec.net.redcode.tas.on.persistence.dao;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {

    /**
     * Creates a new instance of a persistent object.
     *
     * @param newInstance the new instance
     */
    void create(T newInstance);

    /**
     * Reads a persistent object.
     *
     * @param id the id
     * @return the t
     */
    T read(Object id);

    /**
     * Updates a persistent object.
     *
     * @param persistentObject the persistent object
     */
    void update(T persistentObject);

    /**
     * Deletes a persistent object.
     *
     * @param persistentObject the persistent object
     */
    void delete(T persistentObject);

}
