package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CRFTest extends TestCase {
    private CRF crf;
    private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQuestion;

    private ProCtcTerm constipation, diarrhea;
    private Study study;
    private Date effectiveEndDate;

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

        crf.setStudy(study);
        assertFalse(crf.equals(anotherCrf));
        anotherCrf.setStudy(study);

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

    public void testEqualsAndHashCodeMustNotConsiderStudyParticipantCrf() {
        CRF anotherCrf = null;
        crf = new CRF();
        anotherCrf = new CRF();
        crf.setTitle("Cancer CRF");
        anotherCrf.setTitle("Cancer CRF");
        crf.setDescription("Case Report Form for Cancer Patients");
        anotherCrf.setDescription("Case Report Form for Cancer Patients");

        crf.setStatus(CrfStatus.DRAFT);
        crf.addStudyParticipantCrf(new StudyParticipantCrf());
        crf.setCrfVersion("1.0");
        anotherCrf.setCrfVersion("1.0");

        assertEquals("must not consider study participant crf", anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

        crf.setEffectiveEndDate(effectiveEndDate);
        assertFalse(crf.equals(anotherCrf));
        anotherCrf.setEffectiveEndDate(effectiveEndDate);

        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

        crf.setEffectiveStartDate(effectiveEndDate);
        assertFalse(crf.equals(anotherCrf));
        anotherCrf.setEffectiveStartDate(effectiveEndDate);

        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

        crf.setNextVersionId(1);
        assertFalse(crf.equals(anotherCrf));
        anotherCrf.setNextVersionId(1);

        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

        crf.setParentVersionId(1);
        assertFalse(crf.equals(anotherCrf));
        anotherCrf.setParentVersionId(1);

        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);


    }

    public void testEqualsAndHashCodeMustNotConsiderCrfPages() {
        CRF anotherCrf = null;
        crf = new CRF();
        anotherCrf = new CRF();
        crf.setTitle("Cancer CRF");
        anotherCrf.setTitle("Cancer CRF");
        crf.setDescription("Case Report Form for Cancer Patients");
        anotherCrf.setDescription("Case Report Form for Cancer Patients");

        crf.setStatus(CrfStatus.DRAFT);
        crf.addCrfPage();
        crf.setCrfVersion("1.0");
        anotherCrf.setCrfVersion("1.0");

        assertEquals("must not consider crf pages", anotherCrf.hashCode(), crf.hashCode());
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
        crf.addCrfPage(new CRFPage());
        crf.addCrfPage(new CRFPage());
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
        crf.addCrfPage(new CRFPage());
        crf.addCrfPage(new CRFPage());
        crf.addCrfPage(new CRFPage());
        assertEquals("must return 3 crfPages", 3, crf.getCrfPagesSortedByPageNumber().size());

        crf.removeCrfPageByPageNumber(1);

        assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());
        crf.updatePageNumberOfCrfPages();
        for (int i = 0; i < crf.getCrfPagesSortedByPageNumber().size(); i++) {
            assertEquals("must preserve page no", Integer.valueOf(i), crf.getCrfPagesSortedByPageNumber().get(i).getPageNumber());

        }

    }

    public void testRemoveCrfPageItem() {
        crf = new CRF();
        crf.addProCtcTerm(constipation);
        assertEquals("must return 1 crfPages", 1, crf.getCrfPagesSortedByPageNumber().size());

        CRFPage crfPage = crf.getCrfPages().get(0);
        assertEquals("must return 2 crf page items", 2, crfPage.getCrfPageItems().size());

        crf.removeCrfPageItemByQuestion(fifthQuestion);

        assertEquals("must return 1 crfPages", 1, crf.getCrfPagesSortedByPageNumber().size());

        crfPage = crf.getCrfPages().get(0);
        assertEquals("must remove 1 crf page items", 1, crfPage.getCrfPageItems().size());


    }

    private void validateCrfPageItemDisplayOrder(final CRF crf) {

        for (CRFPage crfPage : crf.getCrfPages()) {
            for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
                assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

            }
        }
    }


    private void validateCrfPageAndCrfPageItemOrder(final CRF crf) {
        validateCrfPageItemDisplayOrder(crf);
        verifyCrfPageNumber(crf);
    }


    private void verifyCrfPageNumber(final CRF crf) {
        for (int i = 0; i < crf.getCrfPages().size(); i++) {
            CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(i);
            assertEquals("must preserve crf page number", Integer.valueOf(i), crfPage.getPageNumber());


        }
    }

    public void testAddCrfPageAndAddQuestion() {
        crf = new CRF();
        crf.addCrfPage(firstQuestion);

        assertEquals("must return 1 crfPages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getAllCrfPageItems();
        assertEquals("must be 1 crf page item only", 1, crfPageItems.size());
        assertSame("must create the crf page item for specified question", firstQuestion, crfPageItems.get(0).getProCtcQuestion());


    }

    public void testAddCrfPageAndAddProCtcTerm() {
        crf = new CRF();
        crf.addProCtcTerm(constipation);


        assertEquals("must return 1 crfPages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getAllCrfPageItems();
        assertEquals("must be 2 crf page item", 2, crfPageItems.size());

        assertSame("must create the crf page item for specified question", secondQuestion, crfPageItems.get(1).getProCtcQuestion());
        assertSame("must create the crf page item for specified question", firstQuestion, crfPageItems.get(0).getProCtcQuestion());


    }

    public void testAddCrfPageWithProCtcTermMustRemoveQuestionsFormOtherCrfPagesAlso() {
        crf = new CRF();
        crf.addCrfPage(firstQuestion);
        crf.addProCtcTerm(constipation);

        assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getAllCrfPageItems();

        assertEquals("must be 2 crf page item only", 2, crfPageItems.size());

        assertEquals("must be remove crf page item if same question is added to another page", 0, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());

        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(1);
        assertEquals("must create the crf page item for specified question", 2, crfPage.getCrfPageItems().size());

        assertSame("must create the crf page item for specified question", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertSame("must create the crf page item for specified question", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());


    }

    public void testAddCrfPageWithQuestionMustRemoveQuestionsFormOtherCrfPagesAlso() {
        crf = new CRF();
        crf.addCrfPage(firstQuestion);
        crf.addCrfPage(firstQuestion);

        assertEquals("must return 2 crfPages", 2, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getAllCrfPageItems();

        assertEquals("must be 1 crf page item only", 1, crfPageItems.size());
        assertEquals("must be remove crf page item if same question is added to another page", 0, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());
        assertEquals("must create the crf page item for specified question", 1, crf.getCrfPagesSortedByPageNumber().get(1).getCrfPageItems().size());

        assertSame("must create the crf page item for specified question", firstQuestion, crfPageItems.get(0).getProCtcQuestion());


    }

    @Override
    protected void tearDown() throws Exception {
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    protected void setUp() throws Exception {
        super.setUp();
        firstQuestion = new ProCtcQuestion();
        firstQuestion.setQuestionText("first question");

        fifthQuestion = new ProCtcQuestion();
        fifthQuestion.setQuestionText("first question");

        secondQuestion = new ProCtcQuestion();
        secondQuestion.setQuestionText("second question");

        thirdQuestion = new ProCtcQuestion();
        thirdQuestion.setQuestionText("third question");

        fourthQuestion = new ProCtcQuestion();
        fourthQuestion.setQuestionText("fourth question");

        constipation = new ProCtcTerm();
        constipation.getProCtcQuestions().add(firstQuestion);
        constipation.getProCtcQuestions().add(secondQuestion);

        diarrhea = new ProCtcTerm();
        diarrhea.getProCtcQuestions().add(thirdQuestion);
        diarrhea.getProCtcQuestions().add(fourthQuestion);
        study = new Study();
        effectiveEndDate = new Date();
    }
}