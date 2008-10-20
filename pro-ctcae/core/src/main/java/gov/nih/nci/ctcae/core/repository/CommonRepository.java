package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Persistable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 */

@org.springframework.stereotype.Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CommonRepository {
    protected final Log logger = LogFactory.getLog(getClass());

    private GenericRepository genericRepository;

    public <T extends Persistable> T findById(Class<T> classArg, Integer id) {
        return genericRepository.findById(classArg, id);
    }

    @Required
    public void setGenericRepository(final GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


}
