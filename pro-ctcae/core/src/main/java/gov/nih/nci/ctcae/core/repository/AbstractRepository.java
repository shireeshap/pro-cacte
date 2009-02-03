package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractRepository.
 *
 * @author
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public abstract class AbstractRepository<T extends Persistable, Q extends Query> implements Repository<T, Q> {

    /**
     * The generic repository.
     */
    protected GenericRepository genericRepository;

    /**
     * The log.
     */
    protected Log logger = LogFactory.getLog(getClass());

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.repository.Repository#findById(java.lang.Integer)
      */
    public T findById(final Integer id) {
        return genericRepository.findById(getPersistableClass(), id);
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.repository.Repository#save(gov.nih.nci.ctcae.core.domain.Persistable)
      */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public T save(final T t) {
        return genericRepository.save(t);  //To change body of implemented methods use File | Settings | File Templates.
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.repository.Repository#delete(gov.nih.nci.ctcae.core.domain.Persistable)
      */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(final T t) {
        genericRepository.delete(t);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.repository.Repository#find(gov.nih.nci.ctcae.core.query.Query)
      */
    public Collection<T> find(final Q query) {
        return (Collection<T>) genericRepository.find(query);
    }

    /**
     * Sets the generic repository.
     *
     * @param genericRepository the new generic repository
     */
    @Required
    public void setGenericRepository(final GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.repository.Repository#findSingle(gov.nih.nci.ctcae.core.query.Query)
      */
    public T findSingle(final Q query) {
        return (T) genericRepository.findSingle(query);

    }

    /**
     * Gets the persistable class.
     *
     * @return the persistable class
     */
    protected abstract Class<T> getPersistableClass();
}
