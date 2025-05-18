package util;

import annotation.Loggable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

// Loggable Processor class for methods annotated, intercept method calls and add logging functionality
public class LoggableProcessor {
    private static final Logger LOGGER = LoggerManager.getLogger(LoggableProcessor.class.getName());

    /**
     * Create a proxy that adds logging to methods annotated with @Loggable
     * 
     * @param <T>            : The type of the target object
     * @param target         : The target object
     * @param interfaceClass : The interface that the proxy will implement
     * @return a proxy of the target object with logging added
     */
    @SuppressWarnings("unchecked")
    public static <T> T createLoggingProxy(final T target, Class<T> interfaceClass) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            // Annotation: Override the method of the InvocationHandler interface
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // Check if the method in the target class is annotated with @Loggable
                Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
                Loggable loggable = targetMethod.getAnnotation(Loggable.class);

                if (loggable != null) {
                    return invokeWithLogging(target, targetMethod, args, loggable);
                } else {
                    // If not annotated, just invoke the method normally
                    return targetMethod.invoke(target, args);
                }
            }
        };

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[] { interfaceClass },
                handler);
    }

    /**
     * Invoke a method with logging based on the @Loggable annotation
     * 
     * @param target   : The target object
     * @param method   : The method to invoke
     * @param args     : The method arguments
     * @param loggable : The Loggable annotation
     * @return the result of the method invocation
     * @throws Throwable : If an error occurs during invocation
     */
    private static Object invokeWithLogging(Object target, Method method, Object[] args, Loggable loggable)
            throws Throwable {
        String methodName = target.getClass().getSimpleName() + "." + method.getName();
        Level logLevel = convertLogLevel(loggable.level());
        String message = loggable.value();

        // Log before method execution
        LOGGER.log(logLevel, message + ": " + methodName);
        if (args != null && args.length > 0) {
            LOGGER.log(logLevel, "Arguments: " + Arrays.toString(args));
        }

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            // Invoke the actual method
            result = method.invoke(target, args);

            // Log after successful execution
            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.log(logLevel, methodName + " executed in " + executionTime + "ms");

            // Log the result if it's not void
            if (method.getReturnType() != void.class && result != null) {
                LOGGER.log(logLevel, methodName + " returned: " + result);
            }

            return result;
        } catch (InvocationTargetException e) {
            // Log the exception and rethrow the original cause
            LOGGER.log(Level.SEVERE, methodName + " threw an exception: " + e.getCause().getMessage(), e.getCause());
            throw e.getCause();
        } catch (Exception e) {
            // Log any other exceptions
            LOGGER.log(Level.SEVERE, methodName + " threw an exception: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Convert from Loggable.LogLevel to java.util.logging.Level
     * 
     * @param logLevel : The Loggable.LogLevel
     * @return the corresponding java.util.logging.Level
     */
    private static Level convertLogLevel(Loggable.LogLevel logLevel) {
        switch (logLevel) {
            case INFO:
                return Level.INFO;
            case WARNING:
                return Level.WARNING;
            case SEVERE:
                return Level.SEVERE;
            default:
                return Level.INFO;
        }
    }
}