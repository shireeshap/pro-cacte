package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Suneel Allareddy
 * @since Jan 10, 2010
 */
public class StudyParticipantCrfScheduleNotificationTest extends TestCase {
    private StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification;

    public void testConstructor() {
        studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
        assertNull(studyParticipantCrfScheduleNotification.getId());
        assertNotNull(studyParticipantCrfScheduleNotification.getCreationDate());
        assertNull(studyParticipantCrfScheduleNotification.getCompletionDate());
        assertFalse(studyParticipantCrfScheduleNotification.isMailSent());
        assertEquals(CrfStatus.SCHEDULED, studyParticipantCrfScheduleNotification.getStatus());
        assertNull(studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule());
    }

    public void testGetterAndSetter() {
        studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
        Date d = new Date();
        studyParticipantCrfScheduleNotification.setId(1);
        StudyParticipantCrfSchedule spcschedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfScheduleNotification.setCompletionDate(d);
        studyParticipantCrfScheduleNotification.setCreationDate(d);
        studyParticipantCrfScheduleNotification.setMailSent(true);
        studyParticipantCrfScheduleNotification.setStatus(CrfStatus.COMPLETED);
        studyParticipantCrfScheduleNotification.setStudyParticipantCrfSchedule(spcschedule);

        assertEquals(0, studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule().getStudyParticipantCrfItems().size());
        assertEquals(0, studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule().getStudyParticipantCrfItems().size());
        assertEquals(d, studyParticipantCrfScheduleNotification.getCreationDate());
        assertEquals(d, studyParticipantCrfScheduleNotification.getCompletionDate());
        assertTrue(studyParticipantCrfScheduleNotification.isMailSent());
        assertEquals(CrfStatus.COMPLETED, studyParticipantCrfScheduleNotification.getStatus());
        assertNotNull(studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule());

        studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule().addStudyParticipantCrfItem(new StudyParticipantCrfItem());

        assertEquals(1, studyParticipantCrfScheduleNotification.getStudyParticipantCrfSchedule().getStudyParticipantCrfItems().size());

    }

    public void testEqualsHashCode() {
        studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
        StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification2 = new StudyParticipantCrfScheduleNotification();
        Date d = new Date();
        StudyParticipantCrfSchedule spcschedule = new StudyParticipantCrfSchedule();

        studyParticipantCrfScheduleNotification.setStudyParticipantCrfSchedule(spcschedule);
        studyParticipantCrfScheduleNotification2.setStudyParticipantCrfSchedule(spcschedule);

        assertEquals(studyParticipantCrfScheduleNotification.hashCode(), studyParticipantCrfScheduleNotification.hashCode());
        assertEquals(studyParticipantCrfScheduleNotification, studyParticipantCrfScheduleNotification);

        studyParticipantCrfScheduleNotification.setCreationDate(d);
        assertTrue(studyParticipantCrfScheduleNotification.equals(studyParticipantCrfScheduleNotification2));
        studyParticipantCrfScheduleNotification2.setCreationDate(d);
        assertEquals(studyParticipantCrfScheduleNotification.hashCode(), studyParticipantCrfScheduleNotification2.hashCode());
        assertEquals(studyParticipantCrfScheduleNotification, studyParticipantCrfScheduleNotification2);


        studyParticipantCrfScheduleNotification.setCompletionDate(d);
        assertFalse(studyParticipantCrfScheduleNotification.equals(studyParticipantCrfScheduleNotification2));
        studyParticipantCrfScheduleNotification2.setCompletionDate(d);
        assertEquals(studyParticipantCrfScheduleNotification.hashCode(), studyParticipantCrfScheduleNotification2.hashCode());
        assertEquals(studyParticipantCrfScheduleNotification, studyParticipantCrfScheduleNotification2);

        studyParticipantCrfScheduleNotification.setStatus(CrfStatus.COMPLETED);
        assertFalse(studyParticipantCrfScheduleNotification.equals(studyParticipantCrfScheduleNotification2));
        studyParticipantCrfScheduleNotification2.setStatus(CrfStatus.COMPLETED);
        assertEquals(studyParticipantCrfScheduleNotification.hashCode(), studyParticipantCrfScheduleNotification2.hashCode());
        assertEquals(studyParticipantCrfScheduleNotification, studyParticipantCrfScheduleNotification2);

        studyParticipantCrfScheduleNotification.setMailSent(true);
        assertFalse(studyParticipantCrfScheduleNotification.equals(studyParticipantCrfScheduleNotification2));
        studyParticipantCrfScheduleNotification2.setMailSent(true);
        assertEquals(studyParticipantCrfScheduleNotification.hashCode(), studyParticipantCrfScheduleNotification2.hashCode());
        assertEquals(studyParticipantCrfScheduleNotification, studyParticipantCrfScheduleNotification2);

        
    }


}