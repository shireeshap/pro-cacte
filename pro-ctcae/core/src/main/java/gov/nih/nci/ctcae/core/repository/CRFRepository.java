package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFRepository extends AbstractRepository<CRF, CRFQuery> {

    @Override
    protected Class<CRF> getPersistableClass() {
        return CRF.class;

    }
}
