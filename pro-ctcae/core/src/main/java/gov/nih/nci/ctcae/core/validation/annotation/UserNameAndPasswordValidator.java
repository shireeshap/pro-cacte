package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.ArrayList;

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

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
    public boolean validate(Object value) {
        if (value instanceof User) {
            User user = (User) value;
            if (user.getId() == null) {
                if (!validateUniqueName(user)) return false;
                if (!validatePasswordPolicy(user)) return false;
                if (!validatePasswordAndConfirmPassword(user)) return false;
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
            message = "user.user_exists";
            return false;
        }
    }

    private boolean validatePasswordPolicy(User user) {
        String password = user.getPassword();
        if (password == null || password.length() < 6) {
            message = "user.password_length";
            return false;
        }
        return true;
    }

    private boolean validatePasswordAndConfirmPassword(User user) {
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            message = "user.confirm_password";
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
}