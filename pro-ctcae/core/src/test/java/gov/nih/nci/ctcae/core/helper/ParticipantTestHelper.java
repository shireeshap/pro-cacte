package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class ParticipantTestHelper {

    private static ParticipantRepository participantRepository;
    private static CRFRepository crfRepository;
    private static GenericRepository genericRepository;

    private ParticipantTestHelper() {
    }

    public static void initialize() {
        participantRepository = TestDataManager.participantRepository;
        crfRepository = TestDataManager.crfRepository;
        genericRepository = TestDataManager.genericRepository;

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

        createNotifications(StudyTestHelper.getDefaultStudy().getCrfs().get(0));


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

    public static Participant findParticpantByUserName(String username) {
        ParticipantQuery query = new ParticipantQuery();
        query.filterByUsername(username);
        return participantRepository.findSingle(query);
    }

    private static void createNotifications(CRF crf) {
        createUserNotification(StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser(), crf);
        createUserNotification(StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser(), crf);
    }

    private static Notification createNotification() {
        Notification notification = new Notification();
        notification.setText("<html><head></head><body><table><tr><td><b></b>This is an auto-generated em" +
                "ail from PRO-CTCAE system.</td></tr><tr><td><b>Participant name: </b>test t" +
                "est[12-223]</td></tr><tr><td><b>Participant email: </b>Not specified</td></" +
                "tr><tr><td><b>Participant contact phone: </b>1231231234</td></tr><tr><td><b" +
                ">Study site: </b>Duke University Medical Center</td></tr><tr><td><b>Study: " +
                "</b>Study 5[-1001]</td></tr><tr><td><b>Research nurse: </b>cs2duke cs2duke<" +
                "/td></tr><tr><td><b>Treating physician: </b>cs1duke cs1duke</td></tr></tabl" +
                "e><br>This notification was triggered by following responses: <br><br><tabl" +
                "e border=3D\"1\"><tr><td><b>Symptom</b></td><td><b>Attribute</b></td><td><b>C" +
                "urrent visit (05/12/2009)</b></td><td><b>First visit (05/08/2009)</b></td><" +
                "td><b>Previous visit (05/11/2009)</b></td></tr><tr><td>Pounding or racing h" +
                "eartbeat (palpitations)</td><td>Frequency</td><td>Frequently</td><td>Occasi" +
                "onally</td><td>Almost Constantly</td></tr><tr><td>Pounding or racing heartb" +
                "eat (palpitations)</td><td>Severity</td><td>Severe</td><td>Severe</td><td>V" +
                "ery severe</td></tr><tr><td>Fatigue (tiredness, lack of energy)</td><td>Sev" +
                "erity</td><td>Severe</td><td>None</td><td>Very severe</td></tr><tr><td>Fati" +
                "gue (tiredness, lack of energy)</td><td>Interference</td><td>Very much</td>" +
                "<td></td><td>Very much</td></tr></table></body></html>");
        notification.setDate(new Date());
        return notification;
    }

    private static void createUserNotification(User user, CRF crf) {
        UserNotification userNotification = new UserNotification();
        userNotification.setNew(true);
        userNotification.setUser(user);
        userNotification.setStudy(crf.getStudy());
        userNotification.setUuid(UUID.randomUUID().toString());
        userNotification.setStudyParticipantCrfSchedule(ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0));
        userNotification.setParticipant(ParticipantTestHelper.findParticpantByUserName("charlie.boon"));
        Notification notification = createNotification();
        notification.addUserNotification(userNotification);
        genericRepository.save(notification);
    }


}