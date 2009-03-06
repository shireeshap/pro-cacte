package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.participant.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class SitePIAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        allowedUrls.add(ADD_NOTIFICATION_CLINICAL_STAFF_URL);
        allowedUrls.add(CREATE_PARTICIPANT_URL);

        allowedUrls.add(SEARCH_PARTICIPANT_URL);
        allowedUrls.add(DISPLAY_CALENDAR_URL);
        allowedUrls.add(PARTICIPANT_DISPLAY_STUDY_SITES_URL);


        allowedUrls.add(SCHEDULE_CRF_URL);
        allowedUrls.add(ADD_CRF_SCHEDULE_URL);

        allowedUrls.add(EDIT_PARTICIPANT_URL);


        allowedUrls.add(SEARCH_CLINICAL_STAFF_URL);

        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_PI).getOrganizationClinicalStaff().getClinicalStaff().getUser();

        allowedTabs.add(new ParticipantClinicalStaffTab());
        allowedTabs.add(new ParticipantDetailsTab());
        allowedTabs.add(new ParticipantReviewTab());
        allowedTabs.add(new ScheduleCrfTab());
        allowedTabs.add(new SelectStudyParticipantTab());


    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}