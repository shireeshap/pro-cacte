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
 * The Interface UniqueTitleForCrf.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
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