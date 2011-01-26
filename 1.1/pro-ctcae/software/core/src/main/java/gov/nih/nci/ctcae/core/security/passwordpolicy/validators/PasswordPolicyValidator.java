package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.security.user.Credential;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Mar 31, 2010
 * Time: 4:10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PasswordPolicyValidator {
    public boolean validate(PasswordPolicy policy, User user, ValidationErrors validationErrors)
                    throws ValidationException;

}
