package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class AdminURLAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        allowedUrls.add(SEARCH_CLINICAL_STAFF_URL);
        allowedUrls.add(CREATE_CLINICAL_STAFF_URL);
        allowedUrls.add(ADD_ORGANIZATION_CLINICAL_STAFF_URL);
        allowedUrls.add(CREATE_CCA_URL);


        user = userRepository.loadUserByUsername(SYSTEM_ADMIN);


    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}