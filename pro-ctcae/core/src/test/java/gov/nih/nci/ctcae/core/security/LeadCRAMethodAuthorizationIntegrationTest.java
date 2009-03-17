package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.repository.StudyRepository;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class LeadCRAMethodAuthorizationIntegrationTest extends AbstractPIAndLeadCRAMethodAuthorizationIntegrationTestCase {

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }

    public void testAuthorizeUserForStudy() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(StudyRepository.class);

        allowedMethods.add(SEARCH_STUDY_METHOD);
        allowedMethods.add(EDIT_STUDY_METHOD);
        allowedMethods.add(FIND_SINGLE);
        allowedMethods.add(SEARCH_STUDY_BY_ID_METHOD);
        allowedMethods.add(CREATE_STUDY_METHOD);
        allowedMethods.add(ADD_STUDY_SITE_CLINICAL_STAFF_METHOD);


        authorizeAndUnAuthorizeMethods(studyRepository, StudyRepository.class, allowedMethods);


    }


}