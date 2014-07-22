package gov.nih.nci.ctcae.web.login;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

/**PasswordExpiredControllerTest class.
 * @author Amey
 */
public class PasswordExpiredControllerTest extends AbstractWebIntegrationTestCase{
	PasswordExpiredController controller;
	Participant participant;
	BindException errors;
	private static final String VIEW_NAME = "passwordExpired";
	private static final String LOGOUT_VIEW_NAME = "../pages/j_spring_security_logout";
	private static final String DUMMY_USER_NAME = "dummyUserName";
	private static final String DUMMY_PASSWORD = "dummyPassword";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new PasswordExpiredController();
		participant = ParticipantTestHelper.getDefaultParticipant();
		errors = new BindException(controller, controller.getClass().getName());
		controller.setUserRepository(userRepository);
		controller.setUserNameAndPasswordValidator(userNameAndPasswordValidator);
	}
	
	public void testPasswordExpiredController(){
		controller = new PasswordExpiredController();
		
		assertEquals(User.class, controller.getCommandClass());
		assertEquals(VIEW_NAME, controller.getFormView());
		assertTrue(controller.isSessionForm());
	}
	
	public void testOnSubmit() throws Exception{
		ModelAndView modelAndView;
		modelAndView = controller.onSubmit(request, response, participant.getUser(), errors);
		
		assertEquals(LOGOUT_VIEW_NAME, ((RedirectView) modelAndView.getView()).getUrl());
	}
	
	public void testOnBindAndValidate_invalidUser() throws Exception{
		User user = new User();
		// No valid user with this user name present in the system
		user.setUsername(DUMMY_USER_NAME);
		
		controller.onBindAndValidate(request, user, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testOnBindAndValidate_inCorrectOldPassword() throws Exception{
		User user = new User();
		// Valid username but old password does not match the records
		user.setUsername(participant.getUser().getUsername());
		user.setPassword(DUMMY_PASSWORD);
		
		controller.onBindAndValidate(request, user, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testOnBindAndValidate_usedPassword() throws Exception{
		User user = new User();
		// Re-using the previous password
		user.setUsername(participant.getUser().getUsername());
		user.setPassword(TestDataManager.DEFAULT_PASSWORD);
		user.setNewPassword(TestDataManager.DEFAULT_PASSWORD);
		
		controller.onBindAndValidate(request, user, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testOnBindAndValidate_invalidNewPassword() throws Exception{
		User user = new User();
		// Re-using the previous password
		user.setUsername(participant.getUser().getUsername());
		user.setPassword(TestDataManager.DEFAULT_PASSWORD);
		String newPassword = user.getUsername();
		user.setNewPassword(newPassword);
		user.setConfirmPassword(newPassword);
		
		controller.onBindAndValidate(request, user, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testOnBindAndValidate_validNewPassword() throws Exception{
		User user = new User();
		// Re-using the previous password
		user.setUsername(participant.getUser().getUsername());
		user.setPassword(TestDataManager.DEFAULT_PASSWORD);
		String newPassword = TestDataManager.DEFAULT_PASSWORD + "3!";
		user.setNewPassword(newPassword);
		user.setConfirmPassword(newPassword);
		
		controller.onBindAndValidate(request, user, errors);
		
		assertFalse(errors.hasErrors());
	}
}
