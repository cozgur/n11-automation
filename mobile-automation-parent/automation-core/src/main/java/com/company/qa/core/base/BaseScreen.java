package com.company.qa.core.base;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.wait.WaitHelper;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Abstract base class for screen/page objects in mobile automation.
 * Provides driver access, wait helper, and common element interaction methods.
 */
public abstract class BaseScreen {

    protected final AppiumDriver driver;
    protected final WaitHelper waitHelper;

    protected BaseScreen() {
        this.driver = MobileDriverManager.getDriver();
        this.waitHelper = new WaitHelper(driver);
    }

    protected WebElement find(By locator) {
        return waitHelper.waitForVisible(locator);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void tap(By locator) {
        waitHelper.waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitHelper.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitHelper.waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitHelper.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected AppiumDriver getDriver() {
        return driver;
    }
}
