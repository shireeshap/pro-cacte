package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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
    @Transient
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * {@inheritDoc}
     */
    public boolean isPersisted() {
        return getId() != null;
    }

}