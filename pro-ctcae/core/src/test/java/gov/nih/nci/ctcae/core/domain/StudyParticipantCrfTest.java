package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @since Dec 12, 2008
 */
public class StudyParticipantCrfTest extends TestDataManager {
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


        studyParticipantCrf.setId(1);
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setStudyParticipantAssignment(new StudyParticipantAssignment());
        assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertEquals(new Integer(1), studyParticipantCrf.getId());
        assertEquals(crf, studyParticipantCrf.getCrf());
        assertNotNull(studyParticipantCrf.getStudyParticipantAssignment());

        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());

        assertEquals(1, studyParticipantCrf.getStudyParticipantCrfSchedules().size());


    }

    public void testGetCrfByStatus() {
        Study s = StudyTestHelper.getDefaultStudy();
        StudyParticipantCrf studyParticipantCrf = s.getLeadStudySite().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        studyParticipantCrf = genericRepository.findById(StudyParticipantCrf.class, studyParticipantCrf.getId());
        List<StudyParticipantCrfSchedule> schedulesS = studyParticipantCrf.getStudyParticipantCrfSchedulesByStatus(CrfStatus.SCHEDULED);
        List<StudyParticipantCrfSchedule> schedulesC = studyParticipantCrf.getStudyParticipantCrfSchedulesByStatus(CrfStatus.COMPLETED);
        assertNotNull(schedulesS);
        assertNotNull(schedulesC);
        assertEquals(4, schedulesC.size());
        assertEquals(11, schedulesS.size());
    }


}