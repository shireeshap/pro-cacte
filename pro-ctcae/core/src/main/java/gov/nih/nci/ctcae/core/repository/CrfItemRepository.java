package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.query.CrfItemQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemRepository extends
        AbstractRepository<CrfItem, CrfItemQuery> {

    @Override
    protected Class<CrfItem> getPersistableClass() {
        return CrfItem.class;

    }

}
