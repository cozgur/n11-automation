package com.company.qa.core.wait;

import com.company.qa.core.config.EnvironmentConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Instance-based explicit wait helper for Selenium/Appium elements.
 *
 * <p>Default timeout is sourced from {@link EnvironmentConfig#getExplicitTimeout()},
 * but can be overridden per-instance or per-call.</p>
 */
public class WaitHelper {

    private final WebDriver driver;
    private final int defaultTimeout;

    /**
     * Creates a wait helper with the default timeout from {@link EnvironmentConfig}.
     *
     * @param driver the WebDriver instance to wait on
     */
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = EnvironmentConfig.getInstance().getExplicitTimeout();
    }

    /**
     * Creates a wait helper with a custom default timeout.
     *
     * @param driver         the WebDriver instance to wait on
     * @param timeoutSeconds the default timeout in seconds for all wait operations
     */
    public WaitHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.defaultTimeout = timeoutSeconds;
    }

    /**
     * Waits for the element to become visible using the default timeout.
     *
     * @param locator the element locator
     * @return the visible {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not visible
     *         within the timeout
     */
    public WebElement waitForVisible(By locator) {
        return waitForVisible(locator, defaultTimeout);
    }

    /**
     * Waits for the element to become visible using a custom timeout.
     *
     * @param locator        the element locator
     * @param timeoutSeconds the maximum time to wait in seconds
     * @return the visible {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not visible
     *         within the timeout
     */
    public WebElement waitForVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element to become clickable using the default timeout.
     *
     * @param locator the element locator
     * @return the clickable {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not clickable
     *         within the timeout
     */
    public WebElement waitForClickable(By locator) {
        return waitForClickable(locator, defaultTimeout);
    }

    /**
     * Waits for the element to become clickable using a custom timeout.
     *
     * @param locator        the element locator
     * @param timeoutSeconds the maximum time to wait in seconds
     * @return the clickable {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not clickable
     *         within the timeout
     */
    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for the element to become invisible using the default timeout.
     *
     * @param locator the element locator
     * @return {@code true} if the element becomes invisible within the timeout
     * @throws org.openqa.selenium.TimeoutException if the element is still visible
     *         after the timeout
     */
    public boolean waitForInvisible(By locator) {
        return waitForInvisible(locator, defaultTimeout);
    }

    /**
     * Waits for the element to become invisible using a custom timeout.
     *
     * @param locator        the element locator
     * @param timeoutSeconds the maximum time to wait in seconds
     * @return {@code true} if the element becomes invisible within the timeout
     * @throws org.openqa.selenium.TimeoutException if the element is still visible
     *         after the timeout
     */
    public boolean waitForInvisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element to be present in the DOM using the default timeout.
     *
     * <p>The element does not need to be visible; it only needs to exist in the
     * DOM tree.</p>
     *
     * @param locator the element locator
     * @return the present {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not present
     *         within the timeout
     */
    public WebElement waitForPresence(By locator) {
        return waitForPresence(locator, defaultTimeout);
    }

    /**
     * Waits for the element to be present in the DOM using a custom timeout.
     *
     * @param locator        the element locator
     * @param timeoutSeconds the maximum time to wait in seconds
     * @return the present {@link WebElement}
     * @throws org.openqa.selenium.TimeoutException if the element is not present
     *         within the timeout
     */
    public WebElement waitForPresence(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for the specified text to be present in the element using the default timeout.
     *
     * @param locator the element locator
     * @param text    the text to wait for
     * @return {@code true} if the text is found within the timeout
     * @throws org.openqa.selenium.TimeoutException if the text is not present
     *         within the timeout
     */
    public boolean waitForTextPresent(By locator, String text) {
        return waitForTextPresent(locator, text, defaultTimeout);
    }

    /**
     * Waits for the specified text to be present in the element using a custom timeout.
     *
     * @param locator        the element locator
     * @param text           the text to wait for
     * @param timeoutSeconds the maximum time to wait in seconds
     * @return {@code true} if the text is found within the timeout
     * @throws org.openqa.selenium.TimeoutException if the text is not present
     *         within the timeout
     */
    public boolean waitForTextPresent(By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
