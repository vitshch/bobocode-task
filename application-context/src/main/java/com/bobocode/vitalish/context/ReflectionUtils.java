package com.bobocode.vitalish.context;

import com.bobocode.vitalish.context.annotation.Bean;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static boolean isClass(String fileName) {
        return fileName != null && fileName.endsWith(".class");
    }

    public static String getClassName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static Class<?> getClass(String packageName, String className) {
        String fullClassName = packageName + "." + className;
        try {
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load class: " + fullClassName, e);
        }
    }

    public static String resolveBeanName(Class<?> clazz) {
        String beanName = clazz.getAnnotation(Bean.class).value();
        if (beanName.isEmpty()) {
            var className = clazz.getSimpleName();
            beanName = String.valueOf(className.charAt(0)).toLowerCase() + className.substring(1);
        }
        return beanName;
    }

    public static Object newInstance(BeanDefinition<?> beanDefinition) throws RuntimeException {
        try {
            Constructor<?> constructor = Arrays.stream(beanDefinition.clazz().getConstructors())
                    .filter(c -> c.getParameterCount() == 0)
                    .findFirst().orElseThrow();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to create new instance of class");
        }
    }
}
