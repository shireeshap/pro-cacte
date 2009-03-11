package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.*;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class SiteCRAMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {

    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }

    public void testAuthorizeUserForParticipant() throws Exception {
        List<String> allowedMethods = allowedMethodsMap.get(ParticipantRepository.class);

        allowedMethods.add(SEARCH_PARTICIPANT_METHOD);
        allowedMethods.add(SEARCH_PARTICIPANT_BY_ID_METHOD);
        allowedMethods.add(SEARCH_SINGLE_PARTICIPANT_METHOD);
        allowedMethods.add(CREATE_PARTICIPANT_METHOD);
        allowedMethods.add(ADD_NOTIFICATION_CLINICAL_STAFF_METHOD);
        allowedMethods.add(PARTICIPANT_DISPLAY_STUDY_SITES_METHOD);
        allowedMethods.add(EDIT_PARTICIPANT_METHOD);

        authorizeAndUnAuthorizeMethods(participantRepository, ParticipantRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForClinicalStaff() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(ClinicalStaffRepository.class);
        allowedMethods.add(SEARCH_CLINICAL_STAFF_METHOD);

        allowedMethods.add(SEARCH_CLINICAL_STAFF_BY_SS_ROLE_METHOD);
        allowedMethods.add(SEARCH_CLINICAL_STAFF_METHOD_BY_ID);
        allowedMethods.add(SEARCH_SINGLE_CLINICAL_STAFF_METHOD);

        allowedMethods.add(ADD_ORGANIZATION_CLINICAL_STAFF_METHOD);
        allowedMethods.add(CREATE_CLINICAL_STAFF_METHOD);
        allowedMethods.add(EDIT_CLINICAL_STAFF_METHOD);

        authorizeAndUnAuthorizeMethods(clinicalStaffRepository, ClinicalStaffRepository.class, allowedMethods);


    }

    public void testAuthorizeUserForOrganizationClinicalStaff() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(ClinicalStaffRepository.class);

        allowedMethods.add(SEARCH_CLINICAL_STAFF_BY_SS_METHOD);

        authorizeAndUnAuthorizeMethods(organizationClinicalStaffRepository, OrganizationClinicalStaffRepository.class, allowedMethods);


    }

    public void testAuthorizeUserForCRF() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(CRFRepository.class);

        authorizeAndUnAuthorizeMethods(crfRepository, CRFRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForStudy() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(StudyRepository.class);

        allowedMethods.add(SEARCH_STUDY_METHOD);
        allowedMethods.add(SEARCH_SINGLE_STUDY_METHOD);
        allowedMethods.add(SEARCH_STUDY_BY_ID_METHOD);
        allowedMethods.add(ADD_STUDY_SITE_CLINICAL_STAFF_METHOD);


        authorizeAndUnAuthorizeMethods(studyRepository, StudyRepository.class, allowedMethods);


    }

    public void testAuthorizeUserForStudyParticipantAssignment() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(StudyParticipantAssignmentRepository.class);
        allowedMethods.add(SCHEDULE_CRF_METHOD);
        authorizeAndUnAuthorizeMethods(studyParticipantAssignmentRepository, StudyParticipantAssignmentRepository.class, allowedMethods);

        commitAndStartNewTransaction();

    }


}