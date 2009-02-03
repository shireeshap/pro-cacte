package gov.nih.nci.ctcae.core.domain;

import javax.persistence.MappedSuperclass;

// TODO: Auto-generated Javadoc
/**
 * Base class for all the persistable entities.
 * 
 * @author , Created on July, 20th, 2007
 */
@MappedSuperclass
public abstract class BasePersistable implements Persistable {

    /**
     * {@inheritDoc}
     */
    public boolean isPersisted() {
        return getId() != null;
    }

}