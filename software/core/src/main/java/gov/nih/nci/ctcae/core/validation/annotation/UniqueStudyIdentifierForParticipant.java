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
 * The Interface UniqueIdentifierForParticipant.
 *
 * @author Reshma Koganti
 * @since Dec 09, 2010
 */
@Documented
@ValidatorClass(UniqueStudyIdentifierForParticipantValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueStudyIdentifierForParticipant {

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
