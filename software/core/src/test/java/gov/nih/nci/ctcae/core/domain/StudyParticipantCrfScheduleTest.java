package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Harsh Agarwal
 * @since Dec 12, 2008
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
        StudyParticipantCrf crf = new StudyParticipantCrf();
        crf.setStartDate(new Date());
        studyParticipantCrfSchedule.setStudyParticipantCrf(crf);
        studyParticipantCrfSchedule.setDueDate(d);
        studyParticipantCrfSchedule.setStartDate(d);
        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(null);

        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertEquals(d, studyParticipantCrfSchedule.getDueDate());
        assertEquals(d, studyParticipantCrfSchedule.getStartDate());
        assertEquals(CrfStatus.INPROGRESS, studyParticipantCrfSchedule.getStatus());
        assertNotNull(studyParticipantCrfSchedule.getStudyParticipantCrf());

        studyParticipantCrfSchedule.addStudyParticipantCrfItem(new StudyParticipantCrfItem());

        assertEquals(1, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());


    }

    public void testEqualsHashCode() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        StudyParticipantCrfSchedule studyParticipantCrfSchedule2 = new StudyParticipantCrfSchedule();
        Date d = new Date();
        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStartDate(new Date());
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfSchedule2.setStudyParticipantCrf(studyParticipantCrf);

        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);

        studyParticipantCrfSchedule.setDueDate(d);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setDueDate(d);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        studyParticipantCrfSchedule.setStartDate(d);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setStartDate(d);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);

        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setStatus(CrfStatus.INPROGRESS);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);
        assertEquals("must not consider crf item", studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals("must not consider crf item", studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


    }


}