package com.company.qa.core.base;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for flow/business-logic classes.
 * Provides driver access and a logger.
 */
public abstract class BaseFlow {

    protected final Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

    protected AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }
}
