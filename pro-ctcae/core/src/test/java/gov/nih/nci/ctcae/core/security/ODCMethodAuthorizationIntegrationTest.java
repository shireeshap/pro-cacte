package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.CRFRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ODCMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {
    private User user;

    List<String> allowedMethods = new ArrayList<String>();

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }


    public void testODCMustNotAccessAnyMethodsOfCRF() throws Exception {

        unauthorizeMethods(crfRepository, CRFRepository.class, allowedMethods);


    }


}
