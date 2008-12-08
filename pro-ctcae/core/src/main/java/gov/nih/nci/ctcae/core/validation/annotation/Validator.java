package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Annotation;

/**
 * A constraint validator for a particular annotation
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public interface Validator<A extends Annotation> {
	/**
	 * does the object/element pass the constraints
	 */
	public boolean validate(Object value);

	public boolean validate(Object bean, Object value);

	/**
	 * Take the annotations values
	 *
	 * @param parameters
	 */
	public void initialize(A parameters);

	public String message();
}
