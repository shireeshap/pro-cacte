package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

// TODO: Auto-generated Javadoc
/**
 * not null constraint.
 * 
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
@Documented
@ValidatorClass(NotNullValidator.class)
@Target({METHOD, FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface NotNull {
    
    /**
     * Message.
     * 
     * @return the string
     */
    String message() default "Not null";

}
