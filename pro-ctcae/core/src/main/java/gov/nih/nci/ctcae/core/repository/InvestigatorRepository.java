package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.query.InvestigatorQuery;

/**
 * @author Mehul Gulati
 *         Date: Oct 15, 2008
 */
public class InvestigatorRepository extends AbstractRepository<Investigator, InvestigatorQuery> {

    @Override
    protected Class<Investigator> getPersistableClass() {
        return Investigator.class;
    }

//    @Override
//    public Investigator findById(Integer id) {
//        Investigator investigator = super.findById(id);
//
//        Collection<SiteInvestigator> investigatorCollection = investigator.getSiteInvestigators();
//        for (SiteInvestigator siteInvestigator : investigatorCollection) {
//            siteInvestigator.getOrganization().getDisplayName();
//        }
//        return investigator;
//
//
//    }
}
