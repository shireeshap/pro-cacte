package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Dec 29, 2008
 */
public class CRFPageTest extends TestCase {
	private CRFPage crfPage;
	private ProCtcQuestion proCtcQuestion1;
	private ProCtcQuestion proCtcQuestion2, proCtcQuestion3, proCtcQuestion4;

	private ProCtcTerm constipation, diarrhea;


	public void testConstructor() {
		crfPage = new CRFPage();
		assertNull(crfPage.getDescription());
		assertEquals(Integer.valueOf(0), crfPage.getVersion());
	}

	public void testGetterAndSetter() {
		crfPage = new CRFPage();
		crfPage.setDescription("Case Report Form for Cancer Patients");
		crfPage.setId(2);

		assertEquals("Case Report Form for Cancer Patients", crfPage.getDescription());
		assertEquals(Integer.valueOf(2), crfPage.getId());
	}

	public void testGetCrfItems() {
		crfPage = new CRFPage();
		crfPage.addOrUpdateCrfItem(proCtcQuestion1);
		crfPage.addOrUpdateCrfItem(proCtcQuestion2);
		assertEquals("must return 2 items", 2, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertNotNull("order number must not be null", crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
	}

	public void testAddNewCrfItems() {
		crfPage = new CRFPage();
		crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion1);
		crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion2);
		assertEquals("must return 2 items", 2, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertNotNull("order number must not be null", crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
	}

	public void testAddNewCrfItemsMustNotAddQuestionAgain() {
		crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion1);
		crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion1);
		assertEquals("must return 1 items", 1, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertNotNull("order number must not be null", crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
	}

	public void testAddNewProCtcTerm() {
		crfPage.removeExistingAndAddNewCrfItem(constipation);
		assertEquals("must return 2 items", 2, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertNotNull("order number must not be null", crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
	}

	public void testAddMultipleProCtcTerm() {
		crfPage.removeExistingAndAddNewCrfItem(constipation);
		crfPage.removeExistingAndAddNewCrfItem(diarrhea);
		assertEquals("must return 4 items", 4, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertNotNull("order number must not be null", crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
	}

	public void testReorderCrfItems() {
		crfPage.addOrUpdateCrfItem(proCtcQuestion1);
		crfPage.addOrUpdateCrfItem(proCtcQuestion2);
		assertEquals("must return 2 items", 2, crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}

		crfPage.getCrfItemsSortedByDislayOrder().get(0).setDisplayOrder(1);
		crfPage.getCrfItemsSortedByDislayOrder().get(1).setDisplayOrder(0);
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}

	}

//	public void testEqualsAndHashCode() {
//		CRFPage anotherCrfPage = null;
//		crfPage = null;
//		assertEquals(anotherCrfPage, crfPage);
//		crfPage = new CRFPage();
//		assertFalse(crfPage.equals(anotherCrfPage));
//		anotherCrfPage = new CRFPage();
//		assertEquals(anotherCrfPage, crfPage);
//		assertEquals(anotherCrfPage.hashCode(), crfPage.hashCode());
//
//		crfPage.setDescription("Case Report Form for Cancer Patients");
//		assertFalse(crfPage.equals(anotherCrfPage));
//
//		anotherCrfPage.setDescription("Case Report Form for Cancer Patients");
//		assertEquals(anotherCrfPage.hashCode(), crfPage.hashCode());
//		assertEquals(anotherCrfPage, crfPage);
//
//
//	}

//	public void testEqualsAndHashCodeMustNotConsiderId() {
//		CRFPage anotherCrfPage = new CRFPage();
//		crfPage.setDescription("Case Report Form for Cancer Patients");
//		anotherCrfPage.setDescription("Case Report Form for Cancer Patients");
//
//
//		anotherCrfPage.setId(1);
//		assertEquals("must not consider id", anotherCrfPage.hashCode(), crfPage.hashCode());
//		assertEquals(anotherCrfPage, crfPage);
//
//	}


//	public void testEqualsAndHashCodeMustNotConsiderCrfItems() {
//		CRFPage anotherCrfPage = new CRFPage();
//		crfPage.setDescription("Case Report Form for Cancer Patients");
//		anotherCrfPage.setDescription("Case Report Form for Cancer Patients");
//
//
//		anotherCrfPage.addOrUpdateCrfItem(new ProCtcQuestion(1), null);
//		assertFalse(anotherCrfPage.getCrfItemsSortedByDislayOrder().isEmpty());
//		assertEquals("must not consider study crf", anotherCrfPage.hashCode(), crfPage.hashCode());
//		assertEquals(anotherCrfPage, crfPage);
//
//	}

	protected void setUp() throws Exception {
		super.setUp();

		crfPage = new CRFPage();

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