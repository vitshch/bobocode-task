package com.bobocode.vitalish.context;

import com.bobocode.vitalish.context.annotation.Bean;
import com.bobocode.vitalish.context.exception.NoSuchBeanException;
import com.bobocode.vitalish.context.exception.NoUniqueBeanException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final String packageName;
    private final Map<String, Object> context = new HashMap<>();
    private Set<Class<?>> beanDefinitions;

    public AnnotationConfigApplicationContext(String packageName) {
        this.packageName = packageName;
        scanPackage();
        createBeanInstances();
    }

    private void scanPackage() {
        InputStream resourceStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
        this.beanDefinitions = reader.lines()
                .filter(ReflectionUtils::isClass)
                .map(ReflectionUtils::getClassName)
                .map(className -> ReflectionUtils.getClass(packageName, className))
                .filter(clazz -> clazz.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

    private void createBeanInstances() {
        for (Class<?> clazz : beanDefinitions) {
            context.put(ReflectionUtils.resolveBeanName(clazz), ReflectionUtils.newInstance(clazz));
        }
    }

    public <T> T getBean(Class<T> type) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> matchedBeans = getAllBeans(type);
        if (matchedBeans.size() > 1) {
            throw new NoUniqueBeanException("There are no unique bean for type: '" + type.getName() + "'");
        }
        return matchedBeans.values().stream()
                .findAny()
                .map(type::cast)
                .orElseThrow(() -> new NoSuchBeanException("There are no bean for type: '" + type.getName() + "'"));
    }

    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        return Optional.ofNullable(context.get(name))
                .map(beanType::cast)
                .orElseThrow(() -> new NoSuchBeanException(
                        "There are no bean for name: '" + name + "' and type '" + beanType.getName() + "'"
                ));
    }

    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return context.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
