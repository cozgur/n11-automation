package com.company.qa.core.driver;

import org.openqa.selenium.WebDriver;

/**
 * Common interface for driver lifecycle management.
 *
 * <p>Implementations must be thread-safe, typically by using {@link ThreadLocal}
 * storage so that each test thread maintains its own isolated driver instance.</p>
 *
 * @param <T> the WebDriver subtype managed by this implementation
 */
public interface DriverManager<T extends WebDriver> {

    /**
     * Returns the driver instance bound to the current thread.
     *
     * @return the current thread's driver instance, never {@code null}
     * @throws com.company.qa.core.exception.DriverInitializationException
     *         if no driver has been initialized for the current thread
     */
    T getDriver();

    /**
     * Removes and cleans up the driver instance for the current thread.
     *
     * <p>After this method returns, {@link #getDriver()} will no longer return
     * the previously associated driver. Implementations should also call
     * {@code quit()} on the driver if it is still active.</p>
     */
    void removeDriver();
}
