package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.study.EmptyStudyTab;
import gov.nih.nci.ctcae.web.study.StudyClinicalStaffTab;
import gov.nih.nci.ctcae.web.study.StudyDetailsTab;
import gov.nih.nci.ctcae.web.study.StudySitesTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class CCAAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    List<String> allowedUrls = new ArrayList();

    List<SecuredTab> allowedTabs = new ArrayList();
    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        allowedUrls.add(SEARCH_STUDY_URL);
        allowedUrls.add(EDIT_STUDY_URL);
        allowedUrls.add(STUDY_URL);
        allowedUrls.add(CREATE_STUDY_URL);
        allowedUrls.add(ADD_STUDY_SITE_URL);

        allowedUrls.add(SEARCH_CLINICAL_STAFF_URL);
        allowedUrls.add(ADD_ORGANIZATION_CLINICAL_STAFF_URL);
        allowedUrls.add(CREATE_CLINICAL_STAFF_URL);


        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.CCA).getOrganizationClinicalStaff().getClinicalStaff().getUser();

        allowedTabs.add(new EmptyStudyTab());
        allowedTabs.add(new StudyDetailsTab());
        allowedTabs.add(new StudyClinicalStaffTab());
        allowedTabs.add(new StudySitesTab());


    }

    public void testAuthorizeUrl() throws Exception {

        authorizeUser(user, allowedUrls);
    }

    public void testAuthorizeForTab() throws Exception {

        authorizeUserForTab(user, allowedTabs);
    }


}