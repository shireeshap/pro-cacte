package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ResearchStaff;
import gov.nih.nci.ctcae.core.query.ResearchStaffQuery;

/**
 *@author Mehul Gulati
 * Date: Oct 24, 2008
 */
public class ResearchStaffRepository extends AbstractRepository<ResearchStaff, ResearchStaffQuery> {

    @Override
         protected Class<ResearchStaff> getPersistableClass() {
              return ResearchStaff.class;
    }
}
