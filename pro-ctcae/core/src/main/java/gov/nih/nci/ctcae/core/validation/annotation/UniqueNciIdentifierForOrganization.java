package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Orgnaizations must have unique nci identifier. This is required to create csm-protection-group
 * etc..
 *
 * @author Saurabh Agrawal
 * @crated Oct 27, 2008
 */
@Documented
@ValidatorClass(UniqueNciIdentifierForOrganizationValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface UniqueNciIdentifierForOrganization {
    public abstract String message() default "nci identifier already exists in the database..!";

}