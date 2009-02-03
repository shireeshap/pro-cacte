package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;

// TODO: Auto-generated Javadoc
/**
 * The Class ProCtcRepository.
 * 
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcRepository extends AbstractRepository<ProCtc, ProCtcQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<ProCtc> getPersistableClass() {
        return ProCtc.class;

    }

}
