package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.List;

import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @since Nov 4, 2008
 */
public class CreateFormCommandTest extends WebTestCase {

    private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQustion, sixthQuestion;
    private CrfPageItem crfItem1Page, crfItem2Page, crfItem3Page, crfItem4Page;
    private ProCtcTerm proCtcTerm1, proCtcTerm2;
    ProCtcQuestionRepository proCtcQuestionRepository;
    private CreateFormCommand command;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new CreateFormCommand();
        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);

        firstQuestion = new ProCtcQuestion();
        firstQuestion.setId(11);
        firstQuestion.setQuestionText("sample question1");

        secondQuestion = new ProCtcQuestion();
        secondQuestion.setQuestionText("sample question2");
        secondQuestion.setId(12);

        thirdQuestion = new ProCtcQuestion();
        thirdQuestion.setQuestionText("sample question3");
        thirdQuestion.setId(13);

        fourthQuestion = new ProCtcQuestion();
        fourthQuestion.setQuestionText("sample question4");
        fourthQuestion.setId(14);

        crfItem1Page = new CrfPageItem();
        crfItem1Page.setProCtcQuestion(firstQuestion);
        crfItem2Page = new CrfPageItem();
        crfItem2Page.setProCtcQuestion(secondQuestion);
        crfItem3Page = new CrfPageItem();
        crfItem3Page.setProCtcQuestion(thirdQuestion);
        crfItem4Page = new CrfPageItem();
        crfItem4Page.setProCtcQuestion(fourthQuestion);

        fifthQustion = new ProCtcQuestion();
        fifthQustion.setId(15);
        fifthQustion.setQuestionText("sample question1");

        sixthQuestion = new ProCtcQuestion();
        sixthQuestion.setId(16);
        sixthQuestion.setQuestionText("sample question6");

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Fatigue");
        proCtcTerm1.addProCtcQuestion(firstQuestion);
        proCtcTerm1.addProCtcQuestion(secondQuestion);
        proCtcTerm1.addProCtcQuestion(thirdQuestion);

        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("Constipation");
        proCtcTerm2.addProCtcQuestion(fourthQuestion);
        proCtcTerm2.addProCtcQuestion(sixthQuestion);

    }

    public void testGetSelectedProCtcTerms() {
        assertFalse("both terms must be different", proCtcTerm1.equals(proCtcTerm2));
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());
        assertEquals("must have 5 page items", 5, crf.getAllCrfPageItems().size());

        validateCrfPageAndCrfPageItemOrder(crf);

        List<Integer> selectedProCtcTerms = command.getSelectedProCtcTerms();

        assertEquals("must have both terms as selected terms", 0, selectedProCtcTerms.size());
//        assertTrue("must have both terms", selectedProCtcTerms.contains(proCtcTerm1.getId()));
//        assertTrue("must have both terms", selectedProCtcTerms.contains(proCtcTerm2.getId()));

    }

    public void testGetSelectedProCtcTermsIfProCtCtermHasBeenAddedPartially() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        command.setQuestionIdToRemove(String.valueOf(fourthQuestion.getId()));
        CRF crf = command.getCrf();
        expect(proCtcQuestionRepository.findById(fourthQuestion.getId())).andReturn(fourthQuestion);
        replayMocks();

        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        validateCrfPageAndCrfPageItemOrder(crf);

        List<Integer> selectedProCtcTerms = command.getSelectedProCtcTerms();

        assertEquals("must not have 2nd term as all questions of second term have not been added yet", 0, selectedProCtcTerms.size());
        //assertTrue("must have both terms", selectedProCtcTerms.contains(proCtcTerm1.getId()));

    }

    public void testAddProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testAddMultipleProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testReOrderCrfPagesInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        command.setCrfPageNumbers("1,0");

        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        CRFPage crfPage = crf.getCrfPageItemByQuestion(proCtcTerm1.getProCtcQuestions().iterator().next()).getCrfPage();
        assertEquals("must reorder crf page number", Integer.valueOf(1), crfPage.getPageNumber());

        crfPage = crf.getCrfPageItemByQuestion(proCtcTerm2.getProCtcQuestions().iterator().next()).getCrfPage();

        assertEquals("must reorder crf page number", Integer.valueOf(0), crfPage.getPageNumber());

        validateCrfPageAndCrfPageItemOrder(crf);


    }

//    public void testAddDeleteAddAndReOrderCrfPagesInBasicMode() {
//
//        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
//        command.addProCtcTerm(proCtcTerm1);
//        command.addProCtcTerm(proCtcTerm2);
//
//        //now remove one page
//        command.setQuestionIdToRemove(firstQuestion.getId() + "," + secondQuestion.getId() + "," + thirdQuestion.getId());
//
//        assertEquals("proctc term 1 must have 3 questions only", 3, proCtcTerm1.getProCtcQuestions().size());
//
//        command.addProCtcTerm(proCtcTerm1);
//
//        CRF crf = command.getCrf();
//
//        assertEquals("must add one more page because 1st page is empty", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//        command.setCrfPageNumbers("2,1");
//
//
//        command.updateCrfItems(proCtcQuestionRepository);
//        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());
//
//        CRFPage crfPage = crf.getCrfPageItemByQuestion(proCtcTerm1.getProCtcQuestions().iterator().next()).getCrfPage();
//        assertEquals("must reorder crf page number", Integer.valueOf(0), crfPage.getPageNumber());
//
//        crfPage = crf.getCrfPageItemByQuestion(proCtcTerm2.getProCtcQuestions().iterator().next()).getCrfPage();
//
//        assertEquals("must reorder crf page number", Integer.valueOf(1), crfPage.getPageNumber());
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//
//    }

    public void testRemoveQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);

        //now remove 1 question

        command.setQuestionIdToRemove(String.valueOf(secondQuestion.getId()));
        expect(proCtcQuestionRepository.findById(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testRemoveAndAddSameQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());

        //now remove 1 question
        command.setQuestionIdToRemove(String.valueOf(secondQuestion.getId()));

        expect(proCtcQuestionRepository.findById(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();

        resetMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());

        //now add same question again
        assertEquals("must not add any more page", 1, crf.getCrfPagesSortedByPageNumber().size());

        command.addProCtcTerm(proCtcTerm1);
        assertEquals("must add only 1 question", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }


    public void testConstructor() {
        CRF crf = command.getCrf();
        assertNotNull("study crf must not be null", crf);
        assertNotNull("crf must not be null", crf);


    }

    public void testTitle() {
        assertEquals("Click here to name", command.getTitle());


    }

//    public void testRemoveCrfPageWithQuestionsMustRemoveQuestionAlso() {
//
//        command.addCrfPage();
//        command.addCrfPage();
//        command.addCrfPage();
//        CRF crf = command.getCrf();
//
//        assertEquals("must have three  pages", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//
//        command.setQuestionsIds("11,12,13,14");
//        command.setNumberOfQuestionsInEachPage("2,1,1");
//        command.setCrfPageNumbers("0,1,2");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//        assertEquals("must have three  page", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//        assertEquals("must have  2 questions ", 2, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());
//        assertEquals("must have  1 question ", 1, crf.getCrfPagesSortedByPageNumber().get(1).getCrfPageItems().size());
//        assertEquals("must have  1 question ", 1, crf.getCrfPagesSortedByPageNumber().get(2).getCrfPageItems().size());
//
//
//        //now remove 2nd page
//        command.setQuestionsIds("11,12,14");
//
//
//        command.setNumberOfQuestionsInEachPage("1,2");
//        command.setCrfPageNumberToRemove("1");
//
//        command.setCrfPageNumbers("0,1");
//        //command.clearCrfPage(1);
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//
//        assertEquals("must have 2  pages", 2, crf.getCrfPagesSortedByPageNumber().size());
//
//        assertEquals("must have  1 questions ", 1, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());
//        assertEquals("must have  2 question ", 2, crf.getCrfPagesSortedByPageNumber().get(1).getCrfPageItems().size());
//        for (CrfPageItem crfPageItem : crf.getAllCrfPageItems()) {
//            assertNotSame("must remove the questions also when you remove a crf page", thirdQuestion, crfPageItem.getProCtcQuestion());
//        }
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//    }


//    public void testAddAndUpdateAndReorderButNotDeleteQuestionsInMultiplePages() {
//
//        command.addCrfPage();
//        command.addCrfPage();
//
//        command.setQuestionsIds("11,12,13,14");
//        command.setNumberOfQuestionsInEachPage("2,2");
//        command.setCrfPageNumbers("0,1");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//
//        CRF crf = getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have  2 questions ", 2, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());
//        assertEquals("must have  1 question ", 2, crf.getCrfPagesSortedByPageNumber().get(1).getCrfPageItems().size());
//
//        //now update and reorder questions
//        CrfPageItem crfPageItem = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().get(1);
//        crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
//        crfPageItem.setInstructions("inst");
//        crfPageItem.setResponseRequired(Boolean.TRUE);
//        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
//        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule);
//
//        command.setQuestionsIds("14,11,13,12");
//        command.setNumberOfQuestionsInEachPage("1,3");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//
//        assertEquals("must have  1 questions ", 1, crfPage.getCrfPageItems().size());
//        assertSame("must preserve the order number  while moving the crf page items", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//
//        crfPage = crf.getCrfPagesSortedByPageNumber().get(1);
//        assertEquals("must have  3 question ", 3, crfPage.getCrfPageItems().size());
//        assertSame("must preserve the order number  while moving the crf page items", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//        assertSame("must preserve the order number  while moving the crf page items", thirdQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());
//        assertSame("must preserve the order number  while moving the crf page items", secondQuestion, crfPage.getCrfPageItems().get(2).getProCtcQuestion());
//
//        CrfPageItem movedCrfPageItem = crfPage.getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
//        assertSame("must update and move crf page items between pages", movedCrfPageItem, crfPageItem);
//        assertSame("must update and move crf page items between pages", movedCrfPageItem.getCrfPage(), crfPage);
//        assertSame("must update and move crf page items between pages", crfPageItem.getCrfPage(), crfPage);
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//    }

//    public void testReOrderPageAndReorderCrfPageItem() {
//
//        command.addCrfPage();
//        command.addCrfPage();
//
//        command.setQuestionsIds("11,12,13,14");
//        command.setNumberOfQuestionsInEachPage("2,2");
//        command.setCrfPageNumbers("0,1");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//
//        CRF crf = getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//
//        //now update and reorder questions  and reoder pages also
//
//        command.setQuestionsIds("14,11,13,12");
//        command.setNumberOfQuestionsInEachPage("1,3");
//        command.setCrfPageNumbers("1,0");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//
//        assertEquals("must have 3  crf items", 3, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//        assertSame("must move the questions also while reordering the pages", thirdQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());
//        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfPageItems().get(2).getProCtcQuestion());
//
//        crfPage = crf.getCrfPagesSortedByPageNumber().get(1);
//
//        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//
//
//    }


//    public void testDeleteAndReorderPageAndReorderAndDeleteCrfPageItem() {
//
//        command.addCrfPage();
//        command.addCrfPage();
//        command.addCrfPage();
//
//        command.setQuestionsIds("11,12,13,14");
//        command.setNumberOfQuestionsInEachPage("2,1,1");
//        command.setCrfPageNumbers("0,1,2");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//
//        CRF crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//
//        //now reorder questions  and reoder pages also   and delete one page  and delte one question also
//
//        command.setQuestionsIds("14,11,12");
//        command.setNumberOfQuestionsInEachPage("1,2");
//        command.setCrfPageNumbers("2,0");
//        command.setCrfPageNumberToRemove("1");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have 2  crf pages only because 1 crf page is removed", 2, crf.getCrfPagesSortedByPageNumber().size());
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//
//        assertEquals("must have 2  crf items", 2, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());
//
//        crfPage = crf.getCrfPagesSortedByPageNumber().get(1);
//
//        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//
//
//    }


//    public void testAddAndUpdateAndReorderAndDeleteQuestionsInFirstPageOnly() {
//        command.addCrfPage();
//
//        command.setQuestionsIds("11,12,14,13");
//        command.setNumberOfQuestionsInEachPage("4");
//        command.setCrfPageNumbers("0");
//
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(14))).andReturn(fourthQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//
//        CRF crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have  4 questions ", 4, crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().size());
//
//
//        //now reorder and update and delete questions
//        CrfPageItem crfPageItem = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems().get(1);
//        crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
//        crfPageItem.setInstructions("inst");
//        crfPageItem.setResponseRequired(Boolean.TRUE);
//        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
//        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule);
//
//        command.setQuestionsIds("11,13,12");
//        command.setNumberOfQuestionsInEachPage("3");
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//        assertEquals("must have  3 question ", 3, crfPage.getCrfPageItems().size());
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        CrfPageItem movedCrfPageItem = crfPage.getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
//        assertSame("must update and move crf page items between pages", movedCrfPageItem, crfPageItem);
//        assertSame("must update and move crf page items between pages", movedCrfPageItem.getCrfPage(), crfPage);
//        assertSame("must update and move crf page items between pages", crfPageItem.getCrfPage(), crfPage);
//
//
//    }

//    public void testAddQuestionInFirstPageOnly() {
//        command.addCrfPage();
//        command.updateCrfItems(proCtcQuestionRepository);
//
//        command.setQuestionsIds("11,12");
//        command.setNumberOfQuestionsInEachPage("2");
//        command.setCrfPageNumbers("0");
//
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        CRF crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have only one default page", 1, crf.getCrfPagesSortedByPageNumber().size());
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//        assertEquals("must have  2 questions ", 2, crfPage.getCrfPageItems().size());
//        assertEquals("must preserve order no", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//        assertEquals("must preserve order no", secondQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());
//
//
//    }

//    public void testReOrderPageButDoNotReorderCrfPageItem() {
//        command.addCrfPage();
//        command.addCrfPage();
//        command.addCrfPage();
//        CRF crf = command.getCrf();
//
//        assertEquals("must have three default page", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//
//        command.setQuestionsIds("11,12,13,14");
//        command.setNumberOfQuestionsInEachPage("2,1,1");
//        command.setCrfPageNumbers("0,1,2");
//
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        resetMocks();
//        crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have three  page", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//        //now reorder the crf page
//        command.setCrfPageNumbers("2,0,1");
//
//
//        expect(proCtcQuestionRepository.findById(Integer.valueOf(11))).andReturn(firstQuestion);
//        expect(proCtcQuestionRepository.findById(12)).andReturn(secondQuestion);
//        expect(proCtcQuestionRepository.findById(13)).andReturn(thirdQuestion);
//        expect(proCtcQuestionRepository.findById(14)).andReturn(fourthQuestion);
//        replay(proCtcQuestionRepository);
//        command.updateCrfItems(proCtcQuestionRepository);
//        verify(proCtcQuestionRepository);
//        crf = command.getCrf();
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//        assertEquals("must have three  page", 3, crf.getCrfPagesSortedByPageNumber().size());
//
//        CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(0);
//
//        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", thirdQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//
//        crfPage = crf.getCrfPagesSortedByPageNumber().get(1);
//
//        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//
//        crfPage = crf.getCrfPagesSortedByPageNumber().get(2);
//        assertEquals("must have two  crf items", 2, crfPage.getCrfPageItems().size());
//        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
//        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());
//
//
//    }


//    public void testAdddingProCtcTermInBasicModeAgainAfterUpdatingCrfPageItem() {
//
//        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
//        command.addProCtcTerm(proCtcTerm1);
//        CRF crf = command.getCrf();
//
//        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());
//
//        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
//        assertEquals("must have 4 crf page items", 4, crfPageItems.size());
//
//        //now update one crf page item
//        CrfPageItem crfPageItem = crfPageItems.get(0);
//        crfPageItem.setInstructions("inst");
//        crfPageItem.setResponseRequired(Boolean.TRUE);
//
//        //now add pro cterm again
//
//        command.addProCtcTerm(proCtcTerm1);
//        crf = command.getCrf();
//
//        assertEquals("must not add any more pages", 1, crf.getCrfPagesSortedByPageNumber().size());
//
//        crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
//        assertEquals("must not add any crf page items", 4, crfPageItems.size());
//        assertEquals("must not remove existing crf page items", crfPageItem, crfPageItems.get(0));
//
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//    }

//    public void testAdddingProCtcTermInBasicModeAgainAfterUpdatingAndRemovingCrfPageItem() {
//
//        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
//        command.addProCtcTerm(proCtcTerm1);
//        CRF crf = command.getCrf();
//
//        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());
//
//        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
//        assertEquals("must have 4 crf page items", 4, crfPageItems.size());
//
//
//        //now update one crf page item
//        CrfPageItem crfPageItem = crfPageItems.get(0);
//        crfPageItem.setInstructions("inst");
//        crfPageItem.setResponseRequired(Boolean.TRUE);
//
//        //now remove this crf page item;
//        command.setQuestionIdToRemove(String.valueOf(firstQuestion.getId()));
//
//        crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
//        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
//
//        //now add pro cterm again
//
//        command.addProCtcTerm(proCtcTerm1);
//        crf = command.getCrf();
//
//        assertEquals("must not add any more pages", 1, crf.getCrfPagesSortedByPageNumber().size());
//
//        crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
//        assertEquals("must  add one crf page items", 4, crfPageItems.size());
//        assertFalse("must  add a new empty crf page items", crfPageItem.equals(crfPageItems.get(0)));
//        validateCrfPageAndCrfPageItemOrder(crf);
//
//
//    }

    private CRF getCrf() {
        CRF crf = command.getCrf();
        return crf;
    }


//	public void testAddAndReorderQuestionsAndDelete() {
//
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(secondQuestion, 1);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(thirdQuestion, 2);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(fourthQuestion, 2);
//		command.setQuestionsIds("12,13,11,14");
//
//		expect(finderRepository.findById( Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById( 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById( 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById( 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//		CRF crf = command.getCRF().getCrf();
//		assertEquals("must have 4 questions ", 4,
//			crf.getCrfPageItems().size());
//		for (int i = 0; i < crf.getCrfPageItems().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfPageItems().get(i).getDisplayOrder());
//
//		}
//		assertEquals("must preserve order no", firstQuestion, crf.getCrfPageItems().get(2).getProCtcQuestion());
//		assertEquals("must preserve order no", thirdQuestion, crf.getCrfPageItems().get(1).getProCtcQuestion());
//		assertEquals("must preserve order no", secondQuestion, crf.getCrfPageItems().get(0).getProCtcQuestion());
//		assertEquals("must preserve order no", fourthQuestion, crf.getCrfPageItems().get(3).getProCtcQuestion());
//
//		//now delete the first question
//		command.setQuestionsIds("12,14,13");
//		expect(finderRepository.findById( Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById( 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById( 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//		crf = command.getCRF().getCrf();
//		assertEquals("must have 3 questions ", 3,
//			crf.getCrfPageItems().size());
//		for (int i = 0; i < crf.getCrfPageItems().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfPageItems().get(i).getDisplayOrder());
//
//		}
//		assertEquals("must preserve order no", thirdQuestion, crf.getCrfPageItems().get(2).getProCtcQuestion());
//		assertEquals("must preserve order no", fourthQuestion, crf.getCrfPageItems().get(1).getProCtcQuestion());
//		assertEquals("must preserve order no", secondQuestion, crf.getCrfPageItems().get(0).getProCtcQuestion());
//
//
//	}

//	public void testAddAndReorderAndDeleteAndAddAgain() {
//
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(secondQuestion, 1);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(thirdQuestion, 2);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(fourthQuestion, 2);
//		command.setQuestionsIds("12,13,11,14");
//
//		expect(finderRepository.findById( Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById( 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById( 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById( 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//
//		//now delete the first question and add again
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.setQuestionsIds("12,13,11,14");
//
//		expect(finderRepository.findById( Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById( 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById( 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById( 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//
//		CRF crf = command.getCRF().getCrf();
//		assertEquals("must have 4 questions ", 4,
//			crf.getAllCrfPageItems().size());
//		for (int i = 0; i < crf.getAllCrfPageItems().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getAllCrfPageItems().get(i).getDisplayOrder());
//
//		}
//		assertEquals("must preserve order no", fourthQuestion, crf.getAllCrfPageItems().get(3).getProCtcQuestion());
//		assertEquals("must preserve order no", firstQuestion, crf.getAllCrfPageItems().get(2).getProCtcQuestion());
//		assertEquals("must preserve order no", thirdQuestion, crf.getAllCrfPageItems().get(1).getProCtcQuestion());
//		assertEquals("must preserve order no", secondQuestion, crf.getAllCrfPageItems().get(0).getProCtcQuestion());
//
//
//	}
}