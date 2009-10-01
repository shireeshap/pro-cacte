package gov.nih.nci.ctcae.core.validation.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//
/**
 * The Class UniqueObjectInCollectionValidator.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class UniqueObjectInCollectionValidator extends AbstractValidator<UniqueObjectInCollection> {

    /**
     * The message.
     */
    String message;

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(UniqueObjectInCollectionValidator.class);

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
      */
    public boolean validate(final Object value) {
        logger.info("in the validate method of" + this.getClass().getName());
        if (value instanceof Collection) {
            Set objects = new HashSet((Collection) value);
            return objects.size() == ((Collection) value).size();
        }
        return true;
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
      */
    public void initialize(final UniqueObjectInCollection parameters) {
        this.message = parameters.message();
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
      */
    public String message() {
        return this.message;
	}
}
