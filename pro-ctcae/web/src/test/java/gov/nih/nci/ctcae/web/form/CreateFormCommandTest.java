package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CreateFormCommandTest extends WebTestCase {

	private CreateFormCommand command;
	private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQustion;
	private CrfPageItem crfItem1Page, crfItem2Page, crfItem3Page, crfItem4Page;

	FinderRepository finderRepository;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		command = new CreateFormCommand();
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


	}

	public void testConstructor() {
		StudyCrf studyCrf = command.getStudyCrf();
		assertNotNull("study crf must not be null", studyCrf);
		CRF crf = studyCrf.getCrf();
		assertNotNull("crf must not be null", crf);

		assertFalse("must have one default page", crf.getCrfPages().isEmpty());
		assertEquals("must have one default page", 1, crf.getCrfPages().size());


	}

	public void testTitle() {
		assertEquals("Click here to name", command.getTitle());


	}

	public void testAddAndReorderButNotDeleteQuestionsInMultiplePages() {

		command.addAnotherPage();
		command.addAnotherPage();
		CRF crf = command.getStudyCrf().getCrf();

		assertEquals("must have three  pages", 3, crf.getCrfPages().size());


		command.setQuestionsIds("11,12,13,14");
		command.setNumberOfQuestionsInEachPage("2,1,1");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		resetMocks();
		crf = command.getStudyCrf().getCrf();

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
		crf = command.getStudyCrf().getCrf();

		assertEquals("must have three  pages", 3, crf.getCrfPages().size());

		assertEquals("must have  1 questions ", 1, crf.getCrfPages().get(0).getCrfItemsSortedByDislayOrder().size());
		assertEquals("must have  2 question ", 2, crf.getCrfPages().get(1).getCrfItemsSortedByDislayOrder().size());
		assertEquals("must have  1 question ", 1, crf.getCrfPages().get(2).getCrfItemsSortedByDislayOrder().size());

	}

	public void testAddQuestionInMultiplePages() {

		command.addAnotherPage();
		command.addAnotherPage();
		CRF crf = command.getStudyCrf().getCrf();

		assertEquals("must have three default page", 3, crf.getCrfPages().size());


		command.setQuestionsIds("11,12,13,14");
		command.setNumberOfQuestionsInEachPage("2,1,1");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		crf = command.getStudyCrf().getCrf();

		assertEquals("must have three  page", 3, crf.getCrfPages().size());

		CRFPage crfPage = crf.getCrfPages().get(0);
		assertEquals("must have  2 questions ", 2,
			crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());

		crfPage = crf.getCrfPages().get(1);

		assertEquals("must have  1 question ", 1,
			crfPage.getCrfItemsSortedByDislayOrder().size());

		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			Integer expectedDisplayOrder = crf.getCrfPages().get(0).getCrfPageItems().size() + Integer.valueOf(i + 1);
			assertEquals("must preserve order no", expectedDisplayOrder,
				crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());

		crfPage = crf.getCrfPages().get(2);

		assertEquals("must have  1 question ", 1,
			crfPage.getCrfItemsSortedByDislayOrder().size());

		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			Integer expectedDisplayOrder = crf.getCrfPages().get(0).getCrfPageItems().size() + crf.getCrfPages().get(1).getCrfPageItems().size() + Integer.valueOf(i + 1);
			assertEquals("must preserve order no", expectedDisplayOrder,
				crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", fourthQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());


	}

	public void testAddQuestionInDefaultPageOnly() {
		command.updateCrfItems(finderRepository);

		command.setQuestionsIds("11,12");
		command.setNumberOfQuestionsInEachPage("2");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		CRF crf = command.getStudyCrf().getCrf();

		assertEquals("must have only one default page", 1, crf.getCrfPages().size());

		CRFPage crfPage = crf.getCrfPages().get(0);
		assertEquals("must have  2 questions ", 2,
			crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());


	}

	public void testAddAnotherPage() {

		command.addAnotherPage();
		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have 2 pages", 2, crf.getCrfPages().size());


	}

	public void testAddAndReorderQuestionsButNotDeleteInDefaultPage() {

		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(0, firstQuestion, 3);
		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(0, secondQuestion, 1);
		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(0, thirdQuestion, 2);
		command.setQuestionsIds("12,13,11");
		command.setNumberOfQuestionsInEachPage("3");
		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have only one default page", 1, crf.getCrfPages().size());

		CRFPage crfPage = crf.getCrfPages().get(0);

		assertEquals("must have 3 questions ", 3,
			crfPage.getCrfItemsSortedByDislayOrder().size());
		for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", firstQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(2).getProCtcQuestion());
		assertEquals("must preserve order no", thirdQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(1).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crfPage.getCrfItemsSortedByDislayOrder().get(0).getProCtcQuestion());

	}

//	public void testAddAndReorderQuestionsAndDelete() {
//
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(secondQuestion, 1);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(thirdQuestion, 2);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(fourthQuestion, 2);
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
//		CRF crf = command.getStudyCrf().getCrf();
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
//		crf = command.getStudyCrf().getCrf();
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
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(secondQuestion, 1);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(thirdQuestion, 2);
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(fourthQuestion, 2);
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
//		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(firstQuestion, 3);
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
//		CRF crf = command.getStudyCrf().getCrf();
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
