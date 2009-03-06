package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.study.EmptyStudyTab;
import gov.nih.nci.ctcae.web.study.StudyDetailsTab;
import gov.nih.nci.ctcae.web.study.StudySiteClinicalStaffTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class LeadCRAAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        allowedUrls.add(MANAGE_FORM_URL);

        allowedUrls.add(ADD_QUESTION_TO_FORM_URL);
        allowedUrls.add(ADD_CRF_COMPONENT_URL);
        allowedUrls.add(ALL_CONDITIONS_URL);
        allowedUrls.add(ADD_CONDITIONAL_QUESTION_URL);
        allowedUrls.add(REMOVE_CONDITIONS_URL);
        allowedUrls.add(SET_NAME_URL);
        allowedUrls.add(SUBMIT_FORM_URL);

        allowedUrls.add(CREATE_FORM_URL);
        allowedUrls.add(EDIT_FORM_URL);
        allowedUrls.add(RELEASE_FORM_URL);
        allowedUrls.add(VERSION_FORM_URL);


        allowedUrls.add(EDIT_STUDY_URL);
        allowedUrls.add(SEARCH_STUDY_URL);
        allowedUrls.add(STUDY_URL);
        allowedUrls.add(ADD_STUDY_SITE_CLINICAL_STAFF_URL);

        allowedUrls.add(SEARCH_CLINICAL_STAFF_URL);
        allowedUrls.add(CREATE_CLINICAL_STAFF_URL);
        allowedUrls.add(ADD_ORGANIZATION_CLINICAL_STAFF_URL);


        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();

        allowedTabs.add(new EmptyStudyTab());
        allowedTabs.add(new StudySiteClinicalStaffTab());
        allowedTabs.add(new StudyDetailsTab());
        allowedTabs.add(new EmptyStudyTab());


    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}