package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.*;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class TreatingPhysicanMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {

    private User user;

    @Override
    protected Role getRole() {
        return Role.TREATING_PHYSICIAN;


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(getRole()).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }

    public void testAuthorizeUserForParticipant() throws Exception {
        List<String> allowedMethods = allowedMethodsMap.get(ParticipantRepository.class);

        allowedMethods.add(SEARCH_PARTICIPANT_METHOD);
        allowedMethods.add(SEARCH_PARTICIPANT_BY_ID_METHOD);
        allowedMethods.add(SEARCH_SINGLE_PARTICIPANT_METHOD);
        allowedMethods.add(SEARCH_PARTICIPANT_BY_STUDYSITEID_METHOD);
        allowedMethods.add(SEARCH_PARTICIPANT_BY_STUDYID_METHOD);


        authorizeAndUnAuthorizeMethods(participantRepository, ParticipantRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForClinicalStaff() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(ClinicalStaffRepository.class);

        authorizeAndUnAuthorizeMethods(clinicalStaffRepository, ClinicalStaffRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForCRF() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(CRFRepository.class);
        allowedMethods.add(SEARCH_SINGLE_FORM_METHOD);
        allowedMethods.add(FIND_BY_ID_METHOD);
        allowedMethods.add(FIND_METHOD);
        authorizeAndUnAuthorizeMethods(crfRepository, CRFRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForStudy() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(StudyRepository.class);

        authorizeAndUnAuthorizeMethods(studyRepository, StudyRepository.class, allowedMethods);


    }

    public void testAuthorizeUserForStudyParticipantAssignment() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(StudyParticipantAssignmentRepository.class);
        allowedMethods.add(SCHEDULE_CRF_METHOD);
        allowedMethods.add(FIND_METHOD);
        allowedMethods.add(FIND_BY_ID_METHOD);
        allowedMethods.add(FIND_SINGLE);

        authorizeAndUnAuthorizeMethods(studyParticipantAssignmentRepository, StudyParticipantAssignmentRepository.class, allowedMethods);


    }


}