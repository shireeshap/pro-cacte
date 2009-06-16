package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;

import java.text.ParseException;
import java.util.Date;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class ParticipantTestHelper {

    private static ParticipantRepository participantRepository;
    private static CRFRepository crfRepository;

    private ParticipantTestHelper() {
    }

    public static void initialize() {
        participantRepository = TestDataManager.participantRepository;
        crfRepository = TestDataManager.crfRepository;
    }

    public static void createDefaultParticipants() throws ParseException {

        StudySite ss1 = StudyTestHelper.getDefaultStudy().getStudySites().get(0);
        createParticipant("John", "Locke", "1-1", ss1);
        createParticipant("Jack", "Shepard", "1-2", ss1);
        createParticipant("Kate", "Austen", "1-3", ss1);

        StudySite ss2 = StudyTestHelper.getDefaultStudy().getStudySites().get(1);
        createParticipant("Tom", "Sawyer", "2-1", ss2);
        createParticipant("Charles", "Widmore", "2-2", ss2);
        createParticipant("Hugo", "Hurley", "2-3", ss2);

    }

    public static void createParticipant(String firstName, String lastName, String assignedIdentifier, StudySite studySite) throws ParseException {

        Participant participant = new Participant();
        firstTab_ParticipantDetails(participant, firstName, lastName, assignedIdentifier, studySite);
        secondTab_ParticipantClinicalStaff(participant, studySite);
        assignCrfToParticipantAndCreateSchedules(participant, studySite);
        participantRepository.save(participant);
    }

    private static void firstTab_ParticipantDetails(Participant participant, String firstName, String lastName, String assignedIdentifier, StudySite studySite) {
        participant.setFirstName(firstName);
        participant.setLastName(lastName);
        participant.setBirthDate(new Date());
        participant.setGender(Gender.MALE.toString());
        participant.setAssignedIdentifier(assignedIdentifier);
        participant.setEmailAddress(firstName + "." + lastName + "@demo.com");
        participant.setPhoneNumber("212-311-3442");
        addUserToParticipant(participant);
        addStudyAssignmentToParticipant(participant, studySite);
    }


    private static void secondTab_ParticipantClinicalStaff(Participant participant, StudySite studySite) {
        StudyParticipantAssignment spa = participant.getStudyParticipantAssignments().get(0);

        StudyParticipantClinicalStaff primaryTreatingPhysisican = new StudyParticipantClinicalStaff();
        primaryTreatingPhysisican.setStudyOrganizationClinicalStaff(getStaffByRole(studySite, Role.TREATING_PHYSICIAN));
        primaryTreatingPhysisican.setPrimary(true);
        spa.addStudyParticipantClinicalStaff(primaryTreatingPhysisican);

        StudyParticipantClinicalStaff primaryNurse = new StudyParticipantClinicalStaff();
        primaryNurse.setStudyOrganizationClinicalStaff(getStaffByRole(studySite, Role.NURSE));
        primaryNurse.setPrimary(true);
        spa.addStudyParticipantClinicalStaff(primaryNurse);
    }


    private static StudyOrganizationClinicalStaff getStaffByRole(StudySite studySite, Role role) {
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studySite.getStudyOrganizationClinicalStaffs()) {
            if (studyOrganizationClinicalStaff.getRole().equals(role)) {
                return studyOrganizationClinicalStaff;
            }
        }
        return null;
    }

    private static void addStudyAssignmentToParticipant(Participant participant, StudySite studySite) {
        StudyParticipantAssignment spa = new StudyParticipantAssignment();
        spa.setStudyParticipantIdentifier("1234");
        spa.setStudySite(studySite);
        participant.addStudyParticipantAssignment(spa);
    }

    private static void assignCrfToParticipantAndCreateSchedules(Participant participant, StudySite studySite) throws ParseException {
        if (studySite.getStudy().getCrfs().size() > 0) {
            CRF crf = studySite.getStudy().getCrfs().get(0);
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
                StudyParticipantCrf spc = new StudyParticipantCrf();
                spc.setCrf(crf);
                spc.setStartDate(new Date());
                participant.getStudyParticipantAssignments().get(0).addStudyParticipantCrf(spc);
                crfRepository.generateSchedulesFromCrfCalendar(crf, spc);
            }
        }
    }


    private static void addUserToParticipant(Participant participant) {
        participant.setUser(new User());
        participant.getUser().setUsername(participant.getFirstName() + "." + participant.getLastName());
        participant.getUser().setPassword(TestDataManager.DEFAULT_PASSWORD);
    }


    public static Participant getDefaultParticipant() {
        return findParticpantByUserName("John.Locke");
    }

    private static Participant findParticpantByUserName(String username) {
        ParticipantQuery query = new ParticipantQuery();
        query.filterByUsername(username);
        return participantRepository.findSingle(query);
    }

}