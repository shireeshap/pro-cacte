package gov.nih.nci.ctcae.core.repository;


import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.Query;

import java.util.Collection;

/**
 * Interface for all the repositories.
 *
 * @author , Created on July, 20th, 2007
 */
public interface Repository<T extends Persistable, Q extends Query> {

    /**
     * Find object by primary key
     *
     * @param id the primary key
     *
     * @return the object
     */
    T findById(Integer id);

    /**
     * Save the object in the DB or updates the object if object already exists
     * in the database
     *
     * @param object the object that needs to be saved/updated in the database
     *
     * @return the saved/updated object
     */
    T save(T t);

    /**
     * Deletes the object from database.
     *
     * @param object the object that need to be deleted from database
     */
    void delete(T t);

    /**
     * Find persistable objects for a given query.
     *
     * @param query the query
     *
     * @return the collection of persistable objects
     */
    public Collection<T> find(Q query);

    public T findSingle(final Q query);

}
