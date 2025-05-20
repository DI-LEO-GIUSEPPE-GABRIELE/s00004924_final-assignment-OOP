package ioc;

import annotation.Inject;
import util.LoggerManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// IoC Container Class implementation for dependency injection, implements the
// Inversion of Control pattern and uses Reflection
// It supports dependency injection through the @Inject annotation
public class IoCContainer {
    private static final Logger LOGGER = LoggerManager.getLogger(IoCContainer.class.getName());
    private static IoCContainer instance;
    private final Map<Class<?>, Object> instances = new HashMap<>();

    // Private constructor for Singleton pattern
    private IoCContainer() {
        LOGGER.info("Initializing IoC Container");
    }

    /**
     * Get the instance of the IoC container
     * 
     * @return the IoC container instance
     */
    public static synchronized IoCContainer getInstance() {
        if (instance == null) {
            instance = new IoCContainer();
        }
        return instance;
    }

    /**
     * Register an instance for a specific class
     * 
     * @param clazz    : The class to register
     * @param instance : The instance to register
     * @param <T>      : The type of the class
     */
    public <T> void register(Class<T> clazz, T instance) {
        instances.put(clazz, instance);
        LOGGER.info("Registered instance for class: " + clazz.getName());
    }

    /**
     * Get or create an instance of the specified class
     * 
     * @param clazz : The class to get an instance of
     * @param <T>   : The type of the class
     * @return an instance of the class
     * @throws IoCException : If an error occurs during instantiation
     */
    @SuppressWarnings("unchecked")
    // Annotation: @SuppressWarnings for suppress the unchecked
    public <T> T getInstance(Class<T> clazz) throws IoCException {
        if (instances.containsKey(clazz)) {
            return (T) instances.get(clazz);
        }

        // Create a new instance
        try {
            T instance = createInstance(clazz);
            instances.put(clazz, instance);
            injectDependencies(instance);
            return instance;
        } catch (Exception e) {
            LOGGER.severe("Error creating instance of " + clazz.getName() + ": " + e.getMessage());
            throw new IoCException("Error creating instance of " + clazz.getName(), e);
        }
    }

    /**
     * Create a new instance of the specified class
     * 
     * @param clazz : The class to instantiate
     * @param <T>   : The type of the class
     * @return a new instance of the class
     * @throws Exception : If an error occurs during instantiation
     */
    private <T> T createInstance(Class<T> clazz) throws Exception {
        // Look for a constructor with @Inject
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                constructor.setAccessible(true);
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                // Resolve dependencies for constructor parameters
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = getInstance(paramTypes[i]);
                }

                return (T) constructor.newInstance(params);
            }
        }

        // No @Inject constructor found, use default constructor
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * Inject dependencies into fields annotated with @Inject
     * 
     * @param instance : The instance to inject dependencies into
     * @throws IoCException : If an error occurs during injection
     */
    private void injectDependencies(Object instance) throws IoCException {
        Class<?> clazz = instance.getClass();

        // Inject fields
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    Object dependency = getInstance(field.getType());
                    field.set(instance, dependency);
                    LOGGER.info("Injected dependency into field: " + field.getName() + " of class: " + clazz.getName());
                } catch (Exception e) {
                    LOGGER.severe("Error injecting dependency into field: " + field.getName() + ": " + e.getMessage());
                    throw new IoCException("Error injecting dependency into field: " + field.getName(), e);
                }
            }
        }
    }

    // Clear all registered instances
    public void clear() {
        instances.clear();
        LOGGER.info("Cleared all registered instances");
    }
}