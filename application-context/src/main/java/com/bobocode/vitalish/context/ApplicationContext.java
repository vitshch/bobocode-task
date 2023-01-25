package com.bobocode.vitalish.context;

import com.bobocode.vitalish.context.exception.NoSuchBeanException;
import com.bobocode.vitalish.context.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {

    <T> T getBean(Class<T> type) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

    <T> Map<String, T> getAllBeans(Class<T> beanType);

}
