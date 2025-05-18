package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation for dependency injection, used to mark fields that should be injected by the IoC container
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface Inject {
    /**
     * Optional name for the dependency (keyword "default")
     * 
     * @return the name of the dependency
     */
    String value() default "";
}