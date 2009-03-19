package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.participant.ScheduleCrfTab;
import gov.nih.nci.ctcae.web.participant.SelectStudyParticipantTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class ResearchNurseAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        allowedUrls.add(SEARCH_PARTICIPANT_URL);


        allowedUrls.add(SCHEDULE_CRF_URL);
        allowedUrls.add(ADD_CRF_SCHEDULE_URL);
        allowedUrls.add(DISPLAY_CALENDAR_URL);
        allowedUrls.add(MONITOR_FORM_URL);
        allowedUrls.add(MONITOR_FORM_STATUS_URL);
        allowedUrls.add(MANAGE_FORM_URL);


        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.TREATING_PHYSICIAN).getOrganizationClinicalStaff().getClinicalStaff().getUser();

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