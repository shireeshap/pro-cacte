package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Finder repository is used for finding objects.
 * 
 * @author Vinay Kumar
 */

@org.springframework.stereotype.Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FinderRepository {
    
    /** The logger. */
    protected final Log logger = LogFactory.getLog(getClass());

    /** The generic repository. */
    private GenericRepository genericRepository;

    /**
     * Find by id.
     * 
     * @param classArg the class arg
     * @param id the id
     * 
     * @return the t
     */
    public <T extends Persistable> T findById(Class<T> classArg, Integer id) {
        return genericRepository.findById(classArg, id);
    }

    /**
     * Find.
     * 
     * @param query the query
     * 
     * @return the list<? extends persistable>
     */
    public <T extends Persistable> List<? extends Persistable> find(Query query) {
        return genericRepository.find(query);
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


}
