package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.AbstractTestCase;

/**
 * @author Vinay Kumar
 * @created Dec 29, 2008
 */
public class CRFPageTest extends AbstractTestCase {
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
        crfPage.addProCtcTerm(constipation);
        assertEquals("must return 2 items", 2, crfPage.getCrfPageItems().size());
        validateCrfPage();
    }

    public void testAddNewCrfItems() {
        crfPage = new CRFPage();
        crfPage.addProCtcTerm(constipation);
        assertEquals("must return 2 items", 2, crfPage.getCrfPageItems().size());
        validateCrfPage();
    }

    public void testAddNewCrfItemsMustNotAddQuestionAgain() {
        crfPage.addProCtcTerm(constipation);
        crfPage.addProCtcTerm(constipation);
        assertEquals("must return 2 items", constipation.getProCtcQuestions().size(), crfPage.getCrfPageItems().size());
        validateCrfPage();
    }

    public void testAddNewProCtcTerm() {
        crfPage.addProCtcTerm(constipation);
        assertEquals("must return 2 items", 2, crfPage.getCrfPageItems().size());
        validateCrfPage();
    }

    public void testAddMultipleProCtcTerm() {
        crfPage.addProCtcTerm(constipation);
        crfPage.addProCtcTerm(diarrhea);
        assertEquals("must return 4 items", 4, crfPage.getCrfPageItems().size());
        validateCrfPage();
    }

    private void validateCrfPage() {
        validateCrfPageAndCrfPageItemOrder(crfPage.getCrf());
    }

    //required only for advance form because you can not reorder crf page item in basic form mode
//	public void testReorderCrfItems() {
//		crfPage.addOrUpdateCrfPageItem(proCtcQuestion1, 1);
//		crfPage.addOrUpdateCrfPageItem(proCtcQuestion2, 2);
//		assertEquals("must return 2 items", 2, crfPage.getCrfPageItems().size());
//		for (int i = 0; i < crfPage.getCrfPageItems().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfPageItems().get(i).getDisplayOrder());
//
//		}
//
//		crfPage.getCrfPageItems().get(0).setDisplayOrder(1);
//		crfPage.getCrfPageItems().get(1).setDisplayOrder(0);
//		for (int i = 0; i < crfPage.getCrfPageItems().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i), crfPage.getCrfPageItems().get(i).getDisplayOrder());
//
//		}
//
//	}

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



    protected void setUp() throws Exception {
        super.setUp();

        crfPage = new CRFPage();

        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setQuestionText("first question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setQuestionText("second question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setQuestionText("third question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setQuestionText("fourth question", SupportedLanguageEnum.ENGLISH);

        constipation = new ProCtcTerm();
        constipation.getProCtcQuestions().add(proCtcQuestion1);
        constipation.getProCtcQuestions().add(proCtcQuestion2);

        diarrhea = new ProCtcTerm();
        diarrhea.getProCtcQuestions().add(proCtcQuestion3);
        diarrhea.getProCtcQuestions().add(proCtcQuestion4);
    }
}