package com.company.qa.core.base;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.wait.WaitHelper;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Abstract base class for screen (page) objects in mobile automation.
 *
 * <p>Provides the current thread's {@link AppiumDriver}, a pre-configured
 * {@link WaitHelper}, and convenience methods for common element interactions
 * such as tapping, typing, and reading text.</p>
 *
 * <p>Subclasses should define locators and higher-level actions specific to a
 * single screen of the application under test.</p>
 */
public abstract class BaseScreen {

    /** The Appium driver for the current thread. */
    protected final AppiumDriver driver;

    /** Helper for explicit waits using timeouts from {@link com.company.qa.core.config.EnvironmentConfig}. */
    protected final WaitHelper waitHelper;

    /**
     * Initializes the screen with the current thread's driver and a default
     * {@link WaitHelper}.
     */
    protected BaseScreen() {
        this.driver = MobileDriverManager.getDriver();
        this.waitHelper = new WaitHelper(driver);
    }

    /**
     * Finds a single element after waiting for it to become visible.
     *
     * @param locator the element locator
     * @return the visible {@link WebElement}
     */
    protected WebElement find(By locator) {
        return waitHelper.waitForVisible(locator);
    }

    /**
     * Finds all elements matching the given locator without waiting.
     *
     * @param locator the element locator
     * @return a list of matching {@link WebElement}s (may be empty)
     */
    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Waits for the element to be clickable and then taps (clicks) it.
     *
     * @param locator the element locator
     */
    protected void tap(By locator) {
        waitHelper.waitForClickable(locator).click();
    }

    /**
     * Clears the element's current text and types new text into it.
     *
     * @param locator the element locator
     * @param text    the text to enter
     */
    protected void type(By locator, String text) {
        WebElement element = waitHelper.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns the visible text of the element after waiting for visibility.
     *
     * @param locator the element locator
     * @return the element's visible text
     */
    protected String getText(By locator) {
        return waitHelper.waitForVisible(locator).getText();
    }

    /**
     * Checks whether the element is displayed on screen.
     *
     * @param locator the element locator
     * @return {@code true} if the element is visible, {@code false} if it is
     *         not found or an exception occurs
     */
    protected boolean isDisplayed(By locator) {
        try {
            return waitHelper.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the underlying {@link AppiumDriver} for advanced interactions.
     *
     * @return the current thread's Appium driver
     */
    protected AppiumDriver getDriver() {
        return driver;
    }
}
