package com.bobocode.vitalish.context;

import java.lang.reflect.Field;
import java.util.Set;

public record BeanDefinition<T>(String beanName, Class<T> clazz, Set<Field> autowiredFields) {
}
