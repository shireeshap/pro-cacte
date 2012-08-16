package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.CombinationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private String message = "";

    /**
     * The User repository.
     */
    private UserRepository userRepository;
    PasswordPolicyService passwordPolicyService;
    /**
     * The messageSource
     */
    protected MessageSource messageSource;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */

    public boolean validate(Object value) {
        return validate(value, true, true);
    }

    /**
     *
     * @param value
     * @param validateUserName
     * @param validatePassword
     * @return  false if error occurs and vise versa
     */
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

    /**
     *
     * @param user  The User
     * @return false if error occurs and vise versa
     */
    public boolean validateUniqueName(User user) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(user.getUsername());
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        if ((users == null || users.isEmpty())) {
            return true;
        } else {
            message = messageSource.getMessage("user.user_exists",new Object[] {}, Locale.getDefault());
            return false;
        }
    }

    /**
     *
     * @param userName  the User name
     * @param userId    the User id
     * @return  ture if user name already exists. 
     */
    public boolean validateDwrUniqueName(String userName, Integer userId) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName);
        userQuery.excludeByUserId(userId);
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        if ((users == null || users.isEmpty())) {
            return false;
        } else {
            message = messageSource.getMessage("user.user_exists",new Object[] {}, Locale.getDefault());
            return true;
        }
    }

    /**
     *
     * @param user The user
     * @return false if error with user name
     */
    private boolean validateUserNameLength(User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            message = "Username cannot be empty.";
            return false;
        }
        if (user.getUsername().length() < 6) {
            message = messageSource.getMessage("clinicalStaff.username_length",new Object[] {}, Locale.getDefault());
            return false;
        }
        return true;
    }

    /**
     * 
     * @param user The user
     * @return false if error
     */
    public boolean validatePasswordPolicy(User user) {
        return passwordPolicyService.validatePasswordAgainstCreationPolicy(user);
    }

    /**
     * 
     * @param user The user
     * @return false if password and confirm password does not match
     */
    private boolean validatePasswordAndConfirmPassword(User user) {
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            message = messageSource.getMessage("user.confirm_password",new Object[] {}, Locale.getDefault());
            return false;
        }
        return true;
    }

    /**
     * 
     * @param role The role
     * @param password The Password
     * @param userName The user name
     * @return related message if error exists
     */
    public String validatePasswordPolicyDwr(String role, String password, String userName) {
        PasswordPolicy passwordPolicy = passwordPolicyService.getPasswordPolicy(Role.getByCode(role));
        CombinationPolicy combinationPolicy = passwordPolicy.getPasswordCreationPolicy().getCombinationPolicy();
        message = "";
        if (validateLowerCaseAlphabet(combinationPolicy, password)
                & validateUpperCaseAlphabet(combinationPolicy, password)
                & validateNonAlphaNumeric(combinationPolicy, password)
                & validateBaseTenDigit(combinationPolicy, password)
                & validateMaxSubstringLength(combinationPolicy, password, userName)
                & validateMinPasswordLength(passwordPolicy.getPasswordCreationPolicy().getMinPasswordLength(), password))
            return "";
        return this.message;
    }

    /**
     *
     * @param minLength The min length
     * @param password  The Password
     * @return false if password lenght is less than min length mentioned
     */
    private boolean validateMinPasswordLength(int minLength, String password) {
        if (password.length() < minLength) {
            message = "The minimum length of password must be at least " + minLength + " characters"+ "<br/>" + message;
            return false;
        }
        return true;
    }

    /**
     *
     * @param policy The Combination Policy
     * @param password The Passsword
     * @return false if password does not contain at least one lower case letter
     */
    private boolean validateLowerCaseAlphabet(CombinationPolicy policy, String password) {
        if (policy.isLowerCaseAlphabetRequired()
                && !password.matches(".*[\\p{javaLowerCase}].*")) {
            message = messageSource.getMessage("user.password_lower",new Object[] {}, Locale.getDefault());
            return false;
        }
        return true;
    }

    /**
     *
     * @param policy The Combination Policy
     * @param password The Passsword
     * @return false if password does not contain at least one upper case letter
     */
    private boolean validateUpperCaseAlphabet(CombinationPolicy policy, String password) {
        if (policy.isUpperCaseAlphabetRequired()
                && !password.matches(".*[\\p{javaUpperCase}].*")) {
            message = messageSource.getMessage("user.password_upper",new Object[] {},Locale.getDefault())+ "<br/>" + message;
            return false;
        }
        return true;
    }

    /**
     *
     * @param policy The Combination Policy
     * @param password The Passsword
     * @return false if password does not contain one special character
     */
    private boolean validateNonAlphaNumeric(CombinationPolicy policy, String password) {
        if (policy.isNonAlphaNumericRequired() && password.matches("[\\p{Alnum}]+")) {
            message = messageSource.getMessage("user.password_special",new Object[] {},Locale.getDefault())+ "<br/>" + message;
            return false;
        }
        return true;
    }

    /**
     *
     * @param policy The Combination Policy
     * @param password The Passsword
     * @return false if password does not contain digits
     */
    private boolean validateBaseTenDigit(CombinationPolicy policy, String password) {
        if (policy.isBaseTenDigitRequired()
                && !password.matches(".*[\\p{Digit}].*")) {
            message = messageSource.getMessage("user.password_digit",new Object[] {},Locale.getDefault())+ "<br/>" + message;
            return false;
        }
        return true;
    }

    /**
     *
     * @param policy The Combination Policy
     * @param password The Passsword
     * @param userName The user Name
     * @return false if password contains substring of 3 letters from user name
     */
    private boolean validateMaxSubstringLength(CombinationPolicy policy, String password, String userName) {
        int substringLength = policy.getMaxSubstringLength() + 1;
        for (int i = 0; i < userName.length() - substringLength; i++) {
            try {
                if (password.contains(userName.substring(i, i + substringLength))) {
                    message = messageSource.getMessage("user.password_substring",new Object[] {},Locale.getDefault())+ "<br/>" + message;
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return true;
            }
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

    /**
     *
     * @param userRepository The User Repository
     */

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @param passwordPolicyService The Password Policy
     */
    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }

    /**
     * @param messageSource the messageSource to set
     */
    @Required
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}