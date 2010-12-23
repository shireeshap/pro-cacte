package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.AbstractQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.validation.BeanValidator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//
/**
 * Jpa implementation of {@link GenericRepository}.
 *
 * @author , Created on July, 20th, 2007
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class HibernateGenericRepository<T extends Persistable> extends HibernateDaoSupport implements GenericRepository {

    private BeanValidator beanValidator;

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T extends Persistable> T save(T persistable) {

        beanValidator.validate(persistable);
        if (persistable.isPersisted()) {
            persistable = (T) getHibernateTemplate().merge(persistable);
        } else {
            getHibernateTemplate().saveOrUpdate(persistable);
        }
        return persistable;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T extends Persistable> T create(T persistable) {

        beanValidator.validate(persistable);
        getHibernateTemplate().saveOrUpdate(persistable);
        return persistable;
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Persistable persistable) {
        if (persistable != null) {
            if (persistable.isPersisted()) {
                Persistable entityToRemove = (Persistable) getHibernateTemplate().get(persistable.getClass(), persistable.getId());
                if (entityToRemove != null) {
                    getHibernateTemplate().delete(entityToRemove);
                    getHibernateTemplate().flush();
                }
            }
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
    public List<T> find(final Query query) {

        return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) throws HibernateException,
                    SQLException {
                org.hibernate.Query hiberanteQuery = session.createQuery(query.getQueryString());
                Map<String, Object> queryParameterMap = ((AbstractQuery) query).getParameterMap();
                Map<String, Collection> parameterListMap = ((AbstractQuery) query).getQueryParameterListMap();
                for (String key : queryParameterMap.keySet()) {
                    Object value = queryParameterMap.get(key);
                    hiberanteQuery.setParameter(key, value);

                }
                for (String key : parameterListMap.keySet()) {
                    Collection value = parameterListMap.get(key);
                    hiberanteQuery.setParameterList(key, value);

                }
                if (query.getMaximumResults() != null) {
                    hiberanteQuery.setMaxResults(query.getMaximumResults());
                }

                return hiberanteQuery.list();
            }

        });


    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T findSingle(Query query) {
        List<T> persistables = find(query);
        return persistables != null && !persistables.isEmpty() ? persistables.get(0) : null;

    }

    @Required
    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public void flush() {
        getHibernateTemplate().flush();
    }
}
