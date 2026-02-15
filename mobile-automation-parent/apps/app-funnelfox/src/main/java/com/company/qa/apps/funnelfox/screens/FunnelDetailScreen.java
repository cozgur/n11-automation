package com.company.qa.apps.funnelfox.screens;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FunnelDetailScreen {

    private AppiumDriver<MobileElement> driver() {
        return MobileDriverManager.getDriver();
    }

    private static final By FUNNEL_NAME = By.id("com.funnelfox.app:id/funnelName");
    private static final By STEP_LIST = By.id("com.funnelfox.app:id/stepList");
    private static final By STEP_ITEM = By.id("com.funnelfox.app:id/stepItem");
    private static final By CONVERSION_RATE = By.id("com.funnelfox.app:id/conversionRate");
    private static final By BACK_BUTTON = By.id("com.funnelfox.app:id/backButton");

    public String getFunnelName() {
        return driver().findElement(FUNNEL_NAME).getText();
    }

    public List<WebElement> getSteps() {
        return driver().findElements(STEP_ITEM);
    }

    public String getConversionRate() {
        return driver().findElement(CONVERSION_RATE).getText();
    }

    public void goBack() {
        driver().findElement(BACK_BUTTON).click();
    }
}
