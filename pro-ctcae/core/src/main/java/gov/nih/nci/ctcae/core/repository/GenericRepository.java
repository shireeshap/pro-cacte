package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.Query;

import java.util.Collection;
import java.util.List;

/**
 * Main interface to access ORM layer. It defines all CRUD related operations
 * for a persistable object
 *
 * @author , Created on July, 20th, 2007
 */
public interface GenericRepository {

    /**
     * Find persistable object by primary key
     *
     * @param id the primary key of persistable object
     *
     * @return the persistable object
     */
    <T extends Persistable> T findById(Class<T> classArg, Integer id);

    /**
     * Save the persistable object in the DB or updates the persistable object
     * if object already exists in the database
     *
     * @param persistable the persistable object that needs to be saved/updated
     *                    in the database
     *
     * @return the saved/updated object
     */
    <T extends Persistable> T save(T persistable);

    /**
     * not implemented yet
     */
    Collection<? extends Persistable> save(Collection<? extends Persistable> persistableObjects);

    /**
     * Deletes the persistable object from database.
     *
     * @param persistable object the object that need to be deleted from
     *                    database
     */
    void delete(Persistable persistable);

    /**
     * Find persistable objects for a query.
     *
     * @param query the query
     *
     * @return the collection of persistable objects
     */

    List<? extends Persistable> find(Query query);

    /**
     * Find single persistable object for a query.
     *
     * @param query the query
     *
     * @return the single persistable object
     */

    Persistable findSingle(Query query);

}
