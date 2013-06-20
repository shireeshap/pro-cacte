package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleNotificationQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;

import java.util.Date;
import java.util.List;

/**
 * @author Suneel Allareddy
 * @since Dec 20, 2010
 */

public class StudyParticipantCrfScheduleNotificationIntegrationTest extends TestDataManager {
    private StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification;
    private Participant participant;

    private void deleteExistingParticipant() {
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("john1.locke1");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("john1.locke1");
        genericRepository.delete(genericRepository.findSingle(uq));
        commitAndStartNewTransaction();
    }
    public void testCreateAndUpdateStudyParticipantCrfScheduleNotification() {
        StudySite ss1 = StudyTestHelper.getDefaultStudy().getLeadStudySite();
        Date date = new Date();
        StudyParticipantCrfScheduleNotification notif = new StudyParticipantCrfScheduleNotification();
        notif.setCreationDate(date);
        StudyParticipantCrfSchedule schedule = null;
        try {
            participant = ParticipantTestHelper.createParticipant("John1", "Locke1", "1-1", ss1, 0);
            StudyParticipantAssignment studyParticipantassignment = participant.getStudyParticipantAssignments().get(0);
            StudyParticipantCrf studyParticipantCrf = studyParticipantassignment.getStudyParticipantCrfs().get(0);
            schedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
            schedule.setStudyParticipantCrfScheduleNotification(notif);
            notif.setStudyParticipantCrfSchedule(schedule);
            //creating new record in notification mails
            ParticipantTestHelper.completeParticipantSchedule(participant, ss1, false, AppMode.IVRS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notif = genericRepository.findById(StudyParticipantCrfScheduleNotification.class, schedule.getStudyParticipantCrfScheduleNotification().getId());

        assertNotNull(notif.getId());
        assertEquals(date, notif.getCreationDate());
        assertEquals(CrfStatus.SCHEDULED, notif.getStatus());
        assertFalse(notif.isMailSent());
        assertNotNull(notif.getStudyParticipantCrfSchedule());
        assertNull(notif.getCompletionDate());


        //update the record
        notif.setCompletionDate(date);
        notif.setStatus(CrfStatus.COMPLETED);
        notif = genericRepository.save(notif);

        assertNotNull(notif.getId());
        assertEquals(date, notif.getCreationDate());
        assertEquals(CrfStatus.COMPLETED, notif.getStatus());
        assertFalse(notif.isMailSent());
        assertNotNull(notif.getStudyParticipantCrfSchedule());
        assertEquals(date, notif.getCompletionDate());
        commitAndStartNewTransaction();
        deleteExistingParticipant();
    }

    public void testRetrieveStudyParticipantCrfScheduleNotification() {
        StudySite ss1 = StudyTestHelper.getDefaultStudy().getLeadStudySite();
        Date date = new Date();
        StudyParticipantCrfScheduleNotification notif = new StudyParticipantCrfScheduleNotification();
        notif.setCreationDate(date);
        StudyParticipantCrfSchedule schedule = null;
        try {
            participant = ParticipantTestHelper.createParticipant("John1", "Locke1", "1-1", ss1, 0);
            StudyParticipantAssignment studyParticipantassignment = participant.getStudyParticipantAssignments().get(0);
            StudyParticipantCrf studyParticipantCrf = studyParticipantassignment.getStudyParticipantCrfs().get(0);
            schedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
            schedule.setStudyParticipantCrfScheduleNotification(notif);
            notif.setStudyParticipantCrfSchedule(schedule);
            //creating new record in notification mails
            ParticipantTestHelper.completeParticipantSchedule(participant, ss1, false, AppMode.IVRS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notif = genericRepository.findById(StudyParticipantCrfScheduleNotification.class, schedule.getStudyParticipantCrfScheduleNotification().getId());

        assertNotNull(notif.getId());
        assertEquals(date, notif.getCreationDate());
        assertEquals(CrfStatus.SCHEDULED, notif.getStatus());
        assertFalse(notif.isMailSent());
        assertNotNull(notif.getStudyParticipantCrfSchedule());
        assertNull(notif.getCompletionDate());

        //getting all the list of notifications with SCHEDULED status
        StudyParticipantCrfScheduleNotificationQuery query = new StudyParticipantCrfScheduleNotificationQuery();
        query.filterByStatus(CrfStatus.SCHEDULED);
        List<StudyParticipantCrfScheduleNotification> schedNotifications = genericRepository.find(query);
        notif = schedNotifications.get(0);

        assertNotNull(notif.getId());
        assertEquals(date, notif.getCreationDate());
        assertEquals(CrfStatus.SCHEDULED, notif.getStatus());
        assertFalse(notif.isMailSent());
        assertNotNull(notif.getStudyParticipantCrfSchedule());
        assertNull(notif.getCompletionDate());
        commitAndStartNewTransaction();
        deleteExistingParticipant();
    }

}