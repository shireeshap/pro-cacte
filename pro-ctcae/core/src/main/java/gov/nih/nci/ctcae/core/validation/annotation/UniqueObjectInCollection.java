package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

//
/**
 * The Interface UniqueObjectInCollection.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
@Documented
@ValidatorClass(UniqueObjectInCollectionValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueObjectInCollection

{

    /**
     * Message.
     *
     * @return the string
     */
    public abstract String message() default "Duplicate..!";

}
