package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.AbstractQuery;
import gov.nih.nci.ctcae.core.query.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Jpa implementation of {@link GenericRepository}
 *
 * @author , Created on July, 20th, 2007
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class HibernateGenericRepository<T extends Persistable> extends HibernateDaoSupport implements GenericRepository {


	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T extends Persistable> T save(T persistable) {
		if (persistable.isPersisted()) {
			persistable = (T) getHibernateTemplate().merge(persistable);
		} else {
			getHibernateTemplate().persist(persistable);
		}
		return persistable;
	}


	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Persistable persistable) {
		if (persistable.isPersisted()) {
			Persistable entityToRemove = (Persistable) getHibernateTemplate().get(persistable.getClass(), persistable.getId());
			getHibernateTemplate().delete(entityToRemove);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <T extends Persistable> T findById(Class<T> classArg, Integer id) {

		return id != null && classArg != null ? (T) getHibernateTemplate().get(classArg, id) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Persistable> find(final Query query) {

		return (List<? extends Persistable>) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(final Session session) throws HibernateException,
				SQLException {
				org.hibernate.Query hiberanteQuery = session.createQuery(query.getQueryString());
				Map<String, Object> queryParameterMap = ((AbstractQuery) query).getParameterMap();
				for (String key : queryParameterMap.keySet()) {
					Object value = queryParameterMap.get(key);
					hiberanteQuery.setParameter(key, value);

				}
				return hiberanteQuery.list();
			}

		});


	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Persistable findSingle(Query query) {
		List<? extends Persistable> persistables = find(query);
		if (persistables.size() > 1) {
			String message = "multiple results found for query:" + query;
			logger.error(message + " " + query.getQueryString());

			throw new CtcAeSystemException(message);
		}
		return persistables != null && !persistables.isEmpty() ? persistables.get(0) : null;

	}

}
