package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Annotation;

//
/**
 * The Class AbstractValidator.
 *
 * @author Vinay Kumar
 * @since Dec 8, 2008
 */
public abstract class AbstractValidator<T extends Annotation> implements Validator<T> {

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#validate(java.lang.Object)
      */
    public boolean validate(final Object value) {
        return true;


    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#validate(java.lang.Object, java.lang.Object)
      */
    public boolean validate(final Object bean, final Object value) {
        return true;


    }
}
