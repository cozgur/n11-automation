package com.company.qa.core.retry;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG annotation transformer that automatically assigns {@link RetryAnalyzer}
 * to all test methods.
 *
 * <p>Register this listener in {@code testng.xml} to enable automatic retries
 * for all {@code @Test} methods without annotating each one individually:</p>
 * <pre>{@code
 * <listeners>
 *     <listener class-name="com.company.qa.core.retry.RetryListener"/>
 * </listeners>
 * }</pre>
 *
 * @see RetryAnalyzer
 */
public class RetryListener implements IAnnotationTransformer {

    /**
     * Transforms each test annotation by setting the retry analyzer class to
     * {@link RetryAnalyzer}.
     *
     * @param annotation      the test annotation to transform
     * @param testClass       the test class (may be {@code null})
     * @param testConstructor the test constructor (may be {@code null})
     * @param testMethod      the test method (may be {@code null})
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzerClass(RetryAnalyzer.class);
    }
}
