package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.validation.ValidationErrors;
import gov.nih.nci.ctcae.core.security.user.Credential;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordCreationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

public class PasswordCreationPolicyValidator implements PasswordPolicyValidator {
    private PasswordPolicyValidator combinationValidator;
    private GenericRepository genericRepository;
//      private CSMUserRepository csmUserRepository;

    public PasswordCreationPolicyValidator() {
        combinationValidator = new CombinationValidator();
    }

    /**
     * This method calls all the password creation policy validating methods
     */
    public boolean validate(PasswordPolicy policy, User user, ValidationErrors validationErrors)
            throws ValidationException {
        PasswordCreationPolicy passwordCreationPolicy = policy.getPasswordCreationPolicy();

        if (validateMinPasswordAge(passwordCreationPolicy, user, validationErrors)
                & validateMinPasswordLength(passwordCreationPolicy, user, validationErrors)
                & combinationValidator.validate(policy, user, validationErrors))
            return true;
        return false;
    }

    /**
     * Validates the minimum password age
     *
     * @param policy
     * @return //       * @throws ValidationException - if the user password age is less than one set in passwords creation policy
     */
    public boolean validateMinPasswordAge(PasswordCreationPolicy policy, User user, ValidationErrors validationErrors) {
//        if (user.getPasswordAge() < policy.getMinPasswordAge()) {
//            validationErrors.addValidationError("PCP_001", "Password was changed too recently.");
//            return false;
//        }
        return true;
    }


    /**
     * validates minimum password length
     * note: default value is 7
     *
     * @param policy
     * @return
     * @throws ValidationException - when user password length is greater than or equal to the one set in password creation policy.
     */
    public boolean validateMinPasswordLength(PasswordCreationPolicy policy, User user, ValidationErrors validationErrors) {
        if (user.getPassword().length() >= policy.getMinPasswordLength()) {
            return true;
        } else {
            validationErrors.addValidationError("PCP_003", "The minimum length of password must be at least " + policy.getMinPasswordLength() + " characters", policy.getMinPasswordLength());
            return false;
        }
    }

//      public void setCsmUserRepository(final CSMUserRepository csmUserRepository) {
//          this.csmUserRepository = csmUserRepository;
//      }


}
