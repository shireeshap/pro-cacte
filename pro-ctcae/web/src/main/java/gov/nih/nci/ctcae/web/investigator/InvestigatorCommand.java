package gov.nih.nci.ctcae.web.investigator;

import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.domain.SiteInvestigator;

/**
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class InvestigatorCommand {

    private Investigator investigator;


    public InvestigatorCommand() {
        investigator = new Investigator();
        investigator.addSiteInvestigator(new SiteInvestigator());
    }

    public Investigator getInvestigator() {
        return investigator;
    }

    public void setInvestigator(Investigator investigator) {
        this.investigator = investigator;
    }
}
