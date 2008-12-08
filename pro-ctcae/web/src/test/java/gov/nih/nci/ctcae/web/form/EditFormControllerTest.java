package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new EditFormController();
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
		studyCrf.setCrf(crf);
	}

	public void testFirstPage() throws Exception {
		FormDetailsTab formDetailsTab = new FormDetailsTab();
		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);
		expect(proCtcTermRepository.findAndInitializeTerm(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
		replayMocks();
		ModelAndView modelAndView = controller.handleRequest(request, response);
		verifyMocks();
		Map model = modelAndView.getModel();
		assertEquals("first page should be form details", modelAndView.getViewName(), formDetailsTab.getViewName());
		Object command = model.get("command");
		assertNotNull("must find command object", command);
		assertTrue(command instanceof CreateFormCommand);

	}

	public void testShouldOnlyEditDraftForm() throws Exception {
		studyCrf.getCrf().setStatus(CrfStatus.DRAFT);
		request.setMethod("GET");
		request.addParameter("studyCrfId", "1");
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);
		expect(proCtcTermRepository.findAndInitializeTerm(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
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

	public void testPostRequest() throws Exception {

		request.setMethod("POST");
		request.addParameter("studyCrfId", "1");
		expect(proCtcTermRepository.findAndInitializeTerm(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
		expect(finderRepository.findAndInitializeStudyCrf(Integer.valueOf(1))).andReturn(studyCrf);

		replayMocks();

		ModelAndView modelAndView = controller.handleRequest(request, response);

		verifyMocks();
		Map model = modelAndView.getModel();
//		assertTrue("view must be instance of redirect view", modelAndView.getView() instanceof RedirectView);
//		Object command = model.get("command");
//		assertNull("must not find command object", command);


	}
}