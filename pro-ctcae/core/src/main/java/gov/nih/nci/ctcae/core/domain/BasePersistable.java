package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.MappedSuperclass;

//
/**
 * Base class for all the persistable entities.
 *
 * @author , Created on July, 20th, 2007
 */
@MappedSuperclass
public abstract class BasePersistable implements Persistable {
    /**
     * The Constant logger.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * {@inheritDoc}
     */
    public boolean isPersisted() {
        return getId() != null;
    }

}