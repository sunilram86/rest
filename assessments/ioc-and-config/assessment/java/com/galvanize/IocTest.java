package com.galvanize;

import junit.framework.AssertionFailedError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "formatter.type=csv",
        "formatter.csv.delimiter=tab",
        "formatter.html.tag_name=ul",
})
public class IocTest {

    @Autowired
    private
    ListableBeanFactory factory;

    @Autowired
    private
    ApplicationContext applicationContext;

    @Test
    public void findsInApplicationContext() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(
                ConfigurationProperties.class);

        Optional<String> beanName = Arrays.stream(beanNames).filter(name -> {
            ConfigurationProperties annotation = factory.findAnnotationOnBean(
                    name,
                    ConfigurationProperties.class
            );

            return annotation.value().equals("formatter");
        }).findFirst();

        beanName.ifPresent(s -> {
            Object bean = applicationContext.getBean(s);

            checkGetter(bean, "getType", "csv", "formatter config class");

            Object csv = getObjectForField(bean, "getCsv", "formatter config class");
            checkGetter(csv, "getDelimiter", "tab", "csv config class");

            Object html = getObjectForField(bean, "getHtml", "formatter config class");
            checkGetter(html, "getTagName", "ul", "html config class");
        });

        beanName.orElseThrow(() -> {
            return new AssertionFailedError("Could not find a class with @ConfigurationProperties mapped to formatter");
        });
    }

    private void checkGetter(Object config, String methodName, Object value, String className) {
        Method method = null;
        try {
            method = config.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            fail("Could not find " + methodName + " method on " + className);
        }

        try {
            Object result = method.invoke(config);
            assertThat(result, equalTo(value));
        } catch (IllegalAccessException e) {
            fail(className + "::" + methodName + " is not public");
        } catch (InvocationTargetException e) {
            fail("Error calling " + className + "::" + methodName);
        }

    }

    private Object getObjectForField(Object config, String methodName, String className) {
        Method method = null;
        try {
            method = config.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            fail("Could not find " + methodName + " method on " + className);
        }

        try {
            return method.invoke(config);
        } catch (IllegalAccessException e) {
            fail(className + "::" + methodName + " is not public");
        } catch (InvocationTargetException e) {
            fail("Error calling " + className + "::" + methodName);
        }

        return null;
    }


}
