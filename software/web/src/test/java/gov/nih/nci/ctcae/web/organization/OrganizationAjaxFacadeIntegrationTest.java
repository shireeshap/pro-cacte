package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class OrganizationAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private OrganizationAjaxFacade organizationAjaxFacade;


    public void testMatchOrganizationsByGivingNciCode() {

        List<Organization> organizations = organizationAjaxFacade.matchOrganization("nci");
        assertFalse("must find atleat 1 organization", organizations.isEmpty());
        for (Organization organization : organizations) {
            assertTrue("search in both nci institue code and in name", organization.getName().toLowerCase().contains("nci")
                    || organization.getNciInstituteCode().toLowerCase().contains("nci"));
        }

    }

    public void testMatchOrganizationsByGivingName() {

        List<Organization> organizations = organizationAjaxFacade.matchOrganization("national");
        assertFalse("must find atleat 1 organization", organizations.isEmpty());
        for (Organization organization : organizations) {
            assertTrue("search in both nci institue code and in name", organization.getName().toLowerCase().contains("national")
                    || organization.getNciInstituteCode().toLowerCase().contains("national"));
        }

    }
    public void testMatchOrganizationsStudyId() {

        List<StudyOrganization> organizations = organizationAjaxFacade.matchOrganizationByStudyId("Duke",StudyTestHelper.getDefaultStudy().getId());
        assertEquals(1, organizations.size());
        assertTrue( organizations.get(0).getDisplayName().contains("Duke"));

    }




    @Required
    public void setOrganizationAjaxFacade(OrganizationAjaxFacade organizationAjaxFacade) {
        this.organizationAjaxFacade = organizationAjaxFacade;
    }
}
