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
 * The Interface UniqueIdentifierForStudy.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
@Documented
@ValidatorClass(UniqueIdentifierForStudyValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueIdentifierForStudy {

    /**
     * Max.
     *
     * @return the int
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Min.
     *
     * @return the int
     */
    int min() default 0;

    /**
     * Message.
     *
     * @return the string
     */
    String message() default "Identifier already exists in database";
}
