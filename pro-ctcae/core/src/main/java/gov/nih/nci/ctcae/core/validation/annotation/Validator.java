package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Annotation;

// TODO: Auto-generated Javadoc
/**
 * A constraint validator for a particular annotation.
 * 
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public interface Validator<A extends Annotation> {
	
	/**
	 * does the object/element pass the constraints.
	 * 
	 * @param value the value
	 * 
	 * @return true, if validate
	 */
	public boolean validate(Object value);

	/**
	 * Validate.
	 * 
	 * @param bean the bean
	 * @param value the value
	 * 
	 * @return true, if successful
	 */
	public boolean validate(Object bean, Object value);

	/**
	 * Take the annotations values.
	 * 
	 * @param parameters the parameters
	 */
	public void initialize(A parameters);

	/**
	 * Message.
	 * 
	 * @return the string
	 */
	public String message();
}
