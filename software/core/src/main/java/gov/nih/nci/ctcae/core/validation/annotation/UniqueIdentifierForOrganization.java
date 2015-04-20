package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Amey
 * UniqueIdentifierForOrganizationValidator class
 */
@Documented
@ValidatorClass(UniqueIdentifierForOrganizationValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueIdentifierForOrganization {

    /**
     * Message.
     * @return the string
     */
    String message() default "Identifier already exists in database";
}
