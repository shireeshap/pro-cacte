package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Dec 18, 2008
 */
public class AddOneProCtcTermControllerTest extends WebTestCase {

	private AddOneProCtcTermController controller;

	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcTerm proCtcTerm;
	private CreateFormCommand command;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new AddOneProCtcTermController();
		proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
		controller.setProCtcTermRepository(proCtcTermRepository);
		proCtcTerm = new ProCtcTerm();
		command = new CreateFormCommand();


	}

	public void testSupportedMethod() {
		assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
	}

	public void testHandleRequestIfQuestionIdIsWrong() throws Exception {
		request.addParameter("proCtcTermId", new String[]{"1"});
		expect(proCtcTermRepository.findAndInitializeTerm(1)).andReturn(null);
		replayMocks();
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		assertNull("must return null because no question is present for given id", modelAndView);
	}

	public void testHandleRequestIfQuestionIdIsCorrect() throws Exception {
		request.getSession().setAttribute(CreateFormController.class.getName() + ".FORM." + "command", command);

		request.addParameter("proCtcTermId", new String[]{"1"});
		request.addParameter("displayOrder", new String[]{"1"});
		expect(proCtcTermRepository.findAndInitializeTerm(1)).andReturn(proCtcTerm);
		replayMocks();
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		assertNotNull("must not return null because there is one proCtcTerm  for given id", modelAndView);
		assertEquals("must return proCtcTerm", proCtcTerm, modelAndView.getModel().get("proCtcTerm"));
	}
}