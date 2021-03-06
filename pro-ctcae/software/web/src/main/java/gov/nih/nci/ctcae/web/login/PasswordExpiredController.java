package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author mehul gulati
 *         Date: Apr 9, 2010
 */
public class PasswordExpiredController extends SimpleFormController {

    private UserRepository userRepository;
    private UserNameAndPasswordValidator userNameAndPasswordValidator;

    protected PasswordExpiredController() {
        super();
        setCommandClass(User.class);
        setFormView("passwordExpired");
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        User user = (User) command;
        User realUser = userRepository.findByUserName(user.getUsername()).get(0);
//        realUser.setPassword(user.getNewPassword());
        userRepository.saveOrUpdate(realUser, userRepository.getEncodedPassword(user));
        return new ModelAndView(new RedirectView("../pages/j_spring_security_logout"));
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);
        User user = (User) command;
        List<User> users = userRepository.findByUserName(user.getUsername());
        if (users == null || users.size() != 1) {
            errors.reject("INVALID_USER", " The username that you have provided does not exist in the system. Please provide a valid username.");
            clearAllPasswords(user);
        } else {
            User realUser = users.get(0);
            String encodedPassword = userRepository.getEncodedPassword(user);
            if(realUser.getPassword() == null) {
            	errors.reject("PASSWORD_NOT_SETUP", "The system is not able to find any password being set up for this account." +
            			" Please use the Forgot Password link on the login screen to setup a new password.");
            	clearAllPasswords(user);
            	return;
            }
            if (!realUser.getPassword().equals(encodedPassword)) {
                errors.reject("INVALID_PASSWORD", "The old password does not match for the user name provided. Please provide correct password.");
                clearAllPasswords(user);
            } else {
                if (user.getPassword().equals(user.getNewPassword())) {
                    errors.reject("INVALID_PASSWORD", "Please provide a new password different than the current password.");
                    clearAllPasswords(user);
                } else {
//                    user.setId(realUser.getId());
                    user.setPassword(user.getNewPassword());
                    user.setUserPasswordHistory(realUser.getUserPasswordHistory());
                    user.setUserRoles(realUser.getUserRoles());
//                    userRepository.setUserPasswordWithoutSalting(user, user.getNewPassword());
                    try {
                        boolean validUser = userNameAndPasswordValidator.validate(user, false, true);
                        if (!validUser) {
                            errors.reject("INVALID_PASSWORD", userNameAndPasswordValidator.message());
                            clearAllPasswords(user);
                        }
                    } catch (PasswordCreationPolicyException ex) {
                        clearAllPasswords(user);
                        for (ValidationError ve : ex.getErrors().getErrors()) {
                            errors.reject("INVALID_PASSWORD", ve.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void clearAllPasswords(User user) {
        user.setPassword("");
        user.setNewPassword("");
        user.setConfirmPassword("");
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        this.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }
}
