package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class ParticipantTestHelper {

    private static ParticipantRepository participantRepository;
    private static GenericRepository genericRepository;
    private static NotificationsEvaluationService notificationsEvaluationService;


    private ParticipantTestHelper() {
    }

    public static void initialize() {
        participantRepository = TestDataManager.participantRepository;
        genericRepository = TestDataManager.genericRepository;
        notificationsEvaluationService = new NotificationsEvaluationService();
        notificationsEvaluationService.setGenericRepository(genericRepository);
    }

    public static void createDefaultParticipants() throws Exception {

        StudySite ss1 = StudyTestHelper.getDefaultStudy().getLeadStudySite();
        Participant john = createParticipant("John", "Locke", "1-1", ss1, 0);
        Participant jack = createParticipant("Jack", "Shephard", "1-2", ss1, 0);
        Participant kate = createParticipant("Kate", "Austen", "1-3", ss1, 0);
        Participant sayid = createParticipant("Sayid", "Jarrah", "1-4", ss1, 0);
        Participant sun = createParticipant("Sun", "Kwon", "1-5", ss1, 0);
        Participant miles = createParticipant("Miles", "Straume", "1-6", ss1, 0);
        Participant jim = createParticipant("Jim", "Kwon", "1-7", ss1, 0);

        completeParticipantSchedule(john, ss1, true, AppMode.HOMEWEB);
        completeParticipantSchedule(jack, ss1, false, AppMode.HOMEWEB);
        completeParticipantSchedule(kate, ss1, false, AppMode.HOMEWEB);
        completeParticipantSchedule(sayid, ss1, false ,AppMode.HOMEWEB);
//        completeParticipantSchedule(sun, ss1, false);
//        completeParticipantSchedule(miles, ss1, false);
//        completeParticipantSchedule(jim, ss1, false);

        StudySite ss2 = StudyTestHelper.getDefaultStudy().getNonLeadStudySites().get(0);
        Participant james = createParticipant("James", "Sawyer", "2-1", ss2, 1);
        Participant charles = createParticipant("Charles", "Widmore", "2-2", ss2, 1);
        Participant hugo = createParticipant("Hugo", "Hurley", "2-3", ss2, 1);
        Participant daniel = createParticipant("Daniel", "Faraday", "2-4", ss2, 1);
        Participant ben = createParticipant("Ben", "Linus", "2-5", ss2, 1);
        Participant desmond = createParticipant("Desmond", "Hume", "2-6", ss2, 1);
        Participant juliet = createParticipant("Juliet", "Burke", "2-7", ss2, 1);

        completeParticipantSchedule(james, ss2, false, AppMode.HOMEWEB);
        completeParticipantSchedule(charles, ss2, false ,AppMode.HOMEWEB);
        completeParticipantSchedule(hugo, ss2, false ,AppMode.HOMEWEB);
        completeParticipantSchedule(daniel, ss2, false ,AppMode.HOMEWEB);
//        completeParticipantSchedule(ben, ss2, false);
//        completeParticipantSchedule(desmond, ss2, false);
//        completeParticipantSchedule(juliet, ss2, false);

        completeParticipantSchedule(findParticpantByUserName("Charlie.Boon"), StudyTestHelper.getDefaultStudy().getLeadStudySite(), false ,AppMode.HOMEWEB);
        createNotifications(StudyTestHelper.getDefaultStudy().getCrfs().get(0));

    }

    public static void completeParticipantSchedule(Participant participant, StudySite ss1, boolean executeRule, AppMode submissionMode) throws Exception {
        boolean emailSent = false;
        Random random = new Random();
        StudyParticipantCrf studyParticipantCrf = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        boolean maxDone = false;
        boolean doMax = false;
        for (int i = 0; i < 5; i++) {
            StudyParticipantCrfSchedule schedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(i);
            if (!schedule.isBaseline() && !maxDone) {
//                doMax = true;
                maxDone = true;
            } else {
                doMax = false;
            }
            for (StudyParticipantCrfItem studyParticipantCrfItem : schedule.getStudyParticipantCrfItems()) {
                List<ProCtcValidValue> validValues = (List<ProCtcValidValue>) studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getValidValues();
//                    studyParticipantCrfItem.setProCtcValidValue(validValues.get(i % validValues.size()));
                if (doMax) {
                    studyParticipantCrfItem.setProCtcValidValue(validValues.get(validValues.size() - 1));
                } else {
                    studyParticipantCrfItem.setProCtcValidValue(validValues.get(random.nextInt(validValues.size())));
                }

            }
            schedule.setStatus(CrfStatus.COMPLETED);
            schedule.setFormSubmissionMode(submissionMode);
            schedule.setCompletionDate(new Date());
            genericRepository.save(schedule);
            if (executeRule) {
                if (!emailSent) {
                    emailSent = notificationsEvaluationService.executeRules(schedule, ss1.getStudy().getCrfs().get(0), ss1);
                }
            }
        }
    }

    public static Participant createParticipant(String firstName, String lastName, String assignedIdentifier, StudySite studySite, int armIndex) throws ParseException {

        Participant participant = new Participant();
        firstTab_ParticipantDetails(participant, firstName, lastName, assignedIdentifier, studySite, armIndex);
        participant = participantRepository.save(participant);
        secondTab_ParticipantClinicalStaff(participant, studySite);
        participant = participantRepository.save(participant);
        assignCrfToParticipantAndCreateSchedules(participant, studySite);
        participant = participantRepository.save(participant);
        return participant;
    }

    public static Participant createIVRSParticipant(String firstName, String lastName, String assignedIdentifier, StudySite studySite, int armIndex, String userNumber,int pinNumber) throws ParseException {

        Participant participant = new Participant();
        firstTab_ParticipantDetails(participant, firstName, lastName, assignedIdentifier, studySite, armIndex);
        participant.setUserNumber(userNumber);
        participant.setPinNumber(pinNumber);
        participant = participantRepository.save(participant);
        secondTab_ParticipantClinicalStaff(participant, studySite);
        participant = participantRepository.save(participant);
        assignCrfToParticipantAndCreateSchedules(participant, studySite);
        participant = participantRepository.save(participant);
        return participant;
    }

    private static void firstTab_ParticipantDetails(Participant participant, String firstName, String lastName, String assignedIdentifier, StudySite studySite, int armIndex) {
        participant.setFirstName(firstName);
        participant.setLastName(lastName);
        participant.setBirthDate(new Date());
        participant.setGender(Gender.MALE.toString());
        participant.setAssignedIdentifier(assignedIdentifier);
        participant.setEmailAddress(firstName + "." + lastName + "@demo.com");
        participant.setPhoneNumber("212-311-3442");
        addUserToParticipant(participant);
        addStudyAssignmentToParticipant(participant, studySite, armIndex);
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

    private static void addStudyAssignmentToParticipant(Participant participant, StudySite studySite, int armIndex) {
        StudyParticipantAssignment spa = new StudyParticipantAssignment();
        spa.setStudyParticipantIdentifier("1234");
        spa.setStudySite(studySite);
        spa.setArm(studySite.getStudy().getArms().get(armIndex));
        spa.setStudyStartDate(new Date());
        participant.addStudyParticipantAssignment(spa);
    }

    private static void assignCrfToParticipantAndCreateSchedules(Participant participant, StudySite studySite) throws ParseException {
    	System.out.println("start assignCrfToParticipantAndCreateSchedules");
    	System.out.println("studySite.getStudy().getCrfs" + studySite.getStudy().getCrfs().size());
        if (studySite.getStudy().getCrfs().size() > 0) {
            CRF crf = studySite.getStudy().getCrfs().get(0);
            System.out.println("crf.status: " + crf.getStatus());
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
                StudyParticipantCrf spc = new StudyParticipantCrf();
                spc.setCrf(crf);
                spc.setArm(participant.getStudyParticipantAssignments().get(0).getArm());
                spc.setStartDate(new Date());
                participant.getStudyParticipantAssignments().get(0).addStudyParticipantCrf(spc);
                spc.createSchedules(false);
                //initialize the crfItems under every spcScheduleas crateSchedules does not crate crfItems
                for(StudyParticipantCrfSchedule spcSchedule: spc.getStudyParticipantCrfSchedules()){
                	spcSchedule.getStudyParticipantCrfItems();
                }
            }
        }
    	System.out.println("end assignCrfToParticipantAndCreateSchedules");
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
        ParticipantQuery query = new ParticipantQuery(false);
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