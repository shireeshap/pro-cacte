package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyServiceImpl;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Locale;

/**
 * @author mehul gulati
 *         Date: May 20, 2010
 */
public class ResetPasswordController extends SimpleFormController {

    UserRepository userRepository;
    UserNameAndPasswordValidator userNameAndPasswordValidator;
    PasswordPolicyServiceImpl passwordPolicyService;
    private DelegatingMessageSource messageSource;

	protected ResetPasswordController() {
        super();
        setCommandClass(ResetPasswordCommand.class);
        setFormView("resetPassword");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ResetPasswordCommand command = new ResetPasswordCommand();
        String token = request.getParameter("token");
        command.setUser(userRepository.findByUserToken(token));
        return command;

    }

    @Override
    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {
        ResetPasswordCommand command = (ResetPasswordCommand) e.getTarget();
        ModelAndView modelAndView = super.showForm(httpServletRequest, httpServletResponse, e);

        User user = command.getUser();
        if (user == null || (new Date().getTime() - user.getTokenTime().getTime() > 86400000)) {
            modelAndView.addObject("expired", "true");
        } else {
            modelAndView.addObject("passwordPolicy", passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy()));
        }
        return modelAndView;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        super.onBindAndValidate(request, o, e);
        ResetPasswordCommand command = (ResetPasswordCommand) o;

        User cmdUser = command.getUser();

        User user = new User();
        user.setUsername(command.getUsername());
        user.setPassword(command.getPassword());
        user.setConfirmPassword(command.getConfirmPassword());

        if (!user.getUsername().equals(command.getUsername())) {
        	Locale locale = LocaleContextHolder.getLocale();
        	String errorMsg = messageSource.getMessage("resetpwd.error.1", null, locale);
            e.reject("username", errorMsg);
        }
        try {
            boolean validUser = userNameAndPasswordValidator.validate(user, false, true);
            if (!validUser) {
                e.reject("username", userNameAndPasswordValidator.message());
            }
        } catch (PasswordCreationPolicyException ex) {
            for (ValidationError ve : ex.getErrors().getErrors()) {
                e.reject("password", ve.getMessage());
            }
        }
    }
    

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        ResetPasswordCommand command = (ResetPasswordCommand) o;
        User user = command.getUser();
        user.setPassword(command.getPassword());
        user.setToken(null);
        userRepository.saveWithoutCheck(user);
        ModelAndView modelAndView = new ModelAndView("passwordReset");
        modelAndView.addObject("Message", "user.password.reset");
        return modelAndView;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        this.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyServiceImpl passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }
    

    public void setMessageSource(DelegatingMessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
