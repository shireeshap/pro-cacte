package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Annotation;

/**
 * @author Vinay Kumar
 * @crated Dec 8, 2008
 */
public abstract class AbstractValidator<T extends Annotation> implements Validator<T> {

	public boolean validate(final Object value) {
		return true;


	}

	public boolean validate(final Object bean, final Object value) {
		return true;


	}
}
