package gov.nih.nci.ctcae.web.login;

import java.util.Properties;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import static org.easymock.EasyMock.expect;

/**ForgotUsernameControllerTest class
 * @author Amey
 */
public class ForgotUsernameControllerTest extends AbstractWebIntegrationTestCase{
	ForgotUsernameController controller;
	BindException errors;
	DelegatingMessageSource messageSource;
	Properties properties;
	Participant participant;
	ClinicalStaff clinicalStaff;
	private static final String VIEW_NAME = "forgotUsername";
	private static final String DUMMY_MODE = "testMode";
	private static final String MODE = "mode";
	private static final String PARAM_EMAIL = "email";
	private static final String SHOW_CONFIRMATION_ATTRIBUTE = "showConfirmation";
	private static final String DUMMY_MESSAGE = "testMessage";
	
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ForgotUsernameController();
		errors = registerMockFor(BindException.class);
		messageSource = registerMockFor(DelegatingMessageSource.class);
		properties = registerMockFor(Properties.class);
		controller.setMessageSource(messageSource);
		controller.setProCtcAEProperties(properties);
		controller.setGenericRepository(genericRepository);
		participant = ParticipantTestHelper.getDefaultParticipant();
		clinicalStaff = ClinicalStaffTestHelper.getDefaultClinicalStaff();
	}
	
	public void testForgotUsernameController(){
		controller = new ForgotUsernameController();
		assertEquals(ClinicalStaff.class, controller.getCommandClass());
	}
	
	public void testShowForm() throws Exception{
		ModelAndView modelAndView;
		expect(properties.getProperty("mode.nonidentifying")).andReturn(DUMMY_MODE);

		replayMocks();
		modelAndView = controller.showForm(request, response, errors);
		verifyMocks();
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals(DUMMY_MODE, modelAndView.getModelMap().get(MODE));
	}
	
	public void testProcessFormSubmission_forClinicalStaff() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(PARAM_EMAIL, clinicalStaff.getEmailAddress());
		
		replayMocks();
		modelAndView = controller.processFormSubmission(request, response, null, errors);
		verifyMocks();
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertTrue((Boolean) modelAndView.getModelMap().get(SHOW_CONFIRMATION_ATTRIBUTE));
	}
	
	public void testProcessFormSubmission_forParticipant() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(PARAM_EMAIL, participant.getEmailAddress());
		expect(messageSource.getMessage("username.email.salutation", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		expect(messageSource.getMessage("username.email.message.1", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		expect(messageSource.getMessage("username.email.message.2", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		expect(messageSource.getMessage("username.email.subject", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		
		replayMocks();
		modelAndView = controller.processFormSubmission(request, response, null, errors);
		verifyMocks();
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertTrue((Boolean) modelAndView.getModelMap().get(SHOW_CONFIRMATION_ATTRIBUTE));
	}
}
