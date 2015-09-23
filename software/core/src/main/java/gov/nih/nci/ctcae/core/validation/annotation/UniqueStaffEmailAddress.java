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
 * Date: Dec 20, 2010
 * Time: 2:14:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Documented
@ValidatorClass(UniqueStaffEmailAddressValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueStaffEmailAddress {
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
    String message() default "Email Address already exists in database";

}
