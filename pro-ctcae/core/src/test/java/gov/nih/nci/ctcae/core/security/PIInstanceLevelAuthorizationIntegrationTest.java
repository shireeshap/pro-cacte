package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class PIInstanceLevelAuthorizationIntegrationTest extends AbstractPIAndLeadCRAInstanceLevelAuthorizationIntegrationTestCase {

    @Override
    protected Role getRole() {
        return Role.PI;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        anotherUser = study1.getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", anotherUser);

        user = defaultStudy.getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getUser();


    }
}