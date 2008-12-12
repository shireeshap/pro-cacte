package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Harsh Agarwal
 * @crated Dec 12, 2008
 */
public class StudyParticipantCrfScheduleTest extends TestCase {
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    public void testConstructor() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertNull(studyParticipantCrfSchedule.getId());
        assertNull(studyParticipantCrfSchedule.getDueDate());
        assertNull(studyParticipantCrfSchedule.getStartDate());
        assertEquals(CrfStatus.SCHEDULED, studyParticipantCrfSchedule.getStatus());
        assertNull(studyParticipantCrfSchedule.getStudyParticipantCrf());


    }

    public void testGetterAndSetter() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        Date d = new Date();
        studyParticipantCrfSchedule.setId(1);
        studyParticipantCrfSchedule.setDueDate(d);
        studyParticipantCrfSchedule.setStartDate(d);
        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        studyParticipantCrfSchedule.setStudyParticipantCrf(new StudyParticipantCrf());
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(null);

        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertEquals(d, studyParticipantCrfSchedule.getDueDate());
        assertEquals(d, studyParticipantCrfSchedule.getStartDate());
        assertEquals(CrfStatus.INPROGRESS, studyParticipantCrfSchedule.getStatus());
        assertNotNull(studyParticipantCrfSchedule.getStudyParticipantCrf());

        studyParticipantCrfSchedule.addStudyParticipantCrfItem(new StudyParticipantCrfItem());

        assertEquals(1, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());


    }

    public void testEqulasHashCode() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        StudyParticipantCrfSchedule studyParticipantCrfSchedule2 = new StudyParticipantCrfSchedule();
        Date d = new Date();
        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();

        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);

        studyParticipantCrfSchedule.setId(1);
        studyParticipantCrfSchedule.setDueDate(d);
        studyParticipantCrfSchedule.setStartDate(d);
        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);

        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));

        studyParticipantCrfSchedule2.setId(1);
        studyParticipantCrfSchedule2.setDueDate(d);
        studyParticipantCrfSchedule2.setStartDate(d);
        studyParticipantCrfSchedule2.setStatus(CrfStatus.INPROGRESS);
        studyParticipantCrfSchedule2.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfSchedule2.addStudyParticipantCrfItem(studyParticipantCrfItem);

        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


    }


}