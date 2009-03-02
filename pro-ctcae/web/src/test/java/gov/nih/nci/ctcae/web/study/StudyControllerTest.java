package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class StudyControllerTest extends WebTestCase {
	private StudyController controller;
	private WebControllerValidator validator;
	private StudyRepository studyRepository;

	private Study study;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new CreateStudyController();
		validator = new WebControllerValidatorImpl();
		studyRepository = registerMockFor(StudyRepository.class);
		controller.setStudyRepository(studyRepository);
		controller.setWebControllerValidator(validator);

		study = new Study();
	}

	public void testGetRequest() throws Exception {
		request.setMethod("GET");
		ModelAndView modelAndView = controller.handleRequest(request, response);
		Map model = modelAndView.getModel();
		Object command = model.get("command");
		assertNotNull("must find command object", command);
		assertTrue(command instanceof StudyCommand);

	}

	public void testPostRequest() throws Exception {
		request.setMethod("GET");

		controller.handleRequest(request, response);
		assertNotNull("must have command in session", ControllersUtils.getStudyCommand(request));
		request.setMethod("POST");
		request.setParameter("_finish", "true");
		expect(studyRepository.save(isA(Study.class))).andReturn(study);

		replayMocks();

		ModelAndView modelAndView = controller.handleRequest(request, response);

		verifyMocks();
		Map model = modelAndView.getModel();
		Object command = model.get("command");
		assertNotNull("must  find command object", command);


	}

	public void testPostRequestForSite() throws Exception {
		request.setMethod("GET");

		controller.handleRequest(request, response);
		assertNotNull("must have command in session", ControllersUtils.getStudyCommand(request));


		request.setMethod("POST");
		request.setAttribute(controller.getClass().getName() + ".PAGE." + controller.getCommandName(), 1);


		ModelAndView modelAndView = controller.handleRequest(request, response);

		Map model = modelAndView.getModel();
		Object command = model.get("command");
		assertNotNull("must  find command object", command);


	}
}