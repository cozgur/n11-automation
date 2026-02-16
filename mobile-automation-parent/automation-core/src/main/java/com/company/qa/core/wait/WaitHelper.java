package com.company.qa.core.wait;

import com.company.qa.core.config.EnvironmentConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Instance-based wait helper. Timeout defaults from EnvironmentConfig.
 */
public class WaitHelper {

    private final WebDriver driver;
    private final int defaultTimeout;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = EnvironmentConfig.getInstance().getExplicitTimeout();
    }

    public WaitHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.defaultTimeout = timeoutSeconds;
    }

    public WebElement waitForVisible(By locator) {
        return waitForVisible(locator, defaultTimeout);
    }

    public WebElement waitForVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return waitForClickable(locator, defaultTimeout);
    }

    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisible(By locator) {
        return waitForInvisible(locator, defaultTimeout);
    }

    public boolean waitForInvisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement waitForPresence(By locator) {
        return waitForPresence(locator, defaultTimeout);
    }

    public WebElement waitForPresence(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean waitForTextPresent(By locator, String text) {
        return waitForTextPresent(locator, text, defaultTimeout);
    }

    public boolean waitForTextPresent(By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
