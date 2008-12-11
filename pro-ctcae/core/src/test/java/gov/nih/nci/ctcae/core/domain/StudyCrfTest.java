package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;
import gov.nih.nci.ctcae.core.Fixture;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class StudyCrfTest extends TestCase {

    public void testAddStudyParticipantCrf() {

        StudyCrf studyCrf = new StudyCrf();

        StudyParticipantCrf studyParticipantCrf = null;
        studyCrf.addStudyParticipantCrf(studyParticipantCrf);
        assertEquals(0, studyCrf.getStudyParticipantCrfs().size());

        studyParticipantCrf = new StudyParticipantCrf();
        studyCrf.addStudyParticipantCrf(studyParticipantCrf);

        assertEquals(1, studyCrf.getStudyParticipantCrfs().size());


    }

    public void testHashCodeAndEquals() {

        Study study = Fixture.createStudy("test", "test", "test");
        CRF crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");

        StudyCrf studyCrf1 = new StudyCrf();
        StudyCrf studyCrf2 = new StudyCrf();

        assertTrue(studyCrf1.equals(studyCrf2));

        studyCrf1.setStudy(study);
        studyCrf1.setCrf(crf);
        studyCrf2.setStudy(study);
        studyCrf2.setCrf(crf);

        assertTrue(studyCrf1.equals(studyCrf2));

    }

    public void testCopy() {

        Study study = Fixture.createStudy("test", "test", "test");
        CRF crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");

        StudyCrf studyCrf1 = new StudyCrf();
        studyCrf1.setStudy(study);
        studyCrf1.setCrf(crf);
        StudyCrf studyCrf2 = studyCrf1.getCopy();

        assertEquals(studyCrf1.getStudy(), studyCrf2.getStudy());
        assertEquals(studyCrf1.getCrf().getCrfVersion(), studyCrf2.getCrf().getCrfVersion());
        assertEquals(studyCrf1.getCrf().getDescription(), studyCrf2.getCrf().getDescription());
        assertEquals(studyCrf1.getCrf().getCrfItems(), studyCrf2.getCrf().getCrfItems());
        assertEquals(studyCrf1.getCrf().getStatus(), studyCrf2.getCrf().getStatus());


    }

}