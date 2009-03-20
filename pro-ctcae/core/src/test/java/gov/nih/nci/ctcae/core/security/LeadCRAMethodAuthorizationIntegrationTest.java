package gov.nih.nci.ctcae.core.security;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class LeadCRAMethodAuthorizationIntegrationTest extends AbstractPIAndLeadCRAMethodAuthorizationIntegrationTestCase {

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }


}