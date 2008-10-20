package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;

/**
 * @author Vinay Kumar
 */
public class Fixture {


    public static Organization createOrganization(String name, String nciCode) {

        Organization organization = new Organization();
        organization.setName(name);
        organization.setNciInstituteCode(nciCode);
        return organization;

    }

    public static Study createStudy(final String shortTitle, final String longTitle, final String assignedIdentifier) {
        Study study = new Study();
        study.setShortTitle(shortTitle);
        study.setLongTitle(longTitle);
        study.setAssignedIdentifier(assignedIdentifier);
        return study;
    }
}
