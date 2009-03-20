package gov.nih.nci.ctcae.core.security;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class PIMethodAuthorizationIntegrationTest extends AbstractPIAndLeadCRAMethodAuthorizationIntegrationTestCase {


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }


}