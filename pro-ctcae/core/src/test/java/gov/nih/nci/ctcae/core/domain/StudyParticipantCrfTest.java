package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.Fixture;
import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Harsh Agarwal
 * @crated Dec 12, 2008
 */
public class StudyParticipantCrfTest extends TestCase {
	private StudyParticipantCrf studyParticipantCrf;

	public void testConstructor() {
		studyParticipantCrf = new StudyParticipantCrf();
		assertNull(studyParticipantCrf.getId());
		assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
		assertNull(studyParticipantCrf.getStudyCrf());
		assertNull(studyParticipantCrf.getStudyParticipantAssignment());

	}

	public void testGetterAndSetter() {
		studyParticipantCrf = new StudyParticipantCrf();

		CRF crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
		crf.addCrfPge(new CRFPage());
		crf.addCrfPge(new CRFPage());

		StudyCrf studyCrf = new StudyCrf();
		Date d = new Date();

		studyCrf.setCrf(crf);


		studyParticipantCrf.setId(1);
		studyParticipantCrf.setStudyCrf(studyCrf);
		studyParticipantCrf.setStudyParticipantAssignment(new StudyParticipantAssignment());
		//studyParticipantCrf.addStudyParticipantCrfSchedule(null);

		assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
		assertEquals(new Integer(1), studyParticipantCrf.getId());
		assertEquals(studyCrf, studyParticipantCrf.getStudyCrf());
		assertNotNull(studyParticipantCrf.getStudyParticipantAssignment());

		//studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());

		assertEquals(1, studyParticipantCrf.getStudyParticipantCrfSchedules().size());


	}


}