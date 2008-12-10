package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public abstract class AbstractRepository<T extends Persistable, Q extends Query> implements Repository<T, Q> {
	protected GenericRepository genericRepository;
	protected Log log = LogFactory.getLog(getClass());

	public T findById(final Integer id) {
		return genericRepository.findById(getPersistableClass(), id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T save(final T t) {
		return genericRepository.save(t);  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(final T t) {
		genericRepository.delete(t);
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Collection<T> find(final Q query) {
		return (Collection<T>) genericRepository.find(query);
	}

	@Required
	public void setGenericRepository(final GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

	public T findSingle(final Q query) {
		return (T) genericRepository.findSingle(query);

	}

	protected abstract Class<T> getPersistableClass();
}
