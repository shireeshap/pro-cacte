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

	public void testGetCrfItems() {
		crf = new CRF();
		crf.addOrUpdateCrfItem(proCtcQuestion1, 1);
		crf.addOrUpdateCrfItem(proCtcQuestion2, 2);
		assertEquals("must return 2 items", 2, crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertNotNull("order number must not be null", crf.getCrfItems().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
	}

	public void testAddNewCrfItems() {
		crf = new CRF();
		crf.removeExistingAndAddNewCrfItem(proCtcQuestion1);
		crf.removeExistingAndAddNewCrfItem(proCtcQuestion2);
		assertEquals("must return 2 items", 2, crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertNotNull("order number must not be null", crf.getCrfItems().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
	}

	public void testAddNewProCtcTerm() {
		crf = new CRF();
		crf.removeExistingAndAddNewCrfItem(constipation);
		assertEquals("must return 2 items", 2, crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertNotNull("order number must not be null", crf.getCrfItems().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
	}

	public void testAddMultipleProCtcTerm() {
		crf = new CRF();
		crf.removeExistingAndAddNewCrfItem(constipation);
		crf.removeExistingAndAddNewCrfItem(diarrhea);
		assertEquals("must return 4 items", 4, crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertNotNull("order number must not be null", crf.getCrfItems().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
	}

	public void testReorderCrfItems() {
		crf = new CRF();
		crf.addOrUpdateCrfItem(proCtcQuestion1, 1);
		crf.addOrUpdateCrfItem(proCtcQuestion2, 2);
		assertEquals("must return 2 items", 2, crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}

		crf.getCrfItems().get(0).setDisplayOrder(1);
		crf.getCrfItems().get(1).setDisplayOrder(0);
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i), crf.getCrfItems().get(i).getDisplayOrder());

		}

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
		anotherCrf.setDescription("Case Report Form for Cancer Patients");

		crf.setStatus(CrfStatus.DRAFT);
		anotherCrf.setStatus(CrfStatus.DRAFT);

		crf.setCrfVersion("1.0");
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

		anotherCrf.addOrUpdateCrfItem(new ProCtcQuestion(1), 1);
		assertFalse(anotherCrf.getCrfItems().isEmpty());
		assertEquals("must not consider study crf", anotherCrf.hashCode(), crf.hashCode());
		assertEquals(anotherCrf, crf);

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