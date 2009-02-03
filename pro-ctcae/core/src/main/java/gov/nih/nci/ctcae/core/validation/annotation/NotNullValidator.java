package gov.nih.nci.ctcae.core.validation.annotation;

//
/**
 * Checks a not null restriction on an object.
 *
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
public class NotNullValidator extends AbstractValidator<NotNull> {

    /**
     * The message.
     */
    String message;

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
      */
    public boolean validate(final Object value) {
        return value != null;
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
      */
    public void initialize(final NotNull parameters) {
        this.message = parameters.message();
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
      */
    public String message() {
        return this.message;
	}
}
