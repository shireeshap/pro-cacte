package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.AbstractQuery;
import gov.nih.nci.ctcae.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Jpa implementation of {@link GenericRepository}
 *
 * @author , Created on July, 20th, 2007
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class JpaGenericRepository<T extends Persistable> implements GenericRepository {

    /**
     * The entity manager.
     */
    private EntityManager entityManager;

    /**
     * Injects the entity manager.
     *
     * @param entityManager the entity manager
     */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T extends Persistable> T save(T persistable) {
        T newPersistable = persistable;
        if (persistable.isPersisted()) {
            newPersistable = entityManager.merge(persistable);
        } else {
            entityManager.persist(persistable);
        }
        return newPersistable;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Collection<? extends Persistable> save(Collection<? extends Persistable> persistableObjects) {
        // throw new Exception("method not implemented");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Persistable persistable) {
        if (persistable.isPersisted()) {
            Persistable entityToRemove = entityManager.getReference(persistable.getClass(), persistable.getId());
            entityManager.remove(entityToRemove);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T extends Persistable> T findById(Class<T> classArg, Integer id) {
        return entityManager.find(classArg, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<? extends Persistable> find(Query query) {

        javax.persistence.Query jpaQuery = createQuery(query);

        return jpaQuery.getResultList();
    }

    private javax.persistence.Query createQuery(Query query) {
        javax.persistence.Query jpaQuery = entityManager.createQuery(query.getQueryString());
        Map<String, Object> queryParameterMap = ((AbstractQuery) query).getParameterMap();
        for (String key : queryParameterMap.keySet()) {
            Object value = queryParameterMap.get(key);
            jpaQuery.setParameter(key, value);

        }

        jpaQuery.setMaxResults(query.getMaximumResults());

        return jpaQuery;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Persistable findSingle(Query query) {
        javax.persistence.Query jpaQuery = createQuery(query);
        return (Persistable) jpaQuery.getSingleResult();
    }

}
