package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CreateFormCommandTest extends WebTestCase {

    private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQustion, sixthQuestion;
    private CrfPageItem crfItem1Page, crfItem2Page, crfItem3Page, crfItem4Page;
    private ProCtcTerm proCtcTerm1, proCtcTerm2;
    FinderRepository finderRepository;
    private CreateFormCommand command;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new CreateFormCommand();
        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
        finderRepository = registerMockFor(FinderRepository.class);

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
        proCtcTerm1.addProCtcQuestion(firstQuestion);
        proCtcTerm1.addProCtcQuestion(secondQuestion);
        proCtcTerm1.addProCtcQuestion(thirdQuestion);

        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.addProCtcQuestion(fourthQuestion);
        proCtcTerm2.addProCtcQuestion(sixthQuestion);

    }

    public void testAddProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPages().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testAddMultipleProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        CRF crf = command.getCrf();
        command.updateCrfItems(finderRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPages().size());

        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testReOrderCrfPagesInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        command.setCrfPageNumbers("1,0");

        CRF crf = command.getCrf();
        command.updateCrfItems(finderRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPageItemByQuestion(proCtcTerm1.getProCtcQuestions().iterator().next()).getCrfPage();
        assertEquals("must reorder crf page number", Integer.valueOf(1), crfPage.getPageNumber());

        crfPage = crf.getCrfPageItemByQuestion(proCtcTerm2.getProCtcQuestions().iterator().next()).getCrfPage();

        assertEquals("must reorder crf page number", Integer.valueOf(0), crfPage.getPageNumber());

        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testAddDeleteAddAndReOrderCrfPagesInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);

        //now remove one page
        command.setQuestionIdsToRemove(firstQuestion.getId() + "," + secondQuestion.getId() + "," + thirdQuestion.getId());

        assertEquals("proctc term 1 must have 3 questions only", 3, proCtcTerm1.getProCtcQuestions().size());

        command.addProCtcTerm(proCtcTerm1);

        CRF crf = command.getCrf();

        assertEquals("must add one more page because 1st page is empty", 3, crf.getCrfPages().size());

        command.setCrfPageNumbers("2,1");


        command.updateCrfItems(finderRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPageItemByQuestion(proCtcTerm1.getProCtcQuestions().iterator().next()).getCrfPage();
        assertEquals("must reorder crf page number", Integer.valueOf(0), crfPage.getPageNumber());

        crfPage = crf.getCrfPageItemByQuestion(proCtcTerm2.getProCtcQuestions().iterator().next()).getCrfPage();

        assertEquals("must reorder crf page number", Integer.valueOf(1), crfPage.getPageNumber());

        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testRemoveQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPages().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);

        //now remove 1 question

        command.setQuestionIdsToRemove(String.valueOf(secondQuestion.getId()));
        expect(finderRepository.findAndInitializeProCtcQuestion(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(finderRepository);
        verifyMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testRemoveAndAddSameQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        List<CrfPageItem> crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());

        //now remove 1 question
        command.setQuestionIdsToRemove(String.valueOf(secondQuestion.getId()));

        expect(finderRepository.findAndInitializeProCtcQuestion(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(finderRepository);
        verifyMocks();

        resetMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());

        //now add same question again
        assertEquals("must not add any more page", 1, crf.getCrfPages().size());

        command.addProCtcTerm(proCtcTerm1);
        assertEquals("must add only 1 question", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    public void testAddAnotherPageInBasicFormMode() {
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);

        try {
            command.addCrfPage();
            fail("You can not add new page in basic form creation mode.");
        } catch (CtcAeSystemException e) {

        }

    }


    public void testConstructor() {
        CRF crf = command.getCrf();
        assertNotNull("study crf must not be null", crf);
        assertNotNull("crf must not be null", crf);


    }

    public void testTitle() {
        assertEquals("Click here to name", command.getTitle());


    }

    public void testRemoveCrfPageWithQuestionsMustRemoveQuestionAlso() {

        command.addCrfPage();
        command.addCrfPage();
        command.addCrfPage();
        CRF crf = command.getCrf();

        assertEquals("must have three  pages", 3, crf.getCrfPages().size());


        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,1,1");
        command.setCrfPageNumbers("0,1,2");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);
        assertEquals("must have three  page", 3, crf.getCrfPages().size());

        assertEquals("must have  2 questions ", 2, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 1, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 1, crf.getCrfPages().get(2).getCrfItemsSortedByDislayOrder().size());


        //now remove 2nd page
        command.setQuestionsIds("11,12,14");


        command.setNumberOfQuestionsInEachPage("1,2");
        command.setCrfPageNumbersToRemove("1");

        command.setCrfPageNumbers("0,1");
        //command.clearCrfPage(1);

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        assertEquals("must have 2  pages", 2, crf.getCrfPages().size());

        assertEquals("must have  1 questions ", 1, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  2 question ", 2, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());
        for (CrfPageItem crfPageItem : crf.getAllCrfPageItems()) {
            assertNotSame("must remove the questions also when you remove a crf page", thirdQuestion, crfPageItem.getProCtcQuestion());
        }

        validateCrfPageAndCrfPageItemOrder(crf);

    }


    public void testAddAndReorderButNotDeleteQuestionsInMultiplePages() {

        command.addCrfPage();
        command.addCrfPage();
        command.addCrfPage();
        CRF crf = getCrf();

        assertEquals("must have three  pages", 3, crf.getCrfPages().size());


        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,1,1");
        command.setCrfPageNumbers("0,1,2");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);
        assertEquals("must have three  page", 3, crf.getCrfPages().size());

        assertEquals("must have  2 questions ", 2, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 1, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 1, crf.getCrfPages().get(2).getCrfItemsSortedByDislayOrder().size());


        //now reorder questions

        command.setQuestionsIds("14,11,13,12");
        command.setNumberOfQuestionsInEachPage("1,2,1");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        assertEquals("must have three  pages", 3, crf.getCrfPages().size());

        assertEquals("must have  1 questions ", 1, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  2 question ", 2, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 1, crf.getCrfPages().get(2).getCrfItemsSortedByDislayOrder().size());

        validateCrfPageAndCrfPageItemOrder(crf);

    }


    public void testAddAndUpdateAndReorderButNotDeleteQuestionsInMultiplePages() {

        command.addCrfPage();
        command.addCrfPage();

        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,2");
        command.setCrfPageNumbers("0,1");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();

        CRF crf = getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have  2 questions ", 2, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
        assertEquals("must have  1 question ", 2, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());

        //now update and reorder questions
        CrfPageItem crfPageItem = crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().get(1);
        crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
        crfPageItem.setInstructions("inst");
        crfPageItem.setResponseRequired(Boolean.TRUE);
        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule);

        command.setQuestionsIds("14,11,13,12");
        command.setNumberOfQuestionsInEachPage("1,3");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have  1 questions ", 1, crfPage.getCrfItemsSortedByDislayOrder().size());
        assertSame("must preserve the order number  while moving the crf page items", fourthQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(1);
        assertEquals("must have  3 question ", 3, crfPage.getCrfItemsSortedByDislayOrder().size());
        assertSame("must preserve the order number  while moving the crf page items", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertSame("must preserve the order number  while moving the crf page items", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
        assertSame("must preserve the order number  while moving the crf page items", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());

        CrfPageItem movedCrfPageItem = crfPage.getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
        assertSame("must update and move crf page items between pages", movedCrfPageItem, crfPageItem);
        assertSame("must update and move crf page items between pages", movedCrfPageItem.getCrfPage(), crfPage);
        assertSame("must update and move crf page items between pages", crfPageItem.getCrfPage(), crfPage);

        validateCrfPageAndCrfPageItemOrder(crf);

    }

    public void testReOrderPageAndReorderCrfPageItem() {

        command.addCrfPage();
        command.addCrfPage();

        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,2");
        command.setCrfPageNumbers("0,1");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();

        CRF crf = getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);


        //now update and reorder questions  and reoder pages also

        command.setQuestionsIds("14,11,13,12");
        command.setNumberOfQuestionsInEachPage("1,3");
        command.setCrfPageNumbers("1,0");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        validateCrfPageAndCrfPageItemOrder(crf);

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have 3  crf items", 3, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(1);

        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());


    }

    public void testReOrderCrfPageItemInASinglePage() {

        command.addCrfPage();

        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("4");
        command.setCrfPageNumbers("0");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();

        CRF crf = getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);


        //now update and reorder questions  and reoder pages also

        command.setQuestionsIds("14,11,13,12");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        validateCrfPageAndCrfPageItemOrder(crf);

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have 4  crf items", 4, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(3).getProCtcQuestion());


    }

    public void testDeleteAndReorderPageAndReorderAndDeleteCrfPageItem() {

        command.addCrfPage();
        command.addCrfPage();
        command.addCrfPage();

        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,1,1");
        command.setCrfPageNumbers("0,1,2");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();

        CRF crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);


        //now reorder questions  and reoder pages also   and delete one page  and delte one question also

        command.setQuestionsIds("14,11,12");
        command.setNumberOfQuestionsInEachPage("1,2");
        command.setCrfPageNumbers("2,0");
        command.setCrfPageNumbersToRemove("1");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();

        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have 2  crf pages only because 1 crf page is removed", 2, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have 2  crf items", 2, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(1);

        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());


    }


    public void testAddQuestionInMultiplePages() {

        command.addCrfPage();
        command.addCrfPage();
        command.addCrfPage();
        CRF crf = command.getCrf();

        assertEquals("must have three default page", 3, crf.getCrfPages().size());


        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,1,1");
        command.setCrfPageNumbers("0,1,2");


        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have three  page", 3, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have  2 questions ", 2, crfPage.getCrfItemsSortedByDislayOrder().size());

        assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(1);

        assertEquals("must have  1 question ", 1, crfPage.getCrfItemsSortedByDislayOrder().size());


        assertEquals("must preserve order no", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(2);

        assertEquals("must have  1 question ", 1, crfPage.getCrfItemsSortedByDislayOrder().size());
        assertEquals("must preserve order no", fourthQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());


    }

    public void testAddAndUpdateAndReorderAndDeleteQuestionsInFirstPageOnly() {
        command.addCrfPage();

        command.setQuestionsIds("11,12,14,13");
        command.setNumberOfQuestionsInEachPage("4");
        command.setCrfPageNumbers("0");


        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(14))).andReturn(fourthQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();

        CRF crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have  4 questions ", 4, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());


        //now reorder and update and delete questions
        CrfPageItem crfPageItem = crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().get(1);
        crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
        crfPageItem.setInstructions("inst");
        crfPageItem.setResponseRequired(Boolean.TRUE);
        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule);

        command.setQuestionsIds("11,13,12");
        command.setNumberOfQuestionsInEachPage("3");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();


        CRFPage crfPage = crf.getCrfPages().get(0);
        assertEquals("must have  3 question ", 3, crfPage.getCrfItemsSortedByDislayOrder().size());

        validateCrfPageAndCrfPageItemOrder(crf);

        CrfPageItem movedCrfPageItem = crfPage.getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
        assertSame("must update and move crf page items between pages", movedCrfPageItem, crfPageItem);
        assertSame("must update and move crf page items between pages", movedCrfPageItem.getCrfPage(), crfPage);
        assertSame("must update and move crf page items between pages", crfPageItem.getCrfPage(), crfPage);


    }

    public void testAddQuestionInFirstPageOnly() {
        command.addCrfPage();
        command.updateCrfItems(finderRepository);

        command.setQuestionsIds("11,12");
        command.setNumberOfQuestionsInEachPage("2");
        command.setCrfPageNumbers("0");


        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        CRF crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have only one default page", 1, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPages().get(0);
        assertEquals("must have  2 questions ", 2, crfPage.getCrfItemsSortedByDislayOrder().size());
        assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
        assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());


    }

    public void testReOrderPageButDoNotReorderCrfPageItem() {
        command.addCrfPage();
        command.addCrfPage();
        command.addCrfPage();
        CRF crf = command.getCrf();

        assertEquals("must have three default page", 3, crf.getCrfPages().size());


        command.setQuestionsIds("11,12,13,14");
        command.setNumberOfQuestionsInEachPage("2,1,1");
        command.setCrfPageNumbers("0,1,2");


        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        resetMocks();
        crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have three  page", 3, crf.getCrfPages().size());

        //now reorder the crf page
        command.setCrfPageNumbers("2,0,1");


        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        crf = command.getCrf();
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have three  page", 3, crf.getCrfPages().size());

        CRFPage crfPage = crf.getCrfPages().get(0);

        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", thirdQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(1);

        assertEquals("must have 1  crf items", 1, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", fourthQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());

        crfPage = crf.getCrfPages().get(2);
        assertEquals("must have two  crf items", 2, crfPage.getCrfPageItems().size());
        assertSame("must move the questions also while reordering the pages", firstQuestion, crfPage.getCrfPageItems().get(0).getProCtcQuestion());
        assertSame("must move the questions also while reordering the pages", secondQuestion, crfPage.getCrfPageItems().get(1).getProCtcQuestion());


    }


    public void testAddAnotherPageInAdvanceFormMode() {
        command.addCrfPage();
        command.addCrfPage();
        CRF crf = getCrf();
        assertEquals("must have 2 pages", 2, crf.getCrfPages().size());
        command.addCrfPage();

        crf = command.getCrf();
        assertEquals("must have 3 pages", 3, crf.getCrfPages().size());


    }


    public void testAdddingProCtcTermInBasicModeAgainAfterUpdatingCrfPageItem() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPages().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 4 crf page items", 4, crfPageItems.size());

        //now update one crf page item
        CrfPageItem crfPageItem = crfPageItems.get(0);
        crfPageItem.setInstructions("inst");
        crfPageItem.setResponseRequired(Boolean.TRUE);

        //now add pro cterm again

        command.addProCtcTerm(proCtcTerm1);
        crf = command.getCrf();

        assertEquals("must not add any more pages", 1, crf.getCrfPages().size());

        crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must not add any crf page items", 4, crfPageItems.size());
        assertEquals("must not remove existing crf page items", crfPageItem, crfPageItems.get(0));

        validateCrfPageAndCrfPageItemOrder(crf);

    }

    public void testAdddingProCtcTermInBasicModeAgainAfterUpdatingAndRemovingCrfPageItem() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPages().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 4 crf page items", 4, crfPageItems.size());


        //now update one crf page item
        CrfPageItem crfPageItem = crfPageItems.get(0);
        crfPageItem.setInstructions("inst");
        crfPageItem.setResponseRequired(Boolean.TRUE);

        //now remove this crf page item;
        command.setQuestionIdsToRemove(String.valueOf(firstQuestion.getId()));

        crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());

        //now add pro cterm again

        command.addProCtcTerm(proCtcTerm1);
        crf = command.getCrf();

        assertEquals("must not add any more pages", 1, crf.getCrfPages().size());

        crfPageItems = crf.getCrfPages().get(0).getCrfPageItems();
        assertEquals("must  add one crf page items", 4, crfPageItems.size());
        assertFalse("must  add a new empty crf page items", crfPageItem.equals(crfPageItems.get(0)));
        validateCrfPageAndCrfPageItemOrder(crf);


    }

    private CRF getCrf() {
        CRF crf = command.getCrf();
        return crf;
    }

    public void testAddAndReorderQuestionsButNotDeleteInFirstPage() {
        command.addCrfPage();
        getCrf().addOrUpdateCrfItemInCrfPage(0, secondQuestion, 0);
        getCrf().addOrUpdateCrfItemInCrfPage(0, thirdQuestion, 1);
        getCrf().addOrUpdateCrfItemInCrfPage(0, firstQuestion, 2);

        command.setQuestionsIds("12,13,11");
        command.setCrfPageNumbers("0,1,2");

        command.setNumberOfQuestionsInEachPage("3");
        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        CRF crf = getCrf();
        assertEquals("must have only one default page", 1, crf.getCrfPages().size());
        validateCrfPageAndCrfPageItemOrder(crf);

        CRFPage crfPage = crf.getCrfPages().get(0);
        validateCrfPageAndCrfPageItemOrder(crf);

        assertEquals("must have 3 questions ", 3, crfPage.getCrfItemsSortedByDislayOrder().size());
        assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());
        assertEquals("must preserve order no", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
        assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());

    }


//	public void testAddAndReorderQuestionsAndDelete() {
//
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(secondQuestion, 1);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(thirdQuestion, 2);
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(fourthQuestion, 2);
//		command.setQuestionsIds("12,13,11,14");
//
//		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//		CRF crf = command.getCRF().getCrf();
//		assertEquals("must have 4 questions ", 4,
//			crf.getCrfItemsSortedByDislayOrder().size());
//		for (int i = 0; i < crf.getCrfItemsSortedByDislayOrder().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
//
//		}
//		assertEquals("must preserve order no", firstQuestion, crf.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());
//		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
//		assertEquals("must preserve order no", secondQuestion, crf.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
//		assertEquals("must preserve order no", fourthQuestion, crf.getCrfItemsSortedByDislayOrder().get(3).getProCtcQuestion());
//
//		//now delete the first question
//		command.setQuestionsIds("12,14,13");
//		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//		crf = command.getCRF().getCrf();
//		assertEquals("must have 3 questions ", 3,
//			crf.getCrfItemsSortedByDislayOrder().size());
//		for (int i = 0; i < crf.getCrfItemsSortedByDislayOrder().size(); i++) {
//			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());
//
//		}
//		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());
//		assertEquals("must preserve order no", fourthQuestion, crf.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
//		assertEquals("must preserve order no", secondQuestion, crf.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
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
//		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
//		replay(finderRepository);
//		command.updateCrfItems(finderRepository);
//		verify(finderRepository);
//		resetMocks();
//
//		//now delete the first question and add again
//		command.getCRF().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.setQuestionsIds("12,13,11,14");
//
//		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
//		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
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
