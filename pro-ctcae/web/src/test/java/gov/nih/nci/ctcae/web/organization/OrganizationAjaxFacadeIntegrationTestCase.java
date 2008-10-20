package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class OrganizationAjaxFacadeIntegrationTestCase extends AbstractWebIntegrationTestCase {

    private OrganizationAjaxFacade organizationAjaxFacade;
    private Organization nci;
    private OrganizationRepository organizationRepository;
    private Organization duke;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        nci = Fixture.createOrganization("National Cancer Institute", "NCI");


        nci = organizationRepository.save(nci);

        duke = Fixture.createOrganization("DUKE", "DUKE");

        duke = organizationRepository.save(duke);


    }

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

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setOrganizationAjaxFacade(OrganizationAjaxFacade organizationAjaxFacade) {
        this.organizationAjaxFacade = organizationAjaxFacade;
    }
}
