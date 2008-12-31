package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Nov 17, 2008
 */
public class FindCrfItemControllerTest extends WebTestCase {
	private FindCrfItemController controller;
	private CreateFormCommand command;
	private FinderRepository finderRepository;
	private ProCtcQuestion proCtcQuestion;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new FindCrfItemController();
		finderRepository = registerMockFor(FinderRepository.class);

		command = new CreateFormCommand();
		controller.setFinderRepository(finderRepository);
		proCtcQuestion = new ProCtcQuestion();
		proCtcQuestion.setId(1);
	}

	public void testSupportedMethod() throws Exception {
		assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
	}

	public void testGetRequest() throws Exception {
		request.getSession().setAttribute(CreateFormController.class.getName() + ".FORM." + "command", command);
		request.setMethod("GET");
		request.setParameter("questionId", "1");
		request.setParameter("nextQuestionIndex", "2");
		request.setParameter("previousQuestionIndex", "0");

		command.addAnotherPage();
		command.getStudyCrf().getCrf().addOrUpdateCrfItemInCrfPage(0, proCtcQuestion, 1);
		expect(finderRepository.findById(ProCtcQuestion.class, 1)).andReturn(proCtcQuestion);
		replayMocks();
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();

		assertNotNull(modelAndView.getModel().get("crfPageItem"));
		assertNotNull(modelAndView.getModel().get("nextQuestionIndex"));
		assertNotNull(modelAndView.getModel().get("previousQuestionIndex"));
	}
}