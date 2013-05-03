package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.CombinationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Mar 31, 2010
 * Time: 4:11:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CombinationValidator implements PasswordPolicyValidator {
	UserRepository userRepository;

	public boolean validate(PasswordPolicy policy, User user, ValidationErrors validationErrors)
                    throws ValidationException {
        CombinationPolicy combinationPolicy = policy.getPasswordCreationPolicy().getCombinationPolicy();

        if(validateLowerCaseAlphabet(combinationPolicy, user, validationErrors)
                        & validateUpperCaseAlphabet(combinationPolicy, user, validationErrors)
                        & validateNonAlphaNumeric(combinationPolicy, user, validationErrors)
                        & validateBaseTenDigit(combinationPolicy, user, validationErrors)
                        & validateMaxSubstringLength(combinationPolicy, user, validationErrors)
                        & validateComplianceWithHistory(policy, user, validationErrors))
        	return true;
        return false;
    }

    private boolean validateComplianceWithHistory(PasswordPolicy policy, User user, ValidationErrors validationErrors) {
    	if(user.isPresentInUserPasswordHistory(userRepository.getEncodedPassword(user), policy.getPasswordCreationPolicy())){
    		validationErrors.addValidationError("PCP_009", "The password should not be the same as the last " + policy.getPasswordCreationPolicy().getPasswordHistorySize() + " password(s).");
    		return false;
    	}
    	return true;
	}

	private boolean validateLowerCaseAlphabet(CombinationPolicy policy, User user, ValidationErrors validationErrors){
        if (policy.isLowerCaseAlphabetRequired()
                        && !user.getPassword().matches(".*[\\p{javaLowerCase}].*")) {
        	validationErrors.addValidationError("PCP_004", "The password should have at least one lower case letter");
        	return false;
        }
        return true;
    }

    private boolean validateUpperCaseAlphabet(CombinationPolicy policy, User user, ValidationErrors validationErrors) {
        if (policy.isUpperCaseAlphabetRequired()
                        && !user.getPassword().matches(".*[\\p{javaUpperCase}].*")) {
        	validationErrors.addValidationError("PCP_005", "The password should have at least one upper case letter");
        	return false;
        }
        return true;
    }

    private boolean validateNonAlphaNumeric(CombinationPolicy policy, User user, ValidationErrors validationErrors){
        if (policy.isNonAlphaNumericRequired() && user.getPassword().matches("[\\p{Alnum}]+")) {
        	validationErrors.addValidationError("PCP_006", "The password should have at least one special character");
        	return false;
        }
        return true;
    }

    private boolean validateBaseTenDigit(CombinationPolicy policy, User user, ValidationErrors validationErrors){
        if (policy.isBaseTenDigitRequired()
                        && !user.getPassword().matches(".*[\\p{Digit}].*")) {
        	validationErrors.addValidationError("PCP_007", "The password should have at least one numeral digit{0-9}");
        	return false;
        }
        return true;
    }

    private boolean validateMaxSubstringLength(CombinationPolicy policy, User user, ValidationErrors validationErrors){
        int substringLength = policy.getMaxSubstringLength() + 1;
        String userName = user.getUsername(), password = user.getPassword();
        for (int i = 0; i < userName.length() - substringLength; i++) {
            try {
                if (password.contains(userName.substring(i, i + substringLength))) {
                	validationErrors.addValidationError("PCP_008", "The password should not contain a substring of "+ substringLength + " letters from the username");
                	return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return true;
            }
        }
        return true;
    }

    public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


}
