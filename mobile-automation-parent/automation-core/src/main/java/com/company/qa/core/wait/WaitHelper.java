package com.company.qa.core.wait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {

    private static final long DEFAULT_TIMEOUT = 10;

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return waitForVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return waitForClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisible(WebDriver driver, By locator) {
        return waitForInvisible(driver, locator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForInvisible(WebDriver driver, By locator, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForPresence(WebDriver driver, By locator) {
        return waitForPresence(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForPresence(WebDriver driver, By locator, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
