package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

//
/**
 * Check the non emptyness of the element.
 *
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
public class NotEmptyValidator extends AbstractValidator<NotEmpty> {

    /**
     * The message.
     */
    private String message;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(final NotEmpty parameters) {
        message = parameters.message();

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
     */
    public String message() {
        return this.message;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
    public boolean validate(final Object value) {
        if (value == null) {
            return false;
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        } else if (value instanceof Collection) {
            return ((Collection) value).size() > 0;
        } else if (value instanceof Map) {
            return ((Map) value).size() > 0;
        } else {
            return ((String) value).trim().length() > 0;
        }
    }


}
