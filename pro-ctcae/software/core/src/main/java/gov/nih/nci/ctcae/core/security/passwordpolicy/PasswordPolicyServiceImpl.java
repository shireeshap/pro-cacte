package gov.nih.nci.ctcae.core.security.passwordpolicy;

import org.springframework.beans.factory.annotation.Required;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.LoginPolicyValidator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyValidator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;
import gov.nih.nci.ctcae.core.query.PasswordPolicyQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.List;

public class PasswordPolicyServiceImpl implements PasswordPolicyService {
    PasswordCreationPolicyValidator passwordCreationPolicyValidator;

    LoginPolicyValidator loginPolicyValidator;
    GenericRepository genericRepository;

    public PasswordPolicyServiceImpl() {
        passwordCreationPolicyValidator = new PasswordCreationPolicyValidator();
        loginPolicyValidator = new LoginPolicyValidator();
    }

    public PasswordPolicy getPasswordPolicy(Role role) {
        if (role == null) {
            role = Role.PARTICIPANT;
        }
        PasswordPolicyQuery passwordPolicyQuery = new PasswordPolicyQuery();
        passwordPolicyQuery.filterByRole(role);
        List<PasswordPolicy> passwordPolicies = genericRepository.find(passwordPolicyQuery);
        if (passwordPolicies == null || passwordPolicies.size() == 0) {
            return genericRepository.findById(PasswordPolicy.class, 2);
        }
        return passwordPolicies.get(0);
    }

    public boolean validatePasswordAgainstCreationPolicy(User user)
            throws PasswordCreationPolicyException {
//        user = genericRepository.findById(User.class, user.getId());
        ValidationErrors validationErrors = new ValidationErrors();
        boolean result = passwordCreationPolicyValidator.validate(getPasswordPolicy(user.getRoleForPasswordPolicy()), user, validationErrors);
        if (validationErrors.hasErrors())
            throw new PasswordCreationPolicyException("Error while saving password", validationErrors);
        return result;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
