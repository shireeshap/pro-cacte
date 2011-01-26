package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;

import java.util.Collection;
import java.util.List;

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
        realUser.setPassword(user.getNewPassword());
        userRepository.saveWithoutCheck(realUser);
        return new ModelAndView(new RedirectView("../pages/j_spring_security_logout"));
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);
        User user = (User) command;
        List<User> users = userRepository.findByUserName(user.getUsername());
        if (users == null || users.size() != 1) {
            errors.reject("INVALID_USER", "The username that you have provided does not exist in the system. Please provide a valid username.");
            clearAllPasswords(user);
        } else {
            User realUser = users.get(0);
            String encodedPassword = userRepository.getEncodedPassword(user);
            if (!realUser.getPassword().equals(encodedPassword)) {
                errors.reject("INVALID_PASSWORD", "The old password does not match for the user name provided. Please provide correct password.");
                clearAllPasswords(user);
            } else {
                if (user.getPassword().equals(user.getNewPassword())) {
                    errors.reject("INVALID_PASSWORD", "Please provide a new password different than the current password.");
                    clearAllPasswords(user);
                } else {
                    user.setId(-1000);
                    user.setPassword(user.getNewPassword());
                    try {
                        boolean validUser = userNameAndPasswordValidator.validate(user);
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
