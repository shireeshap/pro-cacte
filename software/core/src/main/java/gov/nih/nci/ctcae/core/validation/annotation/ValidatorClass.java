package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//
/**
 * Link between an constraint annotation and it's validator implementation.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
@Documented
@Target({ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidatorClass {

    /**
     * Value.
     *
     * @return the class<? extends validator>
     */
    Class<? extends Validator> value();
}

