package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.*;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ResearchNurseMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {

    private User user;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.NURSE).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        login(user);

    }

    public void testAuthorizeUserForParticipant() throws Exception {
        List<String> allowedMethods = allowedMethodsMap.get(ParticipantRepository.class);

        allowedMethods.add(SEARCH_PARTICIPANT_METHOD);
        allowedMethods.add(SEARCH_PARTICIPANT_BY_ID_METHOD);
        allowedMethods.add(SEARCH_SINGLE_PARTICIPANT_METHOD);


        authorizeAndUnAuthorizeMethods(participantRepository, ParticipantRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForClinicalStaff() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(ClinicalStaffRepository.class);

        ///must remove it as soon as method security for save method get implemented
        allowedMethods.add(CREATE_CLINICAL_STAFF_METHOD);
        authorizeAndUnAuthorizeMethods(clinicalStaffRepository, ClinicalStaffRepository.class, allowedMethods);


    }


    public void testAuthorizeUserForCRF() throws Exception {

        List<String> allowedMethods = allowedMethodsMap.get(CRFRepository.class);

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

        commitAndStartNewTransaction();

    }


}