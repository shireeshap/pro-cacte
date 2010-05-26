package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class UniqueIdentifierForStudyValidator.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class UserNameAndPasswordValidator extends AbstractValidator<UserNameAndPassword> {

    /**
     * The message.
     */
    private String message;

    /**
     * The study repository.
     */
    private UserRepository userRepository;
    PasswordPolicyService passwordPolicyService;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
    public boolean validate(Object value) {
        return validate(value, true, true);
    }

    public boolean validate(Object value, boolean validateUserName, boolean validatePassword) {
        if (value instanceof User) {
            User user = (User) value;
            if (user.getId() == null && validateUserName) {
                if (!validateUserNameLength(user)) return false;
                if (!validateUniqueName(user)) return false;
            }
            if (validatePassword) {
                if (!validatePasswordAndConfirmPassword(user)) return false;
                if (!validatePasswordPolicy(user)) return false;
            }
        }
        return true;
    }

    private boolean validateUniqueName(User user) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(user.getUsername());
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        if ((users == null || users.isEmpty())) {
            return true;
        } else {
            message = "Username already exists. Please choose another username.";
            return false;
        }
    }

    private boolean validateUserNameLength(User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            message = "Username cannot be empty.";
            return false;
        }
        if (user.getUsername().length() < 6) {
            message = "Username must be at least six characters long.";
            return false;
        }
        return true;
    }

    private boolean validatePasswordPolicy(User user) {
        return passwordPolicyService.validatePasswordAgainstCreationPolicy(user);
    }

    private boolean validatePasswordAndConfirmPassword(User user) {
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            message = "Password does not match confirm password.";
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(UserNameAndPassword parameters) {
        message = parameters.message();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
     */
    public String message() {
        return message;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }
}