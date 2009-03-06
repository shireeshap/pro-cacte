package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.CRFRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class LeadCRAMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {

    private User user;

    List<String> allowedMethods = new ArrayList<String>();


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }


    public void testAuthorizeUserForCRF() throws Exception {

        allowedMethods.add("save");

        unauthorizeMethods(crfRepository, CRFRepository.class, allowedMethods);


    }


}