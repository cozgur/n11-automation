package com.company.qa.core.base;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for flow (business-logic) classes in mobile automation.
 *
 * <p>Provides convenient access to the current thread's {@link AppiumDriver}
 * and a Log4j2 {@link Logger} scoped to the concrete subclass. Flow classes
 * typically orchestrate interactions across multiple screen objects to implement
 * end-to-end user journeys.</p>
 */
public abstract class BaseFlow {

    /** Logger scoped to the concrete subclass for structured logging. */
    protected final Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

    /**
     * Returns the {@link AppiumDriver} bound to the current thread.
     *
     * @return the current thread's Appium driver
     */
    protected AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }
}
