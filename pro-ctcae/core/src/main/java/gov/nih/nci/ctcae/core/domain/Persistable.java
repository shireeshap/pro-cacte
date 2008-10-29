package gov.nih.nci.ctcae.core.domain;

import java.io.Serializable;

/**
 * Interface for the entities that need to persisted in the database.
 *
 * @author , Created on July, 20th, 2007
 */
public interface Persistable  extends Serializable{

    /**
     * Returns the primary key of the persistable entity.
     *
     * @return id. the primary key of the persistable entity.
     */
    Integer getId();

    /**
     * Checks if entity is already persisted in the DB.
     *
     * @return true, if entity is already persisted in the DB; false if not
     */
    boolean isPersisted();

    /**
     * Sets the Id. added only for testing purpose
     *
     * @param id
     */
    void setId(Integer id);


}
