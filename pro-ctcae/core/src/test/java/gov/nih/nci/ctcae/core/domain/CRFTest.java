package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CRFTest extends TestCase {
	private CRF crf;
	private ProCtcQuestion proCtcQuestion1;
	private ProCtcQuestion proCtcQuestion2, proCtcQuestion3, proCtcQuestion4;

	private ProCtcTerm constipation, diarrhea;

	public void testReleased() {
		crf = new CRF();
		assertFalse(crf.isReleased());
		crf.setStatus(CrfStatus.RELEASED);
		assertTrue(crf.isReleased());
	}

	public void testToString() {
		crf = new CRF();
		assertNull(crf.toString());
		crf.setTitle("form 1");
		assertEquals("form 1", crf.getTitle());

	}

	public void testConstructor() {
		crf = new CRF();
		assertNull(crf.getTitle());
		assertNull(crf.getDescription());
		assertEquals("status must be draft by default", CrfStatus.DRAFT, crf.getStatus());
		assertNull(crf.getCrfVersion());
		assertEquals(Integer.valueOf(0), crf.getVersion());
	}

	public void testGetterAndSetter() {
		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		crf.setId(2);

		assertEquals("Cancer CRF", crf.getTitle());
		assertEquals("Case Report Form for Cancer Patients", crf.getDescription());
		assertEquals(CrfStatus.DRAFT, crf.getStatus());
		assertEquals("1.0", crf.getCrfVersion());
		assertEquals(Integer.valueOf(2), crf.getId());
	}





	public void testEqualsAndHashCode() {
		CRF anotherCrf = null;
		assertEquals(anotherCrf, crf);
		crf = new CRF();
		assertFalse(crf.equals(anotherCrf));
		anotherCrf = new CRF();
		assertEquals(anotherCrf, crf);
		assertEquals(anotherCrf.hashCode(), crf.hashCode());

		crf.setTitle("Cancer CRF");
		assertFalse(crf.equals(anotherCrf));

		anotherCrf.setTitle("Cancer CRF");
		assertEquals(anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

		crf.setDescription("Case Report Form for Cancer Patients");
		assertFalse(crf.equals(anotherCrf));

		anotherCrf.setDescription("Case Report Form for Cancer Patients");
		assertEquals(anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

		crf.setStatus(CrfStatus.RELEASED);
		assertFalse(crf.equals(anotherCrf));

		anotherCrf.setStatus(CrfStatus.RELEASED);
		assertEquals(anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

		crf.setCrfVersion("1.0");
		assertFalse(crf.equals(anotherCrf));

		anotherCrf.setCrfVersion("1.0");

		assertEquals(anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

	}

	public void testEqualsAndHashCodeMustNotConsiderId() {
		CRF anotherCrf = null;
		crf = new CRF();
		anotherCrf = new CRF();
		crf.setTitle("Cancer CRF");
		anotherCrf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		anotherCrf.setDescription("Case Report Form for Cancer Patients");

		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		anotherCrf.setCrfVersion("1.0");

		anotherCrf.setId(1);
		assertEquals("must not consider id", anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

	}

	public void testEqualsAndHashCodeMustNotConsiderStudyCrf() {
		CRF anotherCrf = null;
		crf = new CRF();
		anotherCrf = new CRF();
		crf.setTitle("Cancer CRF");
		anotherCrf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		anotherCrf.setDescription("Case Report Form for Cancer Patients");

		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		anotherCrf.setCrfVersion("1.0");

		anotherCrf.setStudyCrf(new StudyCrf());
		assertEquals("must not consider study crf", anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

	}

	public void testEqualsAndHashCodeMustNotConsiderCrfItems() {
		CRF anotherCrf = null;
		crf = new CRF();
		anotherCrf = new CRF();
		crf.setTitle("Cancer CRF");
		anotherCrf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		anotherCrf.setDescription("Case Report Form for Cancer Patients");

		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		anotherCrf.setCrfVersion("1.0");

//		anotherCrf.addOrUpdateCrfItemInCrfPage(new ProCtcQuestion(1), null);
//		assertFalse(anotherCrf.getCrfItemsSortedByDislayOrder().isEmpty());
		assertEquals("must not consider study crf", anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

	}

	public void testGetCrfPageByPageNumber() {
		crf = new CRF();
		crf.addCrfPge(new CRFPage());
		crf.addCrfPge(new CRFPage());
		assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());
		for (int i = 0; i < crf.getCrfPagesSortedByPageNumber().size(); i++) {
			assertEquals("must preserve page no", Integer.valueOf(i), crf.getCrfPagesSortedByPageNumber().get(i).getPageNumber());

		}
		crf.getCrfPagesSortedByPageNumber().get(0).setPageNumber(1);
		crf.getCrfPagesSortedByPageNumber().get(1).setPageNumber(0);

		assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());
		for (int i = 0; i < crf.getCrfPagesSortedByPageNumber().size(); i++) {
			assertEquals("must preserve page no", Integer.valueOf(i), crf.getCrfPagesSortedByPageNumber().get(i).getPageNumber());

		}

	}

	public void testRemoveCrfPageByPageNumber() {
		crf = new CRF();
		crf.addCrfPge(new CRFPage());
		crf.addCrfPge(new CRFPage());
		crf.addCrfPge(new CRFPage());
		assertEquals("must return 3 crfPages", 3, crf.getCrfPagesSortedByPageNumber().size());

		crf.removeCrfPageByPageNumber(1);

		assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());
		crf.updatePageNumberOfCrfPageItems();
		for (int i = 0; i < crf.getCrfPagesSortedByPageNumber().size(); i++) {
			assertEquals("must preserve page no", Integer.valueOf(i), crf.getCrfPagesSortedByPageNumber().get(i).getPageNumber());

		}

	}

	protected void setUp() throws Exception {
		super.setUp();
		proCtcQuestion1 = new ProCtcQuestion();
		proCtcQuestion1.setQuestionText("first question");
		proCtcQuestion2 = new ProCtcQuestion();
		proCtcQuestion2.setQuestionText("second question");
		proCtcQuestion3 = new ProCtcQuestion();
		proCtcQuestion3.setQuestionText("third question");
		proCtcQuestion4 = new ProCtcQuestion();
		proCtcQuestion4.setQuestionText("fourth question");

		constipation = new ProCtcTerm();
		constipation.getProCtcQuestions().add(proCtcQuestion1);
		constipation.getProCtcQuestions().add(proCtcQuestion2);

		diarrhea = new ProCtcTerm();
		diarrhea.getProCtcQuestions().add(proCtcQuestion3);
		diarrhea.getProCtcQuestions().add(proCtcQuestion4);
	}
}