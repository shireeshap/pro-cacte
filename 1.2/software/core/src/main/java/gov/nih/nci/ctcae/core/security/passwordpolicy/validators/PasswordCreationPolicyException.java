package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.validation.ValidationErrors;
import gov.nih.nci.ctcae.core.ProCtcSystemException;

public class PasswordCreationPolicyException extends ProCtcSystemException {
    private static final long serialVersionUID = 6787210172663632951L;

	private ValidationErrors errors;
	public PasswordCreationPolicyException(String message, ValidationErrors errors) {
		super(message);
		this.errors = errors;
	}

	public ValidationErrors getErrors() {
		return errors;
	}
	public void setErrors(ValidationErrors errors) {
		this.errors = errors;
	}
}
