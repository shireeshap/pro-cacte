package gov.nih.nci.ctcae.core.security.passwordpolicy;

import gov.nih.nci.ctcae.core.ProCtcSystemException;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;

public interface PasswordPolicyService {
    /**
     * This method will return the stored password poicy from xml configuration file
     */
    public PasswordPolicy getPasswordPolicy(Role role);

    public boolean validatePasswordAgainstCreationPolicy(User user)
            throws ProCtcSystemException;

}
