package gov.nih.nci.ctcae.web.login;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**FailedAuthenticationControllerTest class.
 * @author Amey
 */
public class ForgotPasswordControllerTest extends AbstractWebIntegrationTestCase{
	ForgotPasswordController controller;
	BindException errors;
	Participant participant;
	ClinicalStaff clinicalStaff;
	DelegatingMessageSource messageSource;
	private static final String FORGOT_PASSWORD_VIEW_NAME = "forgotPassword";
	private static final String RESET_PASSWORD_VIEW_NAME = "passwordReset";
	private static final String USER_NAME = "username";
	private static final String DUMMY_STAFF_USER_NAME = "dummyStaffUserName";
	private static final String MESSAGE_ATTRUBUTE_NAME = "Message";
	private static final String USER_NOT_FOUND = "user.forgotpassword.usernotfound";
	private static final String EMAIL_NOT_FOUND = "user.forgotpassword.participant";
	private static final String PARTICIPANT_SENT_EMAIL_MESSAGE = "user.forgotpassword.participantWithEmail";
	private static final String CLINICIAN_SENT_EMAIL_MESSAGE = "user.forgotpassword.clinicalstaff";
	private static final String DUMMY_MESSAGE = "testMessage";

	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ForgotPasswordController();
		errors = registerMockFor(BindException.class);
		participant = ParticipantTestHelper.getDefaultParticipant();
		clinicalStaff = ClinicalStaffTestHelper.getDefaultClinicalStaff();
		messageSource = registerMockFor(DelegatingMessageSource.class);
	}
	
	public void testForgotPasswordController(){
		assertEquals(ClinicalStaff.class, controller.getCommandClass());
	}
	
	public void testShowForm() throws Exception{
		ModelAndView modelAndView = controller.showForm(request, response, errors);
		assertEquals(FORGOT_PASSWORD_VIEW_NAME, modelAndView.getViewName());
	}
	
	public void testProcessFormSubmission(){
		ModelAndView modelAndView;
		request.setParameter(USER_NAME, DUMMY_STAFF_USER_NAME);
		controller.setUserRepository(userRepository);
		
		modelAndView = controller.processFormSubmission(request, response, null, errors);
		
		assertEquals(FORGOT_PASSWORD_VIEW_NAME, modelAndView.getViewName());
		assertEquals(USER_NOT_FOUND, modelAndView.getModelMap().get(MESSAGE_ATTRUBUTE_NAME));
	}
	
	public void testProcessFormSubmission_forParticipant(){
		ModelAndView modelAndView;
		request.setParameter(USER_NAME, participant.getUser().getUsername());
		controller.setUserRepository(userRepository);
		controller.setMessageSource(messageSource);
		expect(messageSource.getMessage("username.email.salutation", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		expect(messageSource.getMessage("password.email.message.1", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);
		expect(messageSource.getMessage("password.email.subject", null, LocaleContextHolder.getLocale())).andReturn(DUMMY_MESSAGE);

		replayMocks();
		modelAndView = controller.processFormSubmission(request, response, null, errors);
		verifyMocks();
		
		assertEquals(RESET_PASSWORD_VIEW_NAME, modelAndView.getViewName());
		assertEquals(PARTICIPANT_SENT_EMAIL_MESSAGE, modelAndView.getModelMap().get(MESSAGE_ATTRUBUTE_NAME));
	}
	
	public void testProcessFormSubmission_forClinician(){
		ModelAndView modelAndView;
		request.setParameter(USER_NAME, clinicalStaff.getUser().getUsername());
		controller.setUserRepository(userRepository);
		controller.setMessageSource(messageSource);

		modelAndView = controller.processFormSubmission(request, response, null, errors);
		
		assertEquals(RESET_PASSWORD_VIEW_NAME, modelAndView.getViewName());
		assertEquals(CLINICIAN_SENT_EMAIL_MESSAGE, modelAndView.getModelMap().get(MESSAGE_ATTRUBUTE_NAME));
	}
	
}
