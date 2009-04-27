package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class ParticipantURLAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    protected User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        allowedUrls.add(PARTICIPANT_INBOX_URL);
        allowedUrls.add(SUBMIT_FORM_URL);

        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();

    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}