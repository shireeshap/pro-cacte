package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
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
	private CrfItem crfItem1, crfItem2, crfItem3, crfItem4;

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

		crfItem1 = new CrfItem();
		crfItem1.setProCtcQuestion(firstQuestion);
		crfItem2 = new CrfItem();
		crfItem2.setProCtcQuestion(secondQuestion);
		crfItem3 = new CrfItem();
		crfItem3.setProCtcQuestion(thirdQuestion);
		crfItem4 = new CrfItem();
		crfItem4.setProCtcQuestion(fourthQuestion);

		fifthQustion = new ProCtcQuestion();
		fifthQustion.setId(15);
		fifthQustion.setQuestionText("sample question1");


	}

	public void testConstructor() {
		assertNotNull("study crf must not be null", command.getStudyCrf());
		assertNotNull("crf must not be null", command.getStudyCrf().getCrf());

	}

	public void testTitle() {
		assertEquals("Click here to name", command.getTitle());


	}

	public void testUpdateQuestions() {
		command.updateCrfItems(finderRepository);
		assertTrue(command.getStudyCrf().getCrf().getCrfItems().isEmpty());
		command.setQuestionsIds("11,12,15");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(11))).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 12)).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 15)).andReturn(fifthQustion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have only 2 questions because  first and 5th questions are same", 2,
			crf.getCrfItems().size());


	}

	public void testAddAndReorderQuestionsButNotDelete() {

		command.getStudyCrf().getCrf().addOrUpdateCrfItem(firstQuestion, 3);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(secondQuestion, 1);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(thirdQuestion, 2);
		command.setQuestionsIds("12,13,11");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have 3 questions ", 3,
			crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", firstQuestion, crf.getCrfItems().get(2).getProCtcQuestion());
		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItems().get(1).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crf.getCrfItems().get(0).getProCtcQuestion());

	}

	public void testAddAndReorderQuestionsAndDelete() {

		command.getStudyCrf().getCrf().addOrUpdateCrfItem(firstQuestion, 3);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(secondQuestion, 1);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(thirdQuestion, 2);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(fourthQuestion, 2);
		command.setQuestionsIds("12,13,11,14");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		resetMocks();
		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have 4 questions ", 4,
			crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", firstQuestion, crf.getCrfItems().get(2).getProCtcQuestion());
		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItems().get(1).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crf.getCrfItems().get(0).getProCtcQuestion());
		assertEquals("must preserve order no", fourthQuestion, crf.getCrfItems().get(3).getProCtcQuestion());

		//now delete the first question
		command.setQuestionsIds("12,14,13");
		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		resetMocks();
		crf = command.getStudyCrf().getCrf();
		assertEquals("must have 3 questions ", 3,
			crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItems().get(2).getProCtcQuestion());
		assertEquals("must preserve order no", fourthQuestion, crf.getCrfItems().get(1).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crf.getCrfItems().get(0).getProCtcQuestion());


	}

	public void testAddAndReorderAndDeleteAndAddAgain() {

		command.getStudyCrf().getCrf().addOrUpdateCrfItem(firstQuestion, 3);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(secondQuestion, 1);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(thirdQuestion, 2);
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(fourthQuestion, 2);
		command.setQuestionsIds("12,13,11,14");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);
		resetMocks();

		//now delete the first question and add again
		command.getStudyCrf().getCrf().addOrUpdateCrfItem(firstQuestion, 3);
		command.setQuestionsIds("12,13,11,14");

		expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(12))).andReturn(secondQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 13)).andReturn(thirdQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 11)).andReturn(firstQuestion);
		expect(finderRepository.findById(ProCtcQuestion.class, 14)).andReturn(fourthQuestion);
		replay(finderRepository);
		command.updateCrfItems(finderRepository);
		verify(finderRepository);

		CRF crf = command.getStudyCrf().getCrf();
		assertEquals("must have 4 questions ", 4,
			crf.getCrfItems().size());
		for (int i = 0; i < crf.getCrfItems().size(); i++) {
			assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

		}
		assertEquals("must preserve order no", fourthQuestion, crf.getCrfItems().get(3).getProCtcQuestion());
		assertEquals("must preserve order no", firstQuestion, crf.getCrfItems().get(2).getProCtcQuestion());
		assertEquals("must preserve order no", thirdQuestion, crf.getCrfItems().get(1).getProCtcQuestion());
		assertEquals("must preserve order no", secondQuestion, crf.getCrfItems().get(0).getProCtcQuestion());


	}
}
