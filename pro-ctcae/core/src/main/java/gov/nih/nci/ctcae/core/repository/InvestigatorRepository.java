package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.query.InvestigatorQuery;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Oct 15, 2008
 * Time: 2:42:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvestigatorRepository extends AbstractRepository<Investigator, InvestigatorQuery> {

    @Override
        protected Class<Investigator> getPersistableClass() {
            return Investigator.class;
    }
}
