package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.Fixture;
import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Harsh Agarwal
 * @since Dec 12, 2008
 */
public class StudyParticipantCrfTest extends TestCase {
    private StudyParticipantCrf studyParticipantCrf;

    public void testConstructor() {
        studyParticipantCrf = new StudyParticipantCrf();
        assertNull(studyParticipantCrf.getId());
        assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertNull(studyParticipantCrf.getCrf());
        assertNull(studyParticipantCrf.getStudyParticipantAssignment());

    }

    public void testGetterAndSetter() {
        studyParticipantCrf = new StudyParticipantCrf();

        CRF crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
        crf.addCrfPage(new CRFPage());
        crf.addCrfPage(new CRFPage());

        Date d = new Date();


        studyParticipantCrf.setId(1);
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setStudyParticipantAssignment(new StudyParticipantAssignment());
        //studyParticipantCrf.addStudyParticipantCrfSchedule(null);

        assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertEquals(new Integer(1), studyParticipantCrf.getId());
        assertEquals(crf, studyParticipantCrf.getCrf());
        assertNotNull(studyParticipantCrf.getStudyParticipantAssignment());

        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule(), crf);

        assertEquals(1, studyParticipantCrf.getStudyParticipantCrfSchedules().size());


    }


}