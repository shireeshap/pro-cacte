package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.study.EmptyStudyTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class ODCUrlAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        allowedUrls.add(SEARCH_STUDY_URL);
        allowedUrls.add(EDIT_STUDY_URL);

        allowedUrls.add(SEARCH_CLINICAL_STAFF_URL);

        allowedUrls.add(SEARCH_PARTICIPANT_URL);
        allowedUrls.add(MONITOR_FORM_URL);
        allowedUrls.add(MONITOR_FORM_STATUS_URL);
        allowedUrls.add(SHOW_COMPLETED_CRF_URL);
        allowedUrls.add(REPORTS_URL);


        user = defaultStudy.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser();

        allowedTabs.add(new EmptyStudyTab());


    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}