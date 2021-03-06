package gov.nih.nci.ctcae.core.validation.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumInRange {
    /**
      * Use longs, since we don't deal with floating points much in caAERS (we can always define more
      * specialized annotations)
      */
     long min() default Long.MIN_VALUE;

     long max() default Long.MAX_VALUE;    
}
