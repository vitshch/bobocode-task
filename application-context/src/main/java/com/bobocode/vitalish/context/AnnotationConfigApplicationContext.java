package com.bobocode.vitalish.context;

import com.bobocode.vitalish.context.annotation.Autowired;
import com.bobocode.vitalish.context.exception.BeanPostProcessorException;
import com.bobocode.vitalish.context.exception.NoSuchBeanException;
import com.bobocode.vitalish.context.exception.NoUniqueBeanException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Map<String, Object> context = new ConcurrentHashMap<>();
    private Set<BeanDefinition<?>> beanDefinitions;

    public AnnotationConfigApplicationContext(String packageName) {
        createBeanDefinitions(packageName);
        createBeanInstances();
        injectDependencies();
    }

    private void createBeanDefinitions(String packageName) {
        Set<Class<?>> beanClasses = ContextUtils.scanPackage(packageName);
        this.beanDefinitions = beanClasses.stream()
                .map(this::createBeanDefinition)
                .collect(Collectors.toSet());
    }

    private <T> BeanDefinition<T> createBeanDefinition(Class<T> clazz) {
        return new BeanDefinition<>(ReflectionUtils.resolveBeanName(clazz), clazz, getAutowiredFields(clazz));
    }

    private Set<Field> getAutowiredFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(Autowired.class) != null)
                .collect(Collectors.toSet());
    }

    private void createBeanInstances() {
        beanDefinitions.forEach(beanDefinition -> context.put(
                beanDefinition.beanName(),
                ReflectionUtils.newInstance(beanDefinition)
        ));
    }

    private void injectDependencies() {
        beanDefinitions.stream()
                .filter(beanDefinition -> !beanDefinition.autowiredFields().isEmpty())
                .forEach(this::injectDependency);
    }

    private <T> void injectDependency(BeanDefinition<T> beanDefinition) {
        try {
            T bean = getBean(beanDefinition.beanName(), beanDefinition.clazz());
            for (Field field : beanDefinition.autowiredFields()) {
                field.setAccessible(true);
                field.set(bean, getBean(field.getType()));
            }
        } catch (IllegalAccessException e) {
            throw new BeanPostProcessorException("Unable to set Bean dependency", e);
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
