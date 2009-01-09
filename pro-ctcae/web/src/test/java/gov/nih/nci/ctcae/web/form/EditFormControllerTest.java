package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Dec 8, 2008
 */
public class EditFormControllerTest extends WebTestCase {
	private EditFormController controller;
	private WebControllerValidator validator;
	private FinderRepository finderRepository;
	private CRFRepository crfRepository;
	private ProCtcTermRepository proCtcTermRepository;
	private StudyCrf studyCrf;
	private CRF crf;
	private TabConfigurer tabConfigurer;
	private UniqueTitleForCrfValidator uniqueTitleForCrfValidator;
	private NotEmptyValidator notEmptyValidator;


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new EditFormController();
		notEmptyValidator = registerMockFor(NotEmptyValidator.class);
		uniqueTitleForCrfValidator = registerMockFor(UniqueTitleForCrfValidator.class);

		controller.setNotEmptyValidator(notEmptyValidator);
		controller.setUniqueTitleForCrfValidator(uniqueTitleForCrfValidator);
		finderRepository = registerMockFor(FinderRepository.class);
		proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
		crfRepository = registerMockFor(CRFRepository.class);
		tabConfigurer = new StaticTabConfigurer(proCtcTermRepository);
		validator = new WebControllerValidatorImpl();
		controller.setFinderRepository(finderRepository);
		controller.setCrfRepository(crfRepository);
		controller.setWebControllerValidator(validator);
		controller.setTabConfigurer(tabConfigurer);
		studyCrf = new StudyCrf();
		crf = new CRF();
		crf.setTitle("title");
		studyCrf.setCrf(crf);
		crf.setStudyCrf(studyCrf);
	}

	public void testFirstPage() throws Exception {
		ReviewFormTab reviewFormTab = new ReviewFormTab();
		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);
		replayMocks();
		ModelAndView modelAndView = controller.handleRequest(request, response);
		verifyMocks();
		Map model = modelAndView.getModel();
		assertEquals("first page should be review form", modelAndView.getViewName(), reviewFormTab.getViewName());
		Object command = model.get("command");
		assertNotNull("must find command object", command);
		assertTrue(command instanceof CreateFormCommand);

	}

	public void testShouldOnlyEditDraftForm() throws Exception {
		studyCrf.getCrf().setStatus(CrfStatus.DRAFT);
		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);
		replayMocks();
		ModelAndView modelAndView = controller.handleRequest(request, response);
		verifyMocks();
		Map model = modelAndView.getModel();
		Object command = model.get("command");
		assertNotNull("must find command object", command);

	}

	public void testMustNotEditNonDraftForm() throws Exception {
		studyCrf.getCrf().setStatus(CrfStatus.RELEASED);
		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);
		replayMocks();
		try {
			controller.handleRequest(request, response);
			fail("You can not only edit DRAFT forms.");
		} catch (CtcAeSystemException e) {

		}
		verifyMocks();

	}

	public void testProcessFinish() throws Exception {

		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");

		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);

		replayMocks();
		ModelAndView modelAndView1 = controller.handleRequest(request, response);
		verifyMocks();
		CreateFormCommand command = (CreateFormCommand) modelAndView1.getModel().get("command");
		assertNotNull("must find command object", command);
		assertNotNull("must find command object", command.getStudyCrf());

		resetMocks();
		request.setMethod("POST");

		request.addParameter("_finish", "_finish");
		expect(crfRepository.save(studyCrf.getCrf())).andReturn(studyCrf.getCrf());
		expect(notEmptyValidator.validate("title")).andReturn(true);
		expect(uniqueTitleForCrfValidator.validate(crf, crf.getTitle())).andReturn(true);
		replayMocks();

		//expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);


		ModelAndView modelAndView = controller.handleRequest(request, response);

		verifyMocks();
		Map model = modelAndView.getModel();
		assertTrue("view must be instance of redirect view", modelAndView.getView() instanceof RedirectView);
		assertNull("must not find command object", model.get("command"));


	}
}