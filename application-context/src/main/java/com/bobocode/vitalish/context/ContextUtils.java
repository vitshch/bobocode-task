package com.bobocode.vitalish.context;

import com.bobocode.vitalish.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public final class ContextUtils {

    private ContextUtils() {

    }

    public static Set<Class<?>> scanPackage(String packageName) {
        InputStream resourceStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
        return reader.lines()
                .filter(ReflectionUtils::isClass)
                .map(ReflectionUtils::getClassName)
                .map(className -> ReflectionUtils.getClass(packageName, className))
                .filter(clazz -> clazz.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

}
