package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Dec 30, 2010
 * Time: 8:33:36 AM
 * To change this template use File | Settings | File Templates.
 */
@Documented
@ValidatorClass(UniqueParticipantUserNumberValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueParticipantUserNumber {
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
    String message() default "User number already exists in database";
}
