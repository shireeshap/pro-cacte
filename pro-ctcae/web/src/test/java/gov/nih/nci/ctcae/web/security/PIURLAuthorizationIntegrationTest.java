package gov.nih.nci.ctcae.web.security;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class PIURLAuthorizationIntegrationTest extends LeadCRAURLAuthorizationIntegrationTest {


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        user = defaultStudy.getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getUser();


    }


}