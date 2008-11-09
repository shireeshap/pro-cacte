package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.*;

/**
 * Check that a String is not empty (not null and length > 0) or that a Collection (or
 * array or map) is not empty (not null and length > 0)
 *
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
@Documented
@ValidatorClass(NotEmptyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    String message() default "Not empty";
}
