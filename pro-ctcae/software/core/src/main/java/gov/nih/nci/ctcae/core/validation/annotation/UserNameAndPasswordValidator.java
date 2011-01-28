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

    public boolean validateDwrUniqueName(String userName) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName);
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        if ((users == null || users.isEmpty())) {
            return false;
        } else {
            message = "Username already exists. Please choose another username.";
            return true;
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

    public String validatePasswordPolicyDwr(String role, String password,String userName) {
        PasswordPolicy passwordPolicy = passwordPolicyService.getPasswordPolicy(Role.getByCode(role));
        CombinationPolicy combinationPolicy = passwordPolicy.getPasswordCreationPolicy().getCombinationPolicy();
        if(validateLowerCaseAlphabet(combinationPolicy, password)
                        & validateUpperCaseAlphabet(combinationPolicy,password)
                        & validateNonAlphaNumeric(combinationPolicy, password)
                        & validateBaseTenDigit(combinationPolicy, password)
                        & validateMaxSubstringLength(combinationPolicy, password,userName)
                        & validateMinPasswordLength(passwordPolicy.getPasswordCreationPolicy().getMinPasswordLength(),password))
        	return "";
        return this.message;
    }

    private boolean validateMinPasswordLength(int minLength,String password ) {
        if (password.length() < minLength) {
            message="The minimum length of password must be at least " +minLength +" characters";
            return false;
        }
        return true;
    }

    private boolean validateLowerCaseAlphabet(CombinationPolicy policy, String password) {
        if (policy.isLowerCaseAlphabetRequired()
                && !password.matches(".*[\\p{javaLowerCase}].*")) {
            message = "The password should have at least one lower case letter";
            return false;
        }
        return true;
    }

    private boolean validateUpperCaseAlphabet(CombinationPolicy policy, String password) {
        if (policy.isUpperCaseAlphabetRequired()
                        && !password.matches(".*[\\p{javaUpperCase}].*")) {
            message="The password should have at least one upper case letter";
        	return false;
        }
        return true;
    }

     private boolean validateNonAlphaNumeric(CombinationPolicy policy, String password){
        if (policy.isNonAlphaNumericRequired() && password.matches("[\\p{Alnum}]+")) {
            message="The password should have at least one special charcter";
        	return false;
        }
        return true;
    }


    private boolean validateBaseTenDigit(CombinationPolicy policy, String password){
        if (policy.isBaseTenDigitRequired()
                        && !password.matches(".*[\\p{Digit}].*")) {
            message="The password should have at least one numeral digit{0-9}";
        	return false;
        }
        return true;
    }

     private boolean validateMaxSubstringLength(CombinationPolicy policy, String password,String userName){
        int substringLength = policy.getMaxSubstringLength() + 1;
        for (int i = 0; i < userName.length() - substringLength; i++) {
            try {
                if (password.contains(userName.substring(i, i + substringLength))) {
                    message="The password should not contain a substring of 3 letters from the username";
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

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }
}