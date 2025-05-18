package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation for logging method execution, used to mark methods that
// should be logged when executed
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
    /**
     * Optional Message to log before method execution (keyword "default")
     * 
     * @return the message to log
     */
    String value() default "Method execution";

    /**
     * Optional Log level for the message (keyword "default")
     * 
     * @return the log level
     */
    LogLevel level() default LogLevel.INFO;

    // Enum for log levels
    enum LogLevel {
        INFO, WARNING, SEVERE
    }
}