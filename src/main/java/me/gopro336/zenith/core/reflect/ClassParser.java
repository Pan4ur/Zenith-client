package me.gopro336.zenith.core.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Gopro336
 */
public class ClassParser {

    /**
     * Find a field in a class by it's name. Note that this method is
     * only needed because the general reflection method is case
     * sensitive
     *
     * @param clazz The clazz to search
     * @param name The name of the field to search for
     * @return The field or null if none could be located
     */
    private Field findField(Class clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i=0;i<fields.length;i++) {
            if (fields[i].getName().equalsIgnoreCase(name)) {
                if (fields[i].getType().isPrimitive()) {
                    return fields[i];
                }
                if (fields[i].getType() == String.class) {
                    return fields[i];
                }
            }
        }

        return null;
    }

    /**
     * Find a method in a class by it's name. Note that this method is
     * only needed because the general reflection method is case
     * sensitive
     *
     * @param clazz The clazz to search
     * @param name The name of the method to search for
     * @return The method or null if none could be located
     */
    private Method findMethod(Class clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i=0;i<methods.length;i++) {
            if (methods[i].getName().equalsIgnoreCase(name)) {
                Method method = methods[i];
                Class[] params = method.getParameterTypes();

                if (params.length == 1) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * Find a method on a class with a single given parameter.
     *
     * @param clazz The clazz to search through
     * @param name The name of the method to locate
     * @param parameter The type the single parameter must have
     * @return The method or null if none could be located
     */
    private Method findMethod(Class clazz, String name, Class parameter) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i=0;i<methods.length;i++) {
            if (methods[i].getName().equalsIgnoreCase(name)) {
                Method method = methods[i];
                Class[] params = method.getParameterTypes();

                if (params.length == 1) {
                    if (method.getParameterTypes()[0].isAssignableFrom(parameter)) {
                        return method;
                    }
                }
            }
        }

        return null;
    }

}
