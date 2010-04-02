package gov.nih.nci.ctcae.core.security.passwordpolicy;

import org.springframework.beans.factory.annotation.Required;
import gov.nih.nci.ctcae.core.security.user.Credential;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.LoginPolicyValidator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyValidator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;
import gov.nih.nci.ctcae.core.ProCtcSystemException;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

public class PasswordPolicyServiceImpl implements PasswordPolicyService {
    PasswordCreationPolicyValidator passwordCreationPolicyValidator;

    LoginPolicyValidator loginPolicyValidator;
    GenericRepository genericRepository;

//        CSMUserRepository csmUserRepository;

    public PasswordPolicyServiceImpl() {
        passwordCreationPolicyValidator = new PasswordCreationPolicyValidator();
        loginPolicyValidator = new LoginPolicyValidator();
    }

    public PasswordPolicy getPasswordPolicy() {
        return genericRepository.findById(PasswordPolicy.class, 1);
    }

    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
            genericRepository.save(passwordPolicy);
    }

    public String publishPasswordPolicy() {
        return "TODO";
    }

    public String publishPasswordPolicy(String xsltFileName) {
        return "TODO";
    }

    public boolean validatePasswordAgainstCreationPolicy(User user)
            throws PasswordCreationPolicyException {
        ValidationErrors validationErrors = new ValidationErrors();
        boolean result = passwordCreationPolicyValidator.validate(getPasswordPolicy(), user, validationErrors);
        if (validationErrors.hasErrors())
            throw new PasswordCreationPolicyException("Error while saving password", validationErrors);
        return result;
    }

    public boolean validatePasswordAgainstLoginPolicy(Credential credential) throws
            ProCtcSystemException {
        return loginPolicyValidator.validate(getPasswordPolicy(), credential, null);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }




}
