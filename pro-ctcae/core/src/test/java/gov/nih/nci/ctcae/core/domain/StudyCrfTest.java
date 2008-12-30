package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.Fixture;
import junit.framework.TestCase;

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
		assertEquals(studyCrf1.hashCode(), studyCrf2.hashCode());

		studyCrf1.setStudy(study);
		assertFalse(studyCrf1.equals(studyCrf2));
		studyCrf2.setStudy(study);
		assertTrue(studyCrf1.equals(studyCrf2));
		assertEquals(studyCrf1.hashCode(), studyCrf2.hashCode());


		studyCrf1.setCrf(crf);
		assertFalse(studyCrf1.equals(studyCrf2));

		studyCrf2.setCrf(crf);
		assertTrue(studyCrf1.equals(studyCrf2));
		assertEquals(studyCrf1.hashCode(), studyCrf2.hashCode());
		studyCrf1.setId(1);
		studyCrf2.setId(2);
		assertTrue("must not consider id", studyCrf1.equals(studyCrf2));
		assertEquals("must not consider id", studyCrf1.hashCode(), studyCrf2.hashCode());

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
		assertEquals(studyCrf1.getCrf().getCrfPages(), studyCrf2.getCrf().getCrfPages());
		assertEquals(studyCrf1.getCrf().getStatus(), studyCrf2.getCrf().getStatus());


	}

}