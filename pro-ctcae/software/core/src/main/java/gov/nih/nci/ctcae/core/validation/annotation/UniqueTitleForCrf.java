package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//
/**
 * The Interface UniqueTitleForCrf.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
@Documented
@ValidatorClass(UniqueTitleForCrfValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueTitleForCrf {

    /**
     * Max.
     *
     * @return the int
     */
    public abstract int max() default Integer.MAX_VALUE;

    /**
     * Min.
     *
     * @return the int
     */
    public abstract int min() default 0;

    /**
     * Message.
     *
     * @return the string
     */
    public abstract String message() default "Title already exists in database";
}